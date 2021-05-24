package at.pro2future.machineSimulator.command;

import java.util.List;

import Simulator.ProcessOpcUaMapping;

/**
 * This class represents a parameter holder for the base command.
 *
 */
public abstract class CommandParameters {

    private final List<ProcessOpcUaMapping> parameterMappings;
    
    public List<ProcessOpcUaMapping> getParameterMappings() {
        return this.parameterMappings;
    }
    
    protected CommandParameters(List<ProcessOpcUaMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }
}
