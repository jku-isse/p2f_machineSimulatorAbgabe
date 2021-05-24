package at.pro2future.machineSimulator;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import at.pro2future.shopfloors.interfaces.impl.FileDataSource;

import org.eclipse.emf.ecore.EObject;
import OpcUaDefinition.OpcUaDefinitionPackage;
import ProcessCore.Event;
import ProcessCore.ProcessCorePackage;
import Simulator.MachineSimulator;
import Simulator.MsReadEventAdressSpaceAction;
import Simulator.SimulatorPackage;
import at.pro2future.shopfloors.adapters.EventInstance;

/**
 * This class provides the infrastructure for starting up the simulator. It loads
 * the configuration from an given file, registers the packages to parse the configuration and 
 * starts the OPC-UA components.
 * 
 *
 */
public class StartupSimulator {
    
    // Private constructor to avoid the instantiation of this class.
    private StartupSimulator() {
    }

    /**
     * The main method loads the simulator configuration from a configuration file and
     * wires up the simulator. 
     * 
     * @param args
     */
    public static void main(String[] args) {
        FileDataSource pers = new FileDataSource("src/main/resources/simulators/" + args[0] + ".xmi"); 

        //register the generated model/code with the following line
        pers.registerPackage(SimulatorPackage.eNS_URI, SimulatorPackage.eINSTANCE);
        pers.registerPackage(ProcessCorePackage.eNS_URI, ProcessCorePackage.eINSTANCE);
        pers.registerPackage(OpcUaDefinitionPackage.eNS_URI, OpcUaDefinitionPackage.eINSTANCE);
                
        MachineSimulator root;
        try {
            System.out.println("Pro2Future OpcUa Machine Simulator: startup in progress.");
            List<EObject> roots = pers.getShopfloorData();
            root = (MachineSimulator) roots.get(0);
            
            OpcUaServerManager serverManager = new OpcUaServerManager(root);
            serverManager.startup();
                    
            P2fAdressSpaceActionManager actionManager = new P2fAdressSpaceActionManager(root.getAdressSpaceActions(), serverManager.getOpcUaNamespaceManager().getUaObjectAndBuilderProvider());
            actionManager.startup();
            
            System.out.println("Pro2Future OpcUa Machine Simulator: successfully started.");
            

            for(int i = 0; i < root.getAdressSpaceActions().size(); i++) {
                if(!(root.getAdressSpaceActions().get(i) instanceof MsReadEventAdressSpaceAction)) {
                    System.in.read();
                    Event event = ((MsReadEventAdressSpaceAction)root.getAdressSpaceActions().get(i)).getRefersTo();
                    EventInstance eventInstance = new EventInstance(event);
                    eventInstance.parameters = event.getParameters();
                    actionManager.getAdapterEventProvider().enqueueEvent(eventInstance);
                    System.out.println("Pro2Future OpcUa Machine Simulator: " + event.getName() + " sent.");
                }
            }
            
            
            final CompletableFuture<Void> future2 = new CompletableFuture<>();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> future2.complete(null)));
            future2.get();
            
            System.out.println("Pro2Future OpcUa Machine Simulator: shut down in progress.");
            actionManager.shutdown();
            serverManager.shutdown();
            System.out.println("Pro2Future OpcUa Machine Simulator: successfully shut down.");
            
        } catch(Exception e) {
            e.printStackTrace();
        }    
    }
}
