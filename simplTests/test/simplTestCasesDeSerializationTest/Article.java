package simplTestCasesDeSerializationTest;


import java.util.ArrayList;
import java.util.List;


import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.Format;

public class Article
{
	@simpl_scalar
	String title;
	
	@simpl_collection("author")
	List<Author> authors;

	
	public Article(String t, List<Author> a)
	{
		title = t;
		authors = a;
	}
	
	public Article()
	{
		
	}

}
