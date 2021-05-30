package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

import OpcUaDefinition.MsLocalizedText;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.ConversionFailureException;
import at.pro2future.machineSimulator.converter.IConverter;
import at.pro2future.machineSimulator.converter.IUaObjectAndBuilderProvider;
import at.pro2future.machineSimulator.converter.ConvertionNotSupportedException;

/**
 * Converts a {@link #MsLocalizedText} to a {@link #LocalizedText} and vice versa. The class uses the given 
 * factories, the {@link #OpcUaDefinitionFactory} and the {@link #IUaObjectAndBuilderProvider} to perform the transformation.
 *
 */
public class MsLocalizedTextToLocalizedTextConverter implements IConverter<MsLocalizedText, LocalizedText, OpcUaDefinitionFactory, IUaObjectAndBuilderProvider>  {

    /**
     * The singleton instance <code>MsLocalizedTextToLocalizedTextConverter<code> of the converter.
     */
    private static MsLocalizedTextToLocalizedTextConverter instance;
    
    /**
     * Returns the only instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    static MsLocalizedTextToLocalizedTextConverter getInstance() {
        if(instance == null) {
            instance = new MsLocalizedTextToLocalizedTextConverter();
        }
        return instance;
    }
    
    /**
     * The singleton instance <code>MsLocalizedTextToLocalizedTextConverter<code> of the converter.
     */
    private MsLocalizedTextToLocalizedTextConverter() {
    }
    
    /**
     * Creates a {@link MsLocalizedText} from the given {@link LocalizedText} by using an {@link OpcUaDefinitionFactory}.
     * 
     * @param object the <code>MsLocalizedText</code> which should be converted.
     * @param factory the factory which is able to create the <code>MsLocalizedText</code>.
     * @return the corresponding <code>MsLocalizedText</code> object from the given <code>LocalizedText</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public MsLocalizedText createSource(LocalizedText object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException, ConversionFailureException {
        throw new ConvertionNotSupportedException();
    }

    /**
     * Creates a {@link LocalizedText} from the given {@link MsLocalizedText} by using an {@link IUaObjectAndBuilderProvider}.
     * 
     * @param object the <code>MsLocalizedText</code> which should be converted.
     * @param factory the factory which is able to create the <code>LocalizedText</code>.
     * @return the corresponding <code>LocalizedText</code> object from the given <code>MsLocalizedText</code> object.
     * @throws ConvertionNotSupported if the conversion is not supported.
     * @throws ConversionFailureException if an known incompatibility occurs.
     */
    @Override
    public LocalizedText createTarget(MsLocalizedText object, IUaObjectAndBuilderProvider factory) throws ConvertionNotSupportedException, ConversionFailureException {
        return new LocalizedText(object.getLocale(), object.getText());
    }
}
