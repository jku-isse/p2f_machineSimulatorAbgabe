package at.pro2future.machineSimulator.command;

import java.util.List;

import OpcUaDefinition.MsNodeId;
import Simulator.ProcessOpcUaMapping;

public class CallMethodCommandParameters extends CommandParameters  {

    private final MsNodeId callesMethod;
    private final MsNodeId objectContainingMethod;
    private final List<ProcessOpcUaMapping> returnParameterMappings;

    MsNodeId getCallesMethod() {
        return this.callesMethod;
    }

    MsNodeId getObjectContainingMethod() {
        return this.objectContainingMethod;
    }
    
    List<ProcessOpcUaMapping> getReturnParameterMappings() {
        return this.returnParameterMappings;
    }

    public CallMethodCommandParameters(List<ProcessOpcUaMapping> parameterMappings,
            MsNodeId objectContainingMethod, MsNodeId callesMethod, 
            List<ProcessOpcUaMapping> returnParameterMappings) {
        super(parameterMappings);
        this.callesMethod = callesMethod;
        this.objectContainingMethod = objectContainingMethod;
        this.returnParameterMappings = returnParameterMappings;
    }
}
