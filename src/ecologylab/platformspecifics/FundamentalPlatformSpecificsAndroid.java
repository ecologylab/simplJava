package ecologylab.platformspecifics;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.graphics.Color;

import ecologylab.appframework.types.prefs.MetaPrefColor;
import ecologylab.appframework.types.prefs.MetaPrefSet;
import ecologylab.appframework.types.prefs.MetaPrefsTranslationScope;
import ecologylab.appframework.types.prefs.Pref;
import ecologylab.appframework.types.prefs.PrefColor;
import ecologylab.appframework.types.prefs.PrefSet;
import ecologylab.generic.Debug;
import ecologylab.generic.ReflectionTools;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.GenericTypeVar;

public class FundamentalPlatformSpecificsAndroid implements IFundamentalPlatformSpecifics
{
	private static final String[]	READER_FORMAT_NAMES	= new String[] { "jpg", "jpeg", "pjpg", "pjpeg", "gif", "png", };

	public void initialize()
	{
		MetaPrefsTranslationScope.get().addTranslation(MetaPrefColor.class);
	};

	// in ecologylab.serialization.ClassDescriptor;
	public void deriveSuperGenericTypeVariables(ClassDescriptor classDescriptor)
	{
//		Class<?> describedClass = classDescriptor.getDescribedClass();
//		// ArrayList<GenericTypeVar> superClassGenericTypeVars =
//		// classDescriptor.getSuperClassGenericTypeVars();
//
//		if (describedClass == null)
//			return;
//
//		Type superClassType = describedClass.getGenericSuperclass();
//
//		if (superClassType instanceof ParameterizedTypeImpl)
//		{
//			ParameterizedTypeImpl superClassParameterizedType = (ParameterizedTypeImpl) superClassType;
//			classDescriptor.setSuperClassGenericTypeVars(getGenericTypeVars(superClassParameterizedType));
//		}
	}

	// in ecologylab.serialization.FieldDescriptor;
	public void deriveGenericTypeVariables(FieldDescriptor fieldDescriptor)
	{
//		Field field = fieldDescriptor.getField();
//		Type genericType = field.getGenericType();
//		ArrayList<GenericTypeVar> derivedGenericTypeVars;
//		derivedGenericTypeVars = new ArrayList<GenericTypeVar>();
//
//		if (genericType instanceof ParameterizedTypeImpl)
//		{
//			ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl) genericType;
//
//			Type[] types = parameterizedType.getActualTypeArguments();
//
//			if (types == null | types.length <= 0)
//				return;
//
//			for (Type t : types)
//			{
//				GenericTypeVar g = GenericTypeVar.getGenericTypeVar(t);
//				derivedGenericTypeVars.add(g);
//			}
//			fieldDescriptor.setGenericTypeVars(derivedGenericTypeVars);
//		}
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
//			else if (typeArg0 instanceof ParameterizedTypeImpl)
//			{ // nested parameterized type
//				ParameterizedTypeImpl pti = (ParameterizedTypeImpl) typeArg0;
//				result = pti.getRawType();
//			}
//			else if (typeArg0 instanceof TypeVariableImpl)
//			{
//				TypeVariableImpl tvi = (TypeVariableImpl) typeArg0;
//				Type[] tviBounds = tvi.getBounds();
//				result = (Class) tviBounds[0];
//				Debug.debugT(this, "yo! " + result);
//			}
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
//	public static ArrayList<GenericTypeVar> getGenericTypeVars(Type parameterizedType)
//	{
//		return getGenericTypeVars((ParameterizedTypeImpl) parameterizedType);
//	}

//	public static ArrayList<GenericTypeVar> getGenericTypeVars(ParameterizedTypeImpl parameterizedType)
//	{
//		Type[] types = parameterizedType.getActualTypeArguments();
//
//		if (types == null | types.length <= 0)
//			return null;
//
//		ArrayList<GenericTypeVar> returnValue = new ArrayList<GenericTypeVar>();
//		for (Type t : types)
//		{
//			GenericTypeVar g = GenericTypeVar.getGenericTypeVar(t);
//			returnValue.add(g);
//		}
//
//		return returnValue;
//	}

	public void checkBoundParameterizedTypeImpl(GenericTypeVar g, Type bound)
	{
//		if (bound instanceof ParameterizedTypeImpl)
//		{
//			ParameterizedTypeImpl parmeterizedType = (ParameterizedTypeImpl) bound;
//			g.setConstraintClassDescriptor(ClassDescriptor.getClassDescriptor(parmeterizedType
//					.getRawType()));
//
//			Type[] types = parmeterizedType.getActualTypeArguments();
//
//			for (Type type : types)
//			{
//				g.addContraintGenericTypeVar(GenericTypeVar.getGenericTypeVar(type));
//			}
//		}
	}

	public void checkTypeWildcardTypeImpl(GenericTypeVar g, Type type)
	{
//		if (type instanceof WildcardTypeImpl)
//		{
//			g.setName("?");
//			WildcardTypeImpl wildCardType = (WildcardTypeImpl) type;
//			GenericTypeVar.resolveGenericConstraints(g, wildCardType.getUpperBounds());
//		}
	}

	public void checkTypeParameterizedTypeImpl(GenericTypeVar g, Type type)
	{
//		if (type instanceof ParameterizedTypeImpl)
//		{
//			ParameterizedTypeImpl parmeterizedType = (ParameterizedTypeImpl) type;
//			g.setClassDescriptor(ClassDescriptor.getClassDescriptor(parmeterizedType.getRawType()));
//
//			Type[] types = parmeterizedType.getActualTypeArguments();
//
//			for (Type t : types)
//			{
//				g.addGenericTypeVar(GenericTypeVar.getGenericTypeVar(t));
//			}
//		}
	}

	public Object getOrCreatePrefsEditor(MetaPrefSet metaPrefSet, PrefSet prefSet,
			ParsedURL savePrefsPURL, final boolean createJFrame, final boolean isStandalone)
	{
		return null;
	}

	public String[] getReaderFormatNames()
	{
		return READER_FORMAT_NAMES; //ImageIO.getReaderFormatNames();
	}

	public void beep()
	{
	}

	public void showDialog(String msg, String[] digital_options)
	{
//		JOptionPane.showOptionDialog(null, msg, "combinFormation exited", JOptionPane.DEFAULT_OPTION,
//				JOptionPane.WARNING_MESSAGE, null, digital_options, digital_options[0]);
	}

	public Object usePrefColor(String name, Object defaultValue)
	{
		PrefColor pref = (PrefColor) Pref.lookupPref(name);
		if (pref == null)
		{
			pref = new PrefColor(name, (Color) defaultValue);
			pref.register();
		}
		return pref;
	}

	public Object lookupColor(String name, Object defaultValue) throws ClassCastException
	{
		PrefColor prefColor = ((PrefColor) Pref.lookupPref(name));
		return (prefColor == null) ? defaultValue : prefColor.value();
	}

	public Class[] addtionalPrefOpTranslations()
	{
		return null;
	}

	public static final Class[]	PREF_SET_BASE_SUNTRANSLATIONS	=
																														{ PrefColor.class, MetaPrefColor.class };

	public Class[] additionalPrefSetBaseTranslations()
	{
		return PREF_SET_BASE_SUNTRANSLATIONS;
	}

}
