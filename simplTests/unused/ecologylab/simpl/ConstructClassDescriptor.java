package ecologylab.simpl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.types.scalar.BooleanType;
import ecologylab.serialization.types.scalar.ByteType;
import ecologylab.serialization.types.scalar.CharType;
import ecologylab.serialization.types.scalar.DateType;
import ecologylab.serialization.types.scalar.DoubleType;
import ecologylab.serialization.FieldType;
import ecologylab.serialization.types.scalar.FloatType;
import ecologylab.serialization.types.scalar.IntType;
import ecologylab.serialization.types.scalar.LongType;
import ecologylab.serialization.types.scalar.ParsedURLType;
import ecologylab.serialization.types.scalar.PatternType;
import ecologylab.serialization.types.scalar.ReferenceBooleanType;
import ecologylab.serialization.types.scalar.ReferenceDoubleType;
import ecologylab.serialization.types.scalar.ReferenceFloatType;
import ecologylab.serialization.types.scalar.ReferenceIntegerType;
import ecologylab.serialization.types.scalar.ReferenceLongType;
import ecologylab.serialization.types.scalar.ShortType;
import ecologylab.serialization.types.scalar.StringBuilderType;
import ecologylab.serialization.types.scalar.StringType;
import ecologylab.serialization.types.scalar.URLType;
import ecologylab.serialization.types.scalar.UUIDType;
import ecologylab.simpl.simplescalar.SimpleBoolean;
import ecologylab.simpl.simplescalar.SimpleByte;
import ecologylab.simpl.simplescalar.SimpleChar;
import ecologylab.simpl.simplescalar.SimpleDate;
import ecologylab.simpl.simplescalar.SimpleDouble;
import ecologylab.simpl.simplescalar.SimpleFloat;
import ecologylab.simpl.simplescalar.SimpleInteger;
import ecologylab.simpl.simplescalar.SimpleJavaURL;
import ecologylab.simpl.simplescalar.SimpleLong;
import ecologylab.simpl.simplescalar.SimpleParsedURL;
import ecologylab.simpl.simplescalar.SimplePattern;
import ecologylab.simpl.simplescalar.SimpleShort;
import ecologylab.simpl.simplescalar.SimpleString;
import ecologylab.simpl.simplescalar.SimpleStringBuilder;
import ecologylab.simpl.simplescalar.SimpleUUID;
import ecologylab.simpl.simplescalar.Simpleprimboolean;
import ecologylab.simpl.simplescalar.Simpleprimbyte;
import ecologylab.simpl.simplescalar.Simpleprimchar;
import ecologylab.simpl.simplescalar.Simpleprimdouble;
import ecologylab.simpl.simplescalar.Simpleprimfloat;
import ecologylab.simpl.simplescalar.Simpleprimint;
import ecologylab.simpl.simplescalar.Simpleprimlong;
import ecologylab.simpl.simplescalar.Simpleprimshort;

public class ConstructClassDescriptor {

	
	// Todo: Stealthily swap this with a factory method.
	private ClassDescriptor<?> ConstructClassDescriptor(Class<?> lass)
	{
		return ClassDescriptor.getClassDescriptor(lass);
	}
	
	private void testSimpleScalar(Class<?> lass, Class<?> expectedType)
	{
		ClassDescriptor<?> cd = ConstructClassDescriptor(lass);
		
		// Get the one field that is in 	the simple scalar class
		assertEquals(1, cd.allFieldDescriptors().size());	
		FieldDescriptor fd = cd.allFieldDescriptors().get(0);
		assertEquals(lass.getSimpleName().toLowerCase(), fd.getName());
		assertEquals(FieldType.SCALAR, fd.getType());
		// TODO: BUG. //assertEquals(1, fd.getMetaInformation().size());
		assertEquals(expectedType, fd.getScalarType().getClass());
		
		// TODO: Roundtrip the class descriptor. 
		
	}
	
	// TODO: Roundtrip some range of values
	
	@Test
	public void forSimpleBoolean() {
		testSimpleScalar(SimpleBoolean.class, ReferenceBooleanType.class);
	}

	@Test
	public void forSimpleByte() {
		testSimpleScalar(SimpleByte.class, ByteType.class);
	}
	
	@Test
	public void forSimpleChar(){
		testSimpleScalar(SimpleChar.class, CharType.class);
	}
	
	@Test
	public void forSimpleDate(){
		testSimpleScalar(SimpleDate.class, DateType.class);
	}
	
	@Test
	public void forSimpleDouble(){
		testSimpleScalar(SimpleDouble.class, ReferenceDoubleType.class);
	}
	
	@Test
	public void forSimpleFloat(){
		testSimpleScalar(SimpleFloat.class, ReferenceFloatType.class);
	}
	
	@Test
	public void forSimpleInteger()
	{
		testSimpleScalar(SimpleInteger.class, ReferenceIntegerType.class);
	}
	
	@Test
	public void forSimpleJavaURL(){
		testSimpleScalar(SimpleJavaURL.class, URLType.class);
	}
	
	@Test
	public void forSimpleLong(){
		testSimpleScalar(SimpleLong.class, ReferenceLongType.class);
	}
	
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
		testSimpleScalar(Simpleprimint.class, IntType.class);
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
	
	// TODO.
	@Test
	public void forSimpleShort()
	{
		testSimpleScalar(SimpleShort.class, null);
	}
	
	@Test
	public void forSimpleString()
	{
		testSimpleScalar(SimpleString.class, StringType.class);
	}
	
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
	
}
