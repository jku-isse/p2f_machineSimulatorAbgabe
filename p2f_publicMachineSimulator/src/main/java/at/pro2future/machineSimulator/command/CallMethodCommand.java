package at.pro2future.machineSimulator.command;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;

import com.google.common.collect.Lists;

import ProcessCore.Parameter;
import Simulator.ProcessOpcUaMapping;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;
import at.pro2future.machineSimulator.converter.opcUaToMilo.MsNodeIdToNodeIdConverter;
import at.pro2future.shopfloors.adapters.EventInstance;

/**
 * This command executes a method call with the given parameter list. The configuration of this action is provided
 * by an {@link MsMethodAction} which implements the {@link execute} method.
 *
 */
public class CallMethodCommand extends BaseCommand<CallMethodCommandParameters>{

    /**
     * Creates a new command which is able to call a method with the given configuration.
     * 
     * @param msAdressSpaceAction the <code>OpcUaClientManager</code> that is able to communicate with the server.
     * @param callMethodCommandParameters the {@link CallMethodCommandParameters} which provides the configuration.
     */
    public CallMethodCommand(OpcUaClientManager opcUaClientManager, CallMethodCommandParameters callMethodCommandParameters) {
        super(opcUaClientManager, callMethodCommandParameters);
    }


    @Override
    public List<Parameter> execute(List<Parameter> inputParameters) throws ConvertionNotSupportedException, ConversionFailureException, InterruptedException, ExecutionException {
        CallMethodRequest request = new CallMethodRequest(
                MsNodeIdToNodeIdConverter.getInstance().createTarget(this.getCommandParameters().getObjectContainingMethod(), getOpcUaClientManager().getUaObjectAndBuilderProvider()),
                MsNodeIdToNodeIdConverter.getInstance().createTarget(this.getCommandParameters().getCallesMethod(), getOpcUaClientManager().getUaObjectAndBuilderProvider()),
                createInputVariants(inputParameters)
        );
        
        Variant[] outputArguments =  getOpcUaClientManager().createCallMethodRequest(request);
        Parameter[] outputParameters = createOutputParameters(this.getCommandParameters().getParameterMappings(), outputArguments);
        return Lists.newArrayList(outputParameters);
    }
    
    /**
     * This method creates the input arguments for a specific method from an {@link EventInstance}.
     * 
     */
    private static Variant[] createInputVariants(List<Parameter> parameters) {
        Variant[] inputVariants = new Variant[parameters.size()];
        
        for(int i = 0; i < parameters.size(); i++) {
            Parameter parameter = parameters.get(i);
            Variant variant = new Variant(parameter.getValue());
            inputVariants[i] = variant;
        }
        
        return inputVariants;
    }
    
    /**
     * This method handles the returned parameters respectively and creates parameters from it
     * which can then further be process by an event.
     * 
     */
    private static Parameter[] createOutputParameters(List<ProcessOpcUaMapping> processOpcUaMappings, Variant[] variants)  {
        Parameter[] parameters = new Parameter[processOpcUaMappings.size()];
        
        for(int i = 0; i < processOpcUaMappings.size(); i++) {
            parameters[i] = processOpcUaMappings.get(i).getParameter();
            parameters[i].setValue(variants[i]);
        }
        
        return parameters;
    }

}
