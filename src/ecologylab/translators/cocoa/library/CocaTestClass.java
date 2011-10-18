package ecologylab.translators.cocoa.library;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_map;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.StringFormat;

/**
 * Test Class
 * 
 * @author nskhan
 */
public class CocaTestClass extends ElementState
{
	@simpl_scalar
	private int											intAttribute;

	@simpl_scalar
	private float										floatAttribute;

	@simpl_scalar
	private double									doubleAttribute;

	@simpl_scalar
	private byte										byteAttribute;

	@simpl_scalar
	private char										charAttribute;

	@simpl_scalar
	private boolean									booleanAttribute;

	@simpl_scalar
	private long										longAttribute;

	@simpl_scalar
	private short										shortAttribute;

	@simpl_scalar
	private String									stringAttribute;

	@simpl_scalar
	private Date										dateAttribute;

	@simpl_scalar
	private StringBuilder						stringBuilderAttribute;

	@simpl_scalar
	private URL											urlAttribute;

	@simpl_nowrap
	@simpl_collection
	private ArrayList<String>				arrayListAttribute;

	@simpl_map
	private HashMap<String, String>	hashMapAttribute;

	public CocaTestClass() throws MalformedURLException
	{
		this.intAttribute = 1;
		this.floatAttribute = 1.0f;
		this.doubleAttribute = 2.0;
		this.byteAttribute = 1;
		this.charAttribute = 'c';
		this.booleanAttribute = false;
		this.longAttribute = 1;
		this.shortAttribute = 3;
		this.stringAttribute = "234";
		this.dateAttribute = new Date();
		this.stringBuilderAttribute = new StringBuilder();
		// this.urlAttribute = new URL("c:\test");
		this.arrayListAttribute = new ArrayList<String>();
		this.hashMapAttribute = new HashMap<String, String>();

		this.arrayListAttribute.add("234");
		this.hashMapAttribute.put("1", "3");
	}

	public static void main(String args[]) throws SIMPLTranslationException, MalformedURLException
	{
		CocaTestClass test = new CocaTestClass();
		SimplTypesScope.serialize(test, System.out, StringFormat.XML);		
	}

	public void setIntAttribute(int intAttribute)
	{
		this.intAttribute = intAttribute;
	}

	public int getIntAttribute()
	{
		return intAttribute;
	}

	public void setFloatAttribute(float floatAttribute)
	{
		this.floatAttribute = floatAttribute;
	}

	public float getFloatAttribute()
	{
		return floatAttribute;
	}

	public void setDoubleAttribute(double doubleAttribute)
	{
		this.doubleAttribute = doubleAttribute;
	}

	public double getDoubleAttribute()
	{
		return doubleAttribute;
	}

	public void setByteAttribute(byte byteAttribute)
	{
		this.byteAttribute = byteAttribute;
	}

	public byte getByteAttribute()
	{
		return byteAttribute;
	}

	public void setCharAttribute(char charAttribute)
	{
		this.charAttribute = charAttribute;
	}

	public char getCharAttribute()
	{
		return charAttribute;
	}

	public void setBooleanAttribute(boolean booleanAttribute)
	{
		this.booleanAttribute = booleanAttribute;
	}

	public boolean isBooleanAttribute()
	{
		return booleanAttribute;
	}

	public void setLongAttribute(long longAttribute)
	{
		this.longAttribute = longAttribute;
	}

	public long getLongAttribute()
	{
		return longAttribute;
	}

	public void setShortAttribute(short shortAttribute)
	{
		this.shortAttribute = shortAttribute;
	}

	public short getShortAttribute()
	{
		return shortAttribute;
	}

	public void setStringAttribute(String stringAttribute)
	{
		this.stringAttribute = stringAttribute;
	}

	public String getStringAttribute()
	{
		return stringAttribute;
	}

	public void setDateAttribute(Date dateAttribute)
	{
		this.dateAttribute = dateAttribute;
	}

	public Date getDateAttribute()
	{
		return dateAttribute;
	}

	public void setStringBuilderAttribute(StringBuilder stringBuilderAttribute)
	{
		this.stringBuilderAttribute = stringBuilderAttribute;
	}

	public StringBuilder getStringBuilderAttribute()
	{
		return stringBuilderAttribute;
	}

	public void setUrlAttribute(URL urlAttribute)
	{
		this.urlAttribute = urlAttribute;
	}

	public URL getUrlAttribute()
	{
		return urlAttribute;
	}

	public void setArrayListAttribute(ArrayList<String> arrayListAttribute)
	{
		this.arrayListAttribute = arrayListAttribute;
	}

	public ArrayList<String> getArrayListAttribute()
	{
		return arrayListAttribute;
	}

	public void setHashMapAttribute(HashMap<String, String> hashMapAttribute)
	{
		this.hashMapAttribute = hashMapAttribute;
	}

	public HashMap<String, String> getHashMapAttribute()
	{
		return hashMapAttribute;
	}
}
