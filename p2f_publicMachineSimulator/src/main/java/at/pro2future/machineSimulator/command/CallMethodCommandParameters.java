package at.pro2future.machineSimulator.command;

import java.util.List;

import OpcUaDefinition.MsNodeId;
import Simulator.ProcessOpcUaMapping;

public class CallMethodCommandParameters extends CommandParameters  {

    private final MsNodeId callesMethod;
    private final MsNodeId objectContainingMethod;
    private final List<ProcessOpcUaMapping> returnParameterMappings;

    public MsNodeId getCallesMethod() {
        return this.callesMethod;
    }

    public MsNodeId getObjectContainingMethod() {
        return this.objectContainingMethod;
    }
    
    public List<ProcessOpcUaMapping> getReturnParameterMappings() {
        return this.returnParameterMappings;
    }

    public CallMethodCommandParameters(List<ProcessOpcUaMapping> parameterMappings,
            MsNodeId callesMethod,  MsNodeId objectContainingMethod, 
            List<ProcessOpcUaMapping> returnParameterMappings) {
        super(parameterMappings);
        this.callesMethod = callesMethod;
        this.objectContainingMethod = objectContainingMethod;
        this.returnParameterMappings = returnParameterMappings;
    }
}
