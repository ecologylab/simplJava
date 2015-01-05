package simpl.translation.api;

import java.util.List;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;

/**
 * An interface that encompasses translation of FieldDescriptors to source code in some language.
 */
public abstract class FieldTranslator extends BaseTranslator{
	/**
	 * Translates a single field into source code
	 * @param context The ClassDescriptor that the Field is contained within
	 * @param fieldDescriptor The FieldDescriptor to translate into source representation
	 * @return A SourceAppender containing the representation of this Field
	 */
	public abstract SourceAppender translateField(ClassDescriptor context, FieldDescriptor fieldDescriptor);	
	
	/**
	 * Translates all fields for a ClassDescriptor into their source representations.
	 * @param context The ClassDescriptor that the Field is contained within
	 * @return A SourceAppender containing the source represntation of all fields within the Class
	 */
	public abstract SourceAppender translateFields(ClassDescriptor context);
}
