package at.pro2future.machineSimulator.eventHandlers;

import java.util.List;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import ProcessCore.Assignment;
import ProcessCore.Event;
import ProcessCore.Parameter;
import Simulator.MsWriteAction;
import Simulator.ProcessOpcUaMapping;
import Simulator.ProcessOpcUaVariableMapping;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.shopfloors.adapters.EventInstance;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class WriteVariableHandler extends BaseEventHandler {

	private final MsWriteAction writeAction;
	private EventInstance lastEvent; //TODO: move to base
	
	
	public WriteVariableHandler(OpcUaClientManager opcUaClientManager, MsWriteAction writeAction) {
		super(opcUaClientManager);
		this.writeAction = writeAction;
	}
	
	@Override
	public void handleEvent(EventInstance e) {
		
		try {
			for(ProcessOpcUaMapping processOpcUaMapping : writeAction.getProcessOpcUaMappings()) {
				hanldeMapping(processOpcUaMapping, e.parameters);
			}
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}

				
		lastEvent = e;		
	}
	
	private void hanldeMapping(ProcessOpcUaMapping processOpcUaMapping, List<Parameter> parameters) throws Exception {
		if(processOpcUaMapping instanceof ProcessOpcUaVariableMapping) {
			for(Parameter parameter : parameters) {
				ProcessOpcUaVariableMapping processOpcUaVariableMapping = (ProcessOpcUaVariableMapping)processOpcUaMapping;
				//TODO: only parameter name (generare namehashmap --> to improve performance).
				if(EcoreUtil.equals(processOpcUaVariableMapping.getParameter(), processOpcUaVariableMapping.getParameter())) {
					NodeId nodeId = getMsNodeIdToNodeIdConverter().createTo(processOpcUaVariableMapping.getVariableNode().getNodeId(), getOpcUaClientManager().getUaBuilderFactory());
					getOpcUaClientManager().writeValues(nodeId, new DataValue(new Variant(parameter.getValue())));
				}
			}
		}
		else {
			throw new NotImplementedException();
		}		
	}

	@Override
	public EventInstance getEvent() {
		return lastEvent;
	}

	@Override
	public Event getEventType() {
		return this.writeAction.getReactsTo();
	}

	@Override
	public Assignment getRole() {
		return this.writeAction.getReactsTo().getRole();
	}

}
