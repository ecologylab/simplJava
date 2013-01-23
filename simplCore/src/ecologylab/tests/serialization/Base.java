/**
 * 
 */
package ecologylab.tests.serialization;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_scope;

/**
 * @author andruid
 *
 */
public class Base extends ElementState
{

	@simpl_scope(RssPolyWrapTest.POLY_WRAP_TEST_TRANSLATIONS_NAME)
	@simpl_collection	
	ArrayList<RssPolyWrapTest>  			mixins;

	public static final SimplTypesScope BASE_TRANSLATIONS	= 
		SimplTypesScope.get("base_translations", Base.class);
	/**
	 * 
	 */
	public Base()
	{
		// TODO Auto-generated constructor stub
	}

}
