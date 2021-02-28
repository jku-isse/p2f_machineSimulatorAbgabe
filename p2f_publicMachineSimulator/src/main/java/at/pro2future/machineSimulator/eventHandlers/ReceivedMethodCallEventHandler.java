package at.pro2future.machineSimulator.eventHandlers;

import java.util.Arrays;
import java.util.List;

import ProcessCore.AbstractCapability;
import ProcessCore.Event;
import ProcessCore.Parameter;
import Simulator.MsAction;
import Simulator.MsMethodAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.shopfloors.adapters.AdapterEventProvider;
import at.pro2future.shopfloors.adapters.EngineAdapter;
import at.pro2future.shopfloors.adapters.EventHandler;
import at.pro2future.shopfloors.adapters.EventInstance;

public class ReceivedMethodCallEventHandler extends BaseEventHandler implements EngineAdapter {
	private final MsMethodAction methodAction;
	private AdapterEventProvider engine;
	
	@Override
	protected MsAction getMsAction() {
		return this.methodAction;
	}
	
	public AdapterEventProvider getEngine() {
		return this.engine;
	}

	public void setEngine(AdapterEventProvider engine) {
		this.engine = engine;
	}
	
	public ReceivedMethodCallEventHandler(OpcUaClientManager opcUaClientManager, MsMethodAction methodAction) {
		super(opcUaClientManager);
		this.methodAction = methodAction;
	}
	
	public void executeWhenMethodCalled(Parameter... parameters) {
		Event event = this.methodAction.getRefersTo();
		EventInstance eventInstance = new EventInstance(event);
		eventInstance.parameters = Arrays.asList(parameters);
        
		this.engine.enqueueEvent(eventInstance);
	}

	@Override
	public List<Parameter> invokeCapability(AbstractCapability capability, List<Parameter> parameterValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerWithEngine(AdapterEventProvider engine) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<EventHandler> getReceivedEvents() {
		// TODO Auto-generated method stub
		return null;
	}
}
