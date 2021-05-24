package at.pro2future.machineSimulator;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.stack.core.UaException;

import ProcessCore.AbstractCapability;
import ProcessCore.Assignment;
import ProcessCore.Parameter;
import Simulator.MsAdressSpaceAction;
import Simulator.MsCallMethodCapabilityAdressSpaceAction;
import Simulator.MsMethodEventAdressSpaceAction;
import Simulator.MsReadCapabilityAdressSpaceAction;
import Simulator.MsReadEventAdressSpaceAction;
import Simulator.MsWriteCapabilityAdressSpaceAction;
import Simulator.MsWriteEventAdressSpaceAction;
import at.pro2future.machineSimulator.capabilityHandlers.BaseCapabilityHanlder;
import at.pro2future.machineSimulator.capabilityHandlers.CallMethodCapabilityHandler;
import at.pro2future.machineSimulator.capabilityHandlers.ReadCapabilityHandler;
import at.pro2future.machineSimulator.capabilityHandlers.WriteCapabilityHandler;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.eventHandlers.BaseEventHandler;
import at.pro2future.machineSimulator.eventHandlers.CallMethodHandler;
import at.pro2future.machineSimulator.eventHandlers.HandlerCanNotBeCreatedException;
import at.pro2future.machineSimulator.eventHandlers.ISendEventHandler;
import at.pro2future.machineSimulator.eventHandlers.ReadVariablesHandler;
import at.pro2future.machineSimulator.eventHandlers.WriteVariablesHandler;
import at.pro2future.shopfloors.adapters.AdapterEventProvider;
import at.pro2future.shopfloors.adapters.EngineAdapter;
import at.pro2future.shopfloors.adapters.EventHandler;

/**
 * An <code>P2fActionAdapter</code> connects exactly one {@link MsAdressSpaceAction} to an 
 *  P2fEgine by implemented an {@link EngineAdapter}. The events or calls received from the
 *  {@link EngineAdapter} are forward to the <code>MsAction</code> and vice versa.
 * 
 *
 */
public class P2fAdressSpaceActionAdapter<T extends MsAdressSpaceAction> extends AbstractLifecycle implements EngineAdapter {
    
    private final MsAdressSpaceAction msAdressSpaceAction;
    private BaseEventHandler<?> eventAction;
    private BaseCapabilityHanlder<?> baseCapabilityHanlder;
    private final OpcUaClientManager opcUaClientManager;
    
    /**
     * Returns the OpcUaClientManager that is required for the actions to execute.
     * 
     * @return the OpcUaClientManager
     */
    public OpcUaClientManager getOpcUaClientManager() {
        return this.opcUaClientManager;
    }
    
    /**
     * Return the action that is connected to the engine.
     * 
     * @return the action that is connected to the engine.
     */
    public MsAdressSpaceAction getMsAdressSpaceAction() {
        return this.msAdressSpaceAction;
    }

    /**
     * The <code>P2fActionAdapter</code> wraps a specific action, e.g., read action,
     * write action and method call action, so that is is applicable from the 
     * P2fEngine.
     * 
     * @param msAdressSpaceAction The action which should be wrapped.
     * @param uaBuilderFactory A builder which helps the creation of the of the client objects.
     * @throws UaException The exception which is thrown when the connection cannot be established.
     * @throws HandlerCanNotBeCreatedException 
     */
    public P2fAdressSpaceActionAdapter(T msAdressSpaceAction, IUaObjectAndBuilderProvider uaObjectAndBuilderProvider) throws UaException, HandlerCanNotBeCreatedException {
        this.msAdressSpaceAction = msAdressSpaceAction;
        this.opcUaClientManager = new OpcUaClientManager(msAdressSpaceAction.getOpcUaClientInterface(), uaObjectAndBuilderProvider);
        
        if(msAdressSpaceAction instanceof MsWriteEventAdressSpaceAction) {
            this.eventAction = new WriteVariablesHandler(this.opcUaClientManager, (MsWriteEventAdressSpaceAction)msAdressSpaceAction);
        }
        else if(msAdressSpaceAction instanceof MsReadEventAdressSpaceAction) {
            this.eventAction = new ReadVariablesHandler(this.opcUaClientManager, (MsReadEventAdressSpaceAction)msAdressSpaceAction, uaObjectAndBuilderProvider);
        }
        else if(msAdressSpaceAction instanceof MsMethodEventAdressSpaceAction) {
            this.eventAction = new CallMethodHandler(this.opcUaClientManager, (MsMethodEventAdressSpaceAction)msAdressSpaceAction);
        }
        else if(msAdressSpaceAction instanceof MsReadCapabilityAdressSpaceAction) {
            this.baseCapabilityHanlder = new ReadCapabilityHandler(this.opcUaClientManager, (MsReadCapabilityAdressSpaceAction)msAdressSpaceAction);
        }
        else if(msAdressSpaceAction instanceof MsWriteCapabilityAdressSpaceAction) {
            this.baseCapabilityHanlder = new WriteCapabilityHandler(this.opcUaClientManager, (MsWriteCapabilityAdressSpaceAction)msAdressSpaceAction);
        }
        else if(msAdressSpaceAction instanceof MsCallMethodCapabilityAdressSpaceAction) {
            this.baseCapabilityHanlder = new CallMethodCapabilityHandler(this.opcUaClientManager, (MsCallMethodCapabilityAdressSpaceAction)msAdressSpaceAction);
        }
        else{
            throw new HandlerCanNotBeCreatedException("The action type of the action \"" + msAdressSpaceAction + "\" has not been defined.");
        }
    }
    
    /**
     * This method invokes a service with parameters returning output parameters.
     * 
     * @param capability the capability which is provided by the caller.
     * @param parameterValues the parameters containing the capabilities.
     */
    @Override
    public List<Parameter> invokeCapability(AbstractCapability capability, List<Parameter> parameterValues) {
        if(this.baseCapabilityHanlder != null) {
            try {
                return this.baseCapabilityHanlder.invokeCapability(capability, parameterValues);
            } catch (ConvertionNotSupportedException | ConversionFailureException | InterruptedException
                    | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public Assignment getRole() {
        return this.eventAction.getEvent().eventType.getRole();
    }

    @Override
    public void registerWithEngine(AdapterEventProvider adapterEventProvider) {
        if(this.eventAction instanceof ISendEventHandler) {
            ISendEventHandler sendEventAction = (ISendEventHandler)this.eventAction;
            sendEventAction.setAdapterEventProvider(adapterEventProvider);
        }
    }

    @Override
    public List<EventHandler> getReceivedEvents() {
        List<EventHandler> events = new LinkedList<>();
        if(this.eventAction instanceof EventHandler) {
            EventHandler eventHandler = (EventHandler)this.eventAction;
            events.add(eventHandler);
        }
        return events;
    }

    @Override
    protected void onStartup() {
        this.eventAction.startup();
    }

    @Override
    protected void onShutdown() {
        this.eventAction.shutdown();
    }
}
