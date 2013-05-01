package simpl.descriptions;

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
}
