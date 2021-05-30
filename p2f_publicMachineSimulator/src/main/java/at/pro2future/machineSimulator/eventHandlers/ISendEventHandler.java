package at.pro2future.machineSimulator.eventHandlers;

import java.util.ArrayList;
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

    final  List<AdapterEventProvider> adapterEventProviders = new ArrayList<>();
    /**
     * Returns the {@link AdapterEventProvider}s that is able to communicate with the Pro2Futre Engine. One can enqueue an {@link EventInstance} or
     * register to changes with these adapters.
     * 
     * @return the AdapterEventProvider .
     */
     default List<AdapterEventProvider> getAdapterEventProviders(){
         return adapterEventProviders;
     }
    
    
    /**
     * Adds an {@link AdapterEventProvider} that is able to communicate with the Pro2Futre Engine. One can enqueue an {@link EventInstance} or
     * register to changes with these adapters.
     * 
     * @param adapterEventProvider the event provider which is required for sending the event.
     */
    default void registerAdapterEventProvider(AdapterEventProvider adapterEventProvider) {
        adapterEventProviders.add(adapterEventProvider);
    }

    /**
     * Removes an {@link AdapterEventProvider} that is able to communicate with the Pro2Futre Engine. One can enqueue an {@link EventInstance} or
     * register to changes with these adapters.
     * 
     * @param adapterEventProvider the event provider which is required for sending the event.
     */
    default void deregisterEngineAdapter(AdapterEventProvider adapterEventProvider) {
        adapterEventProviders.remove(adapterEventProvider);
    }
    
    /**
     * Sends an {@link EventInstance} of the given {@link Event} to an Pro2FutureEngine. The content of the event is populated 
     * by the given list of {@link Parameter}.
     * 
     * @param event the <code>Event</code> of which an <code>EventInstance</code> should be sent to the Pro2FutureEngine.
     * @param parameters the parameters of the event.
     */
    default void sendEvent(Event event, List<Parameter> parameters){
        if(event != null) {
            EventInstance eventInstance = new EventInstance(event);
            eventInstance.parameters = parameters;
            
            sendEventInstance(eventInstance);
        }
    }
    
    /**
     * Sends an <code>EventInstance</code> to a Pro2FutureEngine. The {@link EventInstance} which should 
     * be sent to an Pro2FutureEngine.
     * 
     * @param eventInstance the <code>EventInstance</code> which should be sent to the Pro2FutureEngine.
     */
    default void sendEventInstance(EventInstance eventInstance){
       for(AdapterEventProvider adapterEventProvider : this.getAdapterEventProviders()) {
           adapterEventProvider.enqueueEvent(eventInstance);
       }
    }
}
