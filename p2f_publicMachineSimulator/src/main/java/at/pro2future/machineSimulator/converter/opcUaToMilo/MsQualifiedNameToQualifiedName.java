package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;

import OpcUaDefinition.MsQualifiedName;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.IConverter;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;

/**
 * Converts a {@link #MsQualifiedName} to a {@link #QualifiedName} and vice versa. The class uses the given 
 * factories, the {@link #OpcUaDefinitionFactory} and the {@link #IUaObjectAndBuilderProvider} to perform the transformation.
 *
 */
public class MsQualifiedNameToQualifiedName implements IConverter<MsQualifiedName, QualifiedName, OpcUaDefinitionFactory, IUaObjectAndBuilderProvider> {

    /**
     * The singleton instance <code>MsQualifiedNameToQualifiedName<code> of the converter.
     */
    private static MsQualifiedNameToQualifiedName instance;
    
    /**
     * Returns the only instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    public static MsQualifiedNameToQualifiedName getInstance() {
        if(instance == null) {
            instance = new MsQualifiedNameToQualifiedName();
        }
        return instance;
    }
    
    /**
     * The singleton instance <code>MsQualifiedNameToQualifiedName<code> of the converter.
     */
    private MsQualifiedNameToQualifiedName() {
    }
    
    /**
     * Creates a {@link QualifiedName} from the given {@link MsQualifiedName} by using an {@link OpcUaDefinitionFactory}.
     * 
     * @param object the <code>QualifiedName</code> which should be converted.
     * @param factory the factory which is able to create the <code>MsQualifiedName</code>.
     * @return the corresponding <code>QualifiedName</code> object from the given <code>MsQualifiedName</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public MsQualifiedName createSource(QualifiedName object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException, ConversionFailureException {
        throw new ConvertionNotSupportedException();
    }

    /**
     * Creates a {@link MsQualifiedName} from the given {@link QualifiedName} by using an {@link IUaObjectAndBuilderProvider}.
     * 
     * @param object the <code>MsQualifiedName</code> which should be converted.
     * @param factory the factory which is able to create the <code>QualifiedName</code>.
     * @return the corresponding <code>QualifiedName</code> object from the given <code>MsQualifiedName</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public QualifiedName createTarget(MsQualifiedName object, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException {
        return new QualifiedName(factory.getNamespaceIndex(), object.getName());
    }

}
