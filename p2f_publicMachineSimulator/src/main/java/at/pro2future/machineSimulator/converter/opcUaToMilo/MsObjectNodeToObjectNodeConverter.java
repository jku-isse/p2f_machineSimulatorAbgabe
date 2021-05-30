package at.pro2future.machineSimulator.converter.opcUaToMilo;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.api.nodes.Node;
import org.eclipse.milo.opcua.sdk.server.api.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsNode;
import OpcUaDefinition.MsObjectNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.IConverter;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;

/**
 * Converts a {@link #MsObjectNode} to a {@link #ObjectNode} and vice versa. The class uses the given 
 * factories, the {@link #OpcUaDefinitionFactory} and the {@link #IUaObjectAndBuilderProvider} to perform the transformation.
 *
 */
class MsObjectNodeToObjectNodeConverter implements IConverter<MsObjectNode, ObjectNode, OpcUaDefinitionFactory, IUaObjectAndBuilderProvider>{

    /**
     * The singleton instance <code>MsObjectNodeToObjectNodeConverter<code> of the converter.
     */
    private static MsObjectNodeToObjectNodeConverter instance;
    
    /**
     * Returns the only instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    static MsObjectNodeToObjectNodeConverter getInstance() {
        if(instance == null) {
            instance = new MsObjectNodeToObjectNodeConverter();
        }
        return instance;
    }
    
    /**
     * The singleton instance <code>MsObjectNodeToObjectNodeConverter<code> of the converter.
     */
    private MsObjectNodeToObjectNodeConverter() {
    }
    
    /**
     * Creates a {@link MsObjectNode} from the given {@link ObjectNode} by using an {@link OpcUaDefinitionFactory}.
     * 
     * @param object the <code>ObjectNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>MsDataVariableNode</code>.
     * @return the corresponding <code>MsObjectNode</code> object from the given <code>ObjectNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public MsObjectNode createSource(ObjectNode object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException, ConversionFailureException {
        throw new ConvertionNotSupportedException();
    }

    /**
     * Creates a {@link MsObjectNode} from the given {@link ObjectNode} by using an {@link IUaObjectAndBuilderProvider}.
     * 
     * @param object the <code>MsObjectNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>VariableNode</code>.
     * @return the corresponding <code>ObjectNode</code> object from the given <code>MsObjectNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public ObjectNode createTarget(MsObjectNode msNode, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException {
        Node msObjectTypeNode = MsNodeToNodeConverter.getInstance().createTarget(msNode.getHasTypeDefinition(), factory);
        
        UaObjectNode uaObjectNode = factory.getUaObjectNodeBuilder()
                .setNodeId(MsNodeIdToNodeIdConverter.getInstance().createTarget(msNode.getNodeId(), factory))
                .setBrowseName(MsQualifiedNameToQualifiedName.getInstance().createTarget(msNode.getBrowseName(), factory)) 
                .setDisplayName(MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDisplayName(), factory)) 
                .setDescription(MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDescription(), factory))  
                .setWriteMask(UInteger.valueOf(msNode.getWriteMask())) 
                .setUserWriteMask(UInteger.valueOf(msNode.getUserWriteMask()))
                .setEventNotifier(UByte.valueOf(msNode.getEventNotifier()))
                .setTypeDefinition(msObjectTypeNode.getNodeId())
                .build();            

        
         Stream<MsNode> componentStream = new ArrayList<MsNode>().stream();
         if(msNode.getHasComponent() != null) {
             componentStream = Stream.concat(componentStream, msNode.getHasComponent().stream());
         }
         if(msNode.getOrganizes() != null) {
             componentStream = Stream.concat(componentStream, msNode.getOrganizes().stream());
         }
         
         componentStream.forEach(component -> {
                try {
                    Node uaNodex = MsNodeToNodeConverter.getInstance().createTarget(component, factory);
                    
                    uaObjectNode.addReference(new Reference(
                            uaObjectNode.getNodeId(),
                            Identifiers.HasComponent,
                            uaNodex.getNodeId().expanded(),
                            true
                        ));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }                
            }
        );
        
        if(msNode.getHasModellingRule() != null) {
            uaObjectNode.addReference(new Reference(uaObjectNode.getNodeId(), Identifiers.HasModellingRule, MsNodeIdToNodeIdConverter.getInstance().createTarget(msNode.getHasModellingRule(), factory).expanded(), true));
        }
        
        factory.getNodeContext().getNodeManager().addNode(uaObjectNode);
        
        return uaObjectNode;  
    }    
}
