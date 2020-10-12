package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.sdk.server.api.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsDataVariableNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class MsDataVariableNodeToDataVariableNodeConverter implements Converter<MsDataVariableNode, VariableNode, OpcUaDefinitionFactory, UaBuilderFactory>{

	@Override
	public MsDataVariableNode createFrom(VariableNode object, OpcUaDefinitionFactory factory) throws Exception {
		throw new NotImplementedException();
	}

	@Override
	public VariableNode createTo(MsDataVariableNode msNode, UaBuilderFactory factory) throws Exception {
		UaVariableNode uaDataTypeNode = factory.getUaVariableNodeBuilder()
				.setNodeId(new MsNodeIdToNodeIdConverter().createTo(msNode.getNodeId(), factory))
				.setBrowseName(new MsQualifiedNameToQualifiedName().createTo(msNode.getBrowseName(), factory)) 
				.setDisplayName(new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDisplayName(), factory)) 
				.setDescription(new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDescription(), factory))  
				.setWriteMask(msNode.getWriteMask() == null ? null : UInteger.valueOf(msNode.getWriteMask())) 
				.setUserWriteMask(msNode.getUserWriteMask() == null ? null : UInteger.valueOf(msNode.getUserWriteMask()))
				.build();
		
		factory.getNodeContext().getNodeManager().addNode(uaDataTypeNode);
        
        return uaDataTypeNode;
	}
}
