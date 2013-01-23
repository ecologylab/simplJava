/**
 * 
 */
package ecologylab.serialization.library.apple;

import java.io.File;
import java.io.IOException;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;
import ecologylab.serialization.formatenums.StringFormat;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class PListTranslations
{
	public static final String		NAME						= "Apple PList";

	protected static final Class	TRANSLATIONS[]	=
																								{
			ecologylab.serialization.library.apple.DictionaryProperty.class, KeyProperty.class,
			PList.class, Property.class, StringProperty.class, ArrayProperty.class,
			IntegerProperty.class, ArrayProperty.class, BooleanProperty.class, TrueProperty.class,
			FalseProperty.class, RealProperty.class, DataProperty.class	};

	public static SimplTypesScope get()
	{
		return SimplTypesScope.get(NAME, TRANSLATIONS);
	}

	public static void main(String[] args) throws SIMPLTranslationException, IOException
	{
		PList result = (PList) PListTranslations.get()
		// .deserialize("/Users/toupsz/Dropbox/ttecBibForBill/simpTest2.xml");
																						.deserialize(	new File("ecologylab/serialization/library/apple/plist.xml"),
																													Format.XML);
		
		
		SimplTypesScope.serialize(result, System.out, StringFormat.XML);
//		ClassDescriptor.serialize(result,
//															new File("/Users/toupsz/Dropbox/ttecBibForBill/tecNewTutMap2.xml"),
//															Format.XML);
	}
	
	public static final String		KEY_NAME						= "KeyTrans";

	protected static final Class	KEY_TRANSLATIONS[]	=
																								{
			KeyProperty.class,
			Property.class, StringProperty.class	};

	public static SimplTypesScope getKey()
	{
		return SimplTypesScope.get(KEY_NAME, KEY_TRANSLATIONS);
	}
}
