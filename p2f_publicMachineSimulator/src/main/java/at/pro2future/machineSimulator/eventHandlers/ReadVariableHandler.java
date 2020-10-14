package at.pro2future.machineSimulator.eventHandlers;

import ProcessCore.Assignment;
import ProcessCore.Event;
import Simulator.MsReadAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.shopfloors.adapters.EngineAdapter;
import at.pro2future.shopfloors.adapters.EventInstance;


public class ReadVariableHandler extends BaseEventHandler {
	private final MsReadAction readAction;
	private EventInstance lastEvent;
	private EngineAdapter engineAdapter;
	
	public ReadVariableHandler(OpcUaClientManager opcUaClientManager, EngineAdapter engineAdapter, MsReadAction readAction) {
		super(opcUaClientManager);
		this.engineAdapter = engineAdapter;
		this.readAction = readAction;
	}
	
	@Override
	public void handleEvent(EventInstance e) {
		lastEvent = e;		
	}

	@Override
	public EventInstance getEvent() {
		return lastEvent;
	}

	@Override
	public Event getEventType() {
		return this.readAction.getReactsTo();
	}

	@Override
	public Assignment getRole() {
		return this.readAction.getReactsTo().getRole();
	}
}
