package simpl.translation.api;

import ecologylab.serialization.ClassDescriptor;


public abstract class ClassTranslator extends BaseTranslator {
	public abstract SourceAppender translateClass(ClassDescriptor cd);
}
