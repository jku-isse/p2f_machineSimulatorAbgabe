package at.pro2future.machineSimulator.eventHandlers;

import java.util.List;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import ProcessCore.Parameter;
import Simulator.MsAction;
import Simulator.MsWriteAction;
import Simulator.ProcessOpcUaMapping;
import Simulator.ProcessOpcUaVariableMapping;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.shopfloors.adapters.EventInstance;


public class WriteVariableHandler extends BaseEventHandler {

	private final MsWriteAction writeAction;
	
	@Override
	protected MsAction getMsAction() {
		return this.writeAction;
	}
	
	public WriteVariableHandler(OpcUaClientManager opcUaClientManager, MsWriteAction writeAction) {
		super(opcUaClientManager);
		this.writeAction = writeAction;
	}
	
	@Override
	public void handleEvent(EventInstance e) {
		super.handleEvent(e);
		
		try {
			for(ProcessOpcUaMapping processOpcUaMapping : this.writeAction.getParameterMappings()) {
				hanldeMapping(processOpcUaMapping, e.parameters);
			}
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}	
	}
	
	private void hanldeMapping(ProcessOpcUaMapping processOpcUaMapping, List<Parameter> parameters) throws Exception {
		if(processOpcUaMapping instanceof ProcessOpcUaVariableMapping) {
			for(Parameter parameter : parameters) {
				ProcessOpcUaVariableMapping processOpcUaVariableMapping = (ProcessOpcUaVariableMapping)processOpcUaMapping;
				//TODO: only parameter name (generare namehashmap --> to improve performance).
				if(EcoreUtil.equals(processOpcUaVariableMapping.getParameter(), processOpcUaVariableMapping.getParameter())) {
					NodeId nodeId = getMsNodeIdToNodeIdConverter().createTo(processOpcUaVariableMapping.getVariableNode().getNodeId(), getOpcUaClientManager().getUaBuilderFactory());
					
					DataValue dataValue = new DataValue(new Variant(parameter.getValue()));
					getOpcUaClientManager().writeValues(nodeId, dataValue);
				}
			}
		}
		else {
			throw new UnsupportedOperationException();
		}		
	}

}
