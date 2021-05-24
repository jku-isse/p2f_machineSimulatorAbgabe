package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.sdk.server.api.nodes.DataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsDataTypeNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.IConverter;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;


/**
 * Converts a {@link #MsDataTypeNode} to a {@link #DataTypeNode} and vice versa. The class uses the given 
 * factories, the {@link #OpcUaDefinitionFactory} and the {@link #IUaObjectAndBuilderProvider} to perform the transformation.
 *
 */
public class MsDataTypeNodeToDataTypeNodeConverter implements IConverter<MsDataTypeNode, DataTypeNode, OpcUaDefinitionFactory, IUaObjectAndBuilderProvider>{

    /**
     * The singleton instance <code>MsDataTypeNodeToDataTypeNodeConverter<code> of the converter.
     */
    private static MsDataTypeNodeToDataTypeNodeConverter instance;
    
    /**
     * Returns the only instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    public static MsDataTypeNodeToDataTypeNodeConverter getInstance() {
        if(instance == null) {
            instance = new MsDataTypeNodeToDataTypeNodeConverter();
        }
        return instance;
    }
    
    /**
     * This private constructor prevents the instantiation of the class from another class. 
     */
    private MsDataTypeNodeToDataTypeNodeConverter() {
    }
    
    /**
     * Creates a {@link MsDataTypeNode} from the given {@link DataTypeNode} by using an {@link OpcUaDefinitionFactory}.
     * 
     * @param object the <code>DataTypeNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>MsDataTypeNode</code>.
     * @return the corresponding <code>MsDataTypeNode</code> object from the given <code>DataTypeNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public MsDataTypeNode createSource(DataTypeNode object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException, ConversionFailureException {
        throw new ConvertionNotSupportedException();
    }

    /**
     * Creates a {@link DataTypeNode} from the given {@link MsDataTypeNode} by using an {@link IUaObjectAndBuilderProvider}.
     * 
     * @param object the <code>MsDataTypeNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>DataTypeNode</code>.
     * @return the corresponding <code>DataTypeNode</code> object from the given <code>MsDataTypeNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public DataTypeNode createTarget(MsDataTypeNode msNode, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException {
        UaDataTypeNode uaDataTypeNode = factory.getUaDataTypeNode(
                MsNodeIdToNodeIdConverter.getInstance().createTarget(msNode.getNodeId(), factory),
                MsQualifiedNameToQualifiedName.getInstance().createTarget(msNode.getBrowseName(), factory),
                MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDisplayName(), factory),
                MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDescription(), factory),  
                UInteger.valueOf(msNode.getWriteMask()),
                UInteger.valueOf(msNode.getUserWriteMask()),
                msNode.isIsAbstract());


        factory.getNodeContext().getNodeManager().addNode(uaDataTypeNode);
        
        return uaDataTypeNode;
    }


}
