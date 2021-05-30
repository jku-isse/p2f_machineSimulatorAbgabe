package at.pro2future.machineSimulator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.stack.core.UaException;

import Simulator.MsAdressSpaceAction;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.eventHandlers.HandlerCanNotBeCreatedException;
import at.pro2future.shopfloors.adapters.EngineAdapter;

/***
 * This action manager can register the given actions on construction. It manages that the 
 * actions are correctly register and deregisterd during startup and shutdown.
 * 
 */
class P2fAdressSpaceActionManager extends AbstractLifecycle {
    
    private final List<P2fAdressSpaceActionAdapter<?>> msAdressSpaceActionsAdapters;
    private final IUaObjectAndBuilderProvider uaObjectAndBuilderProvider;
    
    /**
     * Returns all {@link EngineAdapter} contained in this project.
     * 
     * @return
     */
    List<EngineAdapter> getEngineAdapters() {
        return new ArrayList<>(this.msAdressSpaceActionsAdapters);
    }

    /**
     * Provides an action manager where a specific set of actions will be added.
     * 
     * @param msAdressSpaceAction the list of actions that will be added.
     * @param uaObjectAndBuilderProvider the provider which creates the specific objects.
     * @throws UaException 
     * @throws HandlerCanNotBeCreatedException This exception is thrown when a {@link CallMethodHandler}, {@link ReadVariablesHandler} or {@link WriteVariablesHandler} cannot be instantiated.
     */
    P2fAdressSpaceActionManager(List<MsAdressSpaceAction> msAdressSpaceActions, IUaObjectAndBuilderProvider uaObjectAndBuilderProvider) throws UaException, HandlerCanNotBeCreatedException {
        this.uaObjectAndBuilderProvider = uaObjectAndBuilderProvider;
        this.msAdressSpaceActionsAdapters = new ArrayList<>();
        setMsAdressSpaceAction(msAdressSpaceActions);
    }
    
    private void setMsAdressSpaceAction(List<MsAdressSpaceAction> msAdressSpaceActions) throws UaException, HandlerCanNotBeCreatedException {
        this.msAdressSpaceActionsAdapters.clear();
        for(MsAdressSpaceAction msAdressSpaceAction : msAdressSpaceActions) {
            this.msAdressSpaceActionsAdapters.add(new P2fAdressSpaceActionAdapter<>(msAdressSpaceAction, this.uaObjectAndBuilderProvider));
        }
    }
    
    /**
     * Registers the actions correspondingly.
     */
    @Override
    protected void onStartup() {
        for(P2fAdressSpaceActionAdapter<?> msAdressSpaceActionsAdapter : this.msAdressSpaceActionsAdapters) {
            msAdressSpaceActionsAdapter.getOpcUaClientManager().startup();
            msAdressSpaceActionsAdapter.startup();
        }
    }

    /**
     * Unregisters the actions correspondingly.
     */
    @Override
    protected void onShutdown() {
        for(P2fAdressSpaceActionAdapter<?> msAdressSpaceActionsAdapter : this.msAdressSpaceActionsAdapters) {
            msAdressSpaceActionsAdapter.shutdown();
            msAdressSpaceActionsAdapter.getOpcUaClientManager().shutdown();
        }
    }
}
