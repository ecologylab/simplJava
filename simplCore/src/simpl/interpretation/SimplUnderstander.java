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
	
	public Object understandInterpretation(List<SimplInterpretation> interps, String tagName) throws SIMPLTranslationException
	{
		ClassDescriptor ourDescriptor = this.scope.getClassDescriptorByTag(tagName);
		
		if(ourDescriptor == null)
		{
			throw new SIMPLTranslationException("Tag name {"+tagName+"} is not in the context type scope! Did you initialize your Simpl Types Scope correctly?");
		}
		
		Object ourObject = ourDescriptor.getInstance();
		
		UnderstandingContext understandingContext = new UnderstandingContext(this.scope);
	
		Set<String> refSet = new HashSet<String>();
		
		for(SimplInterpretation interp : interps)
		{
			interp.resolve(ourObject, refSet, understandingContext);
		}
		
		
		if(!refSet.isEmpty())
		{
			throw new RuntimeException("Missed a simpl ref!");
		}
		
		return ourObject;
	}
	
}
