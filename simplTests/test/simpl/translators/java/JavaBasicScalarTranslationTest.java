package simpl.translators.java;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.tutorials.java.Book;

import ecologylab.serialization.ClassDescriptor;

public class JavaBasicScalarTranslationTest {

	@Test
	public void testJavaBasicScalar() {
		// Get the class descriptor for our test class:
		ClassDescriptor bookDescriptor = ClassDescriptor.getClassDescriptor(Book.class);
		
		// Generate the source code
		
		JavaCompilationUnitTranslator translator = new JavaCompilationUnitTranslator();
		
		String result = translator.translateClass(bookDescriptor);

		// This doesn't really validate anything; 
		// But, it does show you what the output would be! 
		System.out.println(result);
	}

}
