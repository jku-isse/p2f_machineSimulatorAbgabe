package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.sdk.server.api.nodes.DataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsDataTypeNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;



public class MsDataTypeNodeToDataTypeNodeConverter implements Converter<MsDataTypeNode, DataTypeNode, OpcUaDefinitionFactory, UaBuilderFactory>{

	@Override
	public MsDataTypeNode createFrom(DataTypeNode object, OpcUaDefinitionFactory factory) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public DataTypeNode createTo(MsDataTypeNode msNode, UaBuilderFactory factory) throws Exception {
		UaDataTypeNode uaDataTypeNode = factory.getUaDataTypeNode(
				new MsNodeIdToNodeIdConverter().createTo(msNode.getNodeId(), factory),
				new MsQualifiedNameToQualifiedName().createTo(msNode.getBrowseName(), factory),
				new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDisplayName(), factory),
				new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDescription(), factory),  
				msNode.getWriteMask() == null ? UInteger.valueOf(Integer.MAX_VALUE) : UInteger.valueOf(msNode.getWriteMask()),
				msNode.getUserWriteMask() == null ?  UInteger.valueOf(Integer.MAX_VALUE) : UInteger.valueOf(msNode.getUserWriteMask()),
				msNode.isIsAbstract());


		factory.getNodeContext().getNodeManager().addNode(uaDataTypeNode);
        
        return uaDataTypeNode;
	}


}
