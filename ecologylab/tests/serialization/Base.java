/**
 * 
 */
package ecologylab.tests.serialization;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.TranslationScope;

/**
 * @author andruid
 *
 */
public class Base extends ElementState
{

	@simpl_scope(RssPolyWrapTest.POLY_WRAP_TEST_TRANSLATIONS_NAME)
	@simpl_collection	
	ArrayList<RssPolyWrapTest>  			mixins;

	public static final TranslationScope BASE_TRANSLATIONS	= 
		TranslationScope.get("base_translations", Base.class);
	/**
	 * 
	 */
	public Base()
	{
		// TODO Auto-generated constructor stub
	}

}
