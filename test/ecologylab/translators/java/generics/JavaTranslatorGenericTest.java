package ecologylab.translators.java.generics;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.GenericTypeVar;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.translators.CodeTranslatorConfig;
import ecologylab.translators.java.JavaTranslationException;
import ecologylab.translators.java.JavaTranslator;

public class JavaTranslatorGenericTest
{

	@Test
	public void testBasicGenerics() throws JavaTranslationException, IOException, SIMPLTranslationException
	{
		SimplTypesScope scope = SimplTypesScope.get("test-basic-generics", SearchResult.class, Search.class, SocialSearchResult.class, SocialSearch.class);
		
		ClassDescriptor cdSearch = scope.getClassDescriptorBySimpleName("Search");
		List<GenericTypeVar> classGenericTypeVars = cdSearch.getGenericTypeVars();
		for (GenericTypeVar genericTypeVar : classGenericTypeVars)
		{
			System.out.println(genericTypeVar.toString());
		}
		
		FieldDescriptor fdSearchResults = cdSearch.getFieldDescriptorByFieldName("searchResults");
		List<GenericTypeVar> fieldGenericTypeVars = fdSearchResults.getGenericTypeVars();
		for (GenericTypeVar genericTypeVar : fieldGenericTypeVars)
		{
			System.out.println(genericTypeVar.toString());
		}
		
		CodeTranslatorConfig ctc = new CodeTranslatorConfig();
		JavaTranslator jt = new JavaTranslator();
		jt.translate(new File("../testJavaTranslator/src/"), scope, ctc);
	}
	
}
