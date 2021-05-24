package at.pro2future.machineSimulator.capabilityHandlers;

import Simulator.MsReadCapabilityAdressSpaceAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.command.ReadCommand;
import at.pro2future.machineSimulator.command.ReadCommandParameters;

public class ReadCapabilityHandler extends BaseCapabilityHanlder<MsReadCapabilityAdressSpaceAction>{

    public ReadCapabilityHandler(OpcUaClientManager opcUaClientManager,
            MsReadCapabilityAdressSpaceAction msReadCapabilityAdressSpaceAction) {
        super(opcUaClientManager, 
                msReadCapabilityAdressSpaceAction,
                new ReadCommand(
                    opcUaClientManager,
                    new ReadCommandParameters(msReadCapabilityAdressSpaceAction.getParameterMappings())
                ));
    }
    
    

}
