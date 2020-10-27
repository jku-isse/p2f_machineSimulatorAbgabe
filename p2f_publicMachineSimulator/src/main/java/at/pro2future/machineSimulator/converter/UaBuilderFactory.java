package at.pro2future.machineSimulator.converter;

import org.eclipse.milo.opcua.sdk.server.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode.UaMethodNodeBuilder;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode.UaObjectNodeBuilder;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode.UaObjectTypeNodeBuilder;
import org.eclipse.milo.opcua.sdk.server.nodes.UaReferenceTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode.UaVariableNodeBuilder;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaViewNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument.ArgumentBuilder;

public class UaBuilderFactory {

	private UaNodeContext nodeContext; 
	
	public UaNodeContext getNodeContext() {
		return  this.nodeContext;
	}
	
	private int namespaceIndex;
	
	public int getNamespaceIndex() {
		return this.namespaceIndex;
	}
	
	public UaBuilderFactory(UaNodeContext nodeContext, int namespaceIndex) {
		this.nodeContext = nodeContext;
		this.namespaceIndex = namespaceIndex;
	}
	
	public UaObjectNodeBuilder getUaObjectNodeBuilder() {
		return UaObjectNode.builder(this.nodeContext);
	}
	
	public UaObjectTypeNodeBuilder getUaObjectTypeNodeBuilder() {
		return UaObjectTypeNode.builder(this.nodeContext);
	}
	
	public UaVariableNodeBuilder getUaVariableNodeBuilder() {
		return UaVariableNode.builder(this.nodeContext);
	}

	
	public UaMethodNodeBuilder getUaMethodNodeBuilder() {
		return UaMethodNode.builder(this.nodeContext);
	}
	
	public ArgumentBuilder<?,?> getArgumentBuilder() {
		return Argument.builder();
	}
	
	public UaViewNode getUaViewNode(
	        NodeId nodeId,
	        QualifiedName browseName,
	        LocalizedText displayName,
	        LocalizedText description,
	        UInteger writeMask,
	        UInteger userWriteMask,
	        Boolean containsNoLoops,
	        UByte eventNotifier) {
		
		return new UaViewNode(this.nodeContext, nodeId, browseName, 
			displayName, description, writeMask, userWriteMask, containsNoLoops,
			eventNotifier);
	}

	
	public UaReferenceTypeNode getUaReferenceTypeNode(
		        NodeId nodeId,
		        QualifiedName browseName,
		        LocalizedText displayName,
		        LocalizedText description,
		        UInteger writeMask,
		        UInteger userWriteMask,
		        Boolean isAbstract,
		        Boolean symmetric,
		        LocalizedText inverseName) {
		return new UaReferenceTypeNode(this.nodeContext, nodeId, browseName, 
				displayName, description, writeMask, userWriteMask, isAbstract,
				symmetric, inverseName);
	}
	
	public UaDataTypeNode getUaDataTypeNode(
	        NodeId nodeId,
	        QualifiedName browseName,
	        LocalizedText displayName,
	        LocalizedText description,
	        UInteger writeMask,
	        UInteger userWriteMask,
	        boolean isAbstract) {
		return new UaDataTypeNode(this.nodeContext, nodeId, browseName, 
			displayName, description, writeMask, userWriteMask, isAbstract);
	}
	
	public UaVariableTypeNode getUaVariableTypeNode(
		        NodeId nodeId,
		        QualifiedName browseName,
		        LocalizedText displayName,
		        LocalizedText description,
		        UInteger writeMask,
		        UInteger userWriteMask,
		        DataValue value,
		        NodeId dataType,
		        Integer valueRank,
		        UInteger[] arrayDimensions,
		        Boolean isAbstract) {
		
		return new UaVariableTypeNode(this.nodeContext, nodeId, browseName, displayName, description,
				writeMask, userWriteMask, value, dataType, valueRank, arrayDimensions, isAbstract);
	}
}
