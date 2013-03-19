package simpl.interpretation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import simpl.core.ISimplTypesScope;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;

public class SimplUnderstander {

	
	private ISimplTypesScope scope;
	
	public SimplUnderstander(ISimplTypesScope contextScope)
	{
		this.scope = contextScope;
	}
	
	public Object understandInterpretation(CompositeInterpretation rootObjectInterpretation) throws SIMPLTranslationException
	{		
		UnderstandingContext understandingContext = new UnderstandingContext(this.scope);
	
		Set<String> refSet = new HashSet<String>();
		
		Object ourObject = rootObjectInterpretation.getValue(null /*Null here because this is a root object*/ , refSet, understandingContext);

		if(!refSet.isEmpty())
		{
			throw new RuntimeException("Missed a simpl ref!");
		}
		
		return ourObject;
	}
	
}
