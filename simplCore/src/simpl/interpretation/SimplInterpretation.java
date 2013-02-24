package simpl.interpretation;

import java.util.Set;

import simpl.exceptions.SIMPLTranslationException;

public interface SimplInterpretation {
	void resolve(Object context, Set<String> refSet,
			UnderstandingContext understandingContext)
			throws SIMPLTranslationException;
	
	/**
	 * Gets the value of the object; The object may be resolved more later, but in general the object will at least have 
	 * the requisite callbacks set up. This facilitates adding the object to lists / maps / etc. 
	 * @throws SIMPLTranslationException 
	 */
	Object getValue(Object context, Set<String> refSet, UnderstandingContext understandingContext) throws SIMPLTranslationException;
	
}
