package at.pro2future.machineSimulator.converter.opcUaToMilo;

import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;

import OpcUaDefinition.MsQualifiedName;
import OpcUaDefinition.OpcUaDefinitionFactory;
import at.pro2future.machineSimulator.converter.Converter;
import at.pro2future.machineSimulator.converter.UaBuilderFactory;

public class MsQualifiedNameToQualifiedName implements Converter<MsQualifiedName, QualifiedName, OpcUaDefinitionFactory, UaBuilderFactory> {

	@Override
	public MsQualifiedName createFrom(QualifiedName object, OpcUaDefinitionFactory factory) throws ConvertionNotSupportedException {
		throw new ConvertionNotSupportedException();
	}

	@Override
	public QualifiedName createTo(MsQualifiedName object, UaBuilderFactory factory) throws ConvertionNotSupportedException {
		return new QualifiedName(factory.getNamespaceIndex(), object.getName());
	}

}
