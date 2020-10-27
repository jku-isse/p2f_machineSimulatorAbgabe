package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.sdk.server.api.nodes.ReferenceTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaReferenceTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsReferenceTypeNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;


public class MsReferenceTypeNodeToReferenceTypeNodeConverter implements Converter<MsReferenceTypeNode, ReferenceTypeNode, OpcUaDefinitionFactory, UaBuilderFactory>{

	@Override
	public MsReferenceTypeNode createFrom(ReferenceTypeNode object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException {
		throw new ConvertionNotSupportedException();
	}

	@Override
	public ReferenceTypeNode createTo(MsReferenceTypeNode msNode, UaBuilderFactory factory) throws ConvertionNotSupportedException {
		UaReferenceTypeNode uaReferenceTypeNode = factory.getUaReferenceTypeNode(
				new MsNodeIdToNodeIdConverter().createTo(msNode.getNodeId(), factory),
				new MsQualifiedNameToQualifiedName().createTo(msNode.getBrowseName(), factory),
				new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDisplayName(), factory),
				new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDescription(), factory),  
				msNode.getWriteMask() == null ? UInteger.valueOf(Integer.MAX_VALUE) :UInteger.valueOf(msNode.getWriteMask()),
				msNode.getUserWriteMask() == null ? UInteger.valueOf(Integer.MAX_VALUE) :UInteger.valueOf(msNode.getUserWriteMask()),
				msNode.isIsAbstract(),
				msNode.isSymmetric(),
				new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDisplayName(), factory)
        );
		
		factory.getNodeContext().getNodeManager().addNode(uaReferenceTypeNode);
                
        return uaReferenceTypeNode;
	}

}
