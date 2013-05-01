package simpl.descriptions;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to provide generic descriptions for underlying Java types 
 */
public class GenericDescriptions {
	
	/**
	 * Obtains a list of type variables for a given class.
	 */
	public static List<GenericTypeVar> getClassTypeVariables(Class<?> c)
	{
		List<GenericTypeVar> result = new ArrayList<GenericTypeVar>();
		
		TypeVariable<?>[] typeVariables = c.getTypeParameters();
		if (typeVariables != null) {
			for (TypeVariable<?> typeVariable : typeVariables)
			{
				GenericTypeVar g = GenericTypeVar.getGenericTypeVarDef(typeVariable, result);
				result.add(g);
			}
		}

		return result;
	}
	
	// TODO: This may need to be moved back to sun specifics.
	// That looks a bit like a mess, and since I don' have an android environment
	// I can't test this. :| 
	
	public static List<GenericTypeVar> getFieldTypeVariables(Field field)
	{
		// Create an array list for the result;
		List<GenericTypeVar> result = new ArrayList<GenericTypeVar>();
		
		Type genericType = field.getGenericType();
		
		// Return an empty list for non-generic fields. 
		if(genericType == null)
		{
			return result;
		}
		
		// If we have a field, we need to obtain the generic context.
		// Fetch the class descriptor to obtain this context
		ClassDescriptor contextCD = ClassDescriptors.getClassDescriptor(field.getDeclaringClass());

		// If it's a TypeVariable, it doesn't have further parameters. 
		if (genericType instanceof TypeVariable)
		{
			TypeVariable tv = (TypeVariable) genericType;
			GenericTypeVar g = GenericTypeVar.getGenericTypeVarRef(tv,contextCD.getGenericTypeVariables());//fieldDescriptor.getGenericTypeVarsContext());
			result.add(g);
		}
		else if (genericType instanceof ParameterizedType)
		{
			// Cast to parametizedType to get access to the getActualTypeArguments() call
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type[] types = parameterizedType.getActualTypeArguments();

			// If we have types, add them to the result
			if(types != null)
			{
				for (Type t : types)
				{
					// Use the contextCD's generic type variables as context.
					GenericTypeVar g = GenericTypeVar.getGenericTypeVarRef(t, contextCD.getGenericTypeVariables());
					result.add(g);
				}
			}
		}
				
		// Return the resulting list of GenericTypeVariables
		return result;
	}
}
