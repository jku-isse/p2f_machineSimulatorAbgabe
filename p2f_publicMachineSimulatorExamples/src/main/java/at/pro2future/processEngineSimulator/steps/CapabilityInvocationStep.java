package at.pro2future.processEngineSimulator.steps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NameNotFoundException;

import com.sun.istack.logging.Logger;

import ProcessCore.AbstractCapability;
import ProcessCore.CapabilityInvocation;
import ProcessCore.Parameter;
import ProcessCore.VariableMapping;
import at.pro2futore.processEngineSimulator.steps.base.ProcessEngineStep;
import at.pro2future.machineSimulator.ExampleAdapterProvider;
import at.pro2future.processEngineSimulator.ProcessEngineExecutionContext;

public class CapabilityInvocationStep<T extends CapabilityInvocation> extends ProcessEngineStep<T>{

    private final static Logger LOGGER = Logger.getLogger(CapabilityInvocationStep.class);
    
    public CapabilityInvocationStep(T processStep, ExampleAdapterProvider exampleAdapterProvider) {
        super(processStep, exampleAdapterProvider);
    }

    @Override
    public boolean executeStep(
            ProcessEngineExecutionContext processEngineStepExecutionContext) throws IOException, NameNotFoundException {
        return true;
    }
    
    
    protected List<Parameter> handleInputMappings(List<VariableMapping> variableMappings, ProcessEngineExecutionContext processEngineStepExecutionContext) throws NameNotFoundException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        for(VariableMapping variableMapping : variableMappings) {
            parameters.add(handleInputMapping(variableMapping, processEngineStepExecutionContext));
        }
        
        return parameters;
    }
    
    protected Parameter handleInputMapping(VariableMapping variableMapping, ProcessEngineExecutionContext processEngineStepExecutionContext) throws NameNotFoundException {
        Parameter rhs = findParameter(variableMapping.getRhs(), processEngineStepExecutionContext.getParameters());
        Parameter lhs = variableMapping.getLhs();
        if(rhs != null) {
            lhs.setValue(rhs.getValue());
        }
        else if(variableMapping.getRhs() != null) {
            lhs.setValue(variableMapping.getRhs().getValue());
        }
        return lhs;
    }
    
    protected List<Parameter> handleOutputMappings(List<Parameter> outputParametes, List<VariableMapping> variableMappings) throws NameNotFoundException {
        List<Parameter> handeldList = new ArrayList<Parameter>();
        for(VariableMapping variableMapping : variableMappings) {
            handeldList.add(handleOutputMapping(variableMapping, outputParametes));
        }
        return handeldList;
    }
    
    protected Parameter handleOutputMapping(VariableMapping variableMapping, List<Parameter> outputParamters) throws NameNotFoundException {
        Parameter rhs = findParameter(variableMapping.getRhs(), outputParamters);
        Parameter lhs = variableMapping.getLhs();
        lhs.setValue(rhs.getValue());
        return lhs;
    }
    
    protected List<Parameter> invokeCapability(AbstractCapability abstractCapability, ExampleAdapterProvider exampleAdapterProvider, List<Parameter> inputParamters) {
        
        List<Parameter> outputParamters = exampleAdapterProvider.invokeCapability(abstractCapability, inputParamters);
        
        LOGGER.info("Capability " + abstractCapability.getID() + " invoked.");
        return outputParamters;
        
    }
    

}
