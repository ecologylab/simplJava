package simpl.translation.api;

import java.util.List;

import simpl.descriptors.ParameterDescriptor;

/**
 * Translates a parameter into a source representation
 */
public abstract class ParameterTranslator extends BaseTranslator{
	/**
	 * Translates a single parameter into a String
	 * @param d A description of a parameter
	 * @return The parameter as a string
	 */
	public abstract String translateParameter(ParameterDescriptor d);
	
	/**
	 * Translates a set of parameters into a parameter list.
	 * @param d An ordered list of paramters. (Method Formals, MetaInformation values, etc)
	 * @return The representation of the parameters, as a string. For example( a,b,c) or (a=10, b=20)
	 */
	public abstract String translateParameterList(List<ParameterDescriptor> d);
	/**
	 * Obtains the value of a given paramter object; used for serializing default values or metaInformation values.
	 * @param argValue
	 * @return
	 */
	public abstract String translateParameterValue(Object argValue);
}
