package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import OpcUaDefinition.MsLocalizedText;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;

public class MsLocalizedTextToLocalizedTextConverter implements Converter<MsLocalizedText, LocalizedText, OpcUaDefinitionFactory, UaBuilderFactory>  {

	@Override
	public MsLocalizedText createFrom(LocalizedText object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException {
		throw new ConvertionNotSupportedException();
	}

	@Override
	public LocalizedText createTo(MsLocalizedText object, UaBuilderFactory factory) throws ConvertionNotSupportedException {
		return new LocalizedText(object.getLocale(), object.getText());
	}



}
