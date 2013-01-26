package ecologylab.platformspecifics;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.stream.XMLInputFactory;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.platformspecifics.ISimplPlatformSpecifics;

import ecologylab.appframework.types.prefs.MetaPrefColor;
import ecologylab.appframework.types.prefs.MetaPrefSet;
import ecologylab.appframework.types.prefs.MetaPrefsTranslationScope;
import ecologylab.appframework.types.prefs.Pref;
import ecologylab.appframework.types.prefs.PrefColor;
import ecologylab.appframework.types.prefs.PrefDelayedOp;
import ecologylab.appframework.types.prefs.PrefSet;
import ecologylab.appframework.types.prefs.gui.PrefsEditor;
import ecologylab.generic.Debug;
import ecologylab.generic.ReflectionTools;
import ecologylab.generic.StringInputStream;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.GenericTypeVar;
import ecologylab.serialization.deserializers.pullhandlers.stringformats.XMLParser;
import ecologylab.serialization.deserializers.pullhandlers.stringformats.XMLParserSun;
import ecologylab.serialization.types.PlatformSpecificTypesSun;

public class FundamentalPlatformSpecificsSun implements ISimplPlatformSpecifics
{
	public void initializePlatformSpecificTranslation()
	{
		MetaPrefsTranslationScope.get().addTranslation(MetaPrefColor.class);
	}

	// in ecologylab.serialization.ClassDescriptor;
	public void deriveSuperClassGenericTypeVars(ClassDescriptor classDescriptor)
	{
		Class<?> describedClass = classDescriptor.getDescribedClass();
		// ArrayList<GenericTypeVar> superClassGenericTypeVars =
		// classDescriptor.getSuperClassGenericTypeVars();

		if (describedClass == null)
			return;

		Type superClassType = describedClass.getGenericSuperclass();

		if (superClassType instanceof ParameterizedType)
		{
			ParameterizedType superClassParameterizedType = (ParameterizedType) superClassType;
			classDescriptor.setSuperClassGenericTypeVars(getGenericTypeVars(superClassParameterizedType, classDescriptor.getGenericTypeVars()));
		}
	}

	// in ecologylab.serialization.FieldDescriptor;
	public void deriveFieldGenericTypeVars(FieldDescriptor fieldDescriptor)
	{
		Field field = fieldDescriptor.getField();
		Type genericType = field.getGenericType();
		ArrayList<GenericTypeVar> derivedGenericTypeVars = new ArrayList<GenericTypeVar>();

		if (genericType instanceof TypeVariable)
		{
			TypeVariable tv = (TypeVariable) genericType;
			GenericTypeVar g = GenericTypeVar.getGenericTypeVarRef(tv, fieldDescriptor.getGenericTypeVarsContext());
			derivedGenericTypeVars.add(g);
		}
		else if (genericType instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType) genericType;

			Type[] types = parameterizedType.getActualTypeArguments();

			if (types == null | types.length <= 0)
				return;

			for (Type t : types)
			{
				GenericTypeVar g = GenericTypeVar.getGenericTypeVarRef(t, fieldDescriptor.getGenericTypeVarsContext());
				derivedGenericTypeVars.add(g);
			}
		}
		
		fieldDescriptor.setGenericTypeVars(derivedGenericTypeVars);
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
			else if (typeArg0 instanceof ParameterizedType)
			{
				// nested parameterized type
				ParameterizedType pti = (ParameterizedType) typeArg0;
				result = (Class) pti.getRawType();
			}
			else if (typeArg0 instanceof TypeVariable)
			{
				TypeVariable tvi = (TypeVariable) typeArg0;
				Type[] tviBounds = tvi.getBounds();
				result = (Class) tviBounds[0];
				Debug.debugT(this, "yo! " + result);
			}
			else
			{
				Debug.error(this, "getTypeArgClass(" + field + ", " + i
						+ " yucky! Consult s.im.mp serialization developers.");
			}
		}
		return result;
	};

	public static ArrayList<GenericTypeVar> getGenericTypeVars(ParameterizedType parameterizedType,
	                                                           ArrayList<GenericTypeVar> scope)
	{
		Type[] types = parameterizedType.getActualTypeArguments();

		if (types == null | types.length <= 0)
			return null;

		ArrayList<GenericTypeVar> returnValue = new ArrayList<GenericTypeVar>();
		for (Type t : types)
		{
			GenericTypeVar g = GenericTypeVar.getGenericTypeVarRef(t, scope);
			returnValue.add(g);
		}

		return returnValue;
	}

	public void checkBoundParameterizedTypeImpl(GenericTypeVar g, Type bound)
	{
		if (bound instanceof ParameterizedType)
		{
			ParameterizedType parmeterizedType = (ParameterizedType) bound;
			Class rawType = (Class) parmeterizedType.getRawType();
			g.setConstraintClassDescriptor(ClassDescriptor.getClassDescriptor(rawType));

			Type[] types = parmeterizedType.getActualTypeArguments();

			for (Type type : types)
			{
				g.addContraintGenericTypeVarArg(GenericTypeVar.getGenericTypeVarRef(type, g.getScope()));
			}
		}
	}

	public void checkTypeWildcardTypeImpl(GenericTypeVar g, Type type)
	{
		if (type instanceof WildcardType)
		{
			g.setName("?");
			WildcardType wildCardType = (WildcardType) type;
			GenericTypeVar.resolveGenericTypeVarReferenceConstraints(g, wildCardType.getUpperBounds());
		}
	}

	public void checkTypeParameterizedTypeImpl(GenericTypeVar g, Type type)
	{
		if (type instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType) type;
			g.setClassDescriptor(ClassDescriptor.getClassDescriptor((Class) parameterizedType.getRawType()));

			Type[] types = parameterizedType.getActualTypeArguments();

			for (Type t : types)
			{
				g.addGenericTypeVarArg(GenericTypeVar.getGenericTypeVarRef(t, g.getScope()));
			}
		}
	}

	PrefsEditor	prefsEditor;

	@Override
	public Object getOrCreatePrefsEditor(MetaPrefSet metaPrefSet, PrefSet prefSet,
			ParsedURL savePrefsPURL, final boolean createJFrame, final boolean isStandalone)
	{
		Object result = prefsEditor;
		if (result == null)
		{
			result = new PrefsEditor(metaPrefSet, prefSet, savePrefsPURL, createJFrame, isStandalone);
		}
		return result;
	}

	public String[] getReaderFormatNames()
	{
		return ImageIO.getReaderFormatNames();
	}

	public void beep()
	{
		Toolkit.getDefaultToolkit().beep();
	}

	public void showDialog(String msg, String[] digital_options)
	{
		JOptionPane.showOptionDialog(null, msg, "combinFormation exited", JOptionPane.DEFAULT_OPTION,
				JOptionPane.WARNING_MESSAGE, null, digital_options, digital_options[0]);
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
	
	public static final Class[] PREF_OP_SUNTRANSLATIONS 	= 
	{
		PrefDelayedOp.class,
	};
	

	@Override
	public Class[] addtionalPrefOpTranslations()
	{
		return PREF_OP_SUNTRANSLATIONS;
	}

	public static final Class[] PREF_SET_BASE_SUNTRANSLATIONS 	= 
	{
		PrefColor.class, PrefDelayedOp.class, MetaPrefColor.class
	};
	
	@Override
	public Class[] additionalPrefSetBaseTranslations()
	{
		return PREF_SET_BASE_SUNTRANSLATIONS;
	}

	@Override
	public void initializePlatformSpecificTypes() 
	{
		new PlatformSpecificTypesSun();
	}

	@Override
	public XMLParser getXMLParser(InputStream inputStream, Charset charSet)
			throws SIMPLTranslationException
	{
		try
		{
			return new XMLParserSun(XMLInputFactory.newInstance().createXMLStreamReader(inputStream, charSet.name()));
		}
		catch (Exception ex)
		{
			throw new SIMPLTranslationException("exception occurred in deserialzation ", ex);
		}
	}

	@Override
	public XMLParser getXMLParser(InputStream inputStream) throws SIMPLTranslationException
	{
		try
		{
			return new XMLParserSun(XMLInputFactory.newInstance().createXMLStreamReader(inputStream));
		}
		catch (Exception ex)
		{
			throw new SIMPLTranslationException("exception occurred in deserialzation ", ex);
		}
	}

	@Override
	public XMLParser getXMLParser(CharSequence charSequence) throws SIMPLTranslationException
	{
		try
		{
			InputStream xmlStream = new StringInputStream(charSequence, StringInputStream.UTF8);
			return new XMLParserSun(XMLInputFactory.newInstance().createXMLStreamReader(xmlStream, "UTF-8"));
		}
		catch (Exception ex)
		{
			throw new SIMPLTranslationException("exception occurred in deserialzation ", ex);
		}
	}

}
