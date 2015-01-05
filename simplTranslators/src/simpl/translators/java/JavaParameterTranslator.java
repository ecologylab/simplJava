package simpl.translators.java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import simpl.descriptors.ParameterDescriptor;
import simpl.translation.api.ParameterTranslator;
import ecologylab.serialization.annotations.Hint;

public class JavaParameterTranslator extends ParameterTranslator {

	public String translateParameter(ParameterDescriptor d) 
	{
		StringBuilder appendable = new StringBuilder();
		
		if(d.isAttributeParameter())
		{
			// Get our value
			String valueString = translateParameterValue(d.getParameterValue());

			if(d.getParameterName() != null)
			{
				appendable.append(d.getParameterName());
				appendable.append("=");
			}
			
			appendable.append(valueString);
		}
		else if(d.isMethodParameter())
		{
			throw new RuntimeException("Invocation / method translation is not implemented yet");
		}else{
			throw new RuntimeException("Invalid ParameterDescriptor at translation time");
		}
	
		return appendable.toString();
	}
	
	public String translateParameterValue(Object argValue)
	{
		if (argValue instanceof String)
		{
			return "\"" + argValue.toString() + "\"";
		}
		else if (argValue instanceof Hint)
		{
			addDependency(Hint.class.getName());
			switch ((Hint) argValue)
			{
				case XML_ATTRIBUTE: return "Hint.XML_ATTRIBUTE"; 
				case XML_LEAF: return "Hint.XML_LEAF"; 
				case XML_LEAF_CDATA: return "Hint.XML_LEAF_CDATA"; 
				case XML_TEXT: return "Hint.XML_TEXT"; 
				case XML_TEXT_CDATA: return "Hint.XML_TEXT_CDATA"; 
				default: return "Hint.UNDEFINED";
			}
		}
		else if (argValue instanceof Class)
		{
			addDependency(((Class<?>) argValue).getName());
			return ((Class<?>) argValue).getSimpleName() + ".class";
		}else if (argValue instanceof Number)
		{
			return argValue.toString();
		}
		else{
			throw new RuntimeException("Type of parameter value is currently not supported: " + argValue.getClass().getName() + " To string: " + argValue.getClass().toString());
		}
	}

	public String translateParameterList(List<ParameterDescriptor> parameterList) {
	
		StringBuilder sb = new StringBuilder();
		 sb.append("(");

		 int size = parameterList.size();
		 for(int i = 0; i < size; i++)
		 {
			 ParameterDescriptor pd = parameterList.get(i);
			 
			 sb.append(translateParameter(pd));
			 
			 if(i + 1 < size)
			 {
				 sb.append(", ");
			 }
		 }
		 
		 sb.append(")");
		 
		 return sb.toString();
	}

	public Set<String> aggregateDependencies() {
		return new HashSet<String>(); // no higher level dependencies. 
	}

}
