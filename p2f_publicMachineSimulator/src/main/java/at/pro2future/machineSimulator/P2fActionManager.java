package at.pro2future.machineSimulator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.stack.core.UaException;

import Simulator.MsAction;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;
import at.pro2future.shopfloors.adapters.AdapterEventProvider;

public class P2fActionManager extends AbstractLifecycle {
	
	private final List<P2fActionAdapter> msActionAdapters;
	
	private final AdapterEventProvider adapterEventProvider;
	private final UaBuilderFactory uaBuilderFactory;
		
	public AdapterEventProvider getAdapterEventProvider() {
		return adapterEventProvider;
	}

	public P2fActionManager(List<MsAction> msActions, UaBuilderFactory uaBuilderFactory) throws UaException {
		this.adapterEventProvider = new AdapterEventProvider();
		this.uaBuilderFactory = uaBuilderFactory;
		msActionAdapters = new ArrayList<P2fActionAdapter>();
		setMsAction(msActions);
	}
	
	private void setMsAction(List<MsAction> msActions) throws UaException {
		msActionAdapters.clear();
		for(MsAction msAction : msActions) {
			msActionAdapters.add(new P2fActionAdapter(msAction, this.uaBuilderFactory));
		}
	}

	@Override
	public void onStartup() {
		for(P2fActionAdapter actionAdapter : msActionAdapters) {
			actionAdapter.getOpcUaClientManager().startup();
			adapterEventProvider.registerEngineAdapter(actionAdapter);
		}		
		
	}

	@Override
	public void onShutdown() {
		for(P2fActionAdapter actionAdapter : msActionAdapters) {
			actionAdapter.getOpcUaClientManager().shutdown();
			adapterEventProvider.deregisterEngineAdapter(actionAdapter);
		}		
	}
}
