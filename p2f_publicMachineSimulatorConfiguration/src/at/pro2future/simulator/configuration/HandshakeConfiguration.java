package at.pro2future.simulator.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;

import com.google.common.collect.Lists;

import ProcessCore.AbstractLoop;
import ProcessCore.Condition;
import ProcessCore.Constant;
import ProcessCore.Event;
import ProcessCore.LocalVariable;
import ProcessCore.Operator;
import ProcessCore.ProcessCoreFactory;
import ProcessCore.ReadVariableCapability;
import Simulator.MachineSimulator;
import Simulator.MsServerInterface;
import Simulator.SimulatorFactory;
import at.pro2future.simulator.sharedMethods.FiabUtils;

public class HandshakeConfiguration implements Supplier<List<EObject>> {

    MachineSimulator sim;

    private final List<EObject> uncontainedObjects = new ArrayList<>();
    
    @Override
    public List<EObject> get() {
        return uncontainedObjects;
    }


    public HandshakeConfiguration() {
        // setup simulator
        this.sim = SimulatorFactory.eINSTANCE.createMachineSimulator();
        this.sim.setName("Hansdshake-Simulator");
        
        // setup variables
        LocalVariable _status = ConfigurationUtil.initializeLocalVariable("_status", "String", null); //the same local varialbe
        Constant _status_ready = ConfigurationUtil.initializeConstance("_status_ready", "String", "READY_LOADED");
        
        //setup events and capabiliest
        Event resetFiabEvent = ProcessCoreFactory.eINSTANCE.createEvent();
        resetFiabEvent.setName("Reset Fiab Event");
        resetFiabEvent.setRole(CommonObjects.DefaultAssignment);
        
        Event initHandoverFiabEvent = ProcessCoreFactory.eINSTANCE.createEvent();
        initHandoverFiabEvent.setName("Init Handover Fiab Event");
        initHandoverFiabEvent.setRole(CommonObjects.DefaultAssignment);
        
        ReadVariableCapability readVariableCapability = ProcessCoreFactory.eINSTANCE.createReadVariableCapability();
        readVariableCapability.setID("ReadVariableCapability");
        readVariableCapability.setName("Read variable capability");
        readVariableCapability.getOutputs().add(_status);
        
        Event startHandoverFiabEvent = ProcessCoreFactory.eINSTANCE.createEvent();
        startHandoverFiabEvent.setName("Start Handover Event");
        startHandoverFiabEvent.setRole(CommonObjects.DefaultAssignment);
        
        Event completeFiabEvent = ProcessCoreFactory.eINSTANCE.createEvent();
        completeFiabEvent.setName("Complete Fiab Event");
        completeFiabEvent.setRole(CommonObjects.DefaultAssignment);
        
        // setup process
        ProcessCore.Process mainProcess = ConfigurationUtil.initializeProcess("MainProcess");
        mainProcess.getSteps().add(ConfigurationUtil.initializeEventSource("Reset Fiab", resetFiabEvent, null));
        mainProcess.getSteps().add(ConfigurationUtil.initializeHumanStep("Wait1", null));
        mainProcess.getSteps().add(ConfigurationUtil.initializeEventSource("Init Handover Fiab Event", initHandoverFiabEvent, null));
        mainProcess.getSteps().add(ConfigurationUtil.initializeHumanStep("Wait2", null));
        
        ProcessCore.Process readVariable =  ConfigurationUtil.initializeProcess("InvokeCapability");
        readVariable.getSteps().add(ConfigurationUtil.initializeReadParameter("ReadStatus", readVariableCapability, Lists.newArrayList(_status)));
        Condition statusNotReadyLoaded  = ConfigurationUtil.initializeSimpleCondition(_status, Operator.NEQ, _status_ready);
        AbstractLoop while_status_status_ready = ConfigurationUtil.initializeHeadLoop("while _status != _status_ready", readVariable, statusNotReadyLoaded);
        mainProcess.getSteps().add(while_status_status_ready);
        mainProcess.getSteps().add(ConfigurationUtil.initializeEventSource("Start Handover Event", startHandoverFiabEvent, null));
        mainProcess.getSteps().add(ConfigurationUtil.initializeHumanStep("Wait3", null));
        mainProcess.getSteps().add(ConfigurationUtil.initializeEventSource("Complete Fiab Event", completeFiabEvent, null));

        this.sim.setStateMachine(mainProcess);

        // setup opcua interface
        MsServerInterface opcUaServerInterface = SimulatorFactory.eINSTANCE.createMsServerInterface();
        opcUaServerInterface.setInstanceInformation(CommonObjects.HandshakeInstanceInformation);
        
        // MsMethodNode        
        this.sim.setOpcUaServerInterface(opcUaServerInterface);
        this.sim.eClass();
        // this.sim.eSet(SimulatorPackage.eINSTANCE.getMachineSimulator_Opcuaserverinterface(),
        // opcUaServerInterface);

        // setup actions
        this.sim.getAdressSpaceActions().add(FiabUtils.msMethodEventReset(resetFiabEvent, null));
        this.sim.getAdressSpaceActions().add(FiabUtils.msMethodEventInitHandover(initHandoverFiabEvent, null));
        this.sim.getAdressSpaceActions().add(FiabUtils.msMethodEventStartHandover(startHandoverFiabEvent, null));
        this.sim.getAdressSpaceActions().add(FiabUtils.msMethodEventComplete(completeFiabEvent, null));
        this.sim.getAdressSpaceActions().add(FiabUtils.msReadCapabilityState(readVariableCapability));
        
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
        uncontainedObjects.add(_status_ready);
        uncontainedObjects.add(_status);
        uncontainedObjects.add(readVariableCapability);
        uncontainedObjects.add(resetFiabEvent);
        uncontainedObjects.add(initHandoverFiabEvent);
        uncontainedObjects.add(startHandoverFiabEvent);
        uncontainedObjects.add(completeFiabEvent);
        uncontainedObjects.addAll(FiabUtils.getFiabMethodsDefaultObects());
        uncontainedObjects.addAll(FiabUtils.getMsMethodEventAdressSpaceActionDefaultObects());
    }

    
}
