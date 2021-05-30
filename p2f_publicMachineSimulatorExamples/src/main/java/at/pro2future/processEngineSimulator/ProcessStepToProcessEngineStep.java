package at.pro2future.processEngineSimulator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ProcessCore.Decision;
import ProcessCore.EventSink;
import ProcessCore.EventSource;
import ProcessCore.HeadLoop;
import ProcessCore.HumanStep;
import ProcessCore.ProcessStep;
import ProcessCore.ReadParameter;
import ProcessCore.SetVariableStep;
import ProcessCore.WriteParameter;
import at.pro2futore.processEngineSimulator.steps.base.ProcessEngineStep;
import at.pro2future.machineSimulator.ExampleAdapterProvider;
import at.pro2future.processEngineSimulator.steps.DecisionStep;
import at.pro2future.processEngineSimulator.steps.EventSinkStep;
import at.pro2future.processEngineSimulator.steps.EventSourceStep;
import at.pro2future.processEngineSimulator.steps.HeadLoopStep;
import at.pro2future.processEngineSimulator.steps.HumanStepStep;
import at.pro2future.processEngineSimulator.steps.ReadParameterStep;
import at.pro2future.processEngineSimulator.steps.SetVariableStepStep;
import at.pro2future.processEngineSimulator.steps.WriteParameterStep;

public class ProcessStepToProcessEngineStep {

    private static ProcessStepToProcessEngineStep instance = null;
    
    public static ProcessStepToProcessEngineStep getInstance() {
        if(instance == null) {
            instance = new ProcessStepToProcessEngineStep();
        }
        return instance;
    }
    
    private ProcessStepToProcessEngineStep() {
    }
    
    public List<ProcessEngineStep<?>> convertAll(Collection<ProcessStep> processSteps, ExampleAdapterProvider exampleAdapterProvider){
        List<ProcessEngineStep<?>>  processEngineSteps = new ArrayList<>();
        for(ProcessStep processStep : processSteps) {
            processEngineSteps.add(convert(processStep, exampleAdapterProvider));
        }
        return processEngineSteps;
    }
    
    public ProcessEngineStep<?> convert(ProcessStep processStep, ExampleAdapterProvider exampleAdapterProvider ){
        if(processStep instanceof Decision) {
            return new DecisionStep((Decision)processStep, exampleAdapterProvider);
        }
        else if(processStep instanceof EventSink) {
            return new EventSinkStep((EventSink)processStep, exampleAdapterProvider);
        }
        else if(processStep instanceof EventSource) {
            return new EventSourceStep((EventSource)processStep, exampleAdapterProvider);
        }
        else if(processStep instanceof HeadLoop) {
            return new HeadLoopStep((HeadLoop)processStep, exampleAdapterProvider);
        }
        else if(processStep instanceof HumanStep) {
            return new HumanStepStep((HumanStep)processStep, exampleAdapterProvider);
        }
        else if(processStep instanceof ReadParameter) {
            return new ReadParameterStep((ReadParameter)processStep, exampleAdapterProvider);
        }
        else if(processStep instanceof WriteParameter) {
            return new WriteParameterStep((WriteParameter)processStep, exampleAdapterProvider);
        }
        else if(processStep instanceof SetVariableStep) {
            return new SetVariableStepStep((SetVariableStep)processStep, exampleAdapterProvider);
        }
        throw new RuntimeException("Could not be converted");
    }
}
