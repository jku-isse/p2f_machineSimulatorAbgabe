package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.sdk.server.api.nodes.ViewNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaViewNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsViewNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.IConverter;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;

/**
 * Converts a {@link #MsViewNode} to a {@link #ViewNode} and vice versa. The class uses the given 
 * factories, the {@link #OpcUaDefinitionFactory} and the {@link #UaBuilderFactory} to perform the transformation.
 *
 */
class MsViewNodeToViewNodeConverter implements IConverter<MsViewNode, ViewNode, OpcUaDefinitionFactory, IUaObjectAndBuilderProvider>{

    /**
     * The singleton instance <code>MsViewNodeToViewNodeConverter<code> of the converter.
     */
    private static MsViewNodeToViewNodeConverter instance;
    
    /**
     * Returns the only instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    static MsViewNodeToViewNodeConverter getInstance() {
        if(instance == null) {
            instance = new MsViewNodeToViewNodeConverter();
        }
        return instance;
    }
    
    /**
     * The singleton instance <code>MsViewNodeToViewNodeConverter<code> of the converter.
     */
    private MsViewNodeToViewNodeConverter() {
    }
    
    /**
     * Creates a {@link ViewNode} from the given {@link MsViewNode} by using an {@link OpcUaDefinitionFactory}.
     * 
     * @param object the <code>ViewNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>MsViewNode</code>.
     * @return the corresponding <code>MsViewNode</code> object from the given <code>ViewNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public MsViewNode createSource(ViewNode object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException, ConversionFailureException
    {
        throw new ConvertionNotSupportedException();
    }

    /**
     * Creates a {@link ViewNode} from the given {@link MsViewNode} by using an {@link IUaObjectAndBuilderProvider}.
     * 
     * @param object the <code>MsViewNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>ViewNode</code>.
     * @return the corresponding <code>ViewNode</code> object from the given <code>MsViewNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public ViewNode createTarget(MsViewNode msNode, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException {
        UaViewNode uaViewNode = factory.getUaViewNode(
                MsNodeIdToNodeIdConverter.getInstance().createTarget(msNode.getNodeId(), factory),
                MsQualifiedNameToQualifiedName.getInstance().createTarget(msNode.getBrowseName(), factory),
                MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDisplayName(), factory),
                MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDescription(), factory),  
                UInteger.valueOf(msNode.getWriteMask()),
                UInteger.valueOf(msNode.getUserWriteMask()),
                msNode.getContainsNoLoops(),
                UByte.valueOf(msNode.getEventNotifier())
        );

        factory.getNodeContext().getNodeManager().addNode(uaViewNode);
        
        return uaViewNode;
    }

}
