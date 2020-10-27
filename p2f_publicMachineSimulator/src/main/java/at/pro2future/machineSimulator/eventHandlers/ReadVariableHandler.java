package at.pro2future.machineSimulator.eventHandlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

import OpcUaDefinition.MsVariableNode;
import ProcessCore.Event;
import ProcessCore.Parameter;
import Simulator.MsAction;
import Simulator.MsReadAction;
import Simulator.ProcessOpcUaMapping;
import Simulator.ProcessOpcUaObjectMapping;
import Simulator.ProcessOpcUaVariableMapping;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;
import at.pro2future.machineSimulator.converter.opcUaToMilo.ConvertionNotSupportedException;
import at.pro2future.machineSimulator.converter.opcUaToMilo.MsNodeIdToNodeIdConverter;
import at.pro2future.shopfloors.adapters.AdapterEventProvider;
import at.pro2future.shopfloors.adapters.EventInstance;


public class ReadVariableHandler extends BaseEventHandler {
	
	private final MsReadAction readAction;
	private final Map<ReadValueId, Parameter> readValueIdToParameterMap = new HashMap<>();
	private AdapterEventProvider engine;
	private UaBuilderFactory factory;
	
	@Override
	protected MsAction getMsAction() {
		return this.readAction;
	}
	
	public AdapterEventProvider getEngine() {
		return this.engine;
	}

	public void setEngine(AdapterEventProvider engine) {
		this.engine = engine;
	}

	public ReadVariableHandler(OpcUaClientManager opcUaClientManager, MsReadAction readAction, UaBuilderFactory factory) throws HandlerCanNotBeCreatedException {
		super(opcUaClientManager);		
		this.readAction = readAction;
		this.factory = factory;
		registerMsReadAction(readAction.getParameterMappings());
	}
	
	private void registerMsReadAction(List<ProcessOpcUaMapping> processOpcUaMappings) throws HandlerCanNotBeCreatedException {
		for(ProcessOpcUaMapping processOpcUaMapping : processOpcUaMappings) {
			if(processOpcUaMapping instanceof ProcessOpcUaObjectMapping) {
				registerMsReadAction(((ProcessOpcUaObjectMapping)processOpcUaMapping).getProcessOpcUaMappings());
			}
			else if(processOpcUaMapping instanceof ProcessOpcUaVariableMapping) {
				ProcessOpcUaVariableMapping processOpcUaVariableMapping = (ProcessOpcUaVariableMapping)processOpcUaMapping;
				hanldeMapping(processOpcUaVariableMapping);
			}
		}
	}

	private void hanldeMapping(ProcessOpcUaMapping processOpcUaMapping) throws HandlerCanNotBeCreatedException  {
		try {
			if(processOpcUaMapping instanceof ProcessOpcUaVariableMapping) {
				MsNodeIdToNodeIdConverter converter = new MsNodeIdToNodeIdConverter();
				MsVariableNode msVariableNode = ((ProcessOpcUaVariableMapping) processOpcUaMapping).getVariableNode();
				NodeId nodeId = converter.createTo(msVariableNode.getNodeId(), this.factory);
				
				ReadValueId readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);
				this.readValueIdToParameterMap.put(readValueId, ((ProcessOpcUaVariableMapping) processOpcUaMapping).getParameter());
			}
			else {
				throw new UnsupportedOperationException();
			}
		}
		catch(UnsupportedOperationException | ConvertionNotSupportedException ex) {
			throw new HandlerCanNotBeCreatedException(ex);
		}
	}
	

	@Override
	protected void onStartup() {
		for(ReadValueId readValueId :  this.readValueIdToParameterMap.keySet()) {
			try {
				getOpcUaClientManager().subscribeValues(readValueId, this::callbackWhenValueChanged);
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	protected void onShutdown() {
		// TODO Uregister subsribed values
	}
	
	private void callbackWhenValueChanged(UaMonitoredItem uaMonitoredItem, DataValue  dataValue) {
		ReadValueId readValueId = uaMonitoredItem.getReadValueId();
		Parameter parameter = EcoreUtil.copy(this.readValueIdToParameterMap.get(readValueId));
		parameter.setValue(dataValue.getValue().getValue());
		
		Event event = this.readAction.getRefersTo();
		EventInstance eventInstance = new EventInstance(event);
		eventInstance.parameters = Arrays.asList(parameter);
        
		this.engine.enqueueEvent(eventInstance);
	}
}

