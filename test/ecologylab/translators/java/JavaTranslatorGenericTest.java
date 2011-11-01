package ecologylab.translators.java;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.GenericTypeVar;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.translators.CodeTranslatorConfig;

public class JavaTranslatorGenericTest
{

	@simpl_inherit
	public class SearchResult extends ElementState
	{
		
		@simpl_scalar
		public String title;
		
	}
	
	@simpl_inherit
	public class Search<T extends SearchResult> extends ElementState
	{
		
		@simpl_scalar
		public String query;
		
		@simpl_collection("search_result")
		public List<T> searchResults;
		
	}
	
	@Test
	public void testBasicGenerics() throws JavaTranslationException, IOException
	{
		SimplTypesScope sts = SimplTypesScope.get("test-basic-generics", SearchResult.class, Search.class);
		
		ClassDescriptor cdSearch = sts.getClassDescriptorBySimpleName("Search");
		List<GenericTypeVar> lgtv = cdSearch.getGenericTypeVars();
		for (GenericTypeVar gtv : lgtv)
		{
			System.out.println(gtv.toString());
		}
		
		FieldDescriptor fdSearchResults = cdSearch.getFieldDescriptorByFieldName("searchResults");
		// fdSearchResults.getGenericTypeVars();
		
		CodeTranslatorConfig ctc = new CodeTranslatorConfig();
		JavaTranslator jt = new JavaTranslator();
		jt.translate(cdSearch, new File("data/testGenerics/basics"), ctc);
	}
	
}
