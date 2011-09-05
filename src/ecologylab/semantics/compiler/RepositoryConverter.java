package ecologylab.semantics.compiler;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;

import ecologylab.semantics.metametadata.MetaMetadataCollectionFieldWithoutChildComposite;
import ecologylab.semantics.metametadata.MetaMetadataCompositeField;
import ecologylab.semantics.metametadata.MetaMetadataField;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.semantics.metametadata.MetaMetadataScalarField;
import ecologylab.semantics.metametadata.MetaMetadataTranslationScope;
import ecologylab.semantics.metametadata.NestedMetaMetadataFieldTranslationScope;
import ecologylab.serialization.ElementState.FORMAT;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;

/**
 * A (preliminary) class for converting repository format. Currently, from XML to JSON.
 * 
 * @author quyin
 *
 */
public class RepositoryConverter
{

	private static File				srcXmlRepoDir		= new File("../ecologylabSemantics/repository/");

	private static File				destJsonRepoDir	= new File("../ecologylabSemantics/repositoryInJSON");

	private TranslationScope	mmdTScope				= null;

	public void convertingRepositoryFromXmlToJson()
	{
		// replace MetaMetadataCollectionField with MetaMetadataCollectionFieldChildComposite
		TranslationScope.get(NestedMetaMetadataFieldTranslationScope.NAME, new Class[] {
				MetaMetadataField.class,
				MetaMetadataScalarField.class,
				MetaMetadataCompositeField.class,
				MetaMetadataCollectionFieldWithoutChildComposite.class,
		});
		mmdTScope = MetaMetadataTranslationScope.get();

		// convert repository to json
		translateRepositoryIntoJSON(srcXmlRepoDir, destJsonRepoDir);
	}

	private void translateRepositoryIntoJSON(File srcDir, File destDir)
	{
		translateRepositoryDirIntoJSON(srcDir, destDir);
		translateRepositoryDirIntoJSON(new File(srcDir, "repositorySources"), new File(destDir, "repositorySources"));
		translateRepositoryDirIntoJSON(new File(srcDir, "powerUser"), new File(destDir, "powerUser"));
	}

	private void translateRepositoryDirIntoJSON(File srcDir, File destDir)
	{
		if (!destDir.exists())
			destDir.mkdir();
		FileFilter filter = new FileFilter()
		{
			@Override
			public boolean accept(File pathname)
			{
				return pathname.getName().endsWith("xml");
			}
		};
		for (File f : srcDir.listFiles(filter))
		{
			try
			{
				MetaMetadataRepository repo = (MetaMetadataRepository) mmdTScope.deserialize(f);
				String json = repo.serialize(FORMAT.JSON).toString();
				if (json != null && json.length() > 0)
				{
					File jsonRepoFile = new File(destDir, f.getName().replace((CharSequence) ".xml", (CharSequence) ".json"));
					FileWriter writer = new FileWriter(jsonRepoFile);
					writer.write(json);
					writer.close();
				}
			}
			catch (SIMPLTranslationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args)
	{
		RepositoryConverter rc = new RepositoryConverter();
		rc.convertingRepositoryFromXmlToJson();
	}

}
