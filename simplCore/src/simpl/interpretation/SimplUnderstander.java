package simpl.interpretation;

import java.util.HashMap;
import java.util.List;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;

public class SimplUnderstander {
	
	
	
	public Object understandInterpretation(List<SimplInterpretation> interps, ClassDescriptor cd) throws SIMPLTranslationException
	{
		Object ourObject = cd.getInstance();
		
		UnderstandingContext understandingContext = new UnderstandingContext();
		SimplRefCallbackMap callbackMap = new SimplRefCallbackMap();
		
		for(SimplInterpretation interp : interps)
		{
			interp.resolve(ourObject, callbackMap, understandingContext);
		}
		
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
	
	public void resolveScalarInterpretation(Object object, ScalarInterpretation si)
	{
		
	}
}
