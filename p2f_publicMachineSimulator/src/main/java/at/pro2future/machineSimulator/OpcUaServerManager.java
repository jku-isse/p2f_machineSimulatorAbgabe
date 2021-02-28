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
import org.eclipse.milo.opcua.stack.server.EndpointConfiguration.Builder;

import Simulator.MachineSimulator;
import Simulator.MsInstanceInformation;

/**
 * The server manager maintains the simulator configuration .
 * 
 * @author johannstoebich
 *
 */
public class OpcUaServerManager extends AbstractLifecycle  {

	//Configuration
	MachineSimulator machineSimulator;
	
	public MachineSimulator getMachineSimulator() {
		return this.machineSimulator;
	}
	
	public void setMachineSimulator(MachineSimulator machineSimulator) {
		this.machineSimulator = machineSimulator;
	}
	
	//Milo infrasturucture;
	private OpcUaServer opcUaServer;
	private OpcUaNamespaceManager opcUaNamespaceManager;
	
	public OpcUaNamespaceManager getOpcUaNamespaceManager() {
		return this.opcUaNamespaceManager;
	}

	public OpcUaServerManager(MachineSimulator machineSimulator) {
		this.machineSimulator = machineSimulator;

		OpcUaServerConfig serverConfig = OpcUaServerConfig.builder()
	            .setApplicationUri("")
	            .setApplicationName(LocalizedText.english(machineSimulator.getInstanceInformation().getDisplayName()))
	            .setEndpoints(createEndpointConfigurations(machineSimulator.getInstanceInformation()))
	            .setBuildInfo(
	                new BuildInfo(
	                    "urn:eclipse:milo:example-server",
	                    "eclipse",
	                    "eclipse milo example server",
	                    OpcUaServer.SDK_VERSION,
	                    "", DateTime.now()))
	            
	            .build();

		this.opcUaServer = new OpcUaServer(serverConfig);

		this.opcUaNamespaceManager = new OpcUaNamespaceManager(this.opcUaServer, 
				machineSimulator.getOpcUaServerInterface());
	}
	
	private Set<EndpointConfiguration> createEndpointConfigurations(MsInstanceInformation instanceInformation){
		Set<EndpointConfiguration> endpointConfigurations = new HashSet<>();
		
		Builder endpointConfigurationBuilder = EndpointConfiguration.newBuilder()
				.setBindAddress(instanceInformation.getHost())
				.setBindPort(instanceInformation.getPort())
				.setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
				.setSecurityPolicy(SecurityPolicy.None)
				.setSecurityMode(MessageSecurityMode.None)
				.addTokenPolicies(
						org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS,
						org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME,
						org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig.USER_TOKEN_POLICY_X509);
		
		if(instanceInformation.getPath() != null) {
			endpointConfigurationBuilder.setPath(instanceInformation.getPath());
		}
		
		endpointConfigurations.add(endpointConfigurationBuilder
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
