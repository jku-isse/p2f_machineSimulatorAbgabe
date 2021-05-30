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

import Simulator.MsInstanceInformation;
import Simulator.MsServerInterface;

/**
 * The server manager maintains the simulators OPC-UA server. It configures the server 
 * endpoints and hold an holds the OPC-UA namespace manager. Routines for starting up
 * or shutting down have been provided.
 * 
 * @author johannstoebich
 *
 */
class OpcUaServerManager extends AbstractLifecycle  {

    //Configuration
    private final MsServerInterface opcUaServerInterface;
        
    //Milo infrasturucture;
    private final OpcUaServer opcUaServer;
    private final OpcUaNamespaceManager opcUaNamespaceManager;
    
    /**
     * Returns the milo UPC-UA namespace manager of the server. The namespace manager contains
     * the server interface.
     * @return
     */
    OpcUaNamespaceManager getOpcUaNamespaceManager() {
        return this.opcUaNamespaceManager;
    }

    /**
     * Initializes a new OPC-UA server manager by providing a 
     * machine simulator configuration. This OPC-UA server manager is able to start and stop
     * an milo OPC-UA server based on the given configuration.
     * 
     * @param machineSimulator the configuration for initializing an OCP-UA server.
     */
    OpcUaServerManager(MsServerInterface opcUaServerInterface) {
        this.opcUaServerInterface = opcUaServerInterface;

        OpcUaServerConfig serverConfig = OpcUaServerConfig.builder()
                .setApplicationUri("")
                .setApplicationName(LocalizedText.english(opcUaServerInterface.getInstanceInformation().getDisplayName()))
                .setEndpoints(createEndpointConfigurations(opcUaServerInterface.getInstanceInformation()))
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
                opcUaServerInterface);
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
    
    
    /**
     * Starts the milo OPC-UA Server and its namespace manager. 
     * The namespace manager can be accessed from outside by calling 
     * {@link #getOpcUaNamespaceManager()}. 
     */
    @Override
    protected void onStartup() {
        try {
            this.opcUaNamespaceManager.startup();
            this.opcUaServer.startup().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stops the milo OPC-UA Server and its namespace manager.
     * The namespace manager can be accessed from outside by calling 
     * {@link #getOpcUaNamespaceManager()}. 
     */
    @Override
    protected void onShutdown() {
        try {
            this.opcUaNamespaceManager.shutdown();
            this.opcUaServer.shutdown().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
