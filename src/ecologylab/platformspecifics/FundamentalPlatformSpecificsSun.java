package ecologylab.platformspecifics;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.GenericTypeVar;

import ecologylab.generic.ReflectionTools;
import ecologylab.generic.Debug;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;
import sun.reflect.generics.reflectiveObjects.WildcardTypeImpl;

public class FundamentalPlatformSpecificsSun implements IFundamentalPlatformSpecifics
{
	// in ecologylab.serialization.ClassDescriptor;
	public void deriveSuperGenericTypeVariables(ClassDescriptor classDescriptor)
	{
		Class<?> describedClass = classDescriptor.getDescribedClass();
//		ArrayList<GenericTypeVar> superClassGenericTypeVars = classDescriptor.getSuperClassGenericTypeVars();
		
		if (describedClass == null)
		return;
	
		Type superClassType = describedClass.getGenericSuperclass();
		
		if (superClassType instanceof ParameterizedTypeImpl)
		{
				ParameterizedTypeImpl superClassParameterizedType = (ParameterizedTypeImpl) superClassType;
				classDescriptor.setSuperClassGenericTypeVars(getGenericTypeVars(superClassParameterizedType));
		}
	}
	
	// in ecologylab.serialization.FieldDescriptor;
	public void deriveGenericTypeVariables(FieldDescriptor fieldDescriptor) {
		Field field = fieldDescriptor.getField();
		Type genericType = field.getGenericType();
		ArrayList<GenericTypeVar> derivedGenericTypeVars;
		derivedGenericTypeVars = new ArrayList<GenericTypeVar>();
		
		if(genericType instanceof ParameterizedTypeImpl)
		{
			ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl) genericType;
			
			Type[] types = parameterizedType.getActualTypeArguments();
	
			if (types == null | types.length <= 0)
				return;
	
			for (Type t : types)
			{
				GenericTypeVar g = GenericTypeVar.getGenericTypeVar(t);
				derivedGenericTypeVars.add(g);
			}
			fieldDescriptor.setGenericTypeVars(derivedGenericTypeVars);
		}
	};
	
	public Class<?> getTypeArgClass(Field field, int i, FieldDescriptor fiedlDescriptor) 
	{
		Class result = null;

		java.lang.reflect.Type[] typeArgs = ReflectionTools.getParameterizedTypeTokens(field);
		if (typeArgs != null)
		{
			final int max = typeArgs.length - 1;
			if (i > max)
				i = max;
			final Type typeArg0 = typeArgs[i];
			if (typeArg0 instanceof Class)
			{
				result = (Class) typeArg0;
			}
			// sun below 
			else if (typeArg0 instanceof ParameterizedTypeImpl)
			{ // nested parameterized type
				ParameterizedTypeImpl pti = (ParameterizedTypeImpl) typeArg0;
				result = pti.getRawType();
			}
			else if (typeArg0 instanceof TypeVariableImpl)
			{
				TypeVariableImpl tvi = (TypeVariableImpl) typeArg0;
				Type[] tviBounds = tvi.getBounds();
				result = (Class) tviBounds[0];
				Debug.debugT(this, "yo! " + result);
			}
			// sun above
			else
			{
				Debug.error(this, "getTypeArgClass(" + field + ", " + i
						+ " yucky! Consult s.im.mp serialization developers.");
			}
		}
		return result;
	};
	
	// in ecologylab.serialization.GenericTypeVar;
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
			GenericTypeVar g = GenericTypeVar.getGenericTypeVar(t);
			returnValue.add(g);
		}

		return returnValue;
	}
	
	public void checkBoundParameterizedTypeImpl(GenericTypeVar g, Type bound)
	{
		if (bound instanceof ParameterizedTypeImpl)
		{
			ParameterizedTypeImpl parmeterizedType = (ParameterizedTypeImpl) bound;
			g.setConstraintClassDescriptor(ClassDescriptor.getClassDescriptor(parmeterizedType
					.getRawType()));

			Type[] types = parmeterizedType.getActualTypeArguments();

			for (Type type : types)
			{
				g.addContraintGenericTypeVar(GenericTypeVar.getGenericTypeVar(type));
			}
		}
	}

	public void checkTypeWildcardTypeImpl(GenericTypeVar g, Type type)
	{
		if (type instanceof WildcardTypeImpl)
		{
			g.setName("?");
			WildcardTypeImpl wildCardType = (WildcardTypeImpl) type;
			GenericTypeVar.resolveGenericConstraints(g, wildCardType.getUpperBounds());
		}
	}
	
	public void checkTypeParameterizedTypeImpl(GenericTypeVar g, Type type)
	{
		if (type instanceof ParameterizedTypeImpl)
		{
			ParameterizedTypeImpl parmeterizedType = (ParameterizedTypeImpl) type;
			g.setClassDescriptor(ClassDescriptor.getClassDescriptor(parmeterizedType.getRawType()));

			Type[] types = parmeterizedType.getActualTypeArguments();

			for (Type t : types)
			{
				g.addGenericTypeVar(GenericTypeVar.getGenericTypeVar(t));
			}
		}
	}
}
