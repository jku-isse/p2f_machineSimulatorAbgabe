package at.pro2future.machineSimulator.command;

import java.util.List;

import Simulator.ProcessOpcUaMapping;

public class ReadCommandParameters extends CommandParameters  {

    public ReadCommandParameters(List<ProcessOpcUaMapping> parameterMappings) {
        super(parameterMappings);
    }

}
