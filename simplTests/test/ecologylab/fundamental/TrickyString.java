package ecologylab.fundamental;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import simpl.annotations.dbal.simpl_scalar;
import simpl.exceptions.SIMPLTranslationException;
import simpl.types.ScalarType;
import simpl.types.TypeRegistry;

import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.StringFormat;

public class TrickyString
{
	@simpl_scalar 
	public String trickyString;
	
	public TrickyString(){}
	
	
	
}
