package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.emf.common.util.EList;
import org.eclipse.milo.opcua.sdk.server.api.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;

import OpcUaDefinition.MsMethodNode;
import OpcUaDefinition.MsVariableNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;
import at.pro2future.machineSimulator.methodService.OpcUaMethodInvocationHandler;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class MsMethodNodeToMethodNodeConverter implements Converter<MsMethodNode, MethodNode, OpcUaDefinitionFactory, UaBuilderFactory>{

	@Override
	public MsMethodNode createFrom(MethodNode object, OpcUaDefinitionFactory factory) throws Exception {
		throw new NotImplementedException();
	}

	@Override
	public MethodNode createTo(MsMethodNode msNode, UaBuilderFactory factory) throws Exception {
		UaMethodNode uaMethodNode = factory.getUaMethodNodeBuilder()
				.setNodeId(new MsNodeIdToNodeIdConverter().createTo(msNode.getNodeId(), factory))
				.setBrowseName(new MsQualifiedNameToQualifiedName().createTo(msNode.getBrowseName(), factory)) 
				.setDisplayName(new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDisplayName(), factory)) 
				.setDescription(new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDescription(), factory))  
				.setWriteMask(msNode.getWriteMask() == null ? null : UInteger.valueOf(msNode.getWriteMask())) 
				.setUserWriteMask(msNode.getUserWriteMask() == null ? null : UInteger.valueOf(msNode.getUserWriteMask()))
				.setExecutable(msNode.getExecutable())
				.setUserExecutable(msNode.getUserExecutalbe())
				.build();
		
		Argument[] inputArguments = convertMsVariableNodeToArgument(msNode.getInputArguments(), factory);
		Argument[] outputArguments = convertMsVariableNodeToArgument(msNode.getOutputArguments(), factory);
		uaMethodNode.setInputArguments(inputArguments);
		uaMethodNode.setOutputArguments(outputArguments);
		uaMethodNode.setInvocationHandler(new OpcUaMethodInvocationHandler(uaMethodNode, msNode));
		
		factory.getNodeContext().getNodeManager().addNode(uaMethodNode);
        
        return uaMethodNode;
	}
	
	private Argument[] convertMsVariableNodeToArgument(EList<MsVariableNode> msVariableNodes, UaBuilderFactory factory) throws Exception {
		Argument[] arguments = new Argument[msVariableNodes.size()];
		
		for(int i = 0; i < msVariableNodes.size(); i++) {
			arguments[i] = convertMsVariableNodeToArgument(msVariableNodes.get(i), factory);
		}
		
		return arguments;
	}
	
	private Argument convertMsVariableNodeToArgument(MsVariableNode msVariableNode, UaBuilderFactory factory) throws Exception {
		Argument arguement = Argument.builder()
				.name(msVariableNode.getBrowseName().getName()) 
				.dataType(new MsNodeIdToNodeIdConverter().createTo(msVariableNode.getNodeId(), factory))				
				.description(new MsLocalizedTextToLocalizedTextConverter().createTo(msVariableNode.getDescription(), factory))
				.valueRank(msVariableNode.getValueRank())
				.build();	
				
		return arguement;
	}

}
