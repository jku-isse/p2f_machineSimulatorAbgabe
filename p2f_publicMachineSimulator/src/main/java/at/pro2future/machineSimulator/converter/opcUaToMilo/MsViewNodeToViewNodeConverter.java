package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.sdk.server.api.nodes.ViewNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaViewNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import OpcUaDefinition.MsViewNode;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;


public class MsViewNodeToViewNodeConverter implements Converter<MsViewNode, ViewNode, OpcUaDefinitionFactory, UaBuilderFactory>{

	@Override
	public MsViewNode createFrom(ViewNode object, OpcUaDefinitionFactory factory) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public ViewNode createTo(MsViewNode msNode, UaBuilderFactory factory) throws Exception {
		UaViewNode uaViewNode =factory.getUaViewNode(
				new MsNodeIdToNodeIdConverter().createTo(msNode.getNodeId(), factory),
				new MsQualifiedNameToQualifiedName().createTo(msNode.getBrowseName(), factory),
				new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDisplayName(), factory),
				new MsLocalizedTextToLocalizedTextConverter().createTo(msNode.getDescription(), factory),  
				msNode.getWriteMask() == null ? UInteger.valueOf(Integer.MAX_VALUE) : UInteger.valueOf(msNode.getWriteMask()),
				msNode.getUserWriteMask() == null ? UInteger.valueOf(Integer.MAX_VALUE) : UInteger.valueOf(msNode.getUserWriteMask()),
				msNode.isContainsNoLoops(),
				UByte.valueOf(msNode.getEventNotifier())
        );

		factory.getNodeContext().getNodeManager().addNode(uaViewNode);
        
        return uaViewNode;
	}

}
