package at.pro2future.machineSimulator;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.stack.core.UaException;

import ProcessCore.AbstractCapability;
import ProcessCore.Assignment;
import ProcessCore.Parameter;
import Simulator.MsAction;
import Simulator.MsMethodAction;
import Simulator.MsReadAction;
import Simulator.MsWriteAction;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;
import at.pro2future.machineSimulator.eventHandlers.BaseEventHandler;
import at.pro2future.machineSimulator.eventHandlers.CallMethodHandler;
import at.pro2future.machineSimulator.eventHandlers.HandlerCanNotBeCreatedException;
import at.pro2future.machineSimulator.eventHandlers.ReadVariableHandler;
import at.pro2future.machineSimulator.eventHandlers.WriteVariableHandler;
import at.pro2future.shopfloors.adapters.AdapterEventProvider;
import at.pro2future.shopfloors.adapters.EngineAdapter;
import at.pro2future.shopfloors.adapters.EventHandler;

public class P2fActionAdapter extends AbstractLifecycle implements EngineAdapter {
	
	private final MsAction action;
	private final BaseEventHandler eventHandler;
	private final OpcUaClientManager opcUaClientManager;
	
	public OpcUaClientManager getOpcUaClientManager() {
		return this.opcUaClientManager;
	}
	
	public MsAction getAction() {
		return this.action;
	}

	public P2fActionAdapter(MsAction action, UaBuilderFactory uaBuilderFactory) throws UaException, HandlerCanNotBeCreatedException {
		this.action = action;
		this.opcUaClientManager = new OpcUaClientManager(action.getOpcUaClientInterface(), uaBuilderFactory);
		
		if(action instanceof MsWriteAction) {
			this.eventHandler = new WriteVariableHandler(this.opcUaClientManager, (MsWriteAction)action);
		}
		else if(action instanceof MsReadAction) {
			this.eventHandler = new ReadVariableHandler(this.opcUaClientManager, (MsReadAction)action, uaBuilderFactory);
		}
		else  {
			this.eventHandler = new CallMethodHandler(this.opcUaClientManager, (MsMethodAction)action);
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
		return 	this.action.getRefersTo().getRole();
	}

	@Override
	public void registerWithEngine(AdapterEventProvider engine) {
		if(this.eventHandler instanceof ReadVariableHandler) {
			((ReadVariableHandler)this.eventHandler).setEngine(engine);
		}
	}

	@Override
	public List<EventHandler> getReceivedEvents() {
		List<EventHandler> events = new LinkedList<>();
		events.add(this.eventHandler);
		return events;
	}

	@Override
	protected void onStartup() {
		this.eventHandler.startup();
		
	}

	@Override
	protected void onShutdown() {
		this.eventHandler.shutdown();
	}
}
