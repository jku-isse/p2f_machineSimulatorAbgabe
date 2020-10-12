package at.pro2future.machineSimulator;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.milo.opcua.stack.core.UaException;

import ProcessCore.AbstractCapability;
import ProcessCore.Assignment;
import ProcessCore.Parameter;
import Simulator.MsAction;
import Simulator.MsCallMethodAction;
import Simulator.MsReadAction;
import Simulator.MsWriteAction;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;
import at.pro2future.machineSimulator.eventHandlers.CallMethodHandler;
import at.pro2future.machineSimulator.eventHandlers.ReadVariableHandler;
import at.pro2future.machineSimulator.eventHandlers.WriteVariableHandler;
import at.pro2future.shopfloors.adapters.AdapterEventProvider;
import at.pro2future.shopfloors.adapters.EngineAdapter;
import at.pro2future.shopfloors.adapters.EventHandler;

public class P2fActionAdapter implements EngineAdapter {
	
	private final MsAction action;
	private final EventHandler actionHandler;
	private final OpcUaClientManager opcUaClientManager;
	private AdapterEventProvider engine;
	
	public OpcUaClientManager getOpcUaClientManager() {
		return opcUaClientManager;
	}
	
	public MsAction getAction() {
		return action;
	}

	public P2fActionAdapter(MsAction action,  UaBuilderFactory uaBuilderFactory) throws UaException {
		this.action = action;
		this.opcUaClientManager = new OpcUaClientManager(action.getOpcuaclientinterface(), uaBuilderFactory);
		
		if(action instanceof MsWriteAction) {
			this.actionHandler = new WriteVariableHandler(opcUaClientManager, (MsWriteAction)action);
		}
		else if(action instanceof MsReadAction) {
			this.actionHandler = new ReadVariableHandler(opcUaClientManager, (MsReadAction)action);
		}
		else  {
			this.actionHandler = new CallMethodHandler(opcUaClientManager, (MsCallMethodAction)action);
		}
	}

	@Override
	public List<Parameter> invokeCapability(AbstractCapability capability, List<Parameter> parameterValues) {
		// TODO What does this? Capability = Method
		// TODO Hello world method implementieren...
		return null;
	}

	@Override
	public Assignment getRole() {
		return 	this.action.getReactsTo().getRole();
	}

	@Override
	public void registerWithEngine(AdapterEventProvider engine) {
		this.engine = engine;
		
	}

	@Override
	public List<EventHandler> getReceivedEvents() {
		List<EventHandler> events = new LinkedList<>();
		events.add(actionHandler);
		return events;
	}
}
