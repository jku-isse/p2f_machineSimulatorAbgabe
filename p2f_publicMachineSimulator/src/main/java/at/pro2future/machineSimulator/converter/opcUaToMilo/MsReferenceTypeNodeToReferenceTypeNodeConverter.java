package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.sdk.server.api.nodes.ReferenceTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaReferenceTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsReferenceTypeNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.IConverter;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;

/**
 * Converts a {@link #MsReferenceTypeNode} to a {@link #ReferenceTypeNode} and vice versa. The class uses the given 
 * factories, the {@link #OpcUaDefinitionFactory} and the {@link #IUaObjectAndBuilderProvider} to perform the transformation.
 *
 */
public class MsReferenceTypeNodeToReferenceTypeNodeConverter implements IConverter<MsReferenceTypeNode, ReferenceTypeNode, OpcUaDefinitionFactory, IUaObjectAndBuilderProvider>{

    /**
     * The singleton instance <code>MsReferenceTypeNodeToReferenceTypeNodeConverter<code> of the converter.
     */
    private static MsReferenceTypeNodeToReferenceTypeNodeConverter instance;
    
    /**
     * Returns the only instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    public static MsReferenceTypeNodeToReferenceTypeNodeConverter getInstance() {
        if(instance == null) {
            instance = new MsReferenceTypeNodeToReferenceTypeNodeConverter();
        }
        return instance;
    }
    
    /**
     * The singleton instance <code>MsReferenceTypeNodeToReferenceTypeNodeConverter<code> of the converter.
     */
    private MsReferenceTypeNodeToReferenceTypeNodeConverter() {
    }
    
    /**
     * Creates a {@link MsReferenceTypeNode} from the given {@link ReferenceTypeNode} by using an {@link OpcUaDefinitionFactory}.
     * 
     * @param object the <code>ReferenceTypeNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>MsReferenceTypeNode</code>.
     * @return the corresponding <code>MsReferenceTypeNode</code> object from the given <code>ReferenceTypeNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public MsReferenceTypeNode createSource(ReferenceTypeNode object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException, ConversionFailureException {
        throw new ConvertionNotSupportedException();
    }

    /**
     * Creates a {@link ReferenceTypeNode} from the given {@link MsReferenceTypeNode} by using an {@link IUaObjectAndBuilderProvider}.
     * 
     * @param object the <code>MsReferenceTypeNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>ReferenceTypeNode</code>.
     * @return the corresponding <code>ReferenceTypeNode</code> object from the given <code>MsReferenceTypeNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public ReferenceTypeNode createTarget(MsReferenceTypeNode msNode, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException {
        UaReferenceTypeNode uaReferenceTypeNode = factory.getUaReferenceTypeNode(
                MsNodeIdToNodeIdConverter.getInstance().createTarget(msNode.getNodeId(), factory),
                MsQualifiedNameToQualifiedName.getInstance().createTarget(msNode.getBrowseName(), factory),
                MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDisplayName(), factory),
                MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDescription(), factory),  
                UInteger.valueOf(msNode.getWriteMask()),
                UInteger.valueOf(msNode.getUserWriteMask()),
                msNode.getIsAbstract(),
                msNode.getSymmetric(),
                MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDisplayName(), factory)
        );
        
        factory.getNodeContext().getNodeManager().addNode(uaReferenceTypeNode);
                
        return uaReferenceTypeNode;
    }

}
