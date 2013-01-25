package ecologylab.fundamental;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.StringFormat;

public class TrickyString
{
	@simpl_scalar 
	public String trickyString;
	
	public TrickyString(){}
	
	@Test
	public void nullStringValue() throws SIMPLTranslationException 
	{
    TrickyString ts = new TrickyString();
    ts.trickyString = null;
    String xml = SimplTypesScope.serialize(ts, StringFormat.XML).toString();
    System.out.println(xml);
    
	  SimplTypesScope typeScope = SimplTypesScope.get("TrickyString", TrickyString.class);
    TrickyString ts1 = (TrickyString) typeScope.deserialize(xml, StringFormat.XML);
    assertNotNull(ts1);
    if (ts1.trickyString != null)
      System.out.println(ts1.trickyString.length());
    assertNull(ts1.trickyString);
	}
	
}
