package at.pro2future.processEngineSimulator.steps;

import java.io.IOException;

import com.sun.istack.logging.Logger;

import ProcessCore.HeadLoop;
import at.pro2futore.processEngineSimulator.steps.base.ProcessEngineStep;
import at.pro2future.machineSimulator.ExampleAdapterProvider;
import at.pro2future.processEngineSimulator.ProcessEngineExecutionContext;
import at.pro2future.processEngineSimulator.ProcessSimulator;
import at.pro2future.processEngineSimulator.helper.EvaluateCondition;

public class HeadLoopStep extends ProcessEngineStep<HeadLoop> {

    private final static Logger LOGGER = Logger.getLogger(HeadLoopStep.class);
    
    private final ProcessSimulator processSimulator ;
    
    public HeadLoopStep(HeadLoop headLoop, ExampleAdapterProvider exampleAdapterProvider) {
        super(headLoop, exampleAdapterProvider);
        
        processSimulator = new ProcessSimulator();
        processSimulator.registerProcess(getProcessStep().getBody(), getExampleAdapterProvider());
    }

    @Override
    public boolean executeStep(
            ProcessEngineExecutionContext processEngineStepExecutionContext) throws IOException {
        
        try {
            while(EvaluateCondition.getInstance().evaluate(getProcessStep().getCondition(),  processEngineStepExecutionContext)) {
                processSimulator.execute(processEngineStepExecutionContext);
                Thread.sleep(100);
            }
            LOGGER.info("Finised " + getProcessStep().getDisplayName() +  ".");
        } catch (Exception e) {
            LOGGER.logSevereException(e);
            return false;
        }
        
        return true;
    }

}
