package at.pro2future.simulator.sharedMethods;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import OpcUaDefinition.MsNodeId;
import ProcessCore.Event;
import Simulator.MsClientInterface;
import Simulator.MsInstanceInformation;
import Simulator.MsMethodEventAdressSpaceAction;
import Simulator.SimulatorFactory;
import at.pro2future.simulator.configuration.ConfigurationUtil;

public class FiabMethods {

    private static final MsInstanceInformation FIAB_IOSTATION = SimulatorFactory.eINSTANCE.createMsInstanceInformation();
    private static final MsClientInterface HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_CLIENT_INTERFACE;
    private static final MsNodeId HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_NODE_ID ;
    private static final MsNodeId HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_START_HANDOVER_NODE_ID;
    
    static {
        FIAB_IOSTATION.setDisplayName("FIABiostation");
        FIAB_IOSTATION.setHost("localhost");
        FIAB_IOSTATION.setPort(4840);
        FIAB_IOSTATION.setPath("milo");
        HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_CLIENT_INTERFACE = SimulatorFactory.eINSTANCE.createMsClientInterface();
        HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_CLIENT_INTERFACE.setInstanceInformation(FiabMethods.FIAB_IOSTATION);
        HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_NODE_ID = ConfigurationUtil.createMsNodeId(true, "VirtualInputStation1/IOSTATION/HANDSHAKE_FU_DefaultServerSideHandshake");
        HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_START_HANDOVER_NODE_ID = ConfigurationUtil.createMsNodeId(true, "VirtualInputStation1/IOSTATION/HANDSHAKE_FU_DefaultServerSideHandshake/START_HANDOVER");
    }
    
    public static List<EObject> getFiabMethodsDefaultObects() {
        return Arrays.asList(
                FIAB_IOSTATION
        );        
    } 
    
    public static MsMethodEventAdressSpaceAction msMethodEventAdressSpaceAction(Event refersTo, Event returnEvent) {
        MsMethodEventAdressSpaceAction action = ConfigurationUtil.msEventAdressSpaceAction(HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_CLIENT_INTERFACE, refersTo, Arrays.asList(), SimulatorFactory.eINSTANCE.createMsMethodEventAdressSpaceAction()); 
        action.setObjectContainingMethod(HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_NODE_ID);
        action.setCallesMethod(HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_START_HANDOVER_NODE_ID);
        action.setReturnEvent(returnEvent);
        
        return action;
    }
    
    public static List<EObject> getMsMethodEventAdressSpaceActionDefaultObects() {
        return Arrays.asList(
                HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_CLIENT_INTERFACE,
                HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_NODE_ID,
                HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_START_HANDOVER_NODE_ID
        );        
    }
}
