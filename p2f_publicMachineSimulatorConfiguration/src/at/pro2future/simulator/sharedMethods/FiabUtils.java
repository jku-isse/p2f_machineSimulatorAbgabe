package at.pro2future.simulator.sharedMethods;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import OpcUaDefinition.MsNodeId;
import OpcUaDefinition.MsVariableNode;
import OpcUaDefinition.MsVariableTypeNode;
import ProcessCore.AbstractCapability;
import ProcessCore.Event;
import Simulator.MsClientInterface;
import Simulator.MsInstanceInformation;
import Simulator.MsMethodEventAdressSpaceAction;
import Simulator.MsReadCapabilityAdressSpaceAction;
import Simulator.MsReadEventAdressSpaceAction;
import Simulator.SimulatorFactory;
import at.pro2future.simulator.configuration.ConfigurationUtil;

public class FiabUtils {

    private static final MsInstanceInformation FIAB_IOSTATION = SimulatorFactory.eINSTANCE.createMsInstanceInformation();
    private static final MsClientInterface HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_CLIENT_INTERFACE;
    private static final MsNodeId HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_NODE_ID ;
    private static final MsNodeId HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_RESET_NODE_ID;
    private static final MsNodeId HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_INIT_HANDOVER_NODE_ID;
    private static final MsNodeId HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_START_HANDOVER_NODE_ID;
    private static final MsNodeId HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_COMPLETE_NODE_ID;
    private static final MsNodeId HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_STATE;
    
    static {
        FIAB_IOSTATION.setDisplayName("FIABiostation");
        FIAB_IOSTATION.setHost("localhost");
        FIAB_IOSTATION.setPort(4840);
        FIAB_IOSTATION.setPath("milo");
        HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_CLIENT_INTERFACE = SimulatorFactory.eINSTANCE.createMsClientInterface();
        HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_CLIENT_INTERFACE.setInstanceInformation(FiabUtils.FIAB_IOSTATION);
        HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_NODE_ID = ConfigurationUtil.createMsNodeId(true, "VirtualInputStation1/IOSTATION/HANDSHAKE_FU_DefaultServerSideHandshake");
        HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_RESET_NODE_ID = ConfigurationUtil.createMsNodeId(true, "VirtualInputStation1/IOSTATION/HANDSHAKE_FU_DefaultServerSideHandshake/Reset");
        HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_INIT_HANDOVER_NODE_ID = ConfigurationUtil.createMsNodeId(true, "VirtualInputStation1/IOSTATION/HANDSHAKE_FU_DefaultServerSideHandshake/INIT_HANDOVER");
        HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_START_HANDOVER_NODE_ID = ConfigurationUtil.createMsNodeId(true, "VirtualInputStation1/IOSTATION/HANDSHAKE_FU_DefaultServerSideHandshake/START_HANDOVER");
        HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_COMPLETE_NODE_ID = ConfigurationUtil.createMsNodeId(true, "VirtualInputStation1/IOSTATION/HANDSHAKE_FU_DefaultServerSideHandshake/Complete");
        HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_STATE = ConfigurationUtil.createMsNodeId(true, "VirtualInputStation1/IOSTATION/HANDSHAKE_FU_DefaultServerSideHandshake/STATE");
    }
    
    public static List<EObject> getFiabMethodsDefaultObects() {
        return Arrays.asList(
                FIAB_IOSTATION
        );        
    } 
    
    public static MsMethodEventAdressSpaceAction msMethodEventReset(Event refersTo, Event returnEvent) {
        return msMethodEventInternal(refersTo, returnEvent, HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_RESET_NODE_ID);
    }
    
    public static MsMethodEventAdressSpaceAction msMethodEventInitHandover(Event refersTo, Event returnEvent) {
        return msMethodEventInternal(refersTo, returnEvent, HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_INIT_HANDOVER_NODE_ID);
    }
    
    public static MsMethodEventAdressSpaceAction msMethodEventStartHandover(Event refersTo, Event returnEvent) {
        return msMethodEventInternal(refersTo, returnEvent, HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_START_HANDOVER_NODE_ID);
    }
    
    public static MsMethodEventAdressSpaceAction msMethodEventComplete(Event refersTo, Event returnEvent) {
        return msMethodEventInternal(refersTo, returnEvent, HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_COMPLETE_NODE_ID);
    }
    
    
    public static MsReadCapabilityAdressSpaceAction msReadCapabilityState(AbstractCapability readCapablitly) {
        MsVariableNode nodeToRead = ConfigurationUtil.createMsDataVariableNode(HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_STATE);
        MsReadCapabilityAdressSpaceAction action = ConfigurationUtil.msReadCapabilityAdressSpaceAction(HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_CLIENT_INTERFACE, readCapablitly, Arrays.asList(nodeToRead), SimulatorFactory.eINSTANCE.createMsReadCapabilityAdressSpaceAction()); 
        return action;
    }
    
    private static MsMethodEventAdressSpaceAction msMethodEventInternal(Event refersTo, Event returnEvent, MsNodeId nodeId) {
        MsMethodEventAdressSpaceAction action = ConfigurationUtil.msEventAdressSpaceAction(HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_CLIENT_INTERFACE, refersTo, Arrays.asList(), SimulatorFactory.eINSTANCE.createMsMethodEventAdressSpaceAction()); 
        action.setObjectContainingMethod(HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_NODE_ID);
        action.setCallesMethod(nodeId);
        action.setReturnEvent(returnEvent);
        
        return action;
    }
    
    public static List<EObject> getMsMethodEventAdressSpaceActionDefaultObects() {
        return Arrays.asList(
                HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_CLIENT_INTERFACE,
                HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_NODE_ID,
                HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_RESET_NODE_ID,
                HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_INIT_HANDOVER_NODE_ID,
                HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_START_HANDOVER_NODE_ID,
                HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_COMPLETE_NODE_ID,
                HANDSHAKE_FU_DEFAULT_SERVER_SIDE_HANDSHAKE_STATE
                
        );        
    }
}
