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
 * This class encapsulates generic type variables declarations on classes and fields 
 * 
 * @author quyin
 * 
 */
public class GenericTypeVar extends Debug
{
	// The declared name of the generic type variable. such as 'M' for Media<M> test;
	// Wild card operator '?' will also be populated in this field. 
	// The classDescriptor will be null if this parameter is populated. 
	@simpl_scalar
	String										name;

	// ClassDescriptor of the declared generic type variable. Such as ClassDescriptor of class Media in MediaSearchResult<Media>; 
	// The name field of the Generic type variable would be null if this field is populated. 
	@simpl_composite
	ClassDescriptor						classDescriptor							= null;

	// If the declared generic type var is also generic. for example. MediaSearchResult<Media<M,T>> 
	// then this collection will hold the generic type variables (such as M & T in example).
	@simpl_collection
	ArrayList<GenericTypeVar>	genericTypeVars							= null;

	// This variable holds the ClassDecriptor of the class declared as a constraint to the generic type variable. 
	// For example this variable will hold ClassDescriptor of class Media if generic type is declared as MediaSearchResult<M extends Media>
	@simpl_composite
	ClassDescriptor						constraintClassDescriptor		= null;

	// This variable holds the collection of generic type variables of a class declared as the constraint to the generic type variable. 
	// For example this variable will hold R & S if generic type variable is declared as MediaSearchResult<M extends Media<R,S>>
	@simpl_collection
	ArrayList<GenericTypeVar>	constraintedGenericTypeVars	= null;
	
	// If a declared generic type variable is used, then this variable holds the reference of the decalared generic type var. 
	// for example in case of MediaSearchResult<M extends Media, M> the use of generic type var M will refer to generic type var of its declaration M extends Media. 
	// this variable will refer to M extends Media in the example. (the name field will contain M). 
	@simpl_composite
	GenericTypeVar						referredGenericTypeVar			= null;

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
