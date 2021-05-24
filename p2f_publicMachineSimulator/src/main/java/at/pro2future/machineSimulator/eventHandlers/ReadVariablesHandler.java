package at.pro2future.machineSimulator.eventHandlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

import ProcessCore.Event;
import ProcessCore.Parameter;
import Simulator.MsReadEventAdressSpaceAction;
import Simulator.ProcessOpcUaMapping;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.command.ReadCommand;
import at.pro2future.machineSimulator.command.ReadCommandParameters;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.converter.opcUaToMilo.MsNodeIdToNodeIdConverter;
import at.pro2future.shopfloors.adapters.AdapterEventProvider;

/**
 *  This <code>EventHandler</code> fires and {@link EventInstance} whenever a variable was written to an OPC-UA Server.
 *  Therefore it is of type {@link ReadVariablesHandler}.
 *  
 */
public class ReadVariablesHandler extends BaseEventHandler<MsReadEventAdressSpaceAction> implements ISendEventHandler {
    
    private final Map<ReadValueId, ProcessOpcUaMapping> readValueIdProcessOpcUaMappings = new HashMap<>();
    private final List<UaMonitoredItem> uaMonitoredItems = new ArrayList<>();
    private AdapterEventProvider adapterEventProvider;
    
    /**
     * The {@link AdapterEventProvider} has to be provided to be able the send an event whenever
     * a variable has changed.
     */
    @Override
    public AdapterEventProvider getAdapterEventProvider() {
        return this.adapterEventProvider;
    }

    /**
     * The {@link AdapterEventProvider} has to be provided to be able the send an event whenever
     * a variable has changed.
     */
    @Override
    public void setAdapterEventProvider(AdapterEventProvider adapterEventProvider) {
        this.adapterEventProvider = adapterEventProvider;
    }

    /**
     * This <code>ReadVariableHandler</code> listens to changes on a specific OpcUa-Variable and will fire an {@link EventInstance} on changes.
     * 
     * @param opcUaClientManager the client manager which will register this {@link ReadVariablesHandler} .
     * @param msReadEventAdressSpaceAction the configuration for this ReadVariableHandler.
     * @param uaObjectAndBuilderProvider a factory which is able to create 
     * @throws HandlerCanNotBeCreatedException
     */
    public ReadVariablesHandler(OpcUaClientManager opcUaClientManager, MsReadEventAdressSpaceAction msReadEventAdressSpaceAction, IUaObjectAndBuilderProvider uaObjectAndBuilderProvider) throws HandlerCanNotBeCreatedException {
        super(opcUaClientManager, 
                msReadEventAdressSpaceAction,
                new ReadCommand(opcUaClientManager, 
                        new ReadCommandParameters(msReadEventAdressSpaceAction.getParameterMappings())));
        registerMsReadEventAdressSpaceAction(msReadEventAdressSpaceAction.getParameterMappings());
    }
    
    private void registerMsReadEventAdressSpaceAction(List<ProcessOpcUaMapping> processOpcUaMappings) throws HandlerCanNotBeCreatedException {
        for(ProcessOpcUaMapping processOpcUaMapping : processOpcUaMappings) {
            hanldeMapping(processOpcUaMapping);
        }
    }

    private void hanldeMapping(ProcessOpcUaMapping processOpcUaMapping) throws HandlerCanNotBeCreatedException  {
        try {
            NodeId nodeId = MsNodeIdToNodeIdConverter.getInstance().createTarget(processOpcUaMapping.getAttributeNodeId(), getOpcUaClientManager().getUaObjectAndBuilderProvider());
            ReadValueId readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);
            this.readValueIdProcessOpcUaMappings.put(readValueId, processOpcUaMapping);
        } catch (ConvertionNotSupportedException | ConversionFailureException ex) {
           throw new HandlerCanNotBeCreatedException(ex);
        }
    }
    
    
    /**
     * This method registers the <code>UaMonitoredItem</code> at the OPC-UA Client. 
     */
    @Override
    protected void onStartup() {
        try {
            for(Entry<ReadValueId, ProcessOpcUaMapping> readValueIdProcessOpcUaMapping : this.readValueIdProcessOpcUaMappings.entrySet()) {
                UaMonitoredItem uaMonitoredItem = getOpcUaClientManager().subscribeValues(readValueIdProcessOpcUaMapping.getKey(), this::callbackWhenValueChanged);
                this.uaMonitoredItems.add(uaMonitoredItem);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method deregisters the <code>UaMonitoredItem</code> from the OPC-UA Client.
     */
    @Override
    protected void onShutdown() {
        for(UaMonitoredItem uaMonitoredItem : this.uaMonitoredItems) {
            try {
                getOpcUaClientManager().unsubscribeValues(uaMonitoredItem);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        this.uaMonitoredItems.clear();
    }
    
    private void callbackWhenValueChanged(UaMonitoredItem uaMonitoredItem, DataValue dataValue) {
        ReadValueId readValueId = uaMonitoredItem.getReadValueId();
        ProcessOpcUaMapping processOpcUaMapping = this.readValueIdProcessOpcUaMappings.get(readValueId);
        Parameter parameterClone = EcoreUtil.copy(processOpcUaMapping.getParameter());
        parameterClone.setValue(dataValue.getValue().getValue());
        
        Event event = this.getMsEventAdressSpaceAction().getRefersTo();
        sendEvent(event, Arrays.asList(parameterClone));
    }
}

