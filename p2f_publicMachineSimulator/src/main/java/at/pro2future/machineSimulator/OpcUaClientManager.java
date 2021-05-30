package at.pro2future.machineSimulator;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import Simulator.MsClientInterface;
import Simulator.MsInstanceInformation;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;

/**
 * The <code>OpcUaClientManager</code> contains the functionalities to connect to an OPC-UA Server. It wraps the 
 * functionalities of an {@link OpcUaClient} for the usage in this simulator.
 * 
 * @author johannstoebich
 *
 */
public class OpcUaClientManager extends AbstractLifecycle  {

    private final static Logger LOGGER = LoggerFactory.getLogger(OpcUaClientManager.class); 
    
    private final OpcUaClient opcUaClient;
    private final IUaObjectAndBuilderProvider uaObjectAndBuilderProvider;
    private final MsClientInterface opcUaClientInterface;
    private final CheckConnectionThread checkConnectionThread;
    private UaSubscription subscription;
    
    /**
     * This method returns the configuration of the client manager. 
     * @return the configuration as {@link MsClientInterface}
     */
    MsClientInterface getOpcUaClientInterface() {
        return this.opcUaClientInterface;
    }
    
    /**
     * The factory used for converting the simulator conifg to OpcUa nodes.
     * @return
     */
    public IUaObjectAndBuilderProvider getUaObjectAndBuilderProvider() {
        return this.uaObjectAndBuilderProvider;
    }

    /**
     * The client manger gets as configuration the so called {@link MsClientInterface} and a factory to transform parts of the configuration to Milo components.
     * 
     * @param opcUaClientInterface
     * @param uaBuilderFactory
     * @throws UaException
     */
    OpcUaClientManager(MsClientInterface opcUaClientInterface, IUaObjectAndBuilderProvider uaObjectAndBuilderProvider) throws UaException {
        OpcUaClientConfig clientConfig = OpcUaClientConfig.builder()
                //.setApplicationUri("")
                //.setApplicationName(LocalizedText.english(opcUaClientInterface.getTargetInstanceInformation().getDisplayName()))
                .setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
                .setApplicationUri("urn:eclipse:milo:examples:client")
                .setCertificate(null)
                .setKeyPair(null)
                .setEndpoint(createEndpointConfiguration(opcUaClientInterface.getInstanceInformation()))
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
        this.uaObjectAndBuilderProvider = uaObjectAndBuilderProvider;
        this.opcUaClientInterface = opcUaClientInterface;
        this.checkConnectionThread = new CheckConnectionThread();
    }
    
    private EndpointDescription createEndpointConfiguration(MsInstanceInformation instanceInformation){    
        UserTokenPolicy[] userTokenPolicies = new UserTokenPolicy[] { 
                org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS};
        
        EndpointConfiguration endpointConfiguration = EndpointConfiguration.newBuilder()
            .setBindAddress(instanceInformation.getHost())
            .setBindPort(instanceInformation.getPort())
            .setPath(instanceInformation.getPath() == null ? "" : instanceInformation.getPath() )
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
    
    /**
     * This method writes a specific {@link DataValue} on a specific {@link NodeId}.
     * 
     * @param nodeId the <code>NodeId</code> which should be written.
     * @param dataValue the <code>DataValue</code> which should be written.
     * @throws UaException throws an exception when the specific server is not available.
     * @throws InterruptedException thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     * @throws ExecutionException Exception thrown when attempting to retrieve the result of a task that aborted by throwing an exception. 
     */
    public void writeValues(NodeId nodeId, DataValue dataValue) throws UaException, InterruptedException, ExecutionException {
        waitForConnection();
        
        CompletableFuture<StatusCode> f = this.opcUaClient.writeValue(nodeId, dataValue);
        
        // ...but block for the results so we write in order
        StatusCode statusCode = f.get();
         
        if (statusCode.isBad()) {
           throw new UaException(statusCode);
        }
    }
    
    /**
     * Subscribes a callback to a value change. The id for which this method should subscribe will 
     * be handed in by an {@link ReadValueId}. 
     * 
     * @param readValueId the <code>ReadValueId</code> for which the callback should subscribe.
     * @param callbackWhenValueChanged the callback which will be registered. 
     * @throws InterruptedException this exception occurs when an interrupt occurred.
     * @throws ExecutionException Exception thrown when attempting to retrieve the result of a task that aborted by throwing an exception. 
     * @return the identification of the subscription.
     */
    public UaMonitoredItem subscribeValues(ReadValueId readValueId, BiConsumer<UaMonitoredItem, DataValue>  callbackWhenValueChanged) throws InterruptedException, ExecutionException {
        waitForConnection();
        
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
        
        List<UaMonitoredItem> subscriptionItems = this.subscription.createMonitoredItems(
                TimestampsToReturn.Both,
                Arrays.asList(request),
                itemCreationCallback
            ).get();
        
        assert subscriptionItems.size() == 1;
        
        return subscriptionItems.get(0);
    }
    
    
    /**
     * Reads an value from a given server by using its <code>NodeId</code>. 
     * 
     * @param nodeId the id of the read value.
     * @return the data value returned by this read action.
     * 
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    public DataValue readValue(NodeId nodeId) throws InterruptedException, ExecutionException {
        waitForConnection();
        return this.opcUaClient.readValue(0, TimestampsToReturn.Server, nodeId).get();
    }

    /**
     * Unsubscription a specific subscription.
     * 
     * @param monitoredItem the <code>UaMonitoredItem</code> which should be unsubscribed.
     * @throws InterruptedException this exception occurs when an interrupt occurred.
     * @throws ExecutionException Exception thrown when attempting to retrieve the result of a task that aborted by throwing an exception. 
     */
    public void unsubscribeValues(UaMonitoredItem monitoredItem) throws InterruptedException, ExecutionException {
        this.subscription.deleteMonitoredItems(Lists.newArrayList(monitoredItem));
    }
    
    /**
     * Calls a specific method given by a {@link CallMethodRequest}.
     * @param callMethodRequest the  of the request which will be called.
     * @return the result of the method call.
     * @throws InterruptedException this exception occurs when an interrupt occurred.
     * @throws ExecutionException Exception thrown when attempting to retrieve the result of a task that aborted by throwing an exception. 
    */
    public Variant[] createCallMethodRequest(CallMethodRequest callMethodRequest) throws InterruptedException, ExecutionException {
        waitForConnection();
        
        CompletableFuture<Variant[]> future = this.opcUaClient.call(callMethodRequest).thenCompose(result -> {
            StatusCode statusCode = result.getStatusCode();
            if (statusCode.isBad()) {
                throw new RuntimeException(statusCode.toString());
             }
            
            CompletableFuture<Variant[]> outputArguments = CompletableFuture.completedFuture(result.getOutputArguments());  
            return outputArguments;
        });
        
        return future.get();
    }
    
    /**
     * This method waits for a connection until it is alive.
     */
    protected void waitForConnection() {
       while(!isConnectionAlive()) {
           try {
               LOGGER.warn("The thread " + Thread.currentThread().getId() + ":" + Thread.currentThread().getName() + " waits for an connection to " + OpcUaClientManager.this.opcUaClientInterface.getInstanceInformation().toString() + ".");
               TimeUnit.SECONDS.sleep(10);
           } catch (InterruptedException e) {
            //check again whenever an interrupt occurs.
           }
       }
    }
    
    /**
     * This method test wheahter the client connection is alive.
     * @return
     */
    protected synchronized boolean isConnectionAlive() {
        try {
           this.opcUaClient.getSession().get();
        } catch (InterruptedException | ExecutionException e) {
           return false;
        }
        return true;
    }
    
    /**
     * Connects this {@link  OpcUaClient} to an OpcUaServer.
     */
    @Override
    protected void onStartup() {
        this.checkConnectionThread.start();
    }

    /**
     * Disconnects this {@link  OpcUaClient} from an OpcUaServer.
     */
    @Override
    protected void onShutdown() {
        try {
            this.checkConnectionThread.interrupt();
            this.opcUaClient.disconnect().get(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }        
    }
    
    /**
     * This method checks weather a connection has been established. If not, it will create a connection.
     */
    private class CheckConnectionThread extends Thread {
        /**
         * This method checks weather a connection has been established. If not, it will create a connection.
         */
        @Override
        public void run() {
            try {
                while(!isInterrupted()) {
                    if(!isConnectionAlive()) {
                        try {
                            OpcUaClientManager.this.opcUaClient.connect().get(60000, TimeUnit.MILLISECONDS);
                            OpcUaClientManager.this.subscription = OpcUaClientManager.this.opcUaClient.getSubscriptionManager()
                                    .createSubscription(1000.0)
                                    .get();
                        } catch (InterruptedException | ExecutionException | TimeoutException e) {
                            LOGGER.warn("A connection could not be established: " + OpcUaClientManager.this.opcUaClientInterface.getInstanceInformation().toString());
                        }
                    }
                    TimeUnit.SECONDS.sleep(10);
                }
            } catch (InterruptedException e) {
                //shut down whenever an interrupt occures
            }
            
        }
    }
}
