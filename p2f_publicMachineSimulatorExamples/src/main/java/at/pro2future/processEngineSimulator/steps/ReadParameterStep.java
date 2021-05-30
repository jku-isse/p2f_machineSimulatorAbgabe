package at.pro2future.processEngineSimulator.steps;

import ProcessCore.Parameter;
import ProcessCore.ReadParameter;

import java.io.IOException;
import java.util.List;

import javax.naming.NameNotFoundException;

import at.pro2future.machineSimulator.ExampleAdapterProvider;
import at.pro2future.processEngineSimulator.ProcessEngineExecutionContext;

public class ReadParameterStep  extends CapabilityInvocationStep<ReadParameter> {
    
    /**
     * This step waits for a given event to occur.
     * 
     * @param readParameter the source config this event.
     */
    public ReadParameterStep(ReadParameter readParameter, ExampleAdapterProvider exampleAdapterProvider) {
        super(readParameter, exampleAdapterProvider);
    }
    
    @Override
    public boolean executeStep(
            ProcessEngineExecutionContext processEngineStepExecutionContext) throws IOException, NameNotFoundException {
        if(!super.executeStep( processEngineStepExecutionContext)){
            return false;
        }
        
        if(getProcessStep().getInvokedCapability() != null) {
            List<Parameter> inputParamters = handleInputMappings(getProcessStep().getInputMappings(), processEngineStepExecutionContext);
            List<Parameter> outputParamters = invokeCapability(getProcessStep().getInvokedCapability(), getExampleAdapterProvider(), inputParamters);
            List<Parameter> handledOutputParamters = handleOutputMappings(outputParamters, getProcessStep().getOutputMappings());
            setParametersToContext(handledOutputParamters, processEngineStepExecutionContext);
        }
        
        return true;
        
    }

}
