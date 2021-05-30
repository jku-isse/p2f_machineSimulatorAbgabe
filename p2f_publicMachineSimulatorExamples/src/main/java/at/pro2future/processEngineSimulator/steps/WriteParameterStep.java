package at.pro2future.processEngineSimulator.steps;

import java.io.IOException;
import java.util.List;

import javax.naming.NameNotFoundException;

import ProcessCore.Parameter;
import ProcessCore.WriteParameter;
import at.pro2future.machineSimulator.ExampleAdapterProvider;
import at.pro2future.processEngineSimulator.ProcessEngineExecutionContext;

public class WriteParameterStep extends CapabilityInvocationStep<WriteParameter> {
    
    
    /**
     * This step waits for a given event to occur.
     * 
     * @param writeParameter the source config this event.
     */
    public WriteParameterStep(WriteParameter writeParameter, ExampleAdapterProvider exampleAdapterProvider) {
        super(writeParameter, exampleAdapterProvider);
    }
    
    @Override
    public boolean executeStep(
            ProcessEngineExecutionContext processEngineStepExecutionContext) throws IOException, NameNotFoundException {
        if(!super.executeStep(processEngineStepExecutionContext)){
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