package simpl.types;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.ReflectionTools;

public class ListType {

	public ListType()
	{
		
	}
	
	public void addTo(Object context, Field f, Object value) throws SIMPLTranslationException
	{
		List l = (List)ReflectionTools.getFieldValue(f, context);
		if(l == null)
		{
			l = new ArrayList<Object>();// Reasonable default if not initialized. 
			ReflectionTools.setFieldValue(l, f, context);
		}
		
		l.add(value);
	}
	
}
