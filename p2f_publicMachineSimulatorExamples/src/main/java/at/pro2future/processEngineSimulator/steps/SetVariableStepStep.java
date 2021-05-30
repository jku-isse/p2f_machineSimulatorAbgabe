package at.pro2future.processEngineSimulator.steps;

import java.io.IOException;

import javax.naming.NameNotFoundException;

import ProcessCore.Parameter;
import ProcessCore.SetVariableStep;
import at.pro2futore.processEngineSimulator.steps.base.ProcessEngineStep;
import at.pro2future.machineSimulator.ExampleAdapterProvider;
import at.pro2future.processEngineSimulator.ProcessEngineExecutionContext;

public class SetVariableStepStep extends ProcessEngineStep<SetVariableStep>{

    public SetVariableStepStep(SetVariableStep processStep, ExampleAdapterProvider exampleAdapterProvider) {
        super(processStep, exampleAdapterProvider);
    }

    @Override
    public boolean executeStep(
            ProcessEngineExecutionContext processEngineStepExecutionContext) throws IOException, NameNotFoundException {
        
        Parameter rhs = findParameter(getProcessStep().getRhs(), processEngineStepExecutionContext.getParameters());
        if(rhs == null) {
            rhs = getProcessStep().getRhs();
        }
        getProcessStep().getModifiedVariable().setValue(rhs.getValue());
        setParameterToContext(getProcessStep().getModifiedVariable(), processEngineStepExecutionContext);
        return true;
    }

}
