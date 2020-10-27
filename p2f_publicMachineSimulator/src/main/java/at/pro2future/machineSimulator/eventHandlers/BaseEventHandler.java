package at.pro2future.machineSimulator.eventHandlers;

import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;

import ProcessCore.Assignment;
import ProcessCore.Event;
import Simulator.MsAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.converter.opcUaToMilo.MsNodeIdToNodeIdConverter;
import at.pro2future.shopfloors.adapters.EventHandler;
import at.pro2future.shopfloors.adapters.EventInstance;

public abstract class BaseEventHandler extends AbstractLifecycle implements EventHandler {
	
	private final OpcUaClientManager opcUaClientManager;
	private final MsNodeIdToNodeIdConverter msNodeIdToNodeIdConverter =  new MsNodeIdToNodeIdConverter();
	private EventInstance lastEvent;
	
	public OpcUaClientManager getOpcUaClientManager() {
		return this.opcUaClientManager;
	}
	
	protected MsNodeIdToNodeIdConverter getMsNodeIdToNodeIdConverter() {
		return this.msNodeIdToNodeIdConverter;
	}
	
	protected abstract MsAction getMsAction();
	
	public BaseEventHandler(OpcUaClientManager opcUaClientManager) {
		this.opcUaClientManager = opcUaClientManager;
	}	
	
	@Override
	public void handleEvent(EventInstance e) {
		this.lastEvent = e;		
	}

	@Override
	public EventInstance getEvent() {
		return this.lastEvent;
	}

	@Override
	public Event getEventType() {
		return this.getMsAction().getRefersTo();
	}

	@Override
	public Assignment getRole() {
		return this.getMsAction().getRefersTo().getRole();
	}
	
	@Override
	protected void onStartup() {
		// Nothing to do on startup...
		
	}

	@Override
	protected void onShutdown() {
		// Nothing to do on shutdown...
		
	}
}
