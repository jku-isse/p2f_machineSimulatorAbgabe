package at.pro2future.machineSimulator;

import java.util.HashSet;

import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.server.EndpointConfiguration;


import Simulator.MsInstanceInformation;
import Simulator.MsServerInterface;


public class OpcUaServerManager extends AbstractLifecycle  {

	//Configuration
	private MsServerInterface opcUaServerInterface;
	private MsInstanceInformation instanceInformation;
	
	//Milo infrasturucture;
	private OpcUaServer opcUaServer;
	private OpcUaNamespaceManager opcUaNamespaceManager;
	
	public OpcUaNamespaceManager getOpcUaNamespaceManager() {
		return this.opcUaNamespaceManager;
	}
	
	public MsServerInterface getOpcUaServerInterface() {
		return this.opcUaServerInterface;
	}

	public void setOpcUaServerInterface(MsInstanceInformation instanceInformation, MsServerInterface opcUaServerInterface) {
		this.instanceInformation = instanceInformation;
		this.opcUaServerInterface = opcUaServerInterface;
	}
	
	public MsInstanceInformation getInstanceInformation() {
		return this.instanceInformation;
	}

	public void setInstanceInformation(MsInstanceInformation instanceInformation) {
		this.instanceInformation = instanceInformation;
	}

	public void setOpcUaServerInterface(MsServerInterface opcUaServerInterface) {
		this.opcUaServerInterface = opcUaServerInterface;
	}

	public OpcUaServerManager(MsInstanceInformation instanceInformation, MsServerInterface opcUaServerInterface) {
		this.opcUaServerInterface = opcUaServerInterface;
		this.instanceInformation = instanceInformation;
		
		OpcUaServerConfig serverConfig = OpcUaServerConfig.builder()
	            .setApplicationUri("")
	            .setApplicationName(LocalizedText.english(instanceInformation.getDisplayName()))
	            .setEndpoints(createEndpointConfigurations(instanceInformation))
	            .setBuildInfo(
	                new BuildInfo(
	                    "urn:eclipse:milo:example-server",
	                    "eclipse",
	                    "eclipse milo example server",
	                    OpcUaServer.SDK_VERSION,
	                    "", DateTime.now()))
	            
	            .build();

		this.opcUaServer = new OpcUaServer(serverConfig);

		this.opcUaNamespaceManager = new OpcUaNamespaceManager(this.opcUaServer, opcUaServerInterface);
	}
	
	private Set<EndpointConfiguration> createEndpointConfigurations(MsInstanceInformation instanceInformation){
		Set<EndpointConfiguration> endpointConfigurations = new HashSet<>();
		
		endpointConfigurations.add(EndpointConfiguration.newBuilder()
	                    .setBindAddress(instanceInformation.getHost())
	                    .setBindPort(instanceInformation.getPort())
	                    .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
	                    .setSecurityPolicy(SecurityPolicy.None)
	                    .setSecurityMode(MessageSecurityMode.None)
	                    .addTokenPolicies(
	                    		org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS,
	                    		org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME,
	                    		org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig.USER_TOKEN_POLICY_X509)
	                    .build());
		
		return endpointConfigurations;
	}
	
	

	@Override
	public void onStartup() {
		try {
			this.opcUaNamespaceManager.startup();
			this.opcUaServer = this.opcUaServer.startup().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onShutdown() {
		try {
			this.opcUaNamespaceManager.shutdown();
			this.opcUaServer.shutdown().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

}
