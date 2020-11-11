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
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;


public class MsObjectTypeNodeToObjectTypeNodeConverter implements Converter<MsObjectTypeNode, ObjectTypeNode, OpcUaDefinitionFactory, UaBuilderFactory>{

	@Override
	public MsObjectTypeNode createFrom(ObjectTypeNode object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException {
		throw new ConvertionNotSupportedException();
	}

	@Override
	public ObjectTypeNode createTo(MsObjectTypeNode msNode, UaBuilderFactory factory) throws ConvertionNotSupportedException {
		
		UaObjectTypeNode uaObjectTypeNode = factory.getUaObjectTypeNodeBuilder()
				.setNodeId(new MsNodeIdToNodeIdConverter().createTo(msNode.getNodeId(), factory))
				.setBrowseName(new MsQualifiedNameToQualifiedName().createTo(msNode.getBrowseName(), factory)) 
				.setDisplayName(new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDisplayName(), factory)) 
				.setDescription(new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDescription(), factory))  
				.setWriteMask(msNode.getWriteMask() == null ?  UInteger.valueOf(Integer.MAX_VALUE) : UInteger.valueOf(msNode.getWriteMask())) 
				.setUserWriteMask(msNode.getUserWriteMask() == null ?  UInteger.valueOf(Integer.MAX_VALUE) : UInteger.valueOf(msNode.getUserWriteMask()))
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
			UaNode uaNode = (UaNode) new MsNodeToNodeConverter().createTo(component, factory);
			
			uaObjectTypeNode.addComponent(uaNode);
		}
		
		factory.getNodeContext().getNodeManager().addNode(uaObjectTypeNode);
        
        return uaObjectTypeNode;
	}
}
