package at.pro2future.machineSimulator.capabilityHandlers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ProcessCore.AbstractCapability;
import ProcessCore.Parameter;
import Simulator.MsCapabilityAdressSpaceAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.command.BaseCommand;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;
import at.pro2future.shopfloors.adapters.EventHandler;
import at.pro2future.shopfloors.adapters.EventInstance;

/**
 * This {@link BaseCapabilityHanlder} provides the actions
 * to be executed whenever a capability arrises.
 * 
 * @param <T>
 */
public abstract class BaseCapabilityHanlder<T extends MsCapabilityAdressSpaceAction> {

    private final OpcUaClientManager opcUaClientManager;
    private final T msCapabilityAdressSpaceAction;
    private final BaseCommand<?> baseCommand;
    
   /**
    * Returns the {@link OpcUaClientManager} for which the actions will be executed.
    * @return
    */
    public OpcUaClientManager getOpcUaClientManager() {
        return this.opcUaClientManager;
    }
    
    /**
     * Returns the action for which the capability handler is defined.
     * @return
     */
    public T getMsCapabilityAdressSpaceAction() {
        return this.msCapabilityAdressSpaceAction;
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
     * @param msCapabilityAdressSpaceAction the action for which the event handler is defined.
     * @param baseCommand the command which is wrapped by this event handler.
     */
    protected BaseCapabilityHanlder(OpcUaClientManager opcUaClientManager, T msCapabilityAdressSpaceAction, BaseCommand<?> baseCommand) {
        this.opcUaClientManager = opcUaClientManager;
        this.msCapabilityAdressSpaceAction = msCapabilityAdressSpaceAction;
        this.baseCommand = baseCommand;
    }
    
    /**
     * This method is invokes a capability every time this handler is invoked.
     * 
     * @param capability the capability which is invoked.
     * @param parameterValues the parameter that are provided with the capability invokation.
     * @return the resulting parameters from this invokation 
     * @throws ExecutionException 
     * @throws InterruptedException 
     * @throws ConversionFailureException 
     * @throws ConvertionNotSupportedException 
     */
    public List<Parameter> invokeCapability(AbstractCapability capability, List<Parameter> parameterValues) throws ConvertionNotSupportedException, ConversionFailureException, InterruptedException, ExecutionException{
       return this.baseCommand.execute(parameterValues);
    }
}
