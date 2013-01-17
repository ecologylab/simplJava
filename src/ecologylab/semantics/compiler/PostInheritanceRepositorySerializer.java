package ecologylab.semantics.compiler;

import java.io.File;

import ecologylab.semantics.collecting.SemanticsSessionScope;
import ecologylab.semantics.generated.library.RepositoryMetadataTranslationScope;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.SimplTypesScope.GRAPH_SWITCH;
import ecologylab.serialization.formatenums.Format;

/**
 * Serialize post-inheritance meta-metadata repository into a single file, for use in languages
 * other than Java.
 * 
 * @author quyin
 *
 */
public class PostInheritanceRepositorySerializer
{

  /**
   * 
   * @param serializedRepoFile
   * @param format
   * @throws SIMPLTranslationException
   */
  public void serializeRepository(File serializedRepoFile, Format format)
      throws SIMPLTranslationException
  {
    SimplTypesScope.graphSwitch = GRAPH_SWITCH.ON;

    SemanticsSessionScope scope = new SemanticsSessionScope(RepositoryMetadataTranslationScope.get(),
                                                            null);
    MetaMetadataRepository repo = scope.getMetaMetadataRepository();
    SimplTypesScope.serialize(repo, serializedRepoFile, format);
  }

  /**
   * @param args
   * @throws SIMPLTranslationException 
   */
  public static void main(String[] args) throws SIMPLTranslationException
  {
    if (args.length != 2)
    {
      System.err.println("args: <serialized-repository-file-path> <format>");
      System.err.println("    <format> can be xml or json");
      System.exit(-1);
    }
    
    File serializedRepoFile = new File(args[0]);
    String fmtName = args[1].toLowerCase();
    Format fmt = null;
    if ("xml".equals(fmtName))
      fmt = Format.XML;
    else if ("json".equals(fmtName))
      fmt = Format.JSON;
    else
    {
      System.err.println("only xml and json formats are supported.");
      System.exit(-2);
    }
    
    PostInheritanceRepositorySerializer pirs = new PostInheritanceRepositorySerializer();
    pirs.serializeRepository(serializedRepoFile, fmt);
  }

}
