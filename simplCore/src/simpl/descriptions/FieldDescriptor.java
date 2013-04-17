package simpl.descriptions;

import java.lang.reflect.Field;
import java.util.Collection;

import simpl.core.ScalarUnmarshallingContext;
import simpl.exceptions.SIMPLTranslationException;
import simpl.types.ListType;
import simpl.types.MapType;
import simpl.types.ScalarType;


/**
 * An interface representing a described simpl field
 */
public interface FieldDescriptor extends IMetaInformationProvider {
	/**
	 * Gets the class that this field was declared in
	 * @return
	 */
	Class<?> getDeclaringClass();
	
	/**
	 * Gets the class descriptor of the declaring class
	 * @return
	 */
	ClassDescriptor getDeclaringClassDescriptor();
	
	/**
	 * Gets the field type of this field, such as SCALAR or COMPOSITE
	 * @return
	 */
	FieldType getType();

	/**
	 * Gets the class descriptor for the declared class of this field.
	 * @return
	 */
	ClassDescriptor getFieldClassDescriptor();

	/**
	 * Gets a collection containing descriptors for all polymorphic 
	 * classes which may be used in this field.
	 * @return
	 */
	Collection<ClassDescriptor> getPolymoprhicFieldDescriptors();
	
	/** 
	 * Gets the enum descriptor for the delcared class of this field
	 */
	EnumerationDescriptor getEnumerationDescriptor();
	
	
	ScalarType getScalarType();
	
	ListType getListType();
	
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

	
	boolean isEnum();
	
	boolean isPolymorphic();
	
	boolean isScalar();
	
	boolean isCollection();

	String getTagName();

	Field getField();

	Object getWrappedFD();

	boolean isCollectionTag(String tagName);
	
	void setValue(Object context, Object value);

	MapType getMapType();

	void setFieldToScalarDefault(Object context,
			ScalarUnmarshallingContext scalarContext) throws SIMPLTranslationException;

	ClassDescriptor getChildClassDescriptor(String tagName);

	boolean isWrapped();
	
	
}
