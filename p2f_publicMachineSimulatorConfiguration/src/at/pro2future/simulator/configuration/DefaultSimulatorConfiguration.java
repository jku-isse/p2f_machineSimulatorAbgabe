package at.pro2future.simulator.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import ProcessCore.Constant;
import ProcessCore.Event;
import ProcessCore.LocalVariable;
import ProcessCore.ProcessCoreFactory;
import Simulator.MachineSimulator;
import Simulator.MsClientInterface;
import Simulator.MsMethodEventAdressSpaceAction;
import OpcUaDefinition.MsMethodNode;
import OpcUaDefinition.MsObjectNode;
import OpcUaDefinition.MsObjectTypeNode;
import OpcUaDefinition.MsPropertyNode;
import Simulator.MsServerInterface;
import OpcUaDefinition.MsVariableNode;
import Simulator.SimulatorFactory;
import at.pro2future.simulator.sharedMethods.FiabMethods;

public class DefaultSimulatorConfiguration implements Supplier<List<EObject>> {

    MachineSimulator sim;

    private final List<EObject> uncontainedObjects = new ArrayList<>();
    
    @Override
    public List<EObject> get() {
        return uncontainedObjects;
    }


    public DefaultSimulatorConfiguration() {
        // setup simulator
        this.sim = SimulatorFactory.eINSTANCE.createMachineSimulator();
        this.sim.setName("DefaultSimulator");
        
        // setup instance information
        MsClientInterface opcUaClientInterface = SimulatorFactory.eINSTANCE.createMsClientInterface();
        opcUaClientInterface.setInstanceInformation(CommonObjects.DefaultSimulatorInstanceInformation);
        
        // setup process
        LocalVariable _tool = ConfigurationUtil.initializeLocalVariable("_tool", "String", "none");
        Constant none = ConfigurationUtil.initializeConstance("None", "String", "none");
        Constant _welding = ConfigurationUtil.initializeConstance("_welding", "String", "welding");
        LocalVariable speed = ConfigurationUtil.initializeLocalVariable("Speed", "Integer", 0);
        Constant zero = ConfigurationUtil.initializeConstance("zero", "Integer", 0);
        Constant five = ConfigurationUtil.initializeConstance("five", "Integer", 5);

        ProcessCore.Process mainProcess = ConfigurationUtil.initializeProcess("MainProcess");
        mainProcess.getSteps().add(ConfigurationUtil.initializeSetVariableStep("Sets a welding tool", _tool, _welding));
        mainProcess.getSteps().add(ConfigurationUtil.initializeSetVariableStep("Start welding", speed, five));
        mainProcess.getSteps().add(ConfigurationUtil.initializeSetVariableStep("Stop welding", speed, zero));
        mainProcess.getSteps().add(ConfigurationUtil.initializeSetVariableStep("To start position", _tool, none));

        this.sim.setStateMachine(mainProcess);

        // setup opcua interface
        MsObjectTypeNode objectType = ConfigurationUtil.initializeMsObjectTypeNode("RootType", "RootType", "RootType");
        MsPropertyNode opcUaSpeed = ConfigurationUtil.initializeMsPropertyNode("Speed", "Speed", "Speed",  "Speed", ConfigurationUtil.createMsNodeId(true), 0, CommonObjects.IntegerDataType, CommonObjects.ModellingRuleMandatory);
        MsPropertyNode opcUaTool = ConfigurationUtil.initializeMsPropertyNode("Tool", "Tool", "Tool", "Tool", ConfigurationUtil.createMsNodeId(true),  "none", CommonObjects.StringDataType, CommonObjects.ModellingRuleMandatory);
        MsMethodNode methodNode = ConfigurationUtil.initializeMsMethodNode("NullMethod", "NullMethod", "NullMethod",
                ConfigurationUtil.createMsNodeId(true, "Method"),  true, true, new ArrayList<MsVariableNode>(),  new ArrayList<MsVariableNode>(),
            "public Variant[] testMethod(Variant[] test){"
            + "     return new Variant[0]; "
            + "}");
        objectType.getHasComponent().add(opcUaSpeed);
        objectType.getHasComponent().add(opcUaTool);
        objectType.getHasComponent().add(methodNode);
        
                
        MsObjectNode object = ConfigurationUtil.initializeMsObjectNode("Root", "Root", "Root");
        object.setHasTypeDefinition(objectType);
        MsVariableNode opcUaSpeedVariable = ConfigurationUtil.copyWithNewNodeId(opcUaSpeed, true);
        MsVariableNode opcUaToolVariable = ConfigurationUtil.copyWithNewNodeId(opcUaTool, true);
        MsMethodNode methodNodeVariable = ConfigurationUtil.copyWithNewNodeId(methodNode, true);
        object.getHasComponent().add(opcUaSpeedVariable);
        object.getHasComponent().add(opcUaToolVariable);
        object.getHasComponent().add(methodNodeVariable);
        
        
        MsObjectNode baseFolder = ConfigurationUtil.initializeMsObjectNode("BaseFolder", "BaseFolder", "BaseFolder");
        baseFolder.setHasTypeDefinition(CommonObjects.FolderType);
        baseFolder.getOrganizes().add(object);
        
        MsServerInterface opcUaServerInterface = SimulatorFactory.eINSTANCE.createMsServerInterface();
        opcUaServerInterface.getNodes().add(baseFolder);
        opcUaServerInterface.setInstanceInformation(CommonObjects.DefaultSimulatorInstanceInformation);
        
        // MsMethodNode        
        
        this.sim.setOpcUaServerInterface(opcUaServerInterface);
        this.sim.eClass();
        // this.sim.eSet(SimulatorPackage.eINSTANCE.getMachineSimulator_Opcuaserverinterface(),
        // opcUaServerInterface);

        // setup actions
        Event callMethodEvent = ProcessCoreFactory.eINSTANCE.createEvent();
        callMethodEvent.setName("Call Method Event2");
        callMethodEvent.setRole(CommonObjects.DefaultAssignment);
        Event sendEventAferMethodCall = ProcessCoreFactory.eINSTANCE.createEvent();
        sendEventAferMethodCall.setName("Send Event after method call");
        sendEventAferMethodCall.setRole(CommonObjects.DefaultAssignment);
        MsMethodEventAdressSpaceAction methodActionFIABiostation = FiabMethods.msMethodEventAdressSpaceAction(callMethodEvent, sendEventAferMethodCall);
        this.sim.getAdressSpaceActions().add(methodActionFIABiostation);
        
        Event speedChangedEvent = ProcessCoreFactory.eINSTANCE.createEvent();
        speedChangedEvent.setName("Speed Changed Event");
        speedChangedEvent.setRole(CommonObjects.DefaultAssignment);
        speedChangedEvent.getParameters().add(speed);
        this.sim.getAdressSpaceActions().add(ConfigurationUtil.msEventAdressSpaceAction(opcUaClientInterface, speedChangedEvent, Arrays.asList(opcUaSpeedVariable), SimulatorFactory.eINSTANCE.createMsWriteEventAdressSpaceAction()));
        
        Event toolChangedEvent = ProcessCoreFactory.eINSTANCE.createEvent();
        toolChangedEvent.setName("Tool Changed Event");
        toolChangedEvent.setRole(CommonObjects.DefaultAssignment);
        toolChangedEvent.getParameters().add(_tool);
        this.sim.getAdressSpaceActions().add(ConfigurationUtil.msEventAdressSpaceAction(opcUaClientInterface, toolChangedEvent, Arrays.asList(opcUaToolVariable), SimulatorFactory.eINSTANCE.createMsReadEventAdressSpaceAction()));
        
        Event callMethodEvent2 = ProcessCoreFactory.eINSTANCE.createEvent();
        callMethodEvent2.setName("Call Method Event");
        callMethodEvent2.setRole(CommonObjects.DefaultAssignment);
        MsMethodEventAdressSpaceAction methodAction = ConfigurationUtil.msEventAdressSpaceAction(opcUaClientInterface, callMethodEvent2, Arrays.asList(), SimulatorFactory.eINSTANCE.createMsMethodEventAdressSpaceAction());
        methodAction.setCallesMethod(methodNode.getNodeId());
        methodAction.setObjectContainingMethod(object.getNodeId());
        this.sim.getAdressSpaceActions().add(methodAction);
        
        // validate sim after setup to find out errors
        Diagnostic validate = Diagnostician.INSTANCE.validate(this.sim);
        if (Diagnostic.ERROR == validate.getSeverity()) {
            throw new RuntimeException(validate.toString());
        }
        else if(Diagnostic.OK != validate.getSeverity()) {
            System.out.print(getClass() + ": " + validate.toString());
        }

        // add all objects which do not have a container (are not target of an contained
        // reference)
        uncontainedObjects.add(this.sim);
        uncontainedObjects.addAll(CommonObjects.getAllDefaultObects());
        uncontainedObjects.add(opcUaClientInterface);
        uncontainedObjects.add(objectType);
        uncontainedObjects.add(five);
        uncontainedObjects.add(_tool);
        uncontainedObjects.add(none);
        uncontainedObjects.add(_welding);
        uncontainedObjects.add(speed);
        uncontainedObjects.add(zero);
        uncontainedObjects.add(five);
        uncontainedObjects.add(callMethodEvent2);
        uncontainedObjects.add(speedChangedEvent);
        uncontainedObjects.add(toolChangedEvent);
        uncontainedObjects.add(sendEventAferMethodCall);
        uncontainedObjects.add(callMethodEvent);
        uncontainedObjects.addAll(FiabMethods.getFiabMethodsDefaultObects());
        uncontainedObjects.addAll(FiabMethods.getMsMethodEventAdressSpaceActionDefaultObects());
    }

    
}
