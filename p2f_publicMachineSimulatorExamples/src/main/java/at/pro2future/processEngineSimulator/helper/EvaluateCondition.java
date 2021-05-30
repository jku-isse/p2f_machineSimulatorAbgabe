package at.pro2future.processEngineSimulator.helper;

import java.util.List;

import javax.naming.NameNotFoundException;

import ProcessCore.Condition;
import ProcessCore.Parameter;
import ProcessCore.SimpleCondition;
import at.pro2future.processEngineSimulator.ProcessEngineExecutionContext;

public class EvaluateCondition {

    /**
     * The singleton instance <code>MsDataVariableNodeToDataVariableNodeConverter<code> of the converter.
     */
    private static EvaluateCondition instance;
    
    /**
     * Returns the only instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    public static EvaluateCondition getInstance() {
        if(instance == null) {
            instance = new EvaluateCondition();
        }
        return instance;
    }
    
    public boolean evaluate(Condition condition, ProcessEngineExecutionContext processEngineStepExecutionContext) throws Exception {
        if(condition instanceof SimpleCondition) {
            SimpleCondition simpleCondition = (SimpleCondition)condition;
            
            Parameter lhsParameter =  findParameter(simpleCondition.getLhs(), processEngineStepExecutionContext.getParameters());
            Object lhs = simpleCondition.getLhs().getValue();
            if(lhsParameter != null) {
                lhs = lhsParameter.getValue();
            }
            
            Parameter rhsParameter =  findParameter(simpleCondition.getRhs(), processEngineStepExecutionContext.getParameters());
            Object rhs = simpleCondition.getRhs().getValue();
            if(rhsParameter != null) {
                rhs = rhsParameter.getValue();
            }
            
            switch(simpleCondition.getOperator()) {
                case EQ:
                    if(lhs == null && rhs == null) {
                        return true;
                    }
                    else if(lhs == null) {
                       return false;
                    }
                    return lhs.equals(rhs);
                case GEQ:
                    return ((Number)lhs).doubleValue() >= ((Number)rhs).doubleValue();
                case GT:
                    return ((Number)lhs).doubleValue() > ((Number)rhs).doubleValue();
                case LEQ:
                    return ((Number)lhs).doubleValue() <= ((Number)rhs).doubleValue();
                case LS:
                    return ((Number)lhs).doubleValue() < ((Number)rhs).doubleValue();
                case NEQ:
                    if(lhs == null && rhs == null) {
                        return false;
                    }
                    else if(lhs == null) {
                       return true;
                    }
                    return !lhs.equals(rhs);
                default:
                    throw new Exception("Evaluation of condition not supported");
             }
            
        }
        throw new Exception("Evaluation of condition failed");
    }
    
    /**
     * Finds a parameter by a given type definition and value definition from an context.
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
    
}
