package at.pro2future.machineSimulator.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import ProcessCore.Parameter;
import Simulator.ProcessOpcUaMapping;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.converter.opcUaToMilo.MsNodeIdToNodeIdConverter;

/**
 * This command is able to update a set of parameters 
 * by using the provided configuration to a server.
 *
 */
public class WriteCommand extends BaseCommand<WriteCommandParameters>{
    
    /**
     * Creates a new write command, which is able to write parameters on to a server.
     * 
     * @param opcUaClientManager the <code>OpcUaClientManager</code> that is able to communicate with the server.
     * @param writeCommandParameters the {@link CommandParameters} which provides the configuration.
     */
    public WriteCommand(OpcUaClientManager opcUaClientManager, WriteCommandParameters writeCommandParameters) {
        super(opcUaClientManager, writeCommandParameters);
    }
    
    @Override
    public List<Parameter> execute(List<Parameter> parameters) {
        try {
            for(ProcessOpcUaMapping processOpcUaMapping : this.getCommandParameters().getParameterMappings()) {
                hanldeMapping(processOpcUaMapping, parameters);
            }
            return new ArrayList<>();
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }    
    }

    
    private void hanldeMapping(ProcessOpcUaMapping processOpcUaMapping, List<Parameter> inputParameters) throws Exception {
        Parameter parameter  = inputParameters.stream()
            .filter(p -> EcoreUtil.equals(p, processOpcUaMapping.getParameter()))
            .findAny()
            .orElseThrow();

        NodeId nodeId = MsNodeIdToNodeIdConverter.getInstance().createTarget(processOpcUaMapping.getAttributeNodeId(), getOpcUaClientManager().getUaObjectAndBuilderProvider());
        
        DataValue dataValue = new DataValue(new Variant(parameter.getValue()));
        getOpcUaClientManager().writeValues(nodeId, dataValue);
    }
}
