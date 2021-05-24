package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.sdk.server.api.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsDataVariableNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.IConverter;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;

/**
 * Converts a {@link #MsDataVariableNode} to a {@link #VariableNode} and vice versa. The class uses the given 
 * factories, the {@link #OpcUaDefinitionFactory} and the {@link #IUaObjectAndBuilderProvider} to perform the transformation.
 *
 */
public class MsDataVariableNodeToDataVariableNodeConverter implements IConverter<MsDataVariableNode, VariableNode, OpcUaDefinitionFactory, IUaObjectAndBuilderProvider>{

    /**
     * The singleton instance <code>MsDataVariableNodeToDataVariableNodeConverter<code> of the converter.
     */
    private static MsDataVariableNodeToDataVariableNodeConverter instance;
    
    /**
     * Returns the only instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    public static MsDataVariableNodeToDataVariableNodeConverter getInstance() {
        if(instance == null) {
            instance = new MsDataVariableNodeToDataVariableNodeConverter();
        }
        return instance;
    }
    
    /**
     * The singleton instance <code>MsDataVariableNodeToDataVariableNodeConverter<code> of the converter.
     */
    private MsDataVariableNodeToDataVariableNodeConverter() {
    }
    
    /**
     * Creates a {@link MsDataVariableNode} from the given {@link VariableNode} by using an {@link OpcUaDefinitionFactory}.
     * 
     * @param object the <code>VariableNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>MsDataVariableNode</code>.
     * @return the corresponding <code>MsDataVariableNode</code> object from the given <code>VariableNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public MsDataVariableNode createSource(VariableNode object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException, ConversionFailureException {
        throw new ConvertionNotSupportedException();
    }

    /**
     * Creates a {@link VariableNode} from the given {@link MsDataVariableNode} by using an {@link IUaObjectAndBuilderProvider}.
     * 
     * @param object the <code>MsDataVariableNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>VariableNode</code>.
     * @return the corresponding <code>VariableNode</code> object from the given <code>MsDataVariableNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public VariableNode createTarget(MsDataVariableNode msNode, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException {
        UaVariableNode uaDataTypeNode = factory.getUaVariableNodeBuilder()
                .setNodeId(MsNodeIdToNodeIdConverter.getInstance().createTarget(msNode.getNodeId(), factory))
                .setBrowseName(MsQualifiedNameToQualifiedName.getInstance().createTarget(msNode.getBrowseName(), factory)) 
                .setDisplayName(MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDisplayName(), factory)) 
                .setDescription(MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDescription(), factory))  
                .setWriteMask(UInteger.valueOf(msNode.getWriteMask())) 
                .setUserWriteMask(UInteger.valueOf(msNode.getUserWriteMask()))
                .setAccessLevel(UByte.valueOf(msNode.getAccessLevel()))
                .setUserAccessLevel(UByte.valueOf(msNode.getUserAccessLevel()))
                .build();
        
        factory.getNodeContext().getNodeManager().addNode(uaDataTypeNode);
        
        return uaDataTypeNode;
    }
}
