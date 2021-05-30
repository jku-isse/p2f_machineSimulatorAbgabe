package at.pro2future.simulator.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import ProcessCore.AbstractLoop;
import ProcessCore.Condition;
import ProcessCore.Event;
import ProcessCore.LocalVariable;
import ProcessCore.Operator;
import Simulator.MachineSimulator;
import Simulator.MsClientInterface;
import OpcUaDefinition.MsObjectNode;
import OpcUaDefinition.MsObjectTypeNode;
import OpcUaDefinition.MsPropertyNode;
import Simulator.MsServerInterface;
import OpcUaDefinition.MsVariableNode;
import Simulator.SimulatorFactory;

public class WorkpieceControlConfiguration implements Supplier<List<EObject>> {

    MachineSimulator sim;
    
    private final List<EObject> uncontainedObjects = new ArrayList<>();

    public MachineSimulator getSim() {
        return sim;
    }

    public List<EObject> getUncontainedObjects() {
        return uncontainedObjects;
    }
    
    @Override
    public List<EObject> get() {
        return uncontainedObjects;
    }

    public WorkpieceControlConfiguration() {
        // setup simulator
        this.sim = SimulatorFactory.eINSTANCE.createMachineSimulator();
        this.sim.setName("WorkpieceControl");
        
        // setup instance information
        MsClientInterface opcUaClientInterface = SimulatorFactory.eINSTANCE.createMsClientInterface();
        opcUaClientInterface.setInstanceInformation(CommonObjects.WorkpieceControlInstanceInformation);

        // setup process
        LocalVariable _workpiece = ConfigurationUtil.initializeLocalVariable("_workpiece", "String", "");
        Event workpieceChangedEvent = ConfigurationUtil.initializeEvent("WorkpiceChangedEvent", CommonObjects.DefaultAssignment, _workpiece);
        ProcessCore.Process mainProcess = setupMainProcess(workpieceChangedEvent,_workpiece);
        this.sim.setStateMachine(mainProcess);
        
        // setup opcua interface
        MsObjectTypeNode objectType = ConfigurationUtil.initializeMsObjectTypeNode("RootType", "RootType", "RootType");
        MsPropertyNode workpiece = ConfigurationUtil.initializeMsPropertyNode("Workpiece", "Workpiece", "Workpiece",  "Workpiece", ConfigurationUtil.createMsNodeId(true), "", CommonObjects.StringDataType, CommonObjects.ModellingRuleMandatory);
        objectType.getHasComponent().add(workpiece);
        
        MsObjectNode object = ConfigurationUtil.initializeMsObjectNode("Root", "Root", "Root");
        object.setHasTypeDefinition(objectType);
        MsVariableNode opcUaWorkpiece = ConfigurationUtil.copyWithNewNodeId(workpiece, true);
        object.getHasComponent().add(opcUaWorkpiece);
        
        MsObjectNode baseFolder = ConfigurationUtil.initializeMsObjectNode("BaseFolder", "BaseFolder", "BaseFolder");
        baseFolder.setHasTypeDefinition(CommonObjects.FolderType);
        baseFolder.getOrganizes().add(object);
        
        MsServerInterface opcUaServerInterface = SimulatorFactory.eINSTANCE.createMsServerInterface();
        opcUaServerInterface.getNodes().add(baseFolder);
        opcUaServerInterface.setInstanceInformation(CommonObjects.WorkpieceControlInstanceInformation);
        this.sim.setOpcUaServerInterface(opcUaServerInterface);
        
        this.sim.getAdressSpaceActions().add(ConfigurationUtil.msEventAdressSpaceAction(opcUaClientInterface, workpieceChangedEvent, Arrays.asList(opcUaWorkpiece), SimulatorFactory.eINSTANCE.createMsReadEventAdressSpaceAction()));
                        
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
        uncontainedObjects.add(workpieceChangedEvent);
        uncontainedObjects.add(objectType);
    }


    private ProcessCore.Process setupMainProcess(Event workpieceChangedEvent, LocalVariable workp) {        
        ProcessCore.Process waitWorkpieceEventProcess = ConfigurationUtil.initializeProcess("Wait Workpiece event");
        waitWorkpieceEventProcess.getSteps().add(ConfigurationUtil.initializeEventSink("workpiece Event Sink", workpieceChangedEvent, Arrays.asList(workp)));
        Condition _workpieceNull  = ConfigurationUtil.initializeSimpleCondition(workp, Operator.EQ, CommonObjects.NullString);
        AbstractLoop while_workpieceNullDoNoting = ConfigurationUtil.initializeHeadLoop("while _workpiece null", waitWorkpieceEventProcess, _workpieceNull);
        
        
        ProcessCore.Process waitWorkpieceEventProcess2 = ConfigurationUtil.initializeProcess("Wait Workpiece event");
        waitWorkpieceEventProcess2.getSteps().add(ConfigurationUtil.initializeEventSink("workpiece Event Sink", workpieceChangedEvent, Arrays.asList(workp)));
        Condition _workpieceNotNull  = ConfigurationUtil.initializeSimpleCondition(workp, Operator.NEQ, CommonObjects.NullString);
        AbstractLoop while_workpieceNotNullDoNoting = ConfigurationUtil.initializeHeadLoop("while _workpiece not null", waitWorkpieceEventProcess2, _workpieceNotNull);
    
        ProcessCore.Process subProcess = ConfigurationUtil.initializeProcess("SubProcess");
        subProcess.getSteps().add(while_workpieceNullDoNoting); // wait until there is a workpiece
        subProcess.getSteps().add(while_workpieceNotNullDoNoting); // wait until the workpiece is taken away.
        
        ProcessCore.Process mainProcess = ConfigurationUtil.initializeProcess("MainProcess");
        Condition alwaysTrue  = ConfigurationUtil.initializeSimpleCondition(CommonObjects.True, Operator.EQ, CommonObjects.True);
        mainProcess.getSteps().add(ConfigurationUtil.initializeHeadLoop("Root loop", subProcess, alwaysTrue)); // execute the subprocess forever.
        return mainProcess;
    }
}