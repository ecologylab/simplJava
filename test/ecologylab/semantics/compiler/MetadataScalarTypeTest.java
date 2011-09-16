/**
 * 
 */
package ecologylab.semantics.compiler;

import java.io.File;
import java.util.ArrayList;

import ecologylab.semantics.metametadata.MetaMetadata;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.semantics.metametadata.MetaMetadataTranslationScope;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Format;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.StringFormat;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.annotations.simpl_collection;

/**
 * @author andruid
 *
 */
public class MetadataScalarTypeTest extends ElementState
{
	@simpl_collection
	ArrayList<MetaMetadata>			metaMetadatas;
	/**
	 * 
	 */
	public MetadataScalarTypeTest()
	{
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] a)
	{
		MetaMetadataRepository.initializeTypes();
		
		TranslationScope mmTS	= MetaMetadataTranslationScope.get();
		
		File file	= new File("data/deserialization/simplMM.xml");
		System.out.println(file.getAbsolutePath());
		
		try
		{
			ElementState	result = (ElementState) mmTS.deserialize(file, Format.XML);
			ClassDescriptor.serialize(result, System.out, StringFormat.XML);
			
			System.out.println();
		}
		catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
