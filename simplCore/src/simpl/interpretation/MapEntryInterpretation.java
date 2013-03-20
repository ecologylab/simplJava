package simpl.interpretation;

import java.util.Set;

import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;

public class MapEntryInterpretation implements SimplInterpretation{
	
	SimplInterpretation keyInterpretation;
	SimplInterpretation valueInterpretation;
	
	public void setKeyInterpetation(SimplInterpretation si)
	{
		this.keyInterpretation = si;
	}
	
	public void setValueInterpretation(SimplInterpretation si)
	{
		this.valueInterpretation = si;
	}
	
	public MapEntryInterpretation(SimplInterpretation key, SimplInterpretation value)
	{
		this.keyInterpretation = key;
		this.valueInterpretation = value;
	}

	@Override
	public void resolve(Object context, Set<String> refSet,
			UnderstandingContext understandingContext)
			throws SIMPLTranslationException {
		throw new SIMPLTranslationException("Resolving a mapEntryInterpretation doesn't make sense! Stop!");
	}

	@Override
	public Object getValue(Object context, Set<String> refSet,
			UnderstandingContext understandingContext)
			throws SIMPLTranslationException {
		
		if(this.keyInterpretation instanceof MapEntryInterpretation || this.valueInterpretation instanceof MapEntryInterpretation)
		{
			throw new SIMPLTranslationException("MapEntryInterpretations cannot be recursively nested!");
		}

		return new MapEntryEvaluation(this.keyInterpretation.getValue(context, refSet, understandingContext), this.valueInterpretation.getValue(context, refSet, understandingContext));
	
	}

	@Override
	public SimplInterpretation interpret(Object context, FieldDescriptor field,
			InterpretationContext interpretationContext)
			throws SIMPLTranslationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimplInterpretation interpretObject(Object theObject,
			InterpretationContext interpretationContext) throws SIMPLTranslationException {
		// TODO Auto-generated method stub
		return null;
	}
}
