package simpl.translation.api;

import java.util.List;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;

/**
 * An interface that encompasses translation of FieldDescriptors to source code in some language.
 * @author twhite
 */
public abstract class FieldTranslator extends BaseTranslator{
	public abstract SourceAppender translateField(ClassDescriptor context, FieldDescriptor fieldDescriptor);	
	public abstract SourceAppender translateFields(ClassDescriptor context);
}
