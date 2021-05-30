package at.pro2future.machineSimulator;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import at.pro2future.shopfloors.interfaces.impl.FileDataSource;

import org.eclipse.emf.ecore.EObject;

import OpcUaDefinition.OpcUaDefinitionPackage;
import ProcessCore.ProcessCorePackage;
import Simulator.MachineSimulator;
import Simulator.SimulatorPackage;
import at.pro2future.processEngineSimulator.ProcessEngineExecutionContext;
import at.pro2future.processEngineSimulator.ProcessSimulator;

/**
 * This class provides the infrastructure for starting up the simulator. It loads
 * the configuration from an given file, registers the packages to parse the configuration and 
 * starts the OPC-UA components.
 * 
 *
 */
public class P2fSimulatorMain {
   
    
    /**
     * The main method loads the simulator configuration from a configuration file and
     * wires up the simulator. 
     * 
     * @param args
     */
    public static void main(String[] args) {
        FileDataSource fileDataSource = new FileDataSource("src/main/resources/simulators/" + args[0] + ".xmi"); 
        //register the generated model/code with the following line
        fileDataSource.registerPackage(SimulatorPackage.eNS_URI, SimulatorPackage.eINSTANCE);
        fileDataSource.registerPackage(ProcessCorePackage.eNS_URI, ProcessCorePackage.eINSTANCE);
        fileDataSource.registerPackage(OpcUaDefinitionPackage.eNS_URI, OpcUaDefinitionPackage.eINSTANCE);
        
        try {
            MachineSimulator machineSimulator;
            List<EObject> roots = fileDataSource.getShopfloorData();
            machineSimulator = (MachineSimulator) roots.get(0);
            
            P2fSimulator p2fSimulator = new P2fSimulator(machineSimulator.getOpcUaServerInterface(), machineSimulator.getAdressSpaceActions());
            p2fSimulator.startup();
            
            ExampleAdapterProvider exampleAdapterProvider = new ExampleAdapterProvider();
            p2fSimulator.registerWithEngine(exampleAdapterProvider);
            ProcessSimulator processEngineSimulator = new ProcessSimulator();
            processEngineSimulator.registerProcess(machineSimulator.getStateMachine(), exampleAdapterProvider);
            ProcessEngineExecutionContext executionContext = new ProcessEngineExecutionContext();
            processEngineSimulator.execute(executionContext);
            /*
            for(int i = 0; i < machineSimulator.getAdressSpaceActions().size(); i++) {
                if(machineSimulator.getAdressSpaceActions().get(i) instanceof MsEventAdressSpaceAction) {
                    System.in.read();
                    Event event = ((MsEventAdressSpaceAction)machineSimulator.getAdressSpaceActions().get(i)).getRefersTo();
                    EventInstance eventInstance = new EventInstance(event);
                    eventInstance.parameters = event.getParameters();
                    exampleAdapterProvider.enqueueEvent(eventInstance);
                    System.out.println("Pro2Future OpcUa Machine Simulator: " + event.getName() + " sent.");
                }
            }*/
            
            
            final CompletableFuture<Void> future2 = new CompletableFuture<>();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> future2.complete(null)));
            future2.get();
            
        } catch(Exception e) {
            e.printStackTrace();
        }     
    }
}
