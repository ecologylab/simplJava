/**
 * 
 */
package ecologylab.serialization;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.WildcardTypeImpl;
import ecologylab.generic.Debug;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * @author quyin
 * 
 */
public class GenericTypeVar extends Debug
{
	@simpl_scalar
	String										name;

	@simpl_composite
	ClassDescriptor						classDescriptor							= null;

	@simpl_collection
	ArrayList<GenericTypeVar>	genericTypeVars							= null;

	@simpl_composite
	ClassDescriptor						constraintClassDescriptor		= null;

	@simpl_collection
	ArrayList<GenericTypeVar>	constraintedGenericTypeVars	= null;
	
	GenericTypeVar						referredGenericTypeVar;

	public GenericTypeVar()
	{
			//for simpl de/serialzation
	}
	
	public String getName()
	{
		return name;
	}
	
	public ClassDescriptor getClassDescriptor()
	{
		return classDescriptor;
	}
	
	public ClassDescriptor getConstraintClassDescriptor()
	{
		return constraintClassDescriptor;
	}

	public void addGenericTypeVar(GenericTypeVar g)
	{
		if (genericTypeVars == null)
			genericTypeVars = new ArrayList<GenericTypeVar>();

		genericTypeVars.add(g);
	}

	public void addContraintGenericTypeVar(GenericTypeVar g)
	{
		if (constraintedGenericTypeVars == null)
			constraintedGenericTypeVars = new ArrayList<GenericTypeVar>();

		constraintedGenericTypeVars.add(g);
	}

	public static ArrayList<GenericTypeVar> getGenericTypeVars(Type parameterizedType)
	{
		return getGenericTypeVars((ParameterizedTypeImpl) parameterizedType);
	}

	public static ArrayList<GenericTypeVar> getGenericTypeVars(ParameterizedTypeImpl parameterizedType)
	{
		Type[] types = parameterizedType.getActualTypeArguments();

		if (types == null | types.length <= 0)
			return null;

		ArrayList<GenericTypeVar> returnValue = new ArrayList<GenericTypeVar>();
		for (Type t : types)
		{
			GenericTypeVar g = getGenericTypeVar(t);
			returnValue.add(g);
		}

		return returnValue;
	}

	public static GenericTypeVar getGenericTypeVar(TypeVariable<?> typeVariable)
	{
		GenericTypeVar g = new GenericTypeVar();
		g.name = typeVariable.getName();

		// resolve constraints
		resolveGenericConstraints(g, typeVariable.getBounds());

		return g;
	}

	public static void resolveGenericConstraints(GenericTypeVar g, Type[] bounds)
	{
		if (bounds == null)
			return;

		Type bound = bounds[0];

		if (bound instanceof Class<?>)
		{
			Class<?> boundClass = (Class<?>) bound;

			if (Object.class != boundClass)
				g.constraintClassDescriptor = ClassDescriptor.getClassDescriptor(boundClass);
		}

		if (bound instanceof ParameterizedTypeImpl)
		{
			ParameterizedTypeImpl parmeterizedType = (ParameterizedTypeImpl) bound;
			g.constraintClassDescriptor = ClassDescriptor.getClassDescriptor(parmeterizedType
					.getRawType());

			Type[] types = parmeterizedType.getActualTypeArguments();

			for (Type type : types)
			{
				g.addContraintGenericTypeVar(getGenericTypeVar(type));
			}
		}

		if (bound instanceof TypeVariable<?>)
		{
			TypeVariable<?> boundTypeVar = (TypeVariable<?>) bound;
			g.addContraintGenericTypeVar(getGenericTypeVar(boundTypeVar));
		}
	}

	public static GenericTypeVar getGenericTypeVar(Type type)
	{
		GenericTypeVar g = new GenericTypeVar();

		if (type instanceof WildcardTypeImpl)
		{
			g.name = "?";
			WildcardTypeImpl wildCardType = (WildcardTypeImpl) type;
			resolveGenericConstraints(g, wildCardType.getUpperBounds());
		}

		if (type instanceof Class<?>)
		{
			Class<?> typeClass = (Class<?>) type;
			g.classDescriptor = ClassDescriptor.getClassDescriptor(typeClass);
			return g;
		}

		if (type instanceof TypeVariable<?>)
		{
			TypeVariable<?> typeVar = (TypeVariable<?>) type;
			return getGenericTypeVar(typeVar);
		}

		if (type instanceof ParameterizedTypeImpl)
		{
			ParameterizedTypeImpl parmeterizedType = (ParameterizedTypeImpl) type;
			g.classDescriptor = ClassDescriptor.getClassDescriptor(parmeterizedType.getRawType());

			Type[] types = parmeterizedType.getActualTypeArguments();

			for (Type t : types)
			{
				g.addGenericTypeVar(getGenericTypeVar(t));
			}
		}

		return g;
	}

	@Override
	public String toString()
	{
		String outputString = new String();

		if (name != null && name != "")
			outputString += name;
		else if (classDescriptor != null)
			outputString += classDescriptor.getDescribedClassSimpleName();
		
		if (genericTypeVars != null)
		{
			for (GenericTypeVar g : genericTypeVars)
			{
				outputString += "<";
				outputString += g.toString();
				outputString += ">";
			}
		}

		if (constraintClassDescriptor != null || constraintedGenericTypeVars != null)
			outputString += " extends ";

		if (constraintClassDescriptor != null)
			outputString += constraintClassDescriptor.getDescribedClassSimpleName();

		if (constraintedGenericTypeVars != null)
		{
			for (GenericTypeVar g : constraintedGenericTypeVars)
			{
				outputString += "<";
				outputString += g.toString();
				outputString += ">";
			}
		}

		return outputString;
	}
}
