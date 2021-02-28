package at.pro2future.machineSimulator.eventHandlers;

import java.util.concurrent.CompletableFuture;

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;

import Simulator.MsAction;
import Simulator.MsMethodAction;
import at.pro2future.machineSimulator.OpcUaClientManager;
import at.pro2future.machineSimulator.converter.opcUaToMilo.MsNodeIdToNodeIdConverter;
import at.pro2future.shopfloors.adapters.EventInstance;

public class CallMethodHandler extends BaseEventHandler {
	private final MsMethodAction methodAction;
	
	@Override
	protected MsAction getMsAction() {
		return this.methodAction;
	}
	
	public CallMethodHandler(OpcUaClientManager opcUaClientManager, MsMethodAction methodAction) {
		super(opcUaClientManager);
		this.methodAction = methodAction;
	}
	
	@Override
	public void handleEvent(EventInstance e){
		super.handleEvent(e);
		try {
			
			CallMethodRequest request = new CallMethodRequest(
		            new MsNodeIdToNodeIdConverter().createTo(this.methodAction.getObjectContainingMethod(), getOpcUaClientManager().getUaBuilderFactory()),
		            new MsNodeIdToNodeIdConverter().createTo(this.methodAction.getCallesMethod(), getOpcUaClientManager().getUaBuilderFactory()),
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
