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
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MsObjectNodeToObjectNodeConverter implements Converter<MsObjectNode, ObjectNode, OpcUaDefinitionFactory, UaBuilderFactory>{

	@Override
	public MsObjectNode createFrom(ObjectNode object, OpcUaDefinitionFactory factory) {
		throw new NotImplementedException();
	}

	@Override
	public ObjectNode createTo(MsObjectNode msNode, UaBuilderFactory factory) throws Exception {
		Node msObjectTypeNode = new MsNodeToNodeConverter().createTo((MsNode)msNode.getHasTypeDefinition(), factory);
		
		UaObjectNode uaObjectNode = factory.getUaObjectNodeBuilder()
				.setNodeId(new MsNodeIdToNodeIdConverter().createTo(msNode.getNodeId(), factory))
				.setBrowseName(new MsQualifiedNameToQualifiedName().createTo(msNode.getBrowseName(), factory)) 
				.setDisplayName(new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDisplayName(), factory)) 
				.setDescription(new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDescription(), factory))  
				.setWriteMask(msNode.getWriteMask() == null ? null : UInteger.valueOf(msNode.getWriteMask())) 
				.setUserWriteMask(msNode.getUserWriteMask() == null ? null : UInteger.valueOf(msNode.getUserWriteMask()))
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
					Node uaNodex = new MsNodeToNodeConverter().createTo(component, factory);
					
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
			uaObjectNode.addReference(new Reference(uaObjectNode.getNodeId(), Identifiers.HasModellingRule, new MsNodeIdToNodeIdConverter().createTo(msNode.getHasModellingRule(), factory).expanded(), true));
		}
		
		factory.getNodeContext().getNodeManager().addNode(uaObjectNode);
        
        return uaObjectNode;  
	}	
}
