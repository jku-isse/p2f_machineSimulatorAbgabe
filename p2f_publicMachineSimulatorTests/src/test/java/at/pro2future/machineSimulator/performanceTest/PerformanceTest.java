package at.pro2future.machineSimulator.performanceTest;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;

import OpcUaDefinition.OpcUaDefinitionPackage;
import ProcessCore.Event;
import ProcessCore.ProcessCoreFactory;
import ProcessCore.ProcessCorePackage;
import ProcessCore.ReadVariableCapability;
import ProcessCore.WriteVariableCapability;
import Simulator.MachineSimulator;
import Simulator.SimulatorPackage;
import at.pro2future.machineSimulator.testInfrastructure.ExampleAdapterProvider;
import at.pro2future.machineSimulator.P2fSimulator;
import at.pro2future.shopfloors.adapters.EventInstance;
import at.pro2future.shopfloors.interfaces.impl.FileDataSource;
import at.pro2future.simulator.configuration.CommonObjects;
import at.pro2future.simulator.configuration.ConfigurationUtil;

@RunWith(JUnit4.class)
@DisplayName("Testing using JUnit 5")
public class PerformanceTest {
    
    static ExampleAdapterProvider exampleAdapterProvider;
    
    @BeforeAll                                         
    public static void setUp() throws Exception {
        FileDataSource fileDataSource = new FileDataSource("src/test/resources/simulators/defaultSimulator.xmi"); 
        //register the generated model/code with the following line
        fileDataSource.registerPackage(SimulatorPackage.eNS_URI, SimulatorPackage.eINSTANCE);
        fileDataSource.registerPackage(ProcessCorePackage.eNS_URI, ProcessCorePackage.eINSTANCE);
        fileDataSource.registerPackage(OpcUaDefinitionPackage.eNS_URI, OpcUaDefinitionPackage.eINSTANCE);
        
        MachineSimulator machineSimulator;
        List<EObject> roots = fileDataSource.getShopfloorData();
        machineSimulator = (MachineSimulator) roots.get(0);
            
        P2fSimulator p2fSimulator = new P2fSimulator(machineSimulator.getOpcUaServerInterface(), machineSimulator.getAdressSpaceActions());
        p2fSimulator.startup();
        exampleAdapterProvider = new ExampleAdapterProvider();
        p2fSimulator.registerWithEngine(exampleAdapterProvider);
        
        //Sent test event (wait until setup is complete).
        Event event = ProcessCoreFactory.eINSTANCE.createEvent();
        event.setName("Speed Changed Event");
        event.setRole(CommonObjects.DefaultAssignment);
        event.getParameters().add(ConfigurationUtil.initializeLocalVariable("Speed", "Integer", 0));
        EventInstance evventInstance = new EventInstance(event);
        evventInstance.parameters.add(ConfigurationUtil.initializeLocalVariable("Speed", "Integer", 0));
        
        exampleAdapterProvider.enqueueEvent(evventInstance);
    }
    
    @RepeatedTest(50)
    public void msWriteEventAdressSpaceActionTest() {
        Event event = ProcessCoreFactory.eINSTANCE.createEvent();
        event.setName("Speed Changed Event");
        event.setRole(CommonObjects.DefaultAssignment);
        event.getParameters().add(ConfigurationUtil.initializeLocalVariable("Speed", "Integer", 0));
        
        
        EventInstance evventInstance = new EventInstance(event);
        evventInstance.parameters.add(ConfigurationUtil.initializeLocalVariable("Speed", "Integer", 0));
        
        exampleAdapterProvider.enqueueEvent(evventInstance);
        assertTrue(true);
    }
    
    @RepeatedTest(50)
    public void msReadEventAdressSpaceActionTest() {
        Event event = ProcessCoreFactory.eINSTANCE.createEvent();
        event.setName("Tool Changed Event");
        event.setRole(CommonObjects.DefaultAssignment);
        event.getParameters().add(ConfigurationUtil.initializeLocalVariable("_tool", "String", "none"));
        
        EventInstance evventInstance = new EventInstance(event);
        evventInstance.parameters.add(ConfigurationUtil.initializeLocalVariable("_tool", "String", "none"));
        
        exampleAdapterProvider.enqueueEvent(evventInstance);
        assertTrue(true);
    }
    
    @RepeatedTest(50)
    public void msMethodEventAdressSpaceActionTest() {
        Event event = ProcessCoreFactory.eINSTANCE.createEvent();
        event.setName("Call Method Event");
        event.setRole(CommonObjects.DefaultAssignment);
        
        EventInstance evventInstance = new EventInstance(event);
        exampleAdapterProvider.enqueueEvent(evventInstance);
        assertTrue(true);
    }
    
    @RepeatedTest(50)
    public void readCapabilityAdressSpaceActionTest() {
        ReadVariableCapability capability = ProcessCoreFactory.eINSTANCE.createReadVariableCapability();
        capability.setID("ReadSpeedCapability");
        capability.setName("Read Speed capability");
        capability.getOutputs().add(ConfigurationUtil.initializeLocalVariable("Speed", "Integer", 0));
        
        exampleAdapterProvider.invokeCapability(capability, Lists.newArrayList(ConfigurationUtil.initializeLocalVariable("Speed", "Integer", 0)));
        assertTrue(true);
    }
    
    @RepeatedTest(50)
    public void writeCapabilityAdressSpaceActionTest() {
        WriteVariableCapability capability = ProcessCoreFactory.eINSTANCE.createWriteVariableCapability();
        capability.setID("WriteSpeedCapability");
        capability.setName("Read Speed capability");
        capability.getInputs().add(ConfigurationUtil.initializeLocalVariable("Speed", "Integer", 0));
        
        exampleAdapterProvider.invokeCapability(capability, Lists.newArrayList(ConfigurationUtil.initializeLocalVariable("Speed", "Integer", 0)));
        assertTrue(true);
    }
    
    @RepeatedTest(50)
    public void callMethodCapabilityAdressSpaceActionTest() {
        WriteVariableCapability capability = ProcessCoreFactory.eINSTANCE.createWriteVariableCapability();
        capability.setID("WriteNotingCapability");
        capability.setName("Write Noting Capability");
        
        exampleAdapterProvider.invokeCapability(capability, Lists.newArrayList());
        assertTrue(true);
    }
    
}
