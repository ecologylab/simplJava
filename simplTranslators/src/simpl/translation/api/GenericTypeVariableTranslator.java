package simpl.translation.api;

import java.util.List;

import ecologylab.serialization.GenericTypeVar;

public abstract class GenericTypeVariableTranslator extends BaseTranslator {
	public abstract SourceAppender translateBoundedGenericType(List<GenericTypeVar> genericTypeVariables);
	public abstract SourceAppender translateUnboundedGenericType(List<GenericTypeVar> genericTypeVariables);
}
