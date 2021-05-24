package at.pro2future.machineSimulator.converter.opcUaToMilo;

import java.util.UUID;

import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsNodeId;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.IConverter;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;

/**
 * Converts a {@link #MsNodeId} to a {@link #NodeId} and vice versa. The class uses the given 
 * factories, the {@link #OpcUaDefinitionFactory} and the {@link #IUaObjectAndBuilderProvider} to perform the transformation.
 *
 */
public class MsNodeIdToNodeIdConverter implements IConverter<MsNodeId, NodeId, OpcUaDefinitionFactory, IUaObjectAndBuilderProvider> {

    /**
     * The singleton instance <code>MsNodeIdToNodeIdConverter<code> of the converter.
     */
    private static MsNodeIdToNodeIdConverter instance;
    
    /**
     * Returns the only instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    public static MsNodeIdToNodeIdConverter getInstance() {
        if(instance == null) {
            instance = new MsNodeIdToNodeIdConverter();
        }
        return instance;
    }
    
    /**
     * The singleton instance <code>MsNodeIdToNodeIdConverter<code> of the converter.
     */
    private MsNodeIdToNodeIdConverter() {
    }
    
    /**
     * Creates a {@link MsNodeId} from the given {@link NodeId} by using an {@link OpcUaDefinitionFactory}.
     * 
     * @param object the <code>NodeId</code> which should be converted.
     * @param factory the factory which is able to create the <code>MsNodeId</code>.
     * @return the corresponding <code>MsNodeId</code> object from the given <code>NodeId</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public MsNodeId createSource(NodeId object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException, ConversionFailureException {
        throw new ConvertionNotSupportedException();
    }

    /**
     * Creates a {@link NodeId} from the given {@link MsNodeId} by using an {@link IUaObjectAndBuilderProvider}.
     * 
     * @param object the <code>MsNodeId</code> which should be converted.
     * @param factory the factory which is able to create the <code>NodeId</code>.
     * @return the corresponding <code>NodeId</code> object from the given <code>MsNodeId</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public NodeId createTarget(MsNodeId object, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException {
        int namespaceIndex = object.getNamespaceIndex();
        Object indentifier = object.getIdentifier();
        
        if(object.isAllowOverrideNamespaceIndex()) {
            namespaceIndex = factory.getNamespaceIndex();
        }
        
        
        if(indentifier instanceof ByteString) {
            return new NodeId(namespaceIndex, (ByteString)object.getIdentifier());
        }
        else if(indentifier instanceof String) {
            return new NodeId(namespaceIndex, (String)object.getIdentifier());
        }
        else if(indentifier instanceof Integer) {
            return new NodeId(namespaceIndex, ((Integer)object.getIdentifier()).intValue());
        }
        else if(indentifier instanceof UInteger) {
            return new NodeId(namespaceIndex, (UInteger)object.getIdentifier());
        }
        else if(indentifier instanceof UUID) {
            return new NodeId(namespaceIndex, (UUID)object.getIdentifier());
        }
        throw new ConvertionNotSupportedException();
    }

}
