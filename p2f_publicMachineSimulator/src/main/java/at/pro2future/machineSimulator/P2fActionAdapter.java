package at.pro2future.machineSimulator;

import java.util.ArrayList;
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
import at.pro2future.machineSimulator.eventHandlers.ReceivedMethodCallEventHandler;
import at.pro2future.machineSimulator.eventHandlers.WriteVariableHandler;
import at.pro2future.shopfloors.adapters.AdapterEventProvider;
import at.pro2future.shopfloors.adapters.EngineAdapter;
import at.pro2future.shopfloors.adapters.EventHandler;

/**
 * An adapter can listen or send envents which have been  
 * 
 * @author johannstoebich
 *
 */
public class P2fActionAdapter extends AbstractLifecycle implements EngineAdapter {
	
	private final MsAction action;
	private final List<BaseEventHandler> eventHandlers = new ArrayList<>();
	private final OpcUaClientManager opcUaClientManager;
	
	public OpcUaClientManager getOpcUaClientManager() {
		return this.opcUaClientManager;
	}
	
	public MsAction getAction() {
		return this.action;
	}

	/**
	 * Adapters an 
	 * 
	 * @param action
	 * @param uaBuilderFactory
	 * @throws UaException
	 * @throws HandlerCanNotBeCreatedException
	 */
	public P2fActionAdapter(MsAction action, UaBuilderFactory uaBuilderFactory) throws UaException, HandlerCanNotBeCreatedException {
		this.action = action;
		this.opcUaClientManager = new OpcUaClientManager(action.getOpcUaClientInterface(), uaBuilderFactory);
		
		if(action instanceof MsWriteAction) {
			this.eventHandlers.add(new WriteVariableHandler(this.opcUaClientManager, (MsWriteAction)action));
		}
		else if(action instanceof MsReadAction) {
			this.eventHandlers.add(new ReadVariableHandler(this.opcUaClientManager, (MsReadAction)action, uaBuilderFactory));
		}
		else if(action instanceof MsMethodAction) {
			this.eventHandlers.add(new CallMethodHandler(this.opcUaClientManager, (MsMethodAction)action));
			this.eventHandlers.add(new ReceivedMethodCallEventHandler(this.opcUaClientManager, (MsMethodAction)action));
		}
		
		if(this.eventHandlers.size() <= 0) {
			throw new HandlerCanNotBeCreatedException("The action type of the action \"" + action + "\" has not been defined.");
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
		for(BaseEventHandler baseEventHandler : this.eventHandlers) {
			if(baseEventHandler instanceof ReadVariableHandler) {
				((ReadVariableHandler)baseEventHandler).setEngine(engine);
			}
		}
	}

	@Override
	public List<EventHandler> getReceivedEvents() {
		List<EventHandler> events = new LinkedList<>();
		for(BaseEventHandler baseEventHandler: this.eventHandlers) {
			events.add(baseEventHandler);
		}
		return events;
	}

	@Override
	protected void onStartup() {
		for(BaseEventHandler baseEventHandler : this.eventHandlers) {
			baseEventHandler.startup();
		}
	}

	@Override
	protected void onShutdown() {
		for(BaseEventHandler baseEventHandler : this.eventHandlers) {
			baseEventHandler.shutdown();
		}
	}
}
