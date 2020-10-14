package at.pro2future.machineSimulator;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.ServiceFaultListener;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.server.EndpointConfiguration;

import Simulator.MsClientInterface;
import Simulator.MsInstanceInformation;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;

public class OpcUaClientManager extends AbstractLifecycle  {


	private final OpcUaClient opcUaClient;
	private final UaBuilderFactory uaBuilderFactory;
	private final MsClientInterface opcUaClientInterface;
	
	public MsClientInterface getOpcUaClientInterface() {
		return opcUaClientInterface;
	}
	public UaBuilderFactory getUaBuilderFactory() {
		return uaBuilderFactory;
	}

	public OpcUaClientManager(MsClientInterface opcUaClientInterface, UaBuilderFactory uaBuilderFactory) throws UaException {
		OpcUaClientConfig clientConfig = OpcUaClientConfig.builder()
	            //.setApplicationUri("")
	            //.setApplicationName(LocalizedText.english(opcUaClientInterface.getTargetInstanceInformation().getDisplayName()))
				.setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
                .setApplicationUri("urn:eclipse:milo:examples:client")
                .setCertificate(null)
                .setKeyPair(null)
	            .setEndpoint(createEndpointConfiguration(opcUaClientInterface.getTargetInstanceInformation()))
	            .setIdentityProvider(new AnonymousProvider())
	            .setRequestTimeout(UInteger.valueOf(5000))	  
	            .setConnectTimeout(UInteger.valueOf(5000))
	            .build();
		
		this.opcUaClient = OpcUaClient.create(clientConfig);
		
		this.opcUaClient.addFaultListener(new ServiceFaultListener() {
			
			@Override
			public void onServiceFault(ServiceFault serviceFault) {
				System.out.println(serviceFault);
				
			}
		});
		this.uaBuilderFactory = uaBuilderFactory;
		this.opcUaClientInterface = opcUaClientInterface;
	}
	
	private EndpointDescription createEndpointConfiguration(MsInstanceInformation instanceInformation){	
		UserTokenPolicy[] userTokenPolicies = new UserTokenPolicy[] { 
				org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS};
		
		EndpointConfiguration endpointConfiguration = EndpointConfiguration.newBuilder()
	        .setBindAddress(instanceInformation.getHost())
	        .setBindPort(instanceInformation.getPort())
	        .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
	        .addTokenPolicies(userTokenPolicies)
	        .build();
		
		
		ApplicationDescription applicationDescription = ApplicationDescription.builder()
				.applicationType(ApplicationType.Server)
				.gatewayServerUri(endpointConfiguration.getEndpointUrl())
				.build();
				
		
		EndpointDescription desc = EndpointDescription.builder()
				.endpointUrl(endpointConfiguration.getEndpointUrl())
				.transportProfileUri(TransportProfile.TCP_UASC_UABINARY.getUri())
				.securityPolicyUri(SecurityPolicy.None.getUri())				
				.securityMode(MessageSecurityMode.None)
				.userIdentityTokens(userTokenPolicies)
				.server(applicationDescription)
				.build();
		
		return desc;
	}
	

	public void writeValues(NodeId nodeId, DataValue dataValue) throws UaException, InterruptedException, ExecutionException, TimeoutException {
		CompletableFuture<StatusCode> f = this.opcUaClient.writeValue(nodeId, dataValue);
		
        // ...but block for the results so we write in order
        StatusCode statusCode = f.get();
         
        if (statusCode.isBad()) {
           throw new UaException(statusCode);
        }
	}
	
	public void callMethod(CallMethodRequest request) throws UaException, InterruptedException, ExecutionException, TimeoutException {
		CompletableFuture<Object> future = this.opcUaClient.call(request).thenCompose( result -> {
            StatusCode statusCode = result.getStatusCode();
            if (statusCode.isBad()) {
                throw new RuntimeException();
             }
            return null;
        });
		future.get();
	}
	
	@Override
	protected void onStartup() {
		try {
			opcUaClient.connect().get(60000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			throw new RuntimeException(e);
		}	
	}

	@Override
	protected void onShutdown() {
		try {
			opcUaClient.disconnect().get(2000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			throw new RuntimeException(e);
		}		
	}
}
