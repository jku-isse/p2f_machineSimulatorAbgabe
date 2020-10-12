package at.pro2future.machineSimulator.eventHandlers;

import ProcessCore.Assignment;
import ProcessCore.Event;
import Simulator.MsReadAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.shopfloors.adapters.EventInstance;


public class ReadVariableHandler extends BaseEventHandler {
	private final MsReadAction readAction;
	private EventInstance lastEvent;
	
	public ReadVariableHandler(OpcUaClientManager opcUaClientManager, MsReadAction readAction) {
		super(opcUaClientManager);
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
