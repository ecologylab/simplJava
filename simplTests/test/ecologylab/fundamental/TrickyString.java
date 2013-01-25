package ecologylab.fundamental;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.StringFormat;
import ecologylab.serialization.types.ScalarType;
import ecologylab.serialization.types.TypeRegistry;

public class TrickyString
{
	@simpl_scalar 
	public String trickyString;
	
	public TrickyString(){}
	
	
	
}
