package at.pro2future.machineSimulator;

import java.util.ArrayList;
import java.util.List;

import ProcessCore.AbstractCapability;
import ProcessCore.Parameter;
import at.pro2future.shopfloors.adapters.AdapterEventProvider;
import at.pro2future.shopfloors.adapters.EngineAdapter;

public class ExampleAdapterProvider extends AdapterEventProvider{

    private final List<EngineAdapter> engineAdapters = new ArrayList<>();
    
    
    @Override
    public void registerEngineAdapter(EngineAdapter engineAdapter) {
        super.registerEngineAdapter(engineAdapter);
        this.engineAdapters.add(engineAdapter);
    }
    
    @Override
    public void deregisterEngineAdapter(EngineAdapter engineAdapter) {
        super.deregisterEngineAdapter(engineAdapter);
        this.engineAdapters.remove(engineAdapter);
    }
    
    public List<Parameter> invokeCapability(AbstractCapability capability, List<Parameter> parameterValues) {
        List<Parameter> parameters = new ArrayList<>();
        for(EngineAdapter egineAdapter : engineAdapters) {
            List<Parameter> parametersOutputs = egineAdapter.invokeCapability(capability, parameterValues);
            if(parametersOutputs != null) {
                parameters.addAll(parametersOutputs);
            }
        }
        return parameters;
    }
}
