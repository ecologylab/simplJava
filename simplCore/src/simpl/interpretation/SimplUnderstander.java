package simpl.interpretation;

import java.util.HashMap;
import java.util.List;

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
		SimplRefCallbackMap callbackMap = new SimplRefCallbackMap();
		
		for(SimplInterpretation interp : interps)
		{
			interp.resolve(ourObject, callbackMap, understandingContext);
		}
		
		// TODO: add a dependency graph here, this will optimize the perf of unrolling the callbacks. 
		for(String ref : callbackMap.getPendingUpdateKeys())
		{
			if(understandingContext.isIDRegistered(ref))
			{
				callbackMap.resolveCallbacks(ref, understandingContext.getRegisteredObject(ref));
			}
		}
		
		if(!callbackMap.isEmpty())
		{
			throw new RuntimeException("Missed a simpl ref!");
		}
		
		return ourObject;
	}
	
}
