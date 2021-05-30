package at.pro2future.processEngineSimulator.steps;

import java.io.IOException;

import com.sun.istack.logging.Logger;

import ProcessCore.HumanStep;
import at.pro2futore.processEngineSimulator.steps.base.ProcessEngineStep;
import at.pro2future.machineSimulator.ExampleAdapterProvider;
import at.pro2future.processEngineSimulator.ProcessEngineExecutionContext;

public class HumanStepStep  extends ProcessEngineStep<HumanStep>{

    private final static Logger LOGGER = Logger.getLogger(HumanStepStep.class);
    /**
     * This step waits for a given event to occur.
     * 
     * @param eventSink the sink configuring this event.
     */
    public HumanStepStep(HumanStep humanStep, ExampleAdapterProvider exampleAdapterProvider) {
        super(humanStep, exampleAdapterProvider);
    }

    @Override
    public boolean executeStep(
            ProcessEngineExecutionContext processEngineStepExecutionContext) throws IOException {
        LOGGER.info("Please press enter to continue.");
        System.in.read();
        return true;
    }
}
