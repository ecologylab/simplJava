/**
 * 
 */
package ecologylab.semantics.compiler;

import java.io.File;
import java.util.ArrayList;

import ecologylab.semantics.metametadata.MetaMetadata;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.semantics.metametadata.MetaMetadataTranslationScope;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;

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
			ElementState	result = mmTS.deserialize(file);
			
			result.serialize(System.out);
			System.out.println();
		}
		catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
