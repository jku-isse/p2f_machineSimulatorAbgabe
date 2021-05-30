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
import ProcessCore.Decision;
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

public class ToolControlConfiguration implements Supplier<List<EObject>> {


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

    public ToolControlConfiguration() {
        // setup simulator
        this.sim = SimulatorFactory.eINSTANCE.createMachineSimulator();
        this.sim.setName("ToolControl");
        
        // setup instance information
        MsClientInterface opcUaClientInterface = SimulatorFactory.eINSTANCE.createMsClientInterface();
        opcUaClientInterface.setInstanceInformation(CommonObjects.ToolControlInstanceInformation);     

        // setup process
        LocalVariable _tool = ConfigurationUtil.initializeLocalVariable("_tool", "String", 0);
        LocalVariable _start = ConfigurationUtil.initializeLocalVariable("_start", "Boolean", false);
        LocalVariable _millingStartEvent = ConfigurationUtil.initializeLocalVariable("_millingStartEvent", "Boolean", true);
        LocalVariable _millingStopEvent = ConfigurationUtil.initializeLocalVariable("_millingStopEvent", "Boolean", false);
        
        Event toolChangedEvent = ConfigurationUtil.initializeEvent("ToolChangedEvent", CommonObjects.DefaultAssignment, _tool);
        Event startEvent = ConfigurationUtil.initializeEvent("StartEvent", CommonObjects.DefaultAssignment, _start);
        Event millingStartEvent = ConfigurationUtil.initializeEvent("MillingStartEvent", CommonObjects.DefaultAssignment, _millingStartEvent);
        Event millingStopEvent = ConfigurationUtil.initializeEvent("MillingStopEvent", CommonObjects.DefaultAssignment, _millingStopEvent);
        ProcessCore.Process mainProcess = setupMainProcess(toolChangedEvent, _tool, startEvent, _start, millingStartEvent, millingStopEvent);
        this.sim.setStateMachine(mainProcess);
        

        // setup opcua interface
        MsObjectTypeNode objectType = ConfigurationUtil.initializeMsObjectTypeNode("RootType", "RootType", "RootType");
        MsPropertyNode tool = ConfigurationUtil.initializeMsPropertyNode("Tool", "Tool", "Tool",  "Tool", ConfigurationUtil.createMsNodeId(true), 0, CommonObjects.StringDataType, CommonObjects.ModellingRuleMandatory);
        MsPropertyNode start = ConfigurationUtil.initializeMsPropertyNode("Start", "Start", "Start",  "Start", ConfigurationUtil.createMsNodeId(true), 0, CommonObjects.BooleanDataType, CommonObjects.ModellingRuleMandatory);
        MsPropertyNode isMilling = ConfigurationUtil.initializeMsPropertyNode("IsMilling", "IsMilling", "IsMilling",  "IsMilling", ConfigurationUtil.createMsNodeId(true), 0, CommonObjects.BooleanDataType, CommonObjects.ModellingRuleMandatory);
        objectType.getHasComponent().addAll(Arrays.asList(tool, start, isMilling));
        
        MsObjectNode object = ConfigurationUtil.initializeMsObjectNode("Root", "Root", "Root");
        object.setHasTypeDefinition(objectType);
        MsVariableNode opcUaTool = ConfigurationUtil.copyWithNewNodeId(tool, true);
        MsVariableNode opcUaStart = ConfigurationUtil.copyWithNewNodeId(start, true);
        MsVariableNode opcUaIsMilling = ConfigurationUtil.copyWithNewNodeId(isMilling, true);
        object.getHasComponent().addAll(Arrays.asList(opcUaTool, opcUaStart, opcUaIsMilling));
        
        
        MsObjectNode baseFolder = ConfigurationUtil.initializeMsObjectNode("BaseFolder", "BaseFolder", "BaseFolder");
        baseFolder.setHasTypeDefinition(CommonObjects.FolderType);
        baseFolder.getOrganizes().add(object);
        
        MsServerInterface opcUaServerInterface = SimulatorFactory.eINSTANCE.createMsServerInterface();
        opcUaServerInterface.getNodes().add(baseFolder);
        opcUaServerInterface.setInstanceInformation(CommonObjects.ToolControlInstanceInformation);
        this.sim.setOpcUaServerInterface(opcUaServerInterface);
        
        this.sim.getAdressSpaceActions().add(ConfigurationUtil.msEventAdressSpaceAction(opcUaClientInterface, toolChangedEvent, Arrays.asList(opcUaTool), SimulatorFactory.eINSTANCE.createMsReadEventAdressSpaceAction()));
        this.sim.getAdressSpaceActions().add(ConfigurationUtil.msEventAdressSpaceAction(opcUaClientInterface, startEvent, Arrays.asList(opcUaStart), SimulatorFactory.eINSTANCE.createMsReadEventAdressSpaceAction()));
        this.sim.getAdressSpaceActions().add(ConfigurationUtil.msEventAdressSpaceAction(opcUaClientInterface, millingStartEvent, Arrays.asList(opcUaIsMilling), SimulatorFactory.eINSTANCE.createMsWriteEventAdressSpaceAction()));
        this.sim.getAdressSpaceActions().add(ConfigurationUtil.msEventAdressSpaceAction(opcUaClientInterface, millingStopEvent, Arrays.asList(opcUaIsMilling), SimulatorFactory.eINSTANCE.createMsWriteEventAdressSpaceAction()));
                        
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
        uncontainedObjects.add(toolChangedEvent);
        uncontainedObjects.add(startEvent);
        uncontainedObjects.add(millingStartEvent);
        uncontainedObjects.add(millingStopEvent);
        uncontainedObjects.add(objectType);
    }

    private ProcessCore.Process setupMainProcess(Event toolChangedEvent, LocalVariable _tool, Event startEvent, LocalVariable _start, Event millingStartEvent, Event millingStopEvent) {    
        ProcessCore.Process milling = ConfigurationUtil.initializeProcess("Milling");
        //TODO: check if sending event works
        milling.getSteps().add(ConfigurationUtil.initializeEventSource("MillingStartEvent Event Source", millingStartEvent, Arrays.asList(CommonObjects.True)));
        //TODO: add delay
        milling.getSteps().add(ConfigurationUtil.initializeEventSource("MillingStopEvent Event Source", millingStopEvent, Arrays.asList(CommonObjects.True)));
        
        ProcessCore.Process waitStartEventProcess = ConfigurationUtil.initializeProcess("Wait Start event");
        waitStartEventProcess.getSteps().add(ConfigurationUtil.initializeEventSink("start Event Sink", startEvent, Arrays.asList(_start)));
        Condition _startTrue  = ConfigurationUtil.initializeSimpleCondition(_start, Operator.EQ, CommonObjects.True);
        Decision check_start = ConfigurationUtil.initializeDecision("Start", milling, waitStartEventProcess, _startTrue);
        ProcessCore.Process check_startProcess = ConfigurationUtil.initializeProcess("check_startProcess");
        check_startProcess.getSteps().add(check_start); 
        
        ProcessCore.Process waitToolChangedEventProcess = ConfigurationUtil.initializeProcess("Wait tool changed event");
        waitToolChangedEventProcess.getSteps().add(ConfigurationUtil.initializeEventSink("tool changed event sink", toolChangedEvent, Arrays.asList( _tool)));
        Condition _toolNull  = ConfigurationUtil.initializeSimpleCondition(_tool, Operator.EQ, CommonObjects.NullString);
        AbstractLoop while_toolNullDoNoting = ConfigurationUtil.initializeHeadLoop("while _tool null", waitToolChangedEventProcess, _toolNull);
        Condition _toolNotNull  = ConfigurationUtil.initializeSimpleCondition(_tool, Operator.NEQ, CommonObjects.NullString);
        AbstractLoop while_toolNotNullCheckStarted = ConfigurationUtil.initializeHeadLoop("while _tool not null check _start", check_startProcess, _toolNotNull);
    
        ProcessCore.Process subProcess = ConfigurationUtil.initializeProcess("SubProcess");
        subProcess.getSteps().add(while_toolNullDoNoting); // wait until there is a tool
        subProcess.getSteps().add(while_toolNotNullCheckStarted); // check condition as long as there is a tool.
        
        ProcessCore.Process mainProcess = ConfigurationUtil.initializeProcess("MainProcess");
        Condition alwaysTrue  = ConfigurationUtil.initializeSimpleCondition(CommonObjects.True, Operator.EQ, CommonObjects.True);
        mainProcess.getSteps().add(ConfigurationUtil.initializeHeadLoop("Root loop", subProcess, alwaysTrue)); // execute the subprocess forever.
        return mainProcess;
    }
}