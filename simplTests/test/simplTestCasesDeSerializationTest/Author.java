package simplTestCasesDeSerializationTest;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_composite_as_scalar;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.Format;


public class Author
{
	@simpl_composite_as_scalar
	@simpl_scalar
	String name;
	
	@simpl_scalar
	String city;
	
	public Author(String n, String c)
	{
		name = n;
		city = c;
	}
	
	public Author()
	{
		
	}


}
