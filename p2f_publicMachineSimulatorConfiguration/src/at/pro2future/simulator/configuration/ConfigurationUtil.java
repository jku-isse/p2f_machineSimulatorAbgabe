package at.pro2future.simulator.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

import OpcUaDefinition.MsDataVariableNode;
import OpcUaDefinition.MsLocalizedText;
import OpcUaDefinition.MsMethodNode;
import OpcUaDefinition.MsNodeId;
import OpcUaDefinition.MsObjectNode;
import OpcUaDefinition.MsObjectTypeNode;
import OpcUaDefinition.MsPropertyNode;
import OpcUaDefinition.MsQualifiedName;
import OpcUaDefinition.MsVariableNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import OpcUaDefinition.OpcUaDefinitionPackage;
import ProcessCore.AbstractCapability;
import ProcessCore.Assignment;
import ProcessCore.Condition;
import ProcessCore.Constant;
import ProcessCore.Decision;
import ProcessCore.Event;
import ProcessCore.HeadLoop;
import ProcessCore.LocalVariable;
import ProcessCore.Operator;
import ProcessCore.Parameter;
import ProcessCore.ProcessCoreFactory;
import ProcessCore.SetVariableStep;
import ProcessCore.SimpleCondition;
import ProcessCore.VariableMapping;
import Simulator.MsCallMethodCapabilityAdressSpaceAction;
import Simulator.MsClientInterface;
import Simulator.MsEventAdressSpaceAction;
import Simulator.MsReadCapabilityAdressSpaceAction;
import Simulator.MsWriteCapabilityAdressSpaceAction;
import Simulator.ProcessOpcUaMapping;
import Simulator.SimulatorFactory;

public class ConfigurationUtil {

    private static int uniqueNumber = 0;
    
    public static MsMethodNode initializeMsMethodNode(String browseName, String displayName, String description, MsNodeId nodeId, Boolean executable, Boolean userExecutable, List<MsVariableNode> inputArguments, List<MsVariableNode> outputArguments,
            String method) {
        MsMethodNode msMethodNode = OpcUaDefinitionFactory.eINSTANCE.createMsMethodNode();
        msMethodNode.setBrowseName(createMsQualifiedName(browseName));
        msMethodNode.setDisplayName(createMsLocalizedText(displayName));
        msMethodNode.setDescription(createMsLocalizedText(description));
        msMethodNode.setNodeId(nodeId);
        msMethodNode.setExecutable(executable);
        msMethodNode.setUserExecutalbe(userExecutable);
        msMethodNode.getInputArguments().addAll(inputArguments);
        msMethodNode.getOutputArguments().addAll(outputArguments);
        msMethodNode.setMethod(method);
        
        return msMethodNode;
    }
    
    public static LocalVariable initializeLocalVariable(String name, String type, Object value) {
        LocalVariable localVariable = ProcessCoreFactory.eINSTANCE.createLocalVariable();
        localVariable.setName(name);
        localVariable.setType(type);
        localVariable.setValue(value);
        return localVariable;
    }

    protected static Constant initializeConstance(String name, String type, Object value) {
        Constant constant = ProcessCoreFactory.eINSTANCE.createConstant();
        constant.setName(name);
        constant.setType(type);
        constant.setValue(value);
        return constant;
    }

    protected static ProcessCore.Process initializeProcess(String displayName) {
        ProcessCore.Process process = ProcessCoreFactory.eINSTANCE.createProcess();
        process.setID(EcoreUtil.generateUUID());
        process.setDisplayName(displayName);
        return process;
    }

    protected static SetVariableStep initializeSetVariableStep(String displayName, Parameter modifiedVariable, Parameter rhs) {
        SetVariableStep setVariableStep = ProcessCoreFactory.eINSTANCE.createSetVariableStep();
        setVariableStep.setID((EcoreUtil.generateUUID()));
        setVariableStep.setDisplayName(displayName);
        setVariableStep.setModifiedVariable(modifiedVariable);
        setVariableStep.setRhs(rhs);
        return setVariableStep;
    }
    
    protected static HeadLoop initializeHeadLoop(String displayName, ProcessCore.Process body, Condition condition) {
        HeadLoop headLoop = ProcessCoreFactory.eINSTANCE.createHeadLoop();
        headLoop.setID((EcoreUtil.generateUUID()));
        headLoop.setDisplayName(displayName);
        headLoop.setBody(body);
        headLoop.setCondition(condition);
        return headLoop;
    }
    
    protected static Condition initializeSimpleCondition(Parameter lhs, Operator operator, Parameter rhs) {
        SimpleCondition simpleCondition = ProcessCoreFactory.eINSTANCE.createSimpleCondition();
        simpleCondition.setLhs(lhs);
        simpleCondition.setOperator(operator);
        simpleCondition.setRhs(rhs);
        return simpleCondition;
    }
    
    protected static Decision initializeDecision(String displayName, ProcessCore.Process ifPart,  ProcessCore.Process elsePart, Condition condition) {
        Decision decision = ProcessCoreFactory.eINSTANCE.createDecision();
        decision.setDisplayName(displayName);
        decision.setCondition(condition);
        decision.setIf(ifPart);
        decision.setElse(elsePart);
        return decision;
    }

    protected static MsObjectTypeNode initializeMsObjectTypeNode(String browseName, String displayName, String description) {

        MsObjectTypeNode opcUaObjectTypeNode = OpcUaDefinitionFactory.eINSTANCE.createMsObjectTypeNode();
        opcUaObjectTypeNode.setBrowseName(createMsQualifiedName(browseName));
        opcUaObjectTypeNode.setDisplayName(createMsLocalizedText(displayName));
        opcUaObjectTypeNode.setDescription(createMsLocalizedText(description));
        opcUaObjectTypeNode.setNodeId(createMsNodeId(true));

        return opcUaObjectTypeNode;
    }

    protected static MsObjectNode initializeMsObjectNode(String browseName, String displayName, String description) {
        MsObjectNode opcUaObject = OpcUaDefinitionFactory.eINSTANCE.createMsObjectNode();
        opcUaObject.setBrowseName(createMsQualifiedName(browseName));
        opcUaObject.setDisplayName(createMsLocalizedText(displayName));
        opcUaObject.setDescription(createMsLocalizedText(description));
        opcUaObject.setNodeId(createMsNodeId(true));
        opcUaObject.setUserWriteMask(Integer.MAX_VALUE);
        opcUaObject.setWriteMask(Integer.MAX_VALUE);
        return opcUaObject;
    }
    
    protected static ProcessCore.EventSource initializeEventSource(String displayName, Event event, List<ProcessCore.Parameter> parameters) {
        List<VariableMapping> variableMappings = initilizeMappings(event.getParameters(), parameters);
        
        ProcessCore.EventSource eventSource = ProcessCoreFactory.eINSTANCE.createEventSource();
        eventSource.setDisplayName(displayName);
        eventSource.setEvent(event);
        eventSource.getInputMappings().addAll(variableMappings);
        return eventSource;
    }
    
    protected static ProcessCore.EventSink initializeEventSink(String displayName, Event event, List<ProcessCore.Parameter> parameters) {
        List<VariableMapping> variableMappings = initilizeMappings(event.getParameters(), parameters);
        
        ProcessCore.EventSink eventSink = ProcessCoreFactory.eINSTANCE.createEventSink();
        eventSink.setDisplayName(displayName);
        eventSink.setEvent(event);
        eventSink.getOutputMappings().addAll(variableMappings);
        return eventSink;
    }
    
    protected static ProcessCore.ReadParameter initializeReadParameter(String displayName, AbstractCapability capability, List<ProcessCore.Parameter> parameters) {
        List<VariableMapping> outputMappings = initilizeMappings(parameters, capability.getOutputs());
        
        ProcessCore.ReadParameter readParameter = ProcessCoreFactory.eINSTANCE.createReadParameter();
        readParameter.setDisplayName(displayName);
        readParameter.setInvokedCapability(capability);
        readParameter.getOutputMappings().addAll(outputMappings);
        return readParameter;
    }
    
    protected static ProcessCore.WriteParameter initializeWriteParameter(String displayName, AbstractCapability capability, List<ProcessCore.Parameter> parameters) {
        List<VariableMapping> inputMappings = initilizeMappings(capability.getInputs(), parameters);
        
        ProcessCore.WriteParameter writeParameter = ProcessCoreFactory.eINSTANCE.createWriteParameter();
        writeParameter.setDisplayName(displayName);
        writeParameter.setInvokedCapability(capability);
        writeParameter.getInputMappings().addAll(inputMappings);
        return writeParameter;
    }

    private static List<VariableMapping> initilizeMappings(List<ProcessCore.Parameter> parametersTo, List<ProcessCore.Parameter> parametersFrom) {
        List<VariableMapping> variableMappings = new ArrayList<VariableMapping>();
        for(int i = 0; i < parametersTo.size(); i++) {
            VariableMapping variableMapping = ProcessCoreFactory.eINSTANCE.createVariableMapping();
            variableMapping.setLhs(parametersTo.get(i));
            variableMapping.setRhs(parametersFrom.get(i));
            variableMappings.add(variableMapping);
        }
        return variableMappings;
    }
    
    protected static ProcessCore.HumanStep initializeHumanStep(String name, String description) {
        ProcessCore.Case humanStep = ProcessCoreFactory.eINSTANCE.createCase();
        humanStep.setName(name);
        humanStep.setDisplayName(name);
        humanStep.setDescription(description);
        return humanStep;
    }

    protected static MsPropertyNode initializeMsPropertyNode(String browseName, String displayName, String description,
            String nodeClass, MsNodeId nodeId, Object value, MsNodeId dataType, MsNodeId modellingRule) {
        MsPropertyNode property = OpcUaDefinitionFactory.eINSTANCE.createMsPropertyNode();
        property.setBrowseName(createMsQualifiedName(browseName));
        property.setDisplayName(createMsLocalizedText(displayName));
        property.setDescription(createMsLocalizedText(description));
        property.setNodeId(nodeId);
        property.setValue(value);
        property.setDataType(dataType);
        property.setHasModellingRule(modellingRule);
        property.setAccessLevel((byte)255);
        property.setUserAccessLevel((byte)255);
        return property;
    }

    public static <T extends MsEventAdressSpaceAction> T msEventAdressSpaceAction (MsClientInterface clientInterface, Event reactsTo,
            List<MsVariableNode> variables, T msAction) {

        msAction.setOpcUaClientInterface(clientInterface);
        msAction.setRefersTo(reactsTo);
        
        if(reactsTo.getParameters().size() != variables.size()) {
            throw new IllegalArgumentException("Cannot create mapping!");
        }

        for(int i = 0; i < reactsTo.getParameters().size(); i++) {
            ProcessOpcUaMapping processOpcUaVariableMapping = SimulatorFactory.eINSTANCE.createProcessOpcUaMapping();
            processOpcUaVariableMapping.setParameter(reactsTo.getParameters().get(i));
            processOpcUaVariableMapping.setAttributeNodeId(variables.get(i).getNodeId());
            
            msAction.getParameterMappings().add(processOpcUaVariableMapping);
        }
        
        return msAction;
    }
    
    
    public static <T extends MsReadCapabilityAdressSpaceAction> T msReadCapabilityAdressSpaceAction (MsClientInterface clientInterface, 
            AbstractCapability abstractCapability, List<MsVariableNode> variables, T msAction) {

        msAction.setOpcUaClientInterface(clientInterface);
        msAction.setRefersTo(abstractCapability);
        
        if(abstractCapability.getOutputs().size() != variables.size()) {
            throw new IllegalArgumentException("Cannot create mapping!");
        }

        for(int i = 0; i < abstractCapability.getOutputs().size(); i++) {
            ProcessOpcUaMapping processOpcUaVariableMapping = SimulatorFactory.eINSTANCE.createProcessOpcUaMapping();
            processOpcUaVariableMapping.setParameter(abstractCapability.getOutputs().get(i));
            processOpcUaVariableMapping.setAttributeNodeId(variables.get(i).getNodeId());
            
            msAction.getParameterMappings().add(processOpcUaVariableMapping);
        }
        
        return msAction;
    }
    
    public static <T extends MsWriteCapabilityAdressSpaceAction> T msWriteCapabilityAdressSpaceAction (MsClientInterface clientInterface, 
            AbstractCapability abstractCapability, List<MsVariableNode> variables, T msAction) {

        msAction.setOpcUaClientInterface(clientInterface);
        msAction.setRefersTo(abstractCapability);
        
        if(abstractCapability.getInputs().size() != variables.size()) {
            throw new IllegalArgumentException("Cannot create mapping!");
        }

        for(int i = 0; i < abstractCapability.getInputs().size(); i++) {
            ProcessOpcUaMapping processOpcUaVariableMapping = SimulatorFactory.eINSTANCE.createProcessOpcUaMapping();
            processOpcUaVariableMapping.setParameter(abstractCapability.getInputs().get(i));
            processOpcUaVariableMapping.setAttributeNodeId(variables.get(i).getNodeId());
            
            msAction.getParameterMappings().add(processOpcUaVariableMapping);
        }
        
        return msAction;
    }
    
    public static <T extends MsCallMethodCapabilityAdressSpaceAction> T msCallMethodCapabilityAdressSpaceAction (MsClientInterface clientInterface, 
            AbstractCapability abstractCapability, List<MsVariableNode> inputVariables, List<MsVariableNode> outputVariables, T msAction) {

        msAction.setOpcUaClientInterface(clientInterface);
        msAction.setRefersTo(abstractCapability);
        
        if(abstractCapability.getInputs().size() != inputVariables.size()) {
            throw new IllegalArgumentException("Cannot create mapping!");
        }
        
        if(abstractCapability.getOutputs().size() != outputVariables.size()) {
            throw new IllegalArgumentException("Cannot create mapping!");
        }
        
        for(int i = 0; i < abstractCapability.getOutputs().size(); i++) {
            ProcessOpcUaMapping processOpcUaVariableMapping = SimulatorFactory.eINSTANCE.createProcessOpcUaMapping();
            processOpcUaVariableMapping.setParameter(abstractCapability.getInputs().get(i));
            processOpcUaVariableMapping.setAttributeNodeId(inputVariables.get(i).getNodeId());
            
            msAction.getParameterMappings().add(processOpcUaVariableMapping);
        }
        
        for(int i = 0; i < abstractCapability.getOutputs().size(); i++) {
            ProcessOpcUaMapping processOpcUaVariableMapping = SimulatorFactory.eINSTANCE.createProcessOpcUaMapping();
            processOpcUaVariableMapping.setParameter(abstractCapability.getOutputs().get(i));
            processOpcUaVariableMapping.setAttributeNodeId(outputVariables.get(i).getNodeId());
            
            msAction.getReturnParameterMapping().add(processOpcUaVariableMapping);
        }
        
        return msAction;
    }
    
    protected static <T extends EObject> T copyWithNewNodeId(T eObject, boolean allowOverrideNamespaceIndex) {
        T eObjectCopy = EcoreUtil.copy(eObject);
        EStructuralFeature structuralFeature = eObject.eClass().getEStructuralFeature(OpcUaDefinitionPackage.MS_NODE__NODE_ID);
        eObject.eSet(structuralFeature, createMsNodeId(allowOverrideNamespaceIndex));
        return eObjectCopy;
    }    
    
    protected static MsNodeId createMsNodeId(boolean allowOverrideNamespaceIndex) {
        MsNodeId msNodeId = OpcUaDefinitionFactory.eINSTANCE.createMsNodeId();
        msNodeId.setNamespaceIndex((short) 0);
        msNodeId.setAllowOverrideNamespaceIndex(allowOverrideNamespaceIndex);

        msNodeId.setIdentifier("NODE_ID_" + uniqueNumber);
        uniqueNumber++;
        //msNodeId.setIdentifier(EcoreUtil.generateUUID().substring(1));
        return msNodeId;
    }
    
    protected static MsNodeId createMsNodeId(boolean allowOverrideNamespaceIndex, NodeId nodeId) {
        MsNodeId msNodeId = createMsNodeId(allowOverrideNamespaceIndex);
        msNodeId.setIdentifier(nodeId.getIdentifier());
        return msNodeId;
    }
    
    protected static MsNodeId createMsNodeId(boolean allowOverrideNamespaceIndex, Integer identifyer) {
        MsNodeId msNodeId = createMsNodeId(allowOverrideNamespaceIndex);
        msNodeId.setIdentifier(identifyer);
        return msNodeId;
    }
    
    public static MsNodeId createMsNodeId(boolean allowOverrideNamespaceIndex, String identifyer) {
        MsNodeId msNodeId = createMsNodeId(allowOverrideNamespaceIndex);
        msNodeId.setIdentifier(identifyer);
        return msNodeId;
    }
    
    public static MsDataVariableNode createMsDataVariableNode(MsNodeId nodeId) {
        MsDataVariableNode msDataVariableNode = OpcUaDefinitionFactory.eINSTANCE.createMsDataVariableNode();
        msDataVariableNode.setNodeId(nodeId);
        return msDataVariableNode;
    }
    
    protected static MsLocalizedText createMsLocalizedText(String text) {
        MsLocalizedText msLocalizedText = OpcUaDefinitionFactory.eINSTANCE.createMsLocalizedText();
        msLocalizedText.setLocale("en");
        msLocalizedText.setText(text);
        return msLocalizedText;
    }
    
    protected static MsQualifiedName createMsQualifiedName(String text) {
        MsQualifiedName msQualifiedName = OpcUaDefinitionFactory.eINSTANCE.createMsQualifiedName();
        msQualifiedName.setNamespaceIndex((short) 0 );
        msQualifiedName.setName(text);
        return msQualifiedName;
    }    
    
      
    

    protected static Event initializeEvent(String name, Assignment assignment, Parameter parameter) {
        return initializeEvent(name, assignment, Arrays.asList(parameter)) ;
    }
    
    protected static Event initializeEvent(String name, Assignment assignment, List<Parameter> parameters) {
        Event workpieceChanged = ProcessCoreFactory.eINSTANCE.createEvent();
        workpieceChanged.setName(name);
        workpieceChanged.setRole(assignment);
        workpieceChanged.getParameters().addAll(parameters);
        return workpieceChanged;
    }
}
