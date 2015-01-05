package simpl.serialization;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.Test;


import legacy.tests.DualBufferOutputStream;
import legacy.tests.maps.ClassDes;
import legacy.tests.maps.FieldDes;
import legacy.tests.maps.TranslationS;
import ecologylab.generic.ReflectionTools;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;

import static org.junit.Assert.*;

public class SimplBibtexDeSerializationTest {

	@Test
	public void bibtexDeSerializationTest() throws SIMPLTranslationException{
		 
		//NOTE: Bibtex is not implemented yet in SimplPullDeserializer, so this test is not useful, or complete, yet.
		
		Author a1 = new Author("Author One", "City");
		Author a2 = new Author("Author Two", "Place");
		
		ArrayList<Author> authors = new ArrayList<Author>();
		authors.add(a1);
		authors.add(a2);
		
		String title = "Article being serialized";
		
		Article article = new Article(title, authors);
		
		Field[] articleFields = Article.class.getFields();
		
		SimplTypesScope tScope = SimplTypesScope.get("bibtexTestScope", Article.class, Author.class);
		
		//BIBTEX===
		DualBufferOutputStream bibtexOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(article, bibtexOStream, Format.BIBTEX);
		
		String bibtexResult = bibtexOStream.toString();
		
		assertEquals(bibtexResult, "@article{title={Article being serialized},authors={Author One,Author Two}}");
		
		InputStream bibtexIStream = new ByteArrayInputStream(bibtexOStream.toByte());
		
		Object bibtexObject = tScope.deserialize(bibtexIStream, Format.BIBTEX);
		
		//assertTrue(bibtexObject instanceof Article);
		
		//Article bibtexArticle = (Article) bibtexObject;
		
		//bibtexArticle.simplEquals()yadaa yadaa
		
		//for(Field i: articleFields){
		//	assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(bibtexArticle, i), ReflectionTools.getFieldValue(article, i));
			
		//}
		
		//Need assert Statements
		
		//===
			
	}
}
