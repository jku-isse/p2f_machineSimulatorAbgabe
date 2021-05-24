package at.pro2future.machineSimulator.eventHandlers;

import java.util.concurrent.ExecutionException;

import Simulator.MsWriteEventAdressSpaceAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.command.WriteCommand;
import at.pro2future.machineSimulator.command.WriteCommandParameters;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;
import at.pro2future.shopfloors.adapters.EventInstance;


/**
 * This {@link WriteVariablesHandler} writes an server variable whenever it receives an {@link EventInstance}.
 * 
 * Therefore it is of type {@link IReceiveEventHandler}.
 *
 */
public class WriteVariablesHandler extends BaseEventHandler<MsWriteEventAdressSpaceAction> implements IReceiveEventHandler<MsWriteEventAdressSpaceAction> {

    
    /**
     * Creates a new <code>WriteVariableHandler</code> which is able to 
     * convert the parameter of an {@link EventInstance} an write it to the server.
     * 
     * @param opcUaClientManager the <code>OpcUaClientManager</code> that is able to communicate with the server.
     * @param msWriteEventAdressSpaceAction the action which defines whether the parameters that should be written.
     */
    public WriteVariablesHandler(OpcUaClientManager opcUaClientManager, MsWriteEventAdressSpaceAction msWriteEventAdressSpaceAction) {
        super(opcUaClientManager, 
                msWriteEventAdressSpaceAction, 
                new WriteCommand(opcUaClientManager, 
                        new WriteCommandParameters(msWriteEventAdressSpaceAction.getParameterMappings())));
    }

    /**
     * This method receives an {@link EventInstance} and extracts the 
     * parameters which then in turn will be written to the server. 
     * @throws ExecutionException 
     * @throws InterruptedException 
     * @throws ConversionFailureException 
     * @throws ConvertionNotSupportedException 
     */
    @Override
    public void handleEvent(EventInstance e)   {
        try {
            super.handleEvent(e);
            getBaseCommand().execute(e.parameters);
        }
        catch(Exception exc) {
            throw new RuntimeException(exc);
        }
    }

}
