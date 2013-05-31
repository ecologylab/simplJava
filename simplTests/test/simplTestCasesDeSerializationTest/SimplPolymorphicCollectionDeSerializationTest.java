package simplTestCasesDeSerializationTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import legacy.tests.DualBufferOutputStream;
import legacy.tests.inheritance.BaseClass;
import legacy.tests.inheritance.ContainingClass;
import legacy.tests.person.Faculty;
import legacy.tests.person.Person;
import legacy.tests.person.PersonDirectory;
import legacy.tests.person.Student;
import legacy.tests.person.StudentDirectory;

import org.junit.Test;

import ecologylab.generic.ReflectionTools;
import ecologylab.serialization.DeserializationHookStrategy;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;

public class SimplPolymorphicCollectionDeSerializationTest {

	@Test
	public void polymorphicCollectionDeSerializationTest()  throws SIMPLTranslationException{
		
		PersonDirectory pd = new PersonDirectory();
		pd.initializeDirectory();
		
		SimplTypesScope translationScope = SimplTypesScope.get("personDirectoryTScope", Person.class,Faculty.class, Student.class, PersonDirectory.class);
		
		Field[] pdFields = PersonDirectory.class.getFields();
		
		
		//JSON===
		DualBufferOutputStream jsonOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(pd, jsonOStream, Format.JSON);
		
		String jsonResult = jsonOStream.toString();
		assertEquals(jsonResult, "{\"person_directory\":{\"persons\":[{\"student\":{\"name\":\"nabeel\",\"stu_num\":\"234342\"}},{\"student\":{\"name\":\"yin\",\"stu_num\":\"423423\"}},{\"faculty\":{\"name\":\"andruid\",\"designation\":\"prof\"}},{\"student\":{\"name\":\"bill\",\"stu_num\":\"4234234\"}},{\"student\":{\"name\":\"sashi\",\"stu_num\":\"5454\"}},{\"student\":{\"name\":\"jon\",\"stu_num\":\"656565\"}}]}}");
		
		InputStream jsonIStream = new ByteArrayInputStream(jsonOStream.toByte());
		
		Object jsonObject = translationScope.deserialize(jsonIStream,Format.JSON);
		assertTrue(jsonObject instanceof PersonDirectory);
		PersonDirectory jsonPD = (PersonDirectory) jsonObject;
		
		for(Field i:pdFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(jsonPD, i), ReflectionTools.getFieldValue(pd, i));
		}
		//===
		
		//XML===
		DualBufferOutputStream xmlOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(pd, xmlOStream, Format.XML);
		
		String xmlResult = xmlOStream.toString();
		assertEquals(xmlResult, "<person_directory><persons><student name=\"nabeel\" stu_num=\"234342\"/><student name=\"yin\" stu_num=\"423423\"/><faculty name=\"andruid\" designation=\"prof\"/><student name=\"bill\" stu_num=\"4234234\"/><student name=\"sashi\" stu_num=\"5454\"/><student name=\"jon\" stu_num=\"656565\"/></persons></person_directory>");
		
		InputStream xmlIStream = new ByteArrayInputStream(xmlOStream.toByte());
		
		Object xmlObject = translationScope.deserialize(xmlIStream,Format.XML);
		assertTrue(xmlObject instanceof PersonDirectory);
		PersonDirectory xmlPD = (PersonDirectory) xmlObject;
		
		for(Field i:pdFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(xmlPD, i), ReflectionTools.getFieldValue(pd, i));
		}
		//===
	}
}
