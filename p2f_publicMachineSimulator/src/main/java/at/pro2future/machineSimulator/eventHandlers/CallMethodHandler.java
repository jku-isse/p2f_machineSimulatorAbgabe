package at.pro2future.machineSimulator.eventHandlers;

import java.util.concurrent.CompletableFuture;

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;

import OpcUaDefinition.MsMethodNode;
import Simulator.MsAction;
import Simulator.MsCallMethodAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.converter.opcUaToMilo.MsNodeIdToNodeIdConverter;
import at.pro2future.shopfloors.adapters.EventInstance;

public class CallMethodHandler extends BaseEventHandler {
	private final MsCallMethodAction callMethodAction;
	
	@Override
	protected MsAction getMsAction() {
		return this.callMethodAction;
	}
	
	public CallMethodHandler(OpcUaClientManager opcUaClientManager, MsCallMethodAction callMethodAction) {
		super(opcUaClientManager);
		this.callMethodAction = callMethodAction;
	}
	
	@Override
	public void handleEvent(EventInstance e){
		super.handleEvent(e);
		try {
			
			CallMethodRequest request = new CallMethodRequest(
		            new MsNodeIdToNodeIdConverter().createTo(this.callMethodAction.getObjectContainingMethod().getNodeId(), getOpcUaClientManager().getUaBuilderFactory()),
		            new MsNodeIdToNodeIdConverter().createTo(this.callMethodAction.getCallesMethod().getNodeId(), getOpcUaClientManager().getUaBuilderFactory()),
		            new Variant[]{}
		        );
			
			CompletableFuture<Variant[]> returnValues = getOpcUaClientManager().callMethod(request);
			returnValues.get();
		}
		catch(Exception exc) {
			throw new RuntimeException(exc);
		}
	}
}
