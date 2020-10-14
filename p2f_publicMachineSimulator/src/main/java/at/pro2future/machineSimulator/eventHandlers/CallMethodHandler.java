package at.pro2future.machineSimulator.eventHandlers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;

import OpcUaDefinition.MsMethodNode;
import ProcessCore.Assignment;
import ProcessCore.Event;
import Simulator.MsCallMethodAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.converter.opcUaToMilo.MsNodeIdToNodeIdConverter;
import at.pro2future.shopfloors.adapters.EventInstance;

public class CallMethodHandler extends BaseEventHandler {
	private final MsCallMethodAction callMethodAction;
	private EventInstance lastEvent;
	
	public CallMethodHandler(OpcUaClientManager opcUaClientManager, MsCallMethodAction callMethodAction) {
		super(opcUaClientManager);
		this.callMethodAction = callMethodAction;
	}
	
	@Override
	public void handleEvent(EventInstance e){
		try {
			MsMethodNode msMethodNode = callMethodAction.getProcessOpcUaMethodMapping();
			CallMethodRequest request = new CallMethodRequest(
					new MsNodeIdToNodeIdConverter().createTo(callMethodAction.getCalledOn().getNodeId(), getOpcUaClientManager().getUaBuilderFactory()),
		            new MsNodeIdToNodeIdConverter().createTo(msMethodNode.getNodeId(), getOpcUaClientManager().getUaBuilderFactory()),
		            new Variant[]{}
		        );
			
			getOpcUaClientManager().callMethod(request);
		}
		catch(Exception exc) {
			throw new RuntimeException(exc);
		}
		
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
