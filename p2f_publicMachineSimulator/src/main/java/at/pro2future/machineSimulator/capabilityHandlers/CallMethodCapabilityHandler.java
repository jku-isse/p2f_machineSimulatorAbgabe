package at.pro2future.machineSimulator.capabilityHandlers;

import Simulator.MsCallMethodCapabilityAdressSpaceAction;
import at.pro2future.machineSimulator.OpcUaClientManager;

public class CallMethodCapabilityHandler extends BaseCapabilityHanlder<MsCallMethodCapabilityAdressSpaceAction>{

    public CallMethodCapabilityHandler(OpcUaClientManager opcUaClientManager,
            MsCallMethodCapabilityAdressSpaceAction msCallMethodCapabilityAdressSpaceAction) {
        super(opcUaClientManager, msCallMethodCapabilityAdressSpaceAction, null);
    }

}
