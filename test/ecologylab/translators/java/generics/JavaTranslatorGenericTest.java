package ecologylab.translators.java.generics;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import org.junit.Test;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.GenericTypeVar;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.translators.CodeTranslatorConfig;
import ecologylab.translators.java.JavaTranslationException;
import ecologylab.translators.java.JavaTranslator;

/**
 * Test generating generics declarations from SIMPL type representations. SIMPL should be able to
 * read generics information from existing classes defined in ecologylab.translators.java.generics
 * in simplTranslators/test/, and generate the same declarations into testJavaTranslator/src/.
 * 
 * You should have the testJavaTranslator project in your development environment, and see if it
 * compiles and has the same generics structure as the sources from simplTranslators/test/.
 * 
 * TODO Automate testing for the generated code.
 * 
 * @author quyin
 */
public class JavaTranslatorGenericTest
{
	
//	public static class My<T extends Number>
//	{
//		@simpl_scalar
//		public T v;
//
//		@simpl_scalar
//		public int n;
//
//		@simpl_scalar
//		public Integer o;
//
//		public List<My> l;
//	}

  /**
   * Test generating basic generics usage: define a generic type var in a base class (Search), and
   * bound it with a specific type in a subclass (SocialSearch).
   * 
   * @throws JavaTranslationException
   * @throws IOException
   * @throws SIMPLTranslationException
   * @throws SecurityException
   * @throws NoSuchFieldException
   */
	@SuppressWarnings("unused")
	@Test
  public void testBasicGenerics() throws JavaTranslationException, IOException,
      SIMPLTranslationException, SecurityException, NoSuchFieldException
	{
		
//		SimplTypesScope s = SimplTypesScope.get("test", My.class);
//		Field f = My.class.getField("v");
//		Type t1 = f.getGenericType();
//		System.out.println(f.getGenericType());
//		f = My.class.getField("n");
//		Type t2 = f.getGenericType();
//		System.out.println(f.getGenericType());
//		ClassDescriptor cd = s.getClassDescriptorBySimpleName("My");
//		FieldDescriptor fd = cd.getFieldDescriptorByFieldName("v");
		
	  
    SimplTypesScope scope = SimplTypesScope.get("test-basic-generics",
                                                SearchResult.class,
                                                Search.class,
                                                SocialSearchResult.class,
                                                SocialSearch.class);

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
		
		cdSearch = scope.getClassDescriptorBySimpleName("SocialSearch");
		classGenericTypeVars = cdSearch.getGenericTypeVars();
		for (GenericTypeVar genericTypeVar : classGenericTypeVars)
		{
			System.out.println(genericTypeVar.toString());
		}
		
		fdSearchResults = cdSearch.getFieldDescriptorByFieldName("searchResults");
		fieldGenericTypeVars = fdSearchResults.getGenericTypeVars();
		for (GenericTypeVar genericTypeVar : fieldGenericTypeVars)
		{
			System.out.println(genericTypeVar.toString());
		}
		
		
		CodeTranslatorConfig ctc = new CodeTranslatorConfig();
		JavaTranslator jt = new JavaTranslator();
		jt.translate(new File("../testJavaTranslator/src/"), scope, ctc);
	}
	
  /**
   * Test generating a more advanced usage of generics: define a generic type var in a base class
   * (Search), and narrow its bound in a subclass (TypedSocialSearch).
   * 
   * @throws JavaTranslationException
   * @throws IOException
   * @throws SIMPLTranslationException
   */
	@Test
  public void testAdvGenerics1() throws JavaTranslationException, IOException,
      SIMPLTranslationException
	{
    SimplTypesScope scope = SimplTypesScope.get("test-adv-generics-1",
                                                SearchResult.class,
                                                Search.class,
                                                SocialSearchResult.class,
                                                SocialSearch.class,
                                                TypedSocialSearch.class);
		
		CodeTranslatorConfig ctc = new CodeTranslatorConfig();
		JavaTranslator jt = new JavaTranslator();
		jt.translate(new File("../testJavaTranslator/src/"), scope, ctc);
	}
	
}
