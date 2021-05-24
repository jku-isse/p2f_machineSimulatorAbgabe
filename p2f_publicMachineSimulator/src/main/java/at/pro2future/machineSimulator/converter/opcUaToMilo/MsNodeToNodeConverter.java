package at.pro2future.machineSimulator.converter.opcUaToMilo;

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
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.IConverter;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;

/**
 * Converts a {@link #MsNode} to a {@link #Node} and vice versa. The class uses the given 
 * factories, the {@link #OpcUaDefinitionFactory} and the {@link #IUaObjectAndBuilderProvider} to perform the transformation.
 *
 */
public class MsNodeToNodeConverter implements IConverter<MsNode, Node, OpcUaDefinitionFactory, IUaObjectAndBuilderProvider>{

    /**
     * The singleton instance <code>MsNodeToNodeConverter<code> of the converter.
     */
    private static MsNodeToNodeConverter instance;
    
    /**
     * Returns the only instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    public static MsNodeToNodeConverter getInstance() {
        if(instance == null) {
            instance = new MsNodeToNodeConverter();
        }
        return instance;
    }
    
    /**
     * The singleton instance <code>MsNodeToNodeConverter<code> of the converter.
     */
    private MsNodeToNodeConverter() {
    }
    
    
    /**
     * Creates a {@link MsNode} from the given {@link MsNode} by using an {@link OpcUaDefinitionFactory}.
     * 
     * @param object the <code>Node</code> which should be converted.
     * @param factory the factory which is able to create the <code>MsNode</code>.
     * @return the corresponding <code>MsNode</code> object from the given <code>Node</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public MsNode createSource(Node object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException, ConversionFailureException {
        throw new ConvertionNotSupportedException();
    }

    /**
     * Creates a {@link MsNode} from the given {@link Node} by using an {@link IUaObjectAndBuilderProvider}.
     * 
     * @param object the <code>MsNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>Node</code>.
     * @return the corresponding <code>Node</code> object from the given <code>MsNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public Node createTarget(MsNode msNode, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException {
        
        
        if(msNode instanceof MsObjectNode) {
            return MsObjectNodeToObjectNodeConverter.getInstance().createTarget((MsObjectNode)msNode, factory);
        }
        else if(msNode instanceof MsObjectTypeNode) {
            return MsObjectTypeNodeToObjectTypeNodeConverter.getInstance().createTarget((MsObjectTypeNode)msNode, factory);
        }
        else if(msNode instanceof MsDataVariableNode) {
            return MsDataVariableNodeToDataVariableNodeConverter.getInstance().createTarget((MsDataVariableNode)msNode, factory);
        }
        else if(msNode instanceof MsPropertyNode) {
            return MsPropertyNodeToPropertyNodeConverter.getInstance().createTarget((MsPropertyNode)msNode, factory);
        }
        else if(msNode instanceof MsVariableTypeNode) {
            return MsVariableTypeNodeToVariableTypeNodeConverter.getInstance().createTarget((MsVariableTypeNode)msNode, factory);
        }
        else if(msNode instanceof MsMethodNode) {
            return MsMethodNodeToMethodNodeConverter.getInstance().createTarget((MsMethodNode)msNode, factory);
        }
        else if(msNode instanceof MsViewNode) {
            return MsViewNodeToViewNodeConverter.getInstance().createTarget((MsViewNode)msNode, factory);
        }
        else if(msNode instanceof MsDataTypeNode) {
            return MsDataTypeNodeToDataTypeNodeConverter.getInstance().createTarget((MsDataTypeNode)msNode, factory);
        }
        else if(msNode instanceof MsReferenceTypeNode) {
            return MsReferenceTypeNodeToReferenceTypeNodeConverter.getInstance().createTarget((MsReferenceTypeNode)msNode, factory);
        }
        throw new ConvertionNotSupportedException();
    }
    
}
