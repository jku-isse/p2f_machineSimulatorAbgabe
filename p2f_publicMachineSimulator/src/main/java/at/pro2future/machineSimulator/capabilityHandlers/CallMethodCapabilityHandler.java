package at.pro2future.machineSimulator.capabilityHandlers;

import Simulator.MsCallMethodCapabilityAdressSpaceAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.command.CallMethodCommand;
import at.pro2future.machineSimulator.command.CallMethodCommandParameters;

public  class CallMethodCapabilityHandler extends BaseCapabilityHanlder<MsCallMethodCapabilityAdressSpaceAction>{

    public CallMethodCapabilityHandler(OpcUaClientManager opcUaClientManager,
            MsCallMethodCapabilityAdressSpaceAction msCallMethodCapabilityAdressSpaceAction) {
        super(opcUaClientManager, msCallMethodCapabilityAdressSpaceAction,   
             new CallMethodCommand(opcUaClientManager,
                     new CallMethodCommandParameters(msCallMethodCapabilityAdressSpaceAction.getParameterMappings(),
                             msCallMethodCapabilityAdressSpaceAction.getObjectContainingMethod(), 
                             msCallMethodCapabilityAdressSpaceAction.getCallesMethod(), 
                             msCallMethodCapabilityAdressSpaceAction.getReturnParameterMapping())));
    }

}
