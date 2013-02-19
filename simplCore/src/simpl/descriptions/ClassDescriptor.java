package simpl.descriptions;

import java.util.Collection;
import java.util.List;

/**
 * Interface for all classdescriptors
 */
public interface ClassDescriptor extends IMetaInformationProvider {
	/**
	 * Gets the java class that represents the class descriptor
	 * @return
	 */
	Class<?> getJavaClass();
	
	/**
	 * Gets the fully qualified java/simpl name of this class
	 * @return
	 */
	String getName();
	
	/**
	 * Gets the namespace / package of this class
	 * @return
	 */
	String getNamespace();
	
	/**
	 * Gets the simple name of this class. 
	 * @return
	 */
	String getSimpleName();
	
	
	/**
	 * Gets a list of the described Fields within this given class
	 * @return
	 */
	List<FieldDescriptor> getFields();
	
	/**
	 * Gets the class decsriptor for this class's superclass, 
	 * if and only if this class was annotated with @simpl_inherit
	 * Returns null otherwise.
	 * @return
	 */
	ClassDescriptor getSuperClassDescriptor();
	
	/**
	 * Returns true if THIS is a superclass of the icd
	 */
	boolean isSuperClass(ClassDescriptor icd);
	
	
	/**
	 * Gets a collection containing other tag names that can refer to this descriptor
	 * @return
	 */
	Collection<String> getOtherTags();
	
}
