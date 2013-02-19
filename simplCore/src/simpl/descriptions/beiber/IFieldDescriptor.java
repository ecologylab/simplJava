package simpl.descriptions.beiber;

import java.util.Collection;

import simpl.descriptions.EnumerationDescriptor;
import simpl.descriptions.FieldType;

/**
 * An interface representing a described simpl field
 */
public interface IFieldDescriptor extends IMetaInformationProvider {
	/**
	 * Gets the class that this field was declared in
	 * @return
	 */
	Class<?> getDeclaringClass();
	
	/**
	 * Gets the class descriptor of the declaring class
	 * @return
	 */
	IClassDescriptor getDeclaringClassDescriptor();
	
	/**
	 * Gets the field type of this field, such as SCALAR or COMPOSITE
	 * @return
	 */
	FieldType getFieldType();

	/**
	 * Gets the class descriptor for the declared class of this field.
	 * @return
	 */
	IClassDescriptor getFieldClassDescriptor();

	/**
	 * Gets a collection containing descriptors for all polymorphic 
	 * classes which may be used in this field.
	 * @return
	 */
	Collection<IClassDescriptor> getPolymoprhicFieldDescriptors();
	
	/** 
	 * Gets the enum descriptor for the delcared class of this field
	 */
	EnumerationDescriptor getEnumerationDescriptor();
	
	/**
	 * Gets the name of this field
	 * @return
	 */
	String getName();
	
	/**
	 * Gets a collection of all othertags that this field ma be referenced by.
	 * @return
	 */
	Collection<String> getOtherTags();
	
}
