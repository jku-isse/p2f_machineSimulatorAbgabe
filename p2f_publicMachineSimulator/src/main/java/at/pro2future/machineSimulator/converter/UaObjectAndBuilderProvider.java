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

/**
 * This provider creates {@link UaNode}s itself or can return builders
 * which can create them respectively. This builder creates the objects
 * always for a specific {@link UaNodeContext} and for a specific namespace index.
 * 
 */
public class UaObjectAndBuilderProvider implements IUaObjectAndBuilderProvider {

    private final UaNodeContext nodeContext; 

    /**
     * Returns the NodeContext this builder operates on. 
     * @return he NodeContext this builder operates on. 
     */
    public UaNodeContext getNodeContext() {
        return  this.nodeContext;
    }
    
    private final int namespaceIndex;
    
    /**
     * Returns the index of the namespace this builder operates on.
     * @return the namespace index this builder operates on.
     */
    @Override
    public int getNamespaceIndex() {
        return this.namespaceIndex;
    }
    
    /**
     * Creates a new <code>UaObjectAndBuilderProvider</code> which creates objects and builders
     * for a specific {@link UaNodeContext} and on a specific namepsace.
     * 
     * @param nodeContext the NodeContext this builder operates on. 
     * @param namespaceIndex the index of the namespace this builder operates on.
     */
    public UaObjectAndBuilderProvider(UaNodeContext nodeContext, int namespaceIndex) {
        this.nodeContext = nodeContext;
        this.namespaceIndex = namespaceIndex;
    }
    

    @Override
    public UaObjectNodeBuilder getUaObjectNodeBuilder() {
        return UaObjectNode.builder(this.nodeContext);
    }
    

    @Override
    public UaObjectTypeNodeBuilder getUaObjectTypeNodeBuilder() {
        return UaObjectTypeNode.builder(this.nodeContext);
    }
    
    @Override
    public UaVariableNodeBuilder getUaVariableNodeBuilder() {
        return UaVariableNode.builder(this.nodeContext);
    }
    
    @Override
    public UaMethodNodeBuilder getUaMethodNodeBuilder() {
        return UaMethodNode.builder(this.nodeContext);
    }
    
    @Override
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

    
    @Override
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
    
    @Override
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
    
    @Override
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
