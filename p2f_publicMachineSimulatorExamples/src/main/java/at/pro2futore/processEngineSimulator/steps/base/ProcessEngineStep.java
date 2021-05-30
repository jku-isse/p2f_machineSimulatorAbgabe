package at.pro2futore.processEngineSimulator.steps.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.NameNotFoundException;

import org.eclipse.emf.ecore.util.EcoreUtil;

import ProcessCore.Parameter;
import ProcessCore.ProcessStep;
import at.pro2future.machineSimulator.ExampleAdapterProvider;
import at.pro2future.processEngineSimulator.ProcessEngineExecutionContext;

/**
 * Determines the interface how a step execution must look link.
 * @author johannstoebich
 *
 */
public abstract class ProcessEngineStep<T extends ProcessStep> {

    private final T processStep;
    private final ExampleAdapterProvider exampleAdapterProvider;
    
    public T getProcessStep() {
        return processStep;
    }
    
    public ExampleAdapterProvider getExampleAdapterProvider() {
        return exampleAdapterProvider;
    }
    
    public ProcessEngineStep(T processStep, ExampleAdapterProvider exampleAdapterProvider) {
        this.processStep = processStep;
        this.exampleAdapterProvider = exampleAdapterProvider;
    }
    
    /**
     * Executes an step and returns wheaten the execution was successful. An execution might change an processEngineStepExecutionContext.
     * 
     * @param exampleAdapterProvider an adapterProvider to connect other programs to the engine.
     * @param processEngineStepExecutionContext the execution context of this engine.
     * @return
     * @throws IOException 
     * @throws NameNotFoundException 
     */
    public abstract boolean executeStep(ProcessEngineExecutionContext processEngineStepExecutionContext) throws IOException, NameNotFoundException;

    
    
    /**
     * Finds parameters in a given list of parameters and overtakes their value 
     * @param parameters
     * @param processEngineStepExecutionContext
     * @return
     * @throws NameNotFoundException 
     */
    protected List<Parameter> findParameters(Collection<Parameter> paramtersToFind, List<Parameter> parametersToSearch) throws NameNotFoundException {
        List<Parameter> paramters = new ArrayList<>();
        for(Parameter parameter : paramtersToFind) {
            Parameter contextParameter = findParameter(parameter, parametersToSearch);
            if(contextParameter != null) {
                paramters.add(EcoreUtil.copy(findParameter(parameter, parametersToSearch)));
            }
            else {
                paramters.add(parameter);
            }
        }
        return paramters;
    }
    
    /**
     * Finds a parameter by a given type definiton and value definition from an context.
     * @param paramter
     * @param processEngineStepExecutionContext
     * @return
     * @throws NameNotFoundException 
     */
    protected Parameter findParameter(Parameter paramter, List<Parameter> parameters) throws NameNotFoundException {
        for(Parameter contextParameter : parameters) {
            if(contextParameter.getName().equals(paramter.getName()) &&
                    contextParameter.getType().equals(paramter.getType())) {
                return contextParameter;
            }
        }
        return null;
    }
    
    /**
     * Adds a parameter list to a context or adapts it values.
     * @param parameters
     * @param processEngineStepExecutionContext
     * @return
     * @throws NameNotFoundException 
     */
    protected void setParametersToContext(Collection<Parameter> parameters, ProcessEngineExecutionContext processEngineStepExecutionContext) throws NameNotFoundException {
        for(Parameter parameter : parameters ) {
            setParameterToContext(parameter, processEngineStepExecutionContext);
        }
    }
    
    /**
     * Adds a parameter  to a context or adaptes it values.
     * @param parameters
     * @param processEngineStepExecutionContext
     * @return
     * @throws NameNotFoundException 
     */
    protected void setParameterToContext(Parameter parameter, ProcessEngineExecutionContext processEngineStepExecutionContext) throws NameNotFoundException {
        Parameter contextParameter = findParameter(parameter, processEngineStepExecutionContext.getParameters());
        if(contextParameter != null) {
            contextParameter.setValue(parameter.getValue());
        }
        else {
            processEngineStepExecutionContext.getParameters().add(parameter);
        }
    }
    

}
