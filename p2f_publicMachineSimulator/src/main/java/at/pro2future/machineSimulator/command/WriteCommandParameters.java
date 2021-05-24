package at.pro2future.machineSimulator.command;

import java.util.List;

import Simulator.ProcessOpcUaMapping;

public class WriteCommandParameters extends CommandParameters {

    public WriteCommandParameters(List<ProcessOpcUaMapping> parameterMappings) {
        super(parameterMappings);
    }

}
