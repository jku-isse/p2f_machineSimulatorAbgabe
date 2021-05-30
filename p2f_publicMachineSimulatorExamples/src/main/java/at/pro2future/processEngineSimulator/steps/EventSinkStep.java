package at.pro2future.processEngineSimulator.steps;

import java.io.IOException;
import java.util.List;

import javax.naming.NameNotFoundException;

import com.sun.istack.logging.Logger;

import ProcessCore.Assignment;
import ProcessCore.Event;
import ProcessCore.EventSink;
import ProcessCore.Parameter;
import at.pro2future.machineSimulator.ExampleAdapterProvider;
import at.pro2future.processEngineSimulator.ProcessEngineExecutionContext;
import at.pro2future.shopfloors.adapters.EventHandler;
import at.pro2future.shopfloors.adapters.EventInstance;

/**
 * This step waits for a given event to occur.
 */
public class EventSinkStep extends CapabilityInvocationStep<EventSink> implements EventHandler{
    
    private final static Logger LOGGER = Logger.getLogger(EventSinkStep.class);
    
    private EventInstance eventInstance;
    
    /**
     * This step waits for a given event to occur.
     * 
     * @param eventSink the sink configuring this event.
     */
    public EventSinkStep(EventSink eventSink, ExampleAdapterProvider exampleAdapterProvider) {
        super(eventSink, exampleAdapterProvider);
        exampleAdapterProvider.registerEventHandler(this);
    }
    
    @Override
    public boolean executeStep(ProcessEngineExecutionContext processEngineStepExecutionContext) throws IOException, NameNotFoundException {
        
        if(!super.executeStep(processEngineStepExecutionContext)) {
            return false;
        }
        
        try {
            while(eventInstance == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LOGGER.info("Event " +  eventInstance.eventType.getName() +  " with paramters " + eventInstance.parameters + " received ");
            List<Parameter> outputParameter = handleOutputMappings(eventInstance.parameters, getProcessStep().getOutputMappings());
            setParametersToContext(outputParameter, processEngineStepExecutionContext);
            
        }
        catch(Exception exc) {
            LOGGER.logSevereException(exc);
            return false;
        }
        finally {
            eventInstance = null;
        }
        
        return true;
    }
    
    @Override
    public void handleEvent(EventInstance e) {
        eventInstance = e;
    }
    @Override
    public EventInstance getEvent() {
        return eventInstance;
    }
    @Override
    public Event getEventType() {
        return this.getProcessStep().getEvent();
    }
    @Override
    public Assignment getRole() {
        return this.getProcessStep().getEvent().getRole();
    }
    
}
