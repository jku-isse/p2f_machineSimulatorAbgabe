package at.pro2future.machineSimulator.eventHandlers;

import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.converter.opcUaToMilo.MsNodeIdToNodeIdConverter;
import at.pro2future.shopfloors.adapters.EventHandler;

public abstract class BaseEventHandler implements EventHandler {
	
	private final OpcUaClientManager opcUaClientManager;
	private final MsNodeIdToNodeIdConverter msNodeIdToNodeIdConverter =  new MsNodeIdToNodeIdConverter();
	
	public OpcUaClientManager getOpcUaClientManager() {
		return opcUaClientManager;
	}
	
	protected MsNodeIdToNodeIdConverter getMsNodeIdToNodeIdConverter() {
		return msNodeIdToNodeIdConverter;
	}
	
	public BaseEventHandler(OpcUaClientManager opcUaClientManager) {
		this.opcUaClientManager = opcUaClientManager;
	}	
}
