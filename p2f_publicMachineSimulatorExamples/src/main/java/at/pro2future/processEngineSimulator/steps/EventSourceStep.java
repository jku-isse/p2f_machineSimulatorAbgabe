package at.pro2future.processEngineSimulator.steps;

import java.io.IOException;
import java.util.List;

import javax.naming.NameNotFoundException;

import com.sun.istack.logging.Logger;

import ProcessCore.EventSource;
import ProcessCore.Parameter;
import at.pro2future.machineSimulator.ExampleAdapterProvider;
import at.pro2future.processEngineSimulator.ProcessEngineExecutionContext;
import at.pro2future.shopfloors.adapters.EventInstance;

public class EventSourceStep extends CapabilityInvocationStep<EventSource> {

    private final static Logger LOGGER = Logger.getLogger(EventSourceStep.class);
    /**
     * This step waits for a given event to occur.
     * 
     * @param eventSource the source config this event.
     */
    public EventSourceStep(EventSource eventSource, ExampleAdapterProvider exampleAdapterProvider) {
       super(eventSource, exampleAdapterProvider);
    }
    
    @Override
    public boolean executeStep(
            ProcessEngineExecutionContext processEngineStepExecutionContext) throws IOException, NameNotFoundException {
        if(!super.executeStep(processEngineStepExecutionContext)) {
            return false;
        }
        
        List<Parameter> possibleEventParamters = handleInputMappings(getProcessStep().getInputMappings(), processEngineStepExecutionContext);
        List<Parameter> parameters = findParameters(this.getProcessStep().getEvent().getParameters(), possibleEventParamters);
        
        EventInstance instance = new EventInstance(this.getProcessStep().getEvent());
        instance.parameters = parameters;
        getExampleAdapterProvider().enqueueEvent(instance);
        
        LOGGER.info("Event " +  instance.eventType.getName() + " with paramters " + parameters + " sent.");
        
        return true;
    }
}
