package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.api.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsPropertyNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.IConverter;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;

/**
 * Converts a {@link #MsPropertyNode} to a {@link #VariableNode} and vice versa. The class uses the given 
 * factories, the {@link #OpcUaDefinitionFactory} and the {@link #IUaObjectAndBuilderProvider} to perform the transformation.
 *
 */
class MsPropertyNodeToPropertyNodeConverter implements IConverter<MsPropertyNode, VariableNode, OpcUaDefinitionFactory, IUaObjectAndBuilderProvider>{

    /**
     * The singleton instance <code>MsPropertyNodeToPropertyNodeConverter<code> of the converter.
     */
    private static MsPropertyNodeToPropertyNodeConverter instance;
    
    /**
     * Returns the only instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    static MsPropertyNodeToPropertyNodeConverter getInstance() {
        if(instance == null) {
            instance = new MsPropertyNodeToPropertyNodeConverter();
        }
        return instance;
    }
    
    /**
     * The singleton instance <code>MsPropertyNodeToPropertyNodeConverter<code> of the converter.
     */
    private MsPropertyNodeToPropertyNodeConverter() {
    }
    
    /**
     * Creates a {@link MsPropertyNode} from the given {@link VariableNode} by using an {@link OpcUaDefinitionFactory}.
     * 
     * @param object the <code>VariableNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>MsPropertyNode</code>.
     * @return the corresponding <code>MsPropertyNode</code> object from the given <code>VariableNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public MsPropertyNode createSource(VariableNode object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException, ConversionFailureException {
        throw new ConvertionNotSupportedException();
    }

    /**
     * Creates a {@link MsPropertyNode} from the given {@link VariableNode} by using an {@link IUaObjectAndBuilderProvider}.
     * 
     * @param object the <code>MsPropertyNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>VariableNode</code>.
     * @return the corresponding <code>VariableNode</code> object from the given <code>MsPropertyNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public VariableNode createTarget(MsPropertyNode msNode, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException {
        UaVariableNode uaPropertyNode = factory.getUaVariableNodeBuilder()
                .setNodeId(MsNodeIdToNodeIdConverter.getInstance().createTarget(msNode.getNodeId(), factory))
                .setBrowseName(MsQualifiedNameToQualifiedName.getInstance().createTarget(msNode.getBrowseName(), factory)) 
                .setDisplayName(MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDisplayName(), factory)) 
                .setDescription(MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDescription(), factory))  
                .setWriteMask(UInteger.valueOf(msNode.getWriteMask())) 
                .setUserWriteMask(UInteger.valueOf(msNode.getUserWriteMask()))
                .setValue(new DataValue(new Variant(msNode.getValue())))
                .setDataType(MsNodeIdToNodeIdConverter.getInstance().createTarget(msNode.getDataType(), factory))
                .setAccessLevel(UByte.valueOf(msNode.getAccessLevel()))
                .setUserAccessLevel(UByte.valueOf(msNode.getUserAccessLevel()))
                //.setValueRank(msNode.getValueRank())
                .setHistorizing(msNode.isHistorizing())
                .build();
        
        if(msNode.getHasModellingRule() != null) {
            uaPropertyNode.addReference(new Reference(uaPropertyNode.getNodeId(), Identifiers.HasModellingRule, MsNodeIdToNodeIdConverter.getInstance().createTarget(msNode.getHasModellingRule(), factory).expanded(), true));
        }
        
        
        factory.getNodeContext().getNodeManager().addNode(uaPropertyNode);
        
        return uaPropertyNode;
    }
}
