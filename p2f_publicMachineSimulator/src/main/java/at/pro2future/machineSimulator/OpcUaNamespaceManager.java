package at.pro2future.machineSimulator;

import java.util.List;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.DataItem;
import org.eclipse.milo.opcua.sdk.server.api.ManagedNamespace;
import org.eclipse.milo.opcua.sdk.server.api.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.api.nodes.Node;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import OpcUaDefinition.MsNode;
import Simulator.MsServerInterface;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;
import at.pro2future.machineSimulator.converter.opcUaToMilo.ConvertionNotSupportedException;
import at.pro2future.machineSimulator.converter.opcUaToMilo.MsNodeToNodeConverter;

/**
 * The namespace manager has the server objects. It contais the
 * server interface and the actions which can be invoked on it.
 * 
 * @author johannstoebich
 *
 */
public class OpcUaNamespaceManager extends ManagedNamespace  {

	private static Logger logger = LoggerFactory.getLogger(OpcUaNamespaceManager.class);
	
	private final MsServerInterface msServerInterface;
	private final UaBuilderFactory uaBuilderFactory;
	private final SubscriptionModel subscriptionModel;
	
	public UaBuilderFactory getUaBuilderFactory() {
		return this.uaBuilderFactory;
	}
	
	/**
	 * Initializes a new namespace manager. 
	 * 
	 * @param opcUaServer The milo OPC-UA server which is started up.
	 * @param msServerInterface The interface definition of the machine simulator defined by the configuration.
	 * @param actions The action which should be published.
	 */
	public OpcUaNamespaceManager(OpcUaServer opcUaServer, MsServerInterface msServerInterface) {
		//super(opcUaServer, msServerInterface.getNamespaceUri());
		super(opcUaServer, "urn:eclipse:milo:hello-world");
		this.msServerInterface = msServerInterface;
		this.subscriptionModel = new SubscriptionModel(opcUaServer, this);
		this.uaBuilderFactory = new UaBuilderFactory(getNodeContext(), getNamespaceIndex().intValue()); 
	}

	/**
	 * This method is called when the client is started. It registers the 
	 * object which are published by the server in the namespace.
	 */
	@Override
	protected void onStartup() {
		super.onStartup();
		this.subscriptionModel.startup();
		
		
		for(MsNode msNode : this.msServerInterface.getNodes()) {
			try {
				Node node = instantiateUaNode(msNode);

				// Make sure our new folder shows up under the server's Objects folder.
				addReference(new Reference(
						node.getNodeId(),
			            Identifiers.Organizes,
			            Identifiers.ObjectsFolder.expanded(),
			            false
			        ));
				
			} catch (ConvertionNotSupportedException  e) {
				logger.error("An error ocurred while creating the namespace.", e);
			} 
		}
	}
	
	private UaNode instantiateUaNode(MsNode msNode) throws ConvertionNotSupportedException {
		MsNodeToNodeConverter nodeConverter = new MsNodeToNodeConverter();
		return (UaNode)nodeConverter.createTo(msNode, this.uaBuilderFactory);
	}
	
	
	
	@Override 
	protected void onShutdown() {
		this.subscriptionModel.shutdown();
		super.onShutdown();
	}
	
	@Override
	public void onDataItemsCreated(List<DataItem> dataItems) {
	    this.subscriptionModel.onDataItemsCreated(dataItems);		
	}

	@Override
	public void onDataItemsModified(List<DataItem> dataItems) {
		this.subscriptionModel.onDataItemsModified(dataItems);		
	}

	@Override
	public void onDataItemsDeleted(List<DataItem> dataItems) {
		this.subscriptionModel.onDataItemsDeleted(dataItems);		
	}

	@Override
	public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
		this.subscriptionModel.onMonitoringModeChanged(monitoredItems);		
	}
	
	protected void addReference(Reference reference) {
		getNodeManager().addReferences(reference, getServer().getNamespaceTable());
	}

}
