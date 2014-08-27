package simpl.serialization;

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

public class SimplMonomorphicCollectionDeSerializationTest {

	@Test
	public void monomorphicCollectionDeSerializationTest() throws SIMPLTranslationException{
		
		StudentDirectory sd = new StudentDirectory();
		sd.initializeDirectory();
		
		Field[] sdFields = StudentDirectory.class.getFields();
		
		SimplTypesScope translationScope = SimplTypesScope.get("studentDirectoryTScope", Person.class,
				Faculty.class, Student.class, StudentDirectory.class);
		
		
		//JSON===
		DualBufferOutputStream jsonOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(sd, jsonOStream, Format.JSON);
		
		String jsonResult = jsonOStream.toString();
		assertEquals(jsonResult, "{\"student_directory\":{\"student\":[{\"name\":\"nabeel\",\"stu_num\":\"234342\"},{\"name\":\"yin\",\"stu_num\":\"423423\"},{\"name\":\"bill\",\"stu_num\":\"4234234\"},{\"name\":\"sashi\",\"stu_num\":\"5454\"},{\"name\":\"jon\",\"stu_num\":\"656565\"}],\"test\":\"nabel\"}}");
		
		InputStream jsonIStream = new ByteArrayInputStream(jsonOStream.toByte());
		
		Object jsonObject = translationScope.deserialize(jsonIStream,Format.JSON);
		assertTrue(jsonObject instanceof StudentDirectory);
		StudentDirectory jsonDirectory = (StudentDirectory) jsonObject;
		
		for(Field i:sdFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(jsonDirectory, i), ReflectionTools.getFieldValue(sd, i));
		}
		
		//===
		
		//XML===
		DualBufferOutputStream xmlOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(sd, xmlOStream, Format.XML);
		
		String xmlResult = xmlOStream.toString();
		assertEquals(xmlResult, "<student_directory><student name=\"nabeel\" stu_num=\"234342\"/><student name=\"yin\" stu_num=\"423423\"/><student name=\"bill\" stu_num=\"4234234\"/><student name=\"sashi\" stu_num=\"5454\"/><student name=\"jon\" stu_num=\"656565\"/><test>nabel</test></student_directory>");
		
		InputStream xmlIStream = new ByteArrayInputStream(xmlOStream.toByte());
		
		Object xmlObject = translationScope.deserialize(xmlIStream,  (DeserializationHookStrategy) null, Format.XML);
		assertTrue(xmlObject instanceof StudentDirectory);
		StudentDirectory xmlDirectory = (StudentDirectory) xmlObject;
		
		for(Field i:sdFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(xmlDirectory, i), ReflectionTools.getFieldValue(sd, i));
		}
		
		//===
	}
}
