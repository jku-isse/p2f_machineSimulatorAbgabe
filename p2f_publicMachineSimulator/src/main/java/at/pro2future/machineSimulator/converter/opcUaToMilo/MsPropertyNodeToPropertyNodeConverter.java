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
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;


public class MsPropertyNodeToPropertyNodeConverter implements Converter<MsPropertyNode, VariableNode, OpcUaDefinitionFactory, UaBuilderFactory>{

	@Override
	public MsPropertyNode createFrom(VariableNode object, OpcUaDefinitionFactory factory) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public VariableNode createTo(MsPropertyNode msNode, UaBuilderFactory factory) throws Exception {
		UaVariableNode uaPropertyNode = factory.getUaVariableNodeBuilder()
				.setNodeId(new MsNodeIdToNodeIdConverter().createTo(msNode.getNodeId(), factory))
				.setBrowseName(new MsQualifiedNameToQualifiedName().createTo(msNode.getBrowseName(), factory)) 
				.setDisplayName(new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDisplayName(), factory)) 
				.setDescription(new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDescription(), factory))  
				.setWriteMask(msNode.getWriteMask() == null ? UInteger.valueOf(Integer.MAX_VALUE) : UInteger.valueOf(msNode.getWriteMask())) 
				.setUserWriteMask(msNode.getUserWriteMask() == null ? UInteger.valueOf(Integer.MAX_VALUE) : UInteger.valueOf(msNode.getUserWriteMask()))
				.setValue(new DataValue(new Variant(msNode.getValue())))
				.setDataType(new MsNodeIdToNodeIdConverter().createTo(msNode.getDataType(), factory))
				.setAccessLevel(UByte.valueOf(msNode.getAccessLevel()))
				.setUserAccessLevel(UByte.valueOf(msNode.getUserAccessLevel()))
				//.setValueRank(msNode.getValueRank())
				.setHistorizing(msNode.isHistorizing())
				.build();
		
		if(msNode.getHasModellingRule() != null) {
			uaPropertyNode.addReference(new Reference(uaPropertyNode.getNodeId(), Identifiers.HasModellingRule, new MsNodeIdToNodeIdConverter().createTo(msNode.getHasModellingRule(), factory).expanded(), true));
		}
		
		
		factory.getNodeContext().getNodeManager().addNode(uaPropertyNode);
        
        return uaPropertyNode;
	}
}
