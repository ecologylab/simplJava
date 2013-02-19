package simpl.descriptions.beiber;

import java.util.Collection;
import java.util.List;

/**
 * Interface for all classdescriptors
 */
public interface IClassDescriptor extends IMetaInformationProvider {
	/**
	 * Gets the java class that represents the class descriptor
	 * @return
	 */
	Class<?> getJavaClass();
	
	/**
	 * Gets the name of this class
	 * @return
	 */
	String getName();
	
	/**
	 * Gets a list of the described Fields within this given class
	 * @return
	 */
	List<IFieldDescriptor> getFields();
	
	/**
	 * Gets the class decsriptor for this class's superclass, 
	 * if and only if this class was annotated with @simpl_inherit
	 * Returns null otherwise.
	 * @return
	 */
	IClassDescriptor getSuperClassDescriptor();
	
	/**
	 * Returns true if THIS is a superclass of the icd
	 */
	boolean isSuperClass(IClassDescriptor icd);
	
	
	/**
	 * Gets a collection containing other tag names that can refer to this descriptor
	 * @return
	 */
	Collection<String> getOtherTags();
	
}
