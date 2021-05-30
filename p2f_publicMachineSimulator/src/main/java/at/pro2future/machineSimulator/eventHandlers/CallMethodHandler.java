package at.pro2future.machineSimulator.eventHandlers;

import ProcessCore.Event;
import Simulator.MsMethodEventAdressSpaceAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.command.CallMethodCommand;
import at.pro2future.machineSimulator.command.CallMethodCommandParameters;
import at.pro2future.shopfloors.adapters.EventInstance;

/**
 * This event handler invokes a method whenever it receives an event. It sends an event whenever the invoked 
 * method returns a result.
 *
 */
public class CallMethodHandler extends  BaseEventHandler<MsMethodEventAdressSpaceAction> implements ISendEventHandler, IReceiveEventHandler<MsMethodEventAdressSpaceAction> {
    
    
    /**
     * Creates a hander which can call on receive a method an can send a event on return.
     * 
     * @param opcUaClientManager the <code>OpcUaClientManager</code> that is able to communicate with the server.
     * @param msMethodEventAdressSpaceAction that contains the configuration of the invoked method.
     */
    public CallMethodHandler(OpcUaClientManager opcUaClientManager, MsMethodEventAdressSpaceAction msMethodEventAdressSpaceAction) {
        super(opcUaClientManager, 
              msMethodEventAdressSpaceAction,
              new CallMethodCommand(opcUaClientManager, new CallMethodCommandParameters(msMethodEventAdressSpaceAction.getParameterMappings(),
                              msMethodEventAdressSpaceAction.getObjectContainingMethod(), 
                              msMethodEventAdressSpaceAction.getCallesMethod(), 
                              msMethodEventAdressSpaceAction.getReturnParameterMapping())));
    }
    
    
    @Override
    public void handleEvent(EventInstance e) {
        try {
            super.handleEvent(e);
            Event event = this.getMsEventAdressSpaceAction().getReturnEvent();
            this.getBaseCommand().execute(e.parameters);
            sendEvent(event, e.parameters);
        }
        catch(Exception exc) {
            throw new RuntimeException(exc);
        }
    }
    

}
