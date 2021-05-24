package at.pro2future.machineSimulator.eventHandlers;

import java.util.List;

import ProcessCore.Event;
import ProcessCore.Parameter;
import at.pro2future.shopfloors.adapters.AdapterEventProvider;
import at.pro2future.shopfloors.adapters.EventInstance;

/**
 * This interface must be implemented by all <code>EventHandlers</code> which can send {@link Event}s.
 * A class which implements this interface should implement the {@link BaseEventHandler} as well.
 *
 */
public interface ISendEventHandler {

    /**
     * Returns the {@link AdapterEventProvider} that is able to communicate with the Pro2Futre Engine. One can enque an {@link EventInstance} or
     * register to changes with this adapter.
     * 
     * @return the AdapterEventProvider .
     */
    AdapterEventProvider getAdapterEventProvider();

    /**
     * Sets the {@link AdapterEventProvider} that is able to communicate with the Pro2Futre Engine. One can enque an {@link EventInstance} or
     * register to changes with this adapter.
     * 
     * @param adapterEventProvider the event provider which is required for sending the even.t
     */
    void setAdapterEventProvider(AdapterEventProvider adapterEventProvider);
    
    /**
     * Sends an {@link EventInstance} of the given {@link Event} to an Pro2FutureEngine. The content of the event is populated 
     * by the given list of {@link Parameter}.
     * 
     * @param event the <code>Event</code> of which an <code>EventInstance</code> should be sent to the Pro2FutureEngine.
     * @param parameters the parameters of the event.
     */
    default void sendEvent(Event event, List<Parameter> parameters){
        EventInstance eventInstance = new EventInstance(event);
        eventInstance.parameters = parameters;
        
        sendEventInstance(eventInstance);
    }
    
    /**
     * Sends an <code>EventInstance</code> to a Pro2FutureEngine. The {@link EventInstance} which should 
     * be sent to an Pro2FutureEngine.
     * 
     * @param eventInstance the <code>EventInstance</code> which should be sent to the Pro2FutureEngine.
     */
    default void sendEventInstance(EventInstance eventInstance){
        this.getAdapterEventProvider().enqueueEvent(eventInstance);
    }
}
