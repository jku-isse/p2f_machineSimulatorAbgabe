package at.pro2future.machineSimulator.eventHandlers;

import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;

import Simulator.MsEventAdressSpaceAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.command.BaseCommand;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;
import at.pro2future.shopfloors.adapters.EventHandler;
import at.pro2future.shopfloors.adapters.EventInstance;

/**
 * A event hander is a template to provide a logic on how an {@link MsAction} must react to an {@link EventInstance}. This logic
 * is implemented within the method {@link BaseEventHandler#handleEvent(EventInstance)}.
 * 
 * All event handlers which derive from this abstract class should implement the {@link BaseEventHandler#handleEvent(EventInstance)} method.
 * 
 * @param <T> the action which is represented by this event hander.
 */
public abstract class BaseEventHandler<T extends MsEventAdressSpaceAction> extends AbstractLifecycle {
    
    private final OpcUaClientManager opcUaClientManager;
    private final T msEventAdressSpaceAction;
    private final BaseCommand<?> baseCommand;
    private EventInstance lastEvent;
    
   /**
    * Returns the {@link OpcUaClientManager} for which the actions will be executed.
    * @return
    */
    OpcUaClientManager getOpcUaClientManager() {
        return this.opcUaClientManager;
    }
    
    /**
     * Returns the action for which the event handler is defined.
     * @return
     */
    public T getMsEventAdressSpaceAction() {
        return this.msEventAdressSpaceAction;
    }
    
    /**
     * Returns the underlying {@link BaseCommand} wrapped by this {@link EventHandler}.
     * @return
     */
    protected  BaseCommand<?> getBaseCommand(){
        return this.baseCommand;
    }
    
    
    /**
     * Initiates an event hander for an specific action.
     * 
     * @param opcUaClientManager the OpcUaClientManger which provides actions the {@link #handleEvent(EventInstance)} routine can use.
     * @param msEventAdressSpaceAction the action for which the event handler is defined.
     * @param baseCommand the command which is wrapped by this event handler.
     */
    protected BaseEventHandler(OpcUaClientManager opcUaClientManager, T msEventAdressSpaceAction, BaseCommand<?> baseCommand) {
        this.opcUaClientManager = opcUaClientManager;
        this.msEventAdressSpaceAction = msEventAdressSpaceAction;
        this.baseCommand = baseCommand;
    }    
    
    /**
     * Contains the routine which process the event.
     * 
     * @param e
     * @throws ExecutionException 
     * @throws InterruptedException 
     * @throws ConversionFailureException 
     * @throws ConvertionNotSupportedException 
     */
    void handleEvent(EventInstance e) throws ConvertionNotSupportedException, ConversionFailureException, InterruptedException, ExecutionException {
        this.lastEvent = e; 
    }

    /**
     * Returns the last event. 
     * 
     * @remark it only returns the last event properly when {@link #handleEvent(EventInstance)} is invoked respectivel.
     * 
     * @return
     */
    public EventInstance getEvent() {
        return this.lastEvent;
    }
    
    /**
     * Provides a default behavior which is doing nothing on startup.
     */
    @Override
    protected void onStartup() {
        // Nothing to do on startup...
        
    }

    /**
     * Provides a default behavior which is doing nothing on shutdown.
     */
    @Override
    protected void onShutdown() {
        // Nothing to do on shutdown...
    }
}
