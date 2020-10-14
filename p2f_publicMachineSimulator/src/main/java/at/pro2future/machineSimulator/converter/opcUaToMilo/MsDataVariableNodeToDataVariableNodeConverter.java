package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.sdk.server.api.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsDataVariableNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;

public class MsDataVariableNodeToDataVariableNodeConverter implements Converter<MsDataVariableNode, VariableNode, OpcUaDefinitionFactory, UaBuilderFactory>{

	@Override
	public MsDataVariableNode createFrom(VariableNode object, OpcUaDefinitionFactory factory) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public VariableNode createTo(MsDataVariableNode msNode, UaBuilderFactory factory) throws Exception {
		UaVariableNode uaDataTypeNode = factory.getUaVariableNodeBuilder()
				.setNodeId(new MsNodeIdToNodeIdConverter().createTo(msNode.getNodeId(), factory))
				.setBrowseName(new MsQualifiedNameToQualifiedName().createTo(msNode.getBrowseName(), factory)) 
				.setDisplayName(new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDisplayName(), factory)) 
				.setDescription(new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDescription(), factory))  
				.setWriteMask(msNode.getWriteMask() == null ? UInteger.valueOf(Integer.MAX_VALUE) : UInteger.valueOf(msNode.getWriteMask())) 
				.setUserWriteMask(msNode.getUserWriteMask() == null ? UInteger.valueOf(Integer.MAX_VALUE) : UInteger.valueOf(msNode.getUserWriteMask()))
				.setAccessLevel(UByte.valueOf(msNode.getAccessLevel()))
				.setUserAccessLevel(UByte.valueOf(msNode.getUserAccessLevel()))				
				.build();
		
		factory.getNodeContext().getNodeManager().addNode(uaDataTypeNode);
        
        return uaDataTypeNode;
	}
}
