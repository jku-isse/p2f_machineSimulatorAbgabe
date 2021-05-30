package at.pro2future.machineSimulator.command;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ProcessCore.Parameter;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;
import at.pro2future.machineSimulator.converter.opcUaToMilo.MsNodeIdToNodeIdConverter;

/**
 * This class represents a <code>BaseCommand</code> for all commands contained in this simulator.
 * Each command must implement the {@link execute} method.
 * 
 *
 */
public abstract class BaseCommand<T extends CommandParameters> {

    private final MsNodeIdToNodeIdConverter msNodeIdToNodeIdConverter =  MsNodeIdToNodeIdConverter.getInstance();
    private final OpcUaClientManager opcUaClientManager;
    private final T commandParameters;
    
    OpcUaClientManager getOpcUaClientManager() {
        return this.opcUaClientManager;
    }
    
    /**
     * Returns a node id converter instance.
     * @return
     */
    MsNodeIdToNodeIdConverter getMsNodeIdToNodeIdConverter() {
        return this.msNodeIdToNodeIdConverter;
    }
    
    /**
     * Returns the current configuration of this command.
     * @return
     */
    T getCommandParameters() {
        return this.commandParameters;
    }
    
    /**
     * Initializes a new command. 
     * 
     * @param opcUaClientManager the client manager which provides the connection to the server.
     * @param commandParameters the commandParameters which provides the configuration of this command.
     */
    protected BaseCommand(OpcUaClientManager opcUaClientManager, T commandParameters) {
        this.opcUaClientManager = opcUaClientManager;
        this.commandParameters = commandParameters;
    }
    
    /**
     * Executes the given command by using the provided configuration {@link MsAction}.
     * @param inputParameters the parameters which will be passed in, in order to execute the method.
     * @return the list of return parameters.
     * @throws ConversionFailureException 
     * @throws ConvertionNotSupportedException 
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    public abstract List<Parameter> execute(List<Parameter> inputParameters) throws ConvertionNotSupportedException, ConversionFailureException, InterruptedException, ExecutionException;

}
