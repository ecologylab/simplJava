package simpl.descriptions;

import java.util.Collection;

/**
 * MetaInformation refers to all of the annotations that may exist on a given object; 
 * for languages that do not have annotations, metainformation simply contains the relevant metainformation correlated to a description. 
 * Format specific serialization information, etc, is all conveyed as metainformation, retrieved via a MetaInformationProvider. (A fancy dictionary!) 
 * @author tom
 *
 */
public interface MetaInformation {
	/**
	 * Returns the name of the annotation
	 * @return
	 */
	String getAnnotationName();
	/**
	 * Gets a collection of parameter descriptiors for this annotation
	 * @return
	 */
	Collection<ParameterDescriptor> getParameters();
	/**
	 * Returns true if the parameter exists in the metainformation
	 * @param parameterName
	 * @return
	 */
	Boolean hasParameter(String parameterName);
	/**
	 * Gets the value of the parameter with name given
	 * @param parameterName
	 * @return
	 */
	Object getValueFor(String parameterName);
	/**
	 * Gets the value of the "Value()" for this metainformation.
	 * @return
	 */
	Object getValue();
	
	/**
	 * Adds a parameter descriptor to the metainformation.
	 * Automaticaly indexes by the name
	 */
	void addParameter(ParameterDescriptor param);
}
