package at.pro2future.machineSimulator.eventHandlers;

import ProcessCore.Assignment;
import ProcessCore.Event;
import Simulator.MsEventAdressSpaceAction;
import at.pro2future.shopfloors.adapters.EventHandler;

/**
 * This interface should be implemented by all <code>EventHandlers</cod> which can receive {@link Event}s.The 
 * Event which will be received by this event handler is defined by a {@link MsAction}. All EventHandlers should also implement
 * the {@link BaseEventHandler} class as well.
 * 
 * @param <T> The configuration for this event handler.
 */
public interface IReceiveEventHandler<T extends MsEventAdressSpaceAction> extends EventHandler {
    
    /**
     * Returns the action for which the event handler is defined.
     * @return
     */
    T getMsEventAdressSpaceAction();
    
    /**
     * The {@link Event} to which the event handler listens to.
     */
    @Override
    default Event getEventType() {
        return this.getMsEventAdressSpaceAction().getRefersTo();
    }

    /**
     * The role this event handler is assigned to.
     */
    @Override
    default Assignment getRole() {
        return this.getMsEventAdressSpaceAction().getRefersTo().getRole();
    }
}
