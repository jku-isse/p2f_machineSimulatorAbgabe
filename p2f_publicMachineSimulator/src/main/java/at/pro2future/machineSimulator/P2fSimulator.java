package at.pro2future.machineSimulator;

import java.io.IOException;
import java.util.List;

import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Simulator.MachineSimulator;
import Simulator.MsAdressSpaceAction;
import Simulator.MsServerInterface;
import at.pro2future.machineSimulator.eventHandlers.HandlerCanNotBeCreatedException;
import at.pro2future.shopfloors.adapters.AdapterEventProvider;
import at.pro2future.shopfloors.adapters.EngineAdapter;

/**
 * This class represents the machine simulator. It can contains an {@link OpcUaServerManager} and 
 * an {@link P2fAdressSpaceActionManager}. The server manager is responsible for starting up the server
 * interface. The action manager is responsible for connecting to the server interface with the OPC-UA client.
 * 
 */
public class P2fSimulator extends AbstractLifecycle{
    
    private final static Logger LOGGER = LoggerFactory.getLogger(P2fSimulator.class); 
    
    private final OpcUaServerManager serverManager;
    private final P2fAdressSpaceActionManager actionManager;
    
    /**
     * Returns an {@link EngineAdapter} which allows sending and receiving events from the simulator. 
     * An {@link EngineAdapter} also allows to call capabilites.
     */
    public List<EngineAdapter> getEngineAdapters(){
        return this.actionManager.getEngineAdapters();
    }
    
    /**
     * Register this machine simulator with an {@link AdapterEventProvider}. This will wire up the conneciton 
     * to the engine.
     * 
     * @param adapterEventProvider the new adapterEventProvider where the actions will be registered.
     */
    public void registerWithEngine(AdapterEventProvider adapterEventProvider) {
        for(EngineAdapter engineAdpater : getEngineAdapters()) {
            adapterEventProvider.registerEngineAdapter(engineAdpater); 
            ((P2fAdressSpaceActionAdapter<?>)engineAdpater).registerWithEngineClean(adapterEventProvider);
        }
    }
    
    /**
     * Register this machine simulator with an {@link AdapterEventProvider}. This will wire up the conneciton 
     * to the engine.
     * 
     * @param adapterEventProvider the new adapterEventProvider where the actions will be registered.
     */
    public void deregisterWithEngine(AdapterEventProvider adapterEventProvider) {
        for(EngineAdapter engineAdpater : getEngineAdapters()) {
            ((P2fAdressSpaceActionAdapter<?>)engineAdpater).deregisterWithEngine(adapterEventProvider);
            adapterEventProvider.deregisterEngineAdapter(engineAdpater);
           
        }
    }
    
    /**
     * This is the main class of an P2fSimulator. The configuration is provided by an {@link MachineSimulator}.
     * @param opcUaServerInterface the server interface of the machine simulator.
     * @param msAdressSpaceActions the action which can be executed by the server.
     * @throws IOException
     * @throws UaException
     * @throws HandlerCanNotBeCreatedException
     */
    public P2fSimulator(MsServerInterface opcUaServerInterface, List<MsAdressSpaceAction> msAdressSpaceActions) throws IOException, UaException, HandlerCanNotBeCreatedException {
        this.serverManager = new OpcUaServerManager(opcUaServerInterface);
        this.actionManager = new P2fAdressSpaceActionManager(msAdressSpaceActions, this.serverManager.getOpcUaNamespaceManager().getUaObjectAndBuilderProvider());
    }

    /**
     * Starts the machine simulator. 
     */
    @Override
    public void onStartup() {
        LOGGER.info("Pro2Future OpcUa Machine Simulator: startup in progress.");
        this.serverManager.startup();
        this.actionManager.startup();
        LOGGER.info("Pro2Future OpcUa Machine Simulator: successfully started.");
    }

    /**
     * Stops the machine simulator.
     */
    @Override
    public void onShutdown() {
        LOGGER.info("Pro2Future OpcUa Machine Simulator: shut down in progress.");
        this.actionManager.shutdown();
        this.serverManager.shutdown();
        LOGGER.info("Pro2Future OpcUa Machine Simulator: successfully shut down.");
    }
}
