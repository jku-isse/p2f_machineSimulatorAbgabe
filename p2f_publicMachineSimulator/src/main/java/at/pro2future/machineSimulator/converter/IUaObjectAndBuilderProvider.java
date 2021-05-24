package at.pro2future.machineSimulator.converter;

import org.eclipse.milo.opcua.sdk.server.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaReferenceTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaViewNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode.UaMethodNodeBuilder;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode.UaObjectNodeBuilder;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode.UaObjectTypeNodeBuilder;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode.UaVariableNodeBuilder;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * This interface defines an <code>IUaObjectAndBuilderProvider</code>. This interface return builders which 
 * can be used to create the corresponding object. If for a specific object type no builder is proved
 * this interface returns functions that can create this object respectively.
 * 
 * @remarks currently no builders for {@link UaViewNode}, {@link UaReferenceTypeNode}, {@link UaDataTypeNode}
 * and {@link UaVariableTypeNode} have been provided by the milo framework.
 * 
 * @author johannstoebich
 */
public interface IUaObjectAndBuilderProvider {
    
    /**
     * Returns the NodeContext this builder operates on. 
     * @return he NodeContext this builder operates on. 
     */
     UaNodeContext getNodeContext();
    
    /**
     * Returns the index of the namespace this builder operates on.
     * @return the namespace index this builder operates on.
     */
    int getNamespaceIndex();
    
    /**
     * Creates a new {@link UaObjectNodeBuilder}.
     * @return the newly created <code>UaObjectNodeBuilder</code>.
     */
    UaObjectNodeBuilder getUaObjectNodeBuilder();
    
    /**
     * Creates a new {@link UaObjectTypeNodeBuilder}.
     * @return the newly created <code>UaObjectTypeNodeBuilder</code>.
     */
    UaObjectTypeNodeBuilder getUaObjectTypeNodeBuilder();
    
    /**
     * Creates a new {@link UaVariableNodeBuilder}.
     * @return the newly created <code>UaVariableNodeBuilder</code>.
     */
    UaVariableNodeBuilder getUaVariableNodeBuilder();
    
    /**
     * Creates a new {@link UaMethodNodeBuilder}.
     * @return the newly created <code>UaMethodNodeBuilder</code>.
     */
    UaMethodNodeBuilder getUaMethodNodeBuilder();
    
    /**
     * Creates a new {@link UaViewNode}.
     * @return the newly created <code>UaViewNode</code>.
     */
    UaViewNode getUaViewNode(
            NodeId nodeId,
            QualifiedName browseName,
            LocalizedText displayName,
            LocalizedText description,
            UInteger writeMask,
            UInteger userWriteMask,
            Boolean containsNoLoops,
            UByte eventNotifier);
    
    /**
     * Creates a new {@link UaReferenceTypeNode}.
     * @return the newly created <code>UaReferenceTypeNode</code>.
     */
    UaReferenceTypeNode getUaReferenceTypeNode(
            NodeId nodeId,
            QualifiedName browseName,
            LocalizedText displayName,
            LocalizedText description,
            UInteger writeMask,
            UInteger userWriteMask,
            Boolean isAbstract,
            Boolean symmetric,
            LocalizedText inverseName);
    
    /**
     * Creates a new {@link UaDataTypeNode}.
     * @return the newly created <code>UaDataTypeNode</code>.
     */
    UaDataTypeNode getUaDataTypeNode(
            NodeId nodeId,
            QualifiedName browseName,
            LocalizedText displayName,
            LocalizedText description,
            UInteger writeMask,
            UInteger userWriteMask,
            boolean isAbstract);
    
    /**
     * Creates a new {@link UaVariableTypeNode}.
     * @return the newly created <code>UaVariableTypeNode</code>.
     */
    UaVariableTypeNode getUaVariableTypeNode(
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
            Boolean isAbstract);
}