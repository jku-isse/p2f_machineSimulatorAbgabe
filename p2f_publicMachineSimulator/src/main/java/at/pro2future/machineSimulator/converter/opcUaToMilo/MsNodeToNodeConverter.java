package at.pro2future.machineSimulator.converter.opcUaToMilo;

import javax.naming.OperationNotSupportedException;

import org.eclipse.milo.opcua.sdk.server.api.nodes.Node;
import OpcUaDefinition.MsDataTypeNode;
import OpcUaDefinition.MsDataVariableNode;
import OpcUaDefinition.MsMethodNode;
import OpcUaDefinition.MsNode;
import OpcUaDefinition.MsObjectNode;
import OpcUaDefinition.MsObjectTypeNode;
import OpcUaDefinition.MsPropertyNode;
import OpcUaDefinition.MsReferenceTypeNode;
import OpcUaDefinition.MsVariableTypeNode;
import OpcUaDefinition.MsViewNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;

public class MsNodeToNodeConverter implements Converter<MsNode, Node, OpcUaDefinitionFactory, UaBuilderFactory>{

	@Override
	public MsNode createFrom(Node object, OpcUaDefinitionFactory factory) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node createTo(MsNode msNode, UaBuilderFactory factory) throws Exception {
		
		
		if(msNode instanceof MsObjectNode) {
			return new MsObjectNodeToObjectNodeConverter().createTo((MsObjectNode)msNode, factory);
		}
		else if(msNode instanceof MsObjectTypeNode) {
			return new MsObjectTypeNodeToObjectTypeNodeConverter().createTo((MsObjectTypeNode)msNode, factory);
		}
		else if(msNode instanceof MsDataVariableNode) {
			return new MsDataVariableNodeToDataVariableNodeConverter().createTo((MsDataVariableNode)msNode, factory);
		}
		else if(msNode instanceof MsPropertyNode) {
			return new MsPropertyNodeToPropertyNodeConverter().createTo((MsPropertyNode)msNode, factory);
		}
		else if(msNode instanceof MsVariableTypeNode) {
			return new MsVariableTypeNodeToVariableTypeNodeConverter().createTo((MsVariableTypeNode)msNode, factory);
		}
		else if(msNode instanceof MsMethodNode) {
			return new MsMethodNodeToMethodNodeConverter().createTo((MsMethodNode)msNode, factory);
		}
		else if(msNode instanceof MsViewNode) {
			return new MsViewNodeToViewNodeConverter().createTo((MsViewNode)msNode, factory);
		}
		else if(msNode instanceof MsDataTypeNode) {
			return new MsDataTypeNodeToDataTypeNodeConverter().createTo((MsDataTypeNode)msNode, factory);
		}
		else if(msNode instanceof MsReferenceTypeNode) {
			return new MsReferenceTypeNodeToReferenceTypeNodeConverter().createTo((MsReferenceTypeNode)msNode, factory);
		}
		throw new OperationNotSupportedException();
	}
	
}
