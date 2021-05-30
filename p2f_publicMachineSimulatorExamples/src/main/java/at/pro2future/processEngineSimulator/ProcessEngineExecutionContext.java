package at.pro2future.processEngineSimulator;

import java.util.ArrayList;
import java.util.List;

import ProcessCore.Parameter;

public class ProcessEngineExecutionContext {

    private final List<Parameter> parameters = new ArrayList<>();
    
    public List<Parameter> getParameters(){
        return parameters;
    }
}
