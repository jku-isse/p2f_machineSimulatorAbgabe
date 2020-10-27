package at.pro2future.machineSimulator;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.emf.ecore.EObject;

import OpcUaDefinition.OpcUaDefinitionPackage;
import ProcessCore.Event;
import ProcessCore.ProcessCorePackage;
import Simulator.MachineSimulator;
import Simulator.SimulatorPackage;
import at.pro2future.shopfloors.adapters.EventInstance;
import at.pro2future.shopfloors.interfaces.impl.FileDataSource;

public class StartupSimulator {

	public static void main(String[] args) {
		FileDataSource pers = new FileDataSource("src/main/resources/simulators/defaultSimulator.xmi"); 

		//register the generated model/code with the following line
		pers.registerPackage(SimulatorPackage.eNS_URI, SimulatorPackage.eINSTANCE);
		pers.registerPackage(ProcessCorePackage.eNS_URI, ProcessCorePackage.eINSTANCE);
		pers.registerPackage(OpcUaDefinitionPackage.eNS_URI, OpcUaDefinitionPackage.eINSTANCE);
				
		MachineSimulator root;
		try {
			System.out.println("Pro2Future OpcUa Machine Simulator: startup in progress.");
			List<EObject> roots = pers.getShopfloorData();
			root = (MachineSimulator) roots.get(0);
			
			OpcUaServerManager serverManager = new OpcUaServerManager(root.getInstanceInformation(), root.getOpcUaServerInterface());
			serverManager.startup();
					
			P2fActionManager actionManager = new P2fActionManager(root.getActions(), serverManager.getOpcUaNamespaceManager().getUaBuilderFactory());
			actionManager.startup();
			
			System.out.println("Pro2Future OpcUa Machine Simulator: successfully started.");
			
	        System.in.read();
	        Event callMethodEvent = root.getActions().get(2).getRefersTo();
	        EventInstance callMethodEventInstance = new EventInstance(callMethodEvent);
	        callMethodEventInstance.parameters = callMethodEvent.getParameters();
	        actionManager.getAdapterEventProvider().enqueueEvent(callMethodEventInstance);
	        System.out.println("Pro2Future OpcUa Machine Simulator: call method event sent.");
	        
			System.in.read();
			Event writeEvent = root.getActions().get(0).getRefersTo();
	        EventInstance writeEventInstance = new EventInstance(writeEvent);
	        writeEventInstance.parameters = writeEvent.getParameters();
	        writeEvent.getParameters().get(0).setValue(5);
	        actionManager.getAdapterEventProvider().enqueueEvent(writeEventInstance);
	        System.out.println("Pro2Future OpcUa Machine Simulator: write event sent.");
	        
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
