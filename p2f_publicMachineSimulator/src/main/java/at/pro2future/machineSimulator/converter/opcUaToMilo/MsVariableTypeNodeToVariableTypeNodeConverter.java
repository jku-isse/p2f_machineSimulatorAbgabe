package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.sdk.server.api.nodes.VariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsVariableTypeNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class MsVariableTypeNodeToVariableTypeNodeConverter implements Converter<MsVariableTypeNode, VariableTypeNode, OpcUaDefinitionFactory, UaBuilderFactory>{

	@Override
	public MsVariableTypeNode createFrom(VariableTypeNode object, OpcUaDefinitionFactory factory) throws Exception {
		throw new NotImplementedException();
	}

	@Override
	public VariableTypeNode createTo(MsVariableTypeNode msNode, UaBuilderFactory factory) throws Exception {
		UaVariableTypeNode uaVariableTypeNode = factory.getUaVariableTypeNode(
				new MsNodeIdToNodeIdConverter().createTo(msNode.getNodeId(), factory),
				new MsQualifiedNameToQualifiedName().createTo(msNode.getBrowseName(), factory),
				new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDisplayName(), factory),
				new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDescription(), factory),  
				msNode.getWriteMask() == null ? null : UInteger.valueOf(msNode.getWriteMask()),
				msNode.getUserWriteMask() == null ? null : UInteger.valueOf(msNode.getUserWriteMask()),
				new DataValue(new Variant(msNode.getValue())),
				new NodeId(factory.getNamespaceIndex(), msNode.getDataType()),
				msNode.getValueRank(),
				null,
				msNode.isIsAbstract()
        );

		factory.getNodeContext().getNodeManager().addNode(uaVariableTypeNode);
        
        return uaVariableTypeNode;
	}

}
