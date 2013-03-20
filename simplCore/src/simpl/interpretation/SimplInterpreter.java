package simpl.interpretation;

import java.util.LinkedList;
import java.util.List;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.types.ScalarType;

public class SimplInterpreter {	
	
	private InterpretationContext thisInterpContext; 
	
	public SimplInterpreter()
	{
		this.thisInterpContext= new InterpretationContext(this);
	}
	
	
	static CompositeInterpretation compositeInterpReference = new CompositeInterpretation("");
	
	public SimplInterpretation interpretInstance(Object obj) throws SIMPLTranslationException
	{
		return compositeInterpReference.interpretObject(obj, thisInterpContext);
	}
	
	public SimplInterpretation interpretField(Object obj, FieldDescriptor fd) throws SIMPLTranslationException 
	{
		// TODO: ELIMINATE THIS HIDEOUS HACK. :) 
		switch(fd.getType())
		{
			case SCALAR:
				return new ScalarInterpretation().interpret(obj, fd, this.thisInterpContext);
			case COMPOSITE_ELEMENT:
				return new CompositeInterpretation("").interpret(obj, fd, this.thisInterpContext);
			default:
				break;
				
		}
				
		return null;
	}
}
