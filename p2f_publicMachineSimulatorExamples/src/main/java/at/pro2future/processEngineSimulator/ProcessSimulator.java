package at.pro2future.processEngineSimulator;

import java.util.ArrayList;
import java.util.List;
import ProcessCore.Process;

import at.pro2futore.processEngineSimulator.steps.base.ProcessEngineStep;
import at.pro2future.machineSimulator.ExampleAdapterProvider;

public class ProcessSimulator {
    
    private final List<ProcessEngineStep<?>> processEngineSteps = new ArrayList<ProcessEngineStep<?>>();
    
    public void registerProcess(Process process, ExampleAdapterProvider exampleAdapterProvider) {
        if(process != null) {
            processEngineSteps.addAll(ProcessStepToProcessEngineStep.getInstance().convertAll(process.getSteps(), exampleAdapterProvider));
        }
    }
    
    public void registerStep(ProcessEngineStep<?> processEngineStep) {
        processEngineSteps.add(processEngineStep);
    }
    
    public void reset() {
        processEngineSteps.clear();
    }
    
    public boolean  execute(ProcessEngineExecutionContext processEngineStepExecutionContext) throws Exception {
        for(ProcessEngineStep<?> processEngineStep : processEngineSteps) {
            boolean result = processEngineStep.executeStep(processEngineStepExecutionContext);
            if(!result) {
                throw new Exception("An error occureed");
            }
        }
        return true;
    }

}
