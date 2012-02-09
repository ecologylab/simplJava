package ecologylab.platformspecifics;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import ecologylab.appframework.types.prefs.MetaPrefSet;
import ecologylab.appframework.types.prefs.PrefSet;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.DeserializationHookStrategy;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.GenericTypeVar;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.deserializers.pullhandlers.stringformats.StringPullDeserializer;
import ecologylab.serialization.formatenums.StringFormat;

public interface IFundamentalPlatformSpecifics
{
	// in ApplicationEnvironment
	void initializePlatformSpecificTranslation();

	// in ecologylab.serialization.ClassDescriptor;
	void deriveSuperGenericTypeVariables(ClassDescriptor classDescriptor);

	// in ecologylab.serialization.FieldDescriptor;
	void deriveGenericTypeVariables(FieldDescriptor fieldDescriptor);

	Class<?> getTypeArgClass(Field field, int i, FieldDescriptor fiedlDescriptor);

	// in ecologylab.serialization.GenericTypeVar;
	void checkBoundParameterizedTypeImpl(GenericTypeVar g, Type bound);

	void checkTypeWildcardTypeImpl(GenericTypeVar g, Type type);

	void checkTypeParameterizedTypeImpl(GenericTypeVar g, Type type);

	// more stuff
	Object getOrCreatePrefsEditor(MetaPrefSet metaPrefSet, PrefSet prefSet, ParsedURL savePrefsPURL,
			final boolean createJFrame, final boolean isStandalone);

	// in ParsedUrl
	String[] getReaderFormatNames();

	// in Generic
	void beep();

	void showDialog(String msg, String[] digital_options);

	// in Pref
	Object usePrefColor(String name, Object defaultValue);

	Object lookupColor(String name, Object defaultValue) throws ClassCastException;

	Class[] addtionalPrefOpTranslations();

	Class[] additionalPrefSetBaseTranslations();

	// in Serialization.deserializers
	StringPullDeserializer getXMLPullDeserializer(SimplTypesScope translationScope,
			TranslationContext translationContext,
			DeserializationHookStrategy deserializationHookStrategy);
	
	// platform specific types
	void initializePlatformSpecificTypes();
}
