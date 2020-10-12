package at.pro2future.machineSimulator.eventHandlers;

import ProcessCore.Assignment;
import ProcessCore.Event;
import Simulator.MsCallMethodAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.shopfloors.adapters.EventInstance;

public class CallMethodHandler extends BaseEventHandler {
	private final MsCallMethodAction callMethodAction;
	private EventInstance lastEvent;
	
	public CallMethodHandler(OpcUaClientManager opcUaClientManager, MsCallMethodAction callMethodAction) {
		super(opcUaClientManager);
		this.callMethodAction = callMethodAction;
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
		return this.callMethodAction.getReactsTo();
	}

	@Override
	public Assignment getRole() {
		return this.callMethodAction.getReactsTo().getRole();
	}
}
