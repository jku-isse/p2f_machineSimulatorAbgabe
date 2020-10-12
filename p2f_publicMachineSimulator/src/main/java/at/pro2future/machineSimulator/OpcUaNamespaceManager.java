package at.pro2future.machineSimulator;

import java.util.List;
import javax.naming.OperationNotSupportedException;

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.DataItem;
import org.eclipse.milo.opcua.sdk.server.api.ManagedNamespace;
import org.eclipse.milo.opcua.sdk.server.api.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.api.nodes.Node;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import OpcUaDefinition.MsNode;
import Simulator.MsServerInterface;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;
import at.pro2future.machineSimulator.converter.opcUaToMilo.MsNodeToNodeConverter;

public class OpcUaNamespaceManager extends ManagedNamespace  {

	private final MsServerInterface msServerInterface;
	private final UaBuilderFactory uaBuilderFactory;
	private final SubscriptionModel subscriptionModel;
	
	public UaBuilderFactory getUaBuilderFactory() {
		return uaBuilderFactory;
	}
	
	public OpcUaNamespaceManager(OpcUaServer opcUaServer, MsServerInterface msServerInterface) {
		//super(opcUaServer, msServerInterface.getNamespaceUri());
		super(opcUaServer, "urn:eclipse:milo:hello-world");
		this.msServerInterface = msServerInterface;
		this.subscriptionModel = new SubscriptionModel(opcUaServer, this);
		this.uaBuilderFactory = new UaBuilderFactory(getNodeContext(), getNamespaceIndex().intValue()); 
	}

	@Override
	protected void onStartup() {
		super.onStartup();
		subscriptionModel.startup();
		
		
		for(MsNode msNode : msServerInterface.getNodes()) {
			try {
				Node node = instantiateUaNode(msNode);

				// Make sure our new folder shows up under the server's Objects folder.
				addReference(new Reference(
						node.getNodeId(),
			            Identifiers.Organizes,
			            Identifiers.ObjectsFolder.expanded(),
			            false
			        ));
				
			} catch (OperationNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private UaNode instantiateUaNode(MsNode msNode) throws Exception {
		MsNodeToNodeConverter nodeConverter = new MsNodeToNodeConverter();
		return (UaNode)nodeConverter.createTo(msNode, this.uaBuilderFactory);
	}
	
	
	
	@Override 
	protected void onShutdown() {
		subscriptionModel.shutdown();
		super.onShutdown();
	}
	
	@Override
	public void onDataItemsCreated(List<DataItem> dataItems) {
	    subscriptionModel.onDataItemsCreated(dataItems);		
	}

	@Override
	public void onDataItemsModified(List<DataItem> dataItems) {
		subscriptionModel.onDataItemsModified(dataItems);		
	}

	@Override
	public void onDataItemsDeleted(List<DataItem> dataItems) {
		subscriptionModel.onDataItemsDeleted(dataItems);		
	}

	@Override
	public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
		subscriptionModel.onMonitoringModeChanged(monitoredItems);		
	}
	
	protected void addReference(Reference reference) {
		getNodeManager().addReferences(reference, getServer().getNamespaceTable());
	}

}
