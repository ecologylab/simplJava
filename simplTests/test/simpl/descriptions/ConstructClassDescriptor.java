package simpl.descriptions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.FieldType;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.StringFormat;
import simpl.types.scalar.*;

import ecologylab.fundamental.simplescalar.SimpleBoolean;
import ecologylab.fundamental.simplescalar.SimpleByte;
import ecologylab.fundamental.simplescalar.SimpleChar;
import ecologylab.fundamental.simplescalar.SimpleDate;
import ecologylab.fundamental.simplescalar.SimpleDouble;
import ecologylab.fundamental.simplescalar.SimpleFloat;
import ecologylab.fundamental.simplescalar.SimpleInteger;
import ecologylab.fundamental.simplescalar.SimpleJavaURL;
import ecologylab.fundamental.simplescalar.SimpleLong;
import ecologylab.fundamental.simplescalar.SimpleParsedURL;
import ecologylab.fundamental.simplescalar.SimplePattern;
import ecologylab.fundamental.simplescalar.SimpleShort;
import ecologylab.fundamental.simplescalar.SimpleString;
import ecologylab.fundamental.simplescalar.SimpleStringBuilder;
import ecologylab.fundamental.simplescalar.SimpleUUID;
import ecologylab.fundamental.simplescalar.Simpleprimboolean;
import ecologylab.fundamental.simplescalar.Simpleprimbyte;
import ecologylab.fundamental.simplescalar.Simpleprimchar;
import ecologylab.fundamental.simplescalar.Simpleprimdouble;
import ecologylab.fundamental.simplescalar.Simpleprimfloat;
import ecologylab.fundamental.simplescalar.Simpleprimint;
import ecologylab.fundamental.simplescalar.Simpleprimlong;
import ecologylab.fundamental.simplescalar.Simpleprimshort;

public class ConstructClassDescriptor {

	
	// Todo: Stealthily swap this with a factory method.
	private ClassDescriptor ConstructClassDescriptor(Class<?> lass)
	{
		return ClassDescriptors.getClassDescriptor(lass);
	}
	
	private void testSimpleScalar(Class<?> lass, Class<?> expectedType)
	{
		ClassDescriptor cd = ConstructClassDescriptor(lass);
		
		// Get the one field that is in 	the simple scalar class
		assertEquals(1, cd.allFieldDescriptors().size());	
		FieldDescriptor fd = cd.allFieldDescriptors().get(0);
		assertEquals(lass.getSimpleName().toLowerCase(), fd.getName());
		assertEquals(FieldType.SCALAR, fd.getType());
		// TODO: BUG. //assertEquals(1, fd.getMetaInformation().size());
	//	assertEquals(expectedType, fd.getJavaType());
		
		// TODO: Roundtrip the class descriptor. 
		
	}
	
	// TODO: Roundtrip some range of values
	
	@Test
	public void forSimpleBoolean() {
		testSimpleScalar(SimpleBoolean.class, BooleanType.class);
	}

	@Test
	public void forSimpleByte() {
		testSimpleScalar(SimpleByte.class, ByteType.class);
	}
	
	@Test
	public void forSimpleChar(){
		testSimpleScalar(SimpleChar.class, CharType.class);
	}
	
	/*
	@Test
	public void forSimpleDate(){
		testSimpleScalar(SimpleDate.class, DateType.class);
	}*/
	
	@Test
	public void forSimpleDouble(){
		testSimpleScalar(SimpleDouble.class, DoubleType.class);
	}
	
	@Test
	public void forSimpleFloat(){
		testSimpleScalar(SimpleFloat.class, FloatType.class);
	}
	
	@Test
	public void forSimpleInteger() throws SIMPLTranslationException
	{
		testSimpleScalar(SimpleInteger.class, IntegerType.class);
		
		for(int i=-5; i<5;i++)
		{
			testSimpleInt(i, StringFormat.XML);
		}
	
		for(int i=-5; i<5;i++)
		{
			testSimpleInt(i, StringFormat.JSON);
		}
	}
	
	private void testSimpleInt(int i , StringFormat format) throws SIMPLTranslationException
	{
		SimpleInteger si = new SimpleInteger();
		si.setSimpleInteger(0);
			
		SimplTypesScope sts = (SimplTypesScope)SimplTypesScopeFactory.name("inttest").translations(SimpleInteger.class).create();
		
		StringBuilder sb = sts.serialize(si, format);
		SimpleInteger result = (SimpleInteger)sts.deserialize(sb.toString(), format);
		
		assertEquals("Roundtrip value did not succeed for default value for: " + format.toString(), si.getSimpleInteger(), result.getSimpleInteger());
	}
	
	/*
	
	@Test
	public void forSimpleJavaURL(){
		testSimpleScalar(SimpleJavaURL.class, URLType.class);
	}*/
	
	@Test
	public void forSimpleLong(){
		testSimpleScalar(SimpleLong.class, LongType.class);
	}
	
	/*
	@Test
	public void forSimpleParsedURL()
	{
		testSimpleScalar(SimpleParsedURL.class, ParsedURLType.class);
	}

	@Test
	public void forSimplePattern()
	{
		testSimpleScalar(SimplePattern.class, PatternType.class);
	}
	*/
	
	@Test
	public void forSimplePrimBoolean()
	{
		testSimpleScalar(Simpleprimboolean.class, BooleanType.class);
	}
	
	@Test
	public void forSimplePrimByte()
	{
		testSimpleScalar(Simpleprimbyte.class, ByteType.class);
	}
	
	@Test
	public void forSimplePrimChar()
	{
		testSimpleScalar(Simpleprimchar.class, CharType.class);
	}
	
	@Test
	public void forSimplePrimDouble()
	 {
		 testSimpleScalar(Simpleprimdouble.class, DoubleType.class);
	 }
	
	@Test
	public void forSimplePrimFloat()
	{
		testSimpleScalar(Simpleprimfloat.class, FloatType.class);
	}
	
	@Test
	public void forSimplePrimInt()
	{
		testSimpleScalar(Simpleprimint.class, IntegerType.class);
	}
	
	@Test
	public void forSimplePrimLong()
	{
		testSimpleScalar(Simpleprimlong.class, LongType.class);
	}
	
	@Test
	public void forSimplePrimShort()
	{
		testSimpleScalar(Simpleprimshort.class, ShortType.class);
	}
	
	@Test
	public void forSimpleShort()
	{
		testSimpleScalar(SimpleShort.class, ShortType.class);
	}
	
	
	@Test
	public void forSimpleString()
	{
		//testSimpleScalar(SimpleString.class, StringType.class);
	}
	
	/*
	@Test
	public void forSimpleStringBuilder()
	{
		testSimpleScalar(SimpleStringBuilder.class, StringBuilderType.class);
	}
	
	@Test
	public void forSimpleUUID()
	{
		testSimpleScalar(SimpleUUID.class, UUIDType.class);
	}
	*/
	
}
