package at.pro2future.machineSimulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.ServiceFaultListener;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.server.EndpointConfiguration;

import Simulator.MsClientInterface;
import Simulator.MsInstanceInformation;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;

/**
 * An OpcUaClient interface reprecents an interface to the server. It wraps the 
 * functionalities of an {@link OpcUaClient} so that it suites for its usage
 * in the simulator.
 * 
 * @author johannstoebich
 *
 */
public class OpcUaClientManager extends AbstractLifecycle  {


	private final OpcUaClient opcUaClient;
	private final UaBuilderFactory uaBuilderFactory;
	private final MsClientInterface opcUaClientInterface;
	private UaSubscription subscription;
	private final List<UaMonitoredItem> monitoredItems = new ArrayList<>();
	
	/**
	 * This method returns the configuration of the client manager.
	 * @return the configuration as {@link MsClientInterface}
	 */
	public MsClientInterface getOpcUaClientInterface() {
		return this.opcUaClientInterface;
	}
	
	/**
	 * The factory used for converting the simulator conifg to OpcUa nodes.
	 * @return
	 */
	public UaBuilderFactory getUaBuilderFactory() {
		return this.uaBuilderFactory;
	}

	/**
	 * The client manger gets as configuration the so called {@link MsClientInterface} and a factory to transform parts of the configuration to Milo components.
	 * 
	 * @param opcUaClientInterface
	 * @param uaBuilderFactory
	 * @throws UaException
	 */
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
	

	public void writeValues(NodeId nodeId, DataValue dataValue) throws UaException, InterruptedException, ExecutionException {
		CompletableFuture<StatusCode> f = this.opcUaClient.writeValue(nodeId, dataValue);
		
        // ...but block for the results so we write in order
        StatusCode statusCode = f.get();
         
        if (statusCode.isBad()) {
           throw new UaException(statusCode);
        }
	}
	
	public void subscribeValues(ReadValueId readValueId,  BiConsumer<UaMonitoredItem, DataValue>  callbackWhenValueChanged) throws InterruptedException, ExecutionException {

        // IMPORTANT: client handle must be unique per item within the context of a subscription.
        // You are not required to use the UaSubscription's client handle sequence; it is provided as a convenience.
        // Your application is free to assign client handles by whatever means necessary.
        UInteger clientHandle = this.subscription.nextClientHandle();

        MonitoringParameters parameters = new MonitoringParameters(
            clientHandle,
            Double.valueOf(1000.0),     // sampling interval
            null,       // filter, null means use default
            Unsigned.uint(10),   // queue size
            Boolean.valueOf(true)        // discard oldest
        );

        // when creating items in MonitoringMode.Reporting this callback is where each item needs to have its
        // value/event consumer hooked up. The alternative is to create the item in sampling mode, hook up the
        // consumer after the creation call completes, and then change the mode for all items to reporting.
        BiConsumer<UaMonitoredItem, Integer> itemCreationCallback =
            (item, id) -> item.setValueConsumer(callbackWhenValueChanged);
            
        MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(
        	readValueId,
            MonitoringMode.Reporting,
            parameters
        );
        
        List<UaMonitoredItem> items = this.subscription.createMonitoredItems(
                TimestampsToReturn.Both,
                Arrays.asList(request),
                itemCreationCallback
            ).get();
        
        this.monitoredItems.addAll(items);
	}
	

	public CompletableFuture<Variant[]> callMethod(CallMethodRequest request) {
		CompletableFuture<Variant[]> future = this.opcUaClient.call(request).thenCompose(result -> {
            StatusCode statusCode = result.getStatusCode();
            if (statusCode.isBad()) {
                throw new RuntimeException();
             }
            
            CompletableFuture<Variant[]> outputArguments = CompletableFuture.completedFuture(result.getOutputArguments());  
            return outputArguments;
        });
		
		return future;
	}
	
	@Override
	protected void onStartup() {
		try {
			this.opcUaClient.connect().get(60000, TimeUnit.MILLISECONDS);
			this.subscription = this.opcUaClient.getSubscriptionManager()
			            .createSubscription(1000.0)
			            .get();
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			throw new RuntimeException(e);
		}	
	}

	@Override
	protected void onShutdown() {
		try {
			this.opcUaClient.disconnect().get(2000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			throw new RuntimeException(e);
		}		
	}
}
