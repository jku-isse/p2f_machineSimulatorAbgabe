package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.sdk.server.api.nodes.VariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsVariableTypeNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.IConverter;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;

/**
 * Converts a {@link #MsVariableTypeNode} to a {@link #VariableTypeNode} and vice versa. The class uses the given 
 * factories, the {@link #OpcUaDefinitionFactory} and the {@link #IUaObjectAndBuilderProvider} to perform the transformation.
 *
 */
public class MsVariableTypeNodeToVariableTypeNodeConverter implements IConverter<MsVariableTypeNode, VariableTypeNode, OpcUaDefinitionFactory, IUaObjectAndBuilderProvider>{

    /**
     * The singleton instance <code>MsVariableTypeNodeToVariableTypeNodeConverter<code> of the converter.
     */
    private static MsVariableTypeNodeToVariableTypeNodeConverter instance;
    
    /**
     * Returns the only instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    public static MsVariableTypeNodeToVariableTypeNodeConverter getInstance() {
        if(instance == null) {
            instance = new MsVariableTypeNodeToVariableTypeNodeConverter();
        }
        return instance;
    }
    
    /**
     * The singleton instance <code>MsVariableTypeNodeToVariableTypeNodeConverter<code> of the converter.
     */
    private MsVariableTypeNodeToVariableTypeNodeConverter() {
    }
    
    /**
     * Creates a {@link MsVariableTypeNode} from the given {@link VariableTypeNode} by using an {@link OpcUaDefinitionFactory}.
     * 
     * @param object the <code>VariableTypeNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>MsVariableTypeNode</code>.
     * @return the corresponding <code>MsVariableTypeNode</code> object from the given <code>VariableTypeNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public MsVariableTypeNode createSource(VariableTypeNode object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException, ConversionFailureException {
        throw new ConvertionNotSupportedException();
    }

    /**
     * Creates a {@link VariableTypeNode} from the given {@link MsVariableTypeNode} by using an {@link IUaObjectAndBuilderProvider}.
     * 
     * @param object the <code>MsDataVariableNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>VariableTypeNode</code>.
     * @return the corresponding <code>VariableTypeNode</code> object from the given <code>MsVariableTypeNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public VariableTypeNode createTarget(MsVariableTypeNode msNode, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException {
        UaVariableTypeNode uaVariableTypeNode = factory.getUaVariableTypeNode(
                MsNodeIdToNodeIdConverter.getInstance().createTarget(msNode.getNodeId(), factory),
                MsQualifiedNameToQualifiedName.getInstance().createTarget(msNode.getBrowseName(), factory),
                MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDisplayName(), factory),
                MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDescription(), factory),  
                UInteger.valueOf(msNode.getWriteMask()),
                UInteger.valueOf(msNode.getUserWriteMask()),
                new DataValue(new Variant(msNode.getValue())),
                new NodeId(factory.getNamespaceIndex(), msNode.getDataType()),
                msNode.getValueRank(),
                null,
                msNode.getIsAbstract()
        );

        factory.getNodeContext().getNodeManager().addNode(uaVariableTypeNode);
        
        return uaVariableTypeNode;
    }

}
