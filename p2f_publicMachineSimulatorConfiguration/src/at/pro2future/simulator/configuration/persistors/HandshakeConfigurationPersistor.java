package at.pro2future.simulator.configuration.persistors;

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import at.pro2future.simulator.configuration.HandshakeConfiguration;

public class HandshakeConfigurationPersistor extends ConfigurationPersistor {

    private final HandshakeConfiguration sim;
    
    public HandshakeConfiguration getConfiguration() {
        return this.sim;
    }
        
    public HandshakeConfigurationPersistor() {
        this.sim = new HandshakeConfiguration();
    }
    
    @Override
    protected String getFileName() {
        return "target/handshake.xmi";
    }

    @Override
    protected List<EObject> getUncontainedObjects() {
        return this.sim.get();
    }
}
