package simpl.tutorials.java;

import static org.junit.Assert.*;

import org.junit.Test;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.formatenums.Format;
import ecologylab.serialization.formatenums.StringFormat;

public class serializeBookDescriptor {

	@Test
	public void serializeABook() throws SIMPLTranslationException {
	
		Book abook = new Book();
		// Here's an instance of our type to serialize. 
		
		abook.setAuthorName("Michael Feathers");
		abook.setBookId(1337);
		abook.setTitle("Working Effectively with Legacy Code");
		// S.IM.PL serialized objects don't have to be JavaBeans;
		// They can just be POJO's too! 
		
		// Serialize to JSON
		String jsonResult = SimplTypesScope.serialize(abook, StringFormat.JSON).toString();
		System.out.println(jsonResult);	 // Print out the result

		// Serialize to XML
		// (Just change the StringFormat parameter!) 
		String xmlResult = SimplTypesScope.serialize(abook, StringFormat.XML).toString();
		System.out.println(xmlResult);	 // Print out the result
	}
	
	@Test
	public void deserializeABook() throws SIMPLTranslationException
	{
		// S.IM.PL Deserialization is guided by the information contained
		// within the "Simpl Types Scope" 
		// To deserialize, we need to either
		// 1: Load a STS from a file
		// 2: Construct our own STS. 
		// (For "Target Languages" which don't have vivid type information
		// Python, etc... 1 is your only option) 
		
		// A STS should contain all of the classes we expect to encounter
		// Here, we're just expecting books! 
		
		SimplTypesScope book_example_sts = SimplTypesScope.get("book_example", Book.class);
		// This STS is called "book_example"; when you have multiple named scopes,
		// you can use those scopes for polymorphic type support...
		// But more on that later.
		
		
		String jsonBook = "{\"book\":{\"title\":\"Working Effectively with Legacy Code\",\"author_name\":\"Michael Feathers\",\"book_id\":\"1337\"}}";
		
		Object result = book_example_sts.deserialize(jsonBook, new TranslationContext(), StringFormat.JSON);
		
		// We get back a book
		assertTrue(result instanceof Book);
		Book book_from_json = (Book)result;
		
		// Validate that our book is what we expected...
		assertEquals("Michael Feathers", book_from_json.getAuthorName());
		assertEquals("Working Effectively with Legacy Code", book_from_json.getTitle());
		assertTrue(1337 == book_from_json.getBookId());
		// (It should be! S.IM.PL should just simply work!) 
		
		// Same process applies for XML or other formats...
		String xmlBook = "<book title=\"Working Effectively with Legacy Code\" author_name=\"Michael Feathers\" book_id=\"1337\"/>";
		
		Object xml_result = book_example_sts.deserialize(xmlBook, new TranslationContext(), StringFormat.XML);
		
		// We get back a book
		assertTrue(xml_result instanceof Book);
		Book book_from_xml = (Book)result;
		
		// Validate that our book is what we expected...
		assertEquals("Michael Feathers", book_from_xml.getAuthorName());
		assertEquals("Working Effectively with Legacy Code", book_from_xml.getTitle());
		assertTrue(1337 == book_from_xml.getBookId());
		// (It should be! S.IM.PL should just simply work!) 

		
		
		
		
		
	}
	
	
}
