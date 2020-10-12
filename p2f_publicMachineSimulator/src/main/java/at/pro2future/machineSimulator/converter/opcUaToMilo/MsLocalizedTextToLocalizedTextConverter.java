package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import OpcUaDefinition.MsLocalizedText;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MsLocalizedTextToLocalizedTextConverter implements Converter<MsLocalizedText, LocalizedText, OpcUaDefinitionFactory, UaBuilderFactory>  {

	@Override
	public MsLocalizedText createFrom(LocalizedText object, OpcUaDefinitionFactory factory) throws Exception {
		throw new NotImplementedException();
	}

	@Override
	public LocalizedText createTo(MsLocalizedText object, UaBuilderFactory factory) throws Exception {
		return new LocalizedText(object.getLocale(), object.getText());
	}



}
