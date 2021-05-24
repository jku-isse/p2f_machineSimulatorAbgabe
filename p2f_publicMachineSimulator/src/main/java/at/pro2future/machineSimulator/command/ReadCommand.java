package at.pro2future.machineSimulator.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import ProcessCore.Parameter;
import Simulator.ProcessOpcUaMapping;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;
import at.pro2future.machineSimulator.converter.opcUaToMilo.MsNodeIdToNodeIdConverter;

public class ReadCommand extends BaseCommand<ReadCommandParameters>{

    public ReadCommand(OpcUaClientManager opcUaClientManager, ReadCommandParameters commandParameters) {
        super(opcUaClientManager, commandParameters);
    }

    @Override
    public List<Parameter> execute(List<Parameter> inputParameters) throws ConvertionNotSupportedException, ConversionFailureException, InterruptedException, ExecutionException {
        
        List<Parameter> parameters = new ArrayList<Parameter>();
        for(ProcessOpcUaMapping processOpcUaMapping : getCommandParameters().getParameterMappings()) {
            parameters.add(hanldeProcessOpcUaMapping(processOpcUaMapping));
        }
        
        return parameters;
    }


    private Parameter hanldeProcessOpcUaMapping(ProcessOpcUaMapping processOpcUaMapping) throws ConvertionNotSupportedException, ConversionFailureException, InterruptedException, ExecutionException   {
        NodeId nodeId = MsNodeIdToNodeIdConverter.getInstance().createTarget(processOpcUaMapping.getAttributeNodeId(), getOpcUaClientManager().getUaObjectAndBuilderProvider());
        DataValue dataValue = getOpcUaClientManager().readValue(nodeId);
        
        Parameter parameterCopy = EcoreUtil.copy(processOpcUaMapping.getParameter());
        parameterCopy.setValue(dataValue.getValue().getValue());
        return parameterCopy;
    }

}
