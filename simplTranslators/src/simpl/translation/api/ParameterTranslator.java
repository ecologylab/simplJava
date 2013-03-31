package simpl.translation.api;

import java.util.List;

import simpl.descriptors.ParameterDescriptor;

public abstract class ParameterTranslator extends BaseTranslator{
	public abstract String translateParameter(ParameterDescriptor d);
	public abstract String translateParameterList(List<ParameterDescriptor> d);
	public abstract String translateParameterValue(Object argValue);
}
