package at.pro2future.processEngineSimulator.steps;

import java.io.IOException;

import com.sun.istack.logging.Logger;

import ProcessCore.Decision;
import at.pro2futore.processEngineSimulator.steps.base.ProcessEngineStep;
import at.pro2future.machineSimulator.ExampleAdapterProvider;
import at.pro2future.processEngineSimulator.ProcessEngineExecutionContext;
import at.pro2future.processEngineSimulator.ProcessSimulator;
import at.pro2future.processEngineSimulator.helper.EvaluateCondition;

public class DecisionStep  extends ProcessEngineStep<Decision>{

    private final static Logger LOGGER = Logger.getLogger(HeadLoopStep.class);
    private final ProcessSimulator ifProcessSimluator;
    private final ProcessSimulator elseProcessSimluator;
    
    public DecisionStep(Decision decision, ExampleAdapterProvider exampleAdapterProvider) {
        super(decision, exampleAdapterProvider);
        
        ifProcessSimluator = new ProcessSimulator();
        ifProcessSimluator.registerProcess(getProcessStep().getIf(), getExampleAdapterProvider());
        elseProcessSimluator = new ProcessSimulator();
        elseProcessSimluator.registerProcess(getProcessStep().getElse(), getExampleAdapterProvider());
    }

    @Override
    public boolean executeStep(
            ProcessEngineExecutionContext processEngineStepExecutionContext ) throws IOException {
        

        
        try {
            if(EvaluateCondition.getInstance().evaluate(getProcessStep().getCondition(), processEngineStepExecutionContext)) {
                return ifProcessSimluator.execute(processEngineStepExecutionContext);
            }
            else {
                return elseProcessSimluator.execute(processEngineStepExecutionContext);
            }
        } catch (Exception e) {
            LOGGER.logSevereException(e);
            return false;
        }
    }

}
