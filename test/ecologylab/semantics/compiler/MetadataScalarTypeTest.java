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
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.formatenums.Format;
import ecologylab.serialization.formatenums.StringFormat;

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
		
		SimplTypesScope mmTS	= MetaMetadataTranslationScope.get();
		
		File file	= new File("data/deserialization/simplMM.xml");
		System.out.println(file.getAbsolutePath());
		
		try
		{
			ElementState	result = (ElementState) mmTS.deserialize(file, Format.XML);
			SimplTypesScope.serialize(result, System.out, StringFormat.XML);
			
			System.out.println();
		}
		catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
