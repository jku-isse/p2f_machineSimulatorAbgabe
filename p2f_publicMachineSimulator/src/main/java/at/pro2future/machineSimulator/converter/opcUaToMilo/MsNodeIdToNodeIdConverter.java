package at.pro2future.machineSimulator.converter.opcUaToMilo;

import java.util.UUID;

import javax.naming.OperationNotSupportedException;

import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsNodeId;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MsNodeIdToNodeIdConverter implements Converter<MsNodeId, NodeId, OpcUaDefinitionFactory, UaBuilderFactory> {

	@Override
	public MsNodeId createFrom(NodeId object, OpcUaDefinitionFactory factory) throws Exception {
		throw new NotImplementedException();
	}

	@Override
	public NodeId createTo(MsNodeId object, UaBuilderFactory factory) throws Exception {
		int namespaceIndex = object.getNamespaceIndex();
		Object indentifier = object.getIdentifier();
		
		if(object.getAllowOverrideNamespaceIndex()) {
			namespaceIndex = factory.getNamespaceIndex();
		}
		
		
		if(indentifier instanceof ByteString) {
			return new NodeId(namespaceIndex, (ByteString)object.getIdentifier());
		}
		else if(indentifier instanceof String) {
			return new NodeId(namespaceIndex, (String)object.getIdentifier());
		}
		else if(indentifier instanceof Integer) {
			return new NodeId(namespaceIndex, (Integer)object.getIdentifier());
		}
		else if(indentifier instanceof UInteger) {
			return new NodeId(namespaceIndex, (UInteger)object.getIdentifier());
		}
		else if(indentifier instanceof UUID) {
			return new NodeId(namespaceIndex, (UUID)object.getIdentifier());
		}
		throw new OperationNotSupportedException();	
	}

}
