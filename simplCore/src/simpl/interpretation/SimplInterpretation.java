package simpl.interpretation;

import java.lang.reflect.Field;
import java.util.Set;

import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;

public interface SimplInterpretation {
	
	/**
	 * Resolves a field in a given object to the value represented by this interpretation
	 * @param context
	 * @param refSet
	 * @param understandingContext
	 * @throws SIMPLTranslationException
	 */
	void resolve(Object context, Set<String> refSet,
			UnderstandingContext understandingContext)
			throws SIMPLTranslationException;
	
	/**
	 * Gets the value of the object; The object may be resolved more later, but in general the object will at least have 
	 * the requisite callbacks set up. This facilitates adding the object to lists / maps / etc. 
	 * @throws SIMPLTranslationException 
	 */
	Object getValue(Object context, Set<String> refSet, UnderstandingContext understandingContext) throws SIMPLTranslationException;
	
	
	/**
	 * A method intended to be run on an instance of a simpl interpretation (created with an empty constructor).
	 * Creates a simpl interpretation for a field, on a given context object, with the interpretation context given)
	 * @param context
	 * @param field
	 * @param interpretationContext
	 * @return
	 * @throws SIMPLTranslationException
	 */
	SimplInterpretation interpret(Object context, FieldDescriptor field, InterpretationContext interpretationContext) throws SIMPLTranslationException;

	SimplInterpretation interpretObject(Object theObject,
			InterpretationContext interpretationContext) throws SIMPLTranslationException;
}
