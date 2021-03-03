package at.pro2future.simulator.sharedMethods;

import java.util.Arrays;

import OpcUaDefinition.MsNodeId;
import ProcessCore.Event;
import Simulator.MsClientInterface;
import Simulator.MsInstanceInformation;
import Simulator.MsMethodAction;
import Simulator.SimulatorFactory;
import at.pro2future.simulator.configuration.ConfigurationUtil;

public class FIABMethods {

	public static final MsInstanceInformation FIABiostation = SimulatorFactory.eINSTANCE.createMsInstanceInformation();
	
	static {
		FIABiostation.setDisplayName("FIABiostation");
		FIABiostation.setHost("localhost");
		FIABiostation.setPort(4840);
		FIABiostation.setPath("milo");
	}
	
	public static MsClientInterface msClientInterfaceFIABiostation() {
		MsClientInterface fiabIostation = SimulatorFactory.eINSTANCE.createMsClientInterface();
		fiabIostation.setTargetInstanceInformation(FIABMethods.FIABiostation);
		return fiabIostation;
	}
	
	public static MsNodeId msObjectNodeFIABiostationNodeId() {
		return ConfigurationUtil.createMsNodeId(true, "VirtualInputStation1/IOSTATION/HANDSHAKE_FU_DefaultServerSideHandshake");
	}
	
	public static MsNodeId msMethodNodeFIABiostationNodeIdStartHandover() {
		return ConfigurationUtil.createMsNodeId(true, "VirtualInputStation1/IOSTATION/HANDSHAKE_FU_DefaultServerSideHandshake/START_HANDOVER");
	}
	
	public static MsMethodAction msMethodActionFIABiostation(Event refersTo) {
		MsMethodAction action = ConfigurationUtil.initializeAction(msClientInterfaceFIABiostation(), refersTo, Arrays.asList(), SimulatorFactory.eINSTANCE.createMsMethodAction()); 
		action.setObjectContainingMethod(msObjectNodeFIABiostationNodeId());
		action.setCallesMethod(msMethodNodeFIABiostationNodeIdStartHandover());
		
		return action;
	}
}
