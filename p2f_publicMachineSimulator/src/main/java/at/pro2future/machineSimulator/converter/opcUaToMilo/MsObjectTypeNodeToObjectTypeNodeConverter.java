package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.api.nodes.ObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsNode;
import OpcUaDefinition.MsObjectTypeNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.IConverter;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;
import at.pro2future.machineSimulator.converter.UaObjectAndBuilderProvider;

/**
 * Converts a {@link #MsObjectTypeNode} to a {@link #ObjectTypeNode} and vice versa. The class uses the given 
 * factories, the {@link #OpcUaDefinitionFactory} and the {@link #IUaObjectAndBuilderProvider} to perform the transformation.
 *
 */
class MsObjectTypeNodeToObjectTypeNodeConverter implements IConverter<MsObjectTypeNode, ObjectTypeNode, OpcUaDefinitionFactory, IUaObjectAndBuilderProvider>{

    /**
     * The singleton instance <code>MsObjectTypeNodeToObjectTypeNodeConverter<code> of the converter.
     */
    private static MsObjectTypeNodeToObjectTypeNodeConverter instance;
    
    /**
     * Returns the only instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    static MsObjectTypeNodeToObjectTypeNodeConverter getInstance() {
        if(instance == null) {
            instance = new MsObjectTypeNodeToObjectTypeNodeConverter();
        }
        return instance;
    }
    
    /**
     * The singleton instance <code>MsObjectTypeNodeToObjectTypeNodeConverter<code> of the converter.
     */
    private MsObjectTypeNodeToObjectTypeNodeConverter() {
    }
    
    /**
     * Creates a {@link MsObjectTypeNode} from the given {@link ObjectTypeNode} by using an {@link OpcUaDefinitionFactory}.
     * 
     * @param object the <code>ObjectTypeNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>MsObjectTypeNode</code>.
     * @return the corresponding <code>MsObjectTypeNode</code> object from the given <code>ObjectTypeNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public MsObjectTypeNode createSource(ObjectTypeNode object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException, ConversionFailureException {
        throw new ConvertionNotSupportedException();
    }

    /**
     * Creates a {@link ObjectTypeNode} from the given {@link MsObjectTypeNode} by using an {@link UaObjectAndBuilderProvider}.
     * 
     * @param object the <code>MsObjectTypeNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>ObjectTypeNode</code>.
     * @return the corresponding <code>ObjectTypeNode</code> object from the given <code>MsObjectTypeNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public ObjectTypeNode createTarget(MsObjectTypeNode msNode, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException {
        
        UaObjectTypeNode uaObjectTypeNode = factory.getUaObjectTypeNodeBuilder()
                .setNodeId(MsNodeIdToNodeIdConverter.getInstance().createTarget(msNode.getNodeId(), factory))
                .setBrowseName(MsQualifiedNameToQualifiedName.getInstance().createTarget(msNode.getBrowseName(), factory)) 
                .setDisplayName(MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDisplayName(), factory)) 
                .setDescription(MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDescription(), factory))  
                .setWriteMask(UInteger.valueOf(msNode.getWriteMask())) 
                .setUserWriteMask(UInteger.valueOf(msNode.getUserWriteMask()))
                .setIsAbstract(msNode.isIsAbstract())
                .build();
        
        factory.getNodeContext().getServer().getObjectTypeManager().registerObjectType(
                  uaObjectTypeNode.getNodeId(),
                UaObjectNode.class,
                UaObjectNode::new
            );
                
        // Add the inverse HasSubtype relationship.
        uaObjectTypeNode.addReference(new Reference(
                uaObjectTypeNode.getNodeId(),
                Identifiers.HasSubtype,
                Identifiers.BaseObjectType.expanded(),
                false
            ));
        
        for(MsNode component : msNode.getHasComponent()) {
            UaNode uaNode = (UaNode) MsNodeToNodeConverter.getInstance().createTarget(component, factory);
            
            uaObjectTypeNode.addComponent(uaNode);
        }
        
        factory.getNodeContext().getNodeManager().addNode(uaObjectTypeNode);
        
        return uaObjectTypeNode;
    }
}
