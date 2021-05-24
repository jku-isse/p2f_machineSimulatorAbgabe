package at.pro2future.machineSimulator.capabilityHandlers;


import Simulator.MsWriteCapabilityAdressSpaceAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.command.WriteCommand;
import at.pro2future.machineSimulator.command.WriteCommandParameters;

public class WriteCapabilityHandler extends BaseCapabilityHanlder<MsWriteCapabilityAdressSpaceAction> {

    public WriteCapabilityHandler(OpcUaClientManager opcUaClientManager,
            MsWriteCapabilityAdressSpaceAction msCapabilityAdressSpaceAction) {
        super(opcUaClientManager,
             msCapabilityAdressSpaceAction, 
             new WriteCommand(opcUaClientManager, 
                     new WriteCommandParameters(msCapabilityAdressSpaceAction.getParameterMappings())
       ));
    }

}
