package at.pro2future.machineSimulator.methodService.test;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.junit.jupiter.api.Test;

import OpcUaDefinition.MsLocalizedText;
import OpcUaDefinition.MsMethodNode;
import OpcUaDefinition.MsNodeId;
import OpcUaDefinition.MsQualifiedName;
import OpcUaDefinition.MsVariableNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.methodService.OpcUaMethodInvocationHandler;

public class DefaultMethodTest {


    @Test
    void invokePrivateMethod() {
    	
    	MsMethodNode methodNode = initializeMsMethodNode("NullMethod", "NullMethod", "NullMethod",
				"Method",  true, new ArrayList<MsVariableNode>(),  new ArrayList<MsVariableNode>(),
			"public Variant[] testMethod(Variant[] test){"
			+ "     return new Variant[0]; "
			+ "}");
    
		
    	OpcUaMethodInvocationHandler methodInvokationHanlder = new OpcUaMethodInvocationHandler(null, methodNode);
    	try {
			methodInvokationHanlder.invoke(null, new Variant[0]);
		} catch (UaException e) {
			assertTrue(false);
		}
    }
    
	private static MsMethodNode initializeMsMethodNode(String browseName, String displayName, String description, String nodeId, Boolean userExecutable, List<MsVariableNode> inputArguments, List<MsVariableNode> outputArguments,
			String method) {
		MsMethodNode msMethodNode = OpcUaDefinitionFactory.eINSTANCE.createMsMethodNode();
		MsQualifiedName browseNameNode = OpcUaDefinitionFactory.eINSTANCE.createMsQualifiedName();
		browseNameNode.setName(displayName);
		MsLocalizedText displayNameNode = OpcUaDefinitionFactory.eINSTANCE.createMsLocalizedText();
		displayNameNode.setText(displayName);
		MsLocalizedText descriptionNode = OpcUaDefinitionFactory.eINSTANCE.createMsLocalizedText();
		descriptionNode.setText(description);
		MsNodeId nodeIdNode = OpcUaDefinitionFactory.eINSTANCE.createMsNodeId();
		nodeIdNode.setIdentifier(nodeId);
		
		msMethodNode.setBrowseName(browseNameNode);
		msMethodNode.setDisplayName(displayNameNode);
		msMethodNode.setDescription(descriptionNode);
		msMethodNode.setNodeId(nodeIdNode);
		msMethodNode.setUserExecutalbe(userExecutable);
		msMethodNode.getInputArguments().addAll(inputArguments);
		msMethodNode.getOutputArguments().addAll(outputArguments);
		msMethodNode.setMethod(method);
		
		return msMethodNode;
	}

}
