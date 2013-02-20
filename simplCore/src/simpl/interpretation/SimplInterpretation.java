package simpl.interpretation;

import simpl.exceptions.SIMPLTranslationException;

public interface SimplInterpretation {
	void resolve(Object context, SimplRefCallbackMap callbackMap,
			UnderstandingContext understandingContext)
			throws SIMPLTranslationException;
}
