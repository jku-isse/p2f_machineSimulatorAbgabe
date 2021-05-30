package at.pro2future.machineSimulator.converter.opcUaToMilo;

import java.io.IOException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.milo.opcua.sdk.server.api.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;

import OpcUaDefinition.MsMethodNode;
import OpcUaDefinition.MsVariableNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.IConverter;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;
import at.pro2future.machineSimulator.methodService.OpcUaMethodInvocationHandler;

/**
 * Converts a {@link #MsMethodNode} to a {@link #MethodNode} and vice versa. The class uses the given 
 * factories, the {@link #OpcUaDefinitionFactory} and the {@link #IUaObjectAndBuilderProvider} to perform the transformation.
 *
 */
public class MsMethodNodeToMethodNodeConverter implements IConverter<MsMethodNode, MethodNode, OpcUaDefinitionFactory, IUaObjectAndBuilderProvider>{

    /**
     * The singleton instance <code>MsMethodNodeToMethodNodeConverter<code> of the converter.
     */
    private static MsMethodNodeToMethodNodeConverter instance;
    
    /**
     * Returns the only instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    static MsMethodNodeToMethodNodeConverter getInstance() {
        if(instance == null) {
            instance = new MsMethodNodeToMethodNodeConverter();
        }
        return instance;
    }
    
    /**
     * The singleton instance <code>MsMethodNodeToMethodNodeConverter<code> of the converter.
     */
    private MsMethodNodeToMethodNodeConverter() {
    }
    
    /**
     * Creates a {@link MsMethodNode} from the given {@link MethodNode} by using an {@link OpcUaDefinitionFactory}.
     * 
     * @param object the <code>MethodNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>MsMethodNode</code>.
     * @return the corresponding <code>MsMethodNode</code> object from the given <code>MethodNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public MsMethodNode createSource(MethodNode object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException, ConversionFailureException {
        throw new ConvertionNotSupportedException();
    }

    /**
     * Creates a {@link MethodNode} from the given {@link MsMethodNode} by using an {@link IUaObjectAndBuilderProvider}.
     * 
     * @param object the <code>MsMethodNode</code> which should be converted.
     * @param factory the factory which is able to create the <code>MethodNode</code>.
     * @return the corresponding <code>MethodNode</code> object from the given <code>MsMethodNode</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public MethodNode createTarget(MsMethodNode msNode, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException {
        UaMethodNode uaMethodNode = factory.getUaMethodNodeBuilder()
                .setNodeId(MsNodeIdToNodeIdConverter.getInstance().createTarget(msNode.getNodeId(), factory))
                .setBrowseName(MsQualifiedNameToQualifiedName.getInstance().createTarget(msNode.getBrowseName(), factory)) 
                .setDisplayName(MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDisplayName(), factory)) 
                .setDescription(MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msNode.getDescription(), factory))  
                .setWriteMask(UInteger.valueOf(msNode.getWriteMask())) 
                .setUserWriteMask(UInteger.valueOf(msNode.getUserWriteMask()))
                .setExecutable(msNode.isExecutable())
                .setUserExecutable(msNode.isUserExecutalbe())
                .build();
        
        Argument[] inputArguments = convertMsVariableNodeToArgument(msNode.getInputArguments(), factory);
        Argument[] outputArguments = convertMsVariableNodeToArgument(msNode.getOutputArguments(), factory);
        uaMethodNode.setInputArguments(inputArguments);
        uaMethodNode.setOutputArguments(outputArguments);
        try {
            uaMethodNode.setInvocationHandler(new OpcUaMethodInvocationHandler(uaMethodNode, msNode));
        } catch (IOException e) {
            throw new ConversionFailureException("Could not create a MethodNode from an MsMethodNode", e);
        }
        
        factory.getNodeContext().getNodeManager().addNode(uaMethodNode);
        
        return uaMethodNode;
    }
    
    private static Argument[] convertMsVariableNodeToArgument(EList<MsVariableNode> msVariableNodes, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException  {
        Argument[] arguments = new Argument[msVariableNodes.size()];
        
        for(int i = 0; i < msVariableNodes.size(); i++) {
            arguments[i] = convertMsVariableNodeToArgument(msVariableNodes.get(i), factory);
        }
        
        return arguments;
    }
    
    private static Argument convertMsVariableNodeToArgument(MsVariableNode msVariableNode, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException  {
        Argument arguement = Argument.builder()
                .name(msVariableNode.getBrowseName().getName()) 
                .dataType(MsNodeIdToNodeIdConverter.getInstance().createTarget(msVariableNode.getNodeId(), factory))                
                .description(MsLocalizedTextToLocalizedTextConverter.getInstance().createTarget(msVariableNode.getDescription(), factory))
                .valueRank(msVariableNode.getValueRank())
                .build();    
                
        return arguement;
    }

}
