package ecologylab.simpl.translators.csharp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.generic.Debug;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.simpl.translators.DefaultDependencyTracker;
import ecologylab.translators.CodeTranslatorConfig;
import ecologylab.translators.net.DotNetTranslationException;

/**
 * 
 * @author quyin
 * 
 */
public class CSharpTranslator extends Debug
{

  private CSharpScopeTranslator       scopeTranslator;

  private CSharpNameTypeResolver      nameTypeResolver;

  private CSharpTypeLibraryTranslator typeLibTranslator;

  private CSharpDependencyTranslator  dependencyTranslator;

  /**
   * 
   */
  public CSharpTranslator()
  {
    scopeTranslator = new CSharpScopeTranslator();
    nameTypeResolver = new CSharpNameTypeResolver();
    typeLibTranslator = new CSharpTypeLibraryTranslator();
    dependencyTranslator = new CSharpDependencyTranslator();

    typeLibTranslator.excludedClassNames = scopeTranslator.excludedClassNames;
    typeLibTranslator.nameTypeResolver = nameTypeResolver;
  }

  /**
   * 
   * @param typeScope
   * @param parentDir
   * @param config
   * @throws IOException
   * @throws DotNetTranslationException
   * @throws SIMPLTranslationException
   */
  public void translate(SimplTypesScope typeScope,
                        final File parentDir,
                        CodeTranslatorConfig config)
      throws IOException, DotNetTranslationException, SIMPLTranslationException
  {
    ClassTranslatedEventHandler eventHandler = new ClassTranslatedEventHandler()
    {
      @Override
      public void classTranslated(String classNamespace,
                                  String classSimpleName,
                                  String classDef,
                                  Set<String> dependencies)
      {
        writeTranslatedClass(parentDir, classNamespace, classSimpleName, classDef, dependencies);
      }
    };
    scopeTranslator.addEventHandler(eventHandler);
    scopeTranslator.translate(typeScope, config);
    scopeTranslator.removeEventHandler(eventHandler);

    String typeLibClassDef =
        typeLibTranslator.generateTypeLibraryClass(typeScope,
                                                   typeScope.getName(),
                                                   config.getLibraryTScopeClassPackage(),
                                                   config.getLibraryTScopeClassSimpleName(),
                                                   parentDir);
    writeTranslatedClass(parentDir,
                         config.getLibraryTScopeClassPackage(),
                         config.getLibraryTScopeClassSimpleName(),
                         typeLibClassDef,
                         null);
  }

  /**
   * 
   * @param parentDir
   * @param classNamespace
   * @param classSimpleName
   * @param classDef
   * @param dependencies
   */
  protected void writeTranslatedClass(final File parentDir,
                                      String classNamespace,
                                      String classSimpleName,
                                      String classDef,
                                      Set<String> dependencies)
  {
    File outputFile = null;
    try
    {
      outputFile = createFileWithDirStructure(parentDir,
                                              classNamespace.split("\\."),
                                              classSimpleName,
                                              ".cs");
    }
    catch (IOException e)
    {
      error("Cannot create file for " + classNamespace + "." + classSimpleName);
      e.printStackTrace();
    }

    BufferedWriter bufferedWriter;
    try
    {
      bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
      bufferedWriter.append(dependencyTranslator.translateDependencies(dependencies));
      bufferedWriter.append(classDef);
      bufferedWriter.close();
    }
    catch (IOException e)
    {
      error("Cannot write to file " + outputFile);
      e.printStackTrace();
    }
  }

  /**
   * 
   * @param parentDir
   * @param dirStructure
   * @param name
   * @param extension
   * @return
   * @throws IOException
   */
  protected File createFileWithDirStructure(File parentDir,
                                            String[] dirStructure,
                                            String name,
                                            String extension)
      throws IOException
  {
    File dir = parentDir;
    for (String directoryName : dirStructure)
    {
      dir = new File(dir, directoryName);
    }
    PropertiesAndDirectories.createDirsAsNeeded(dir);

    File file = new File(dir, name + extension);
    if (file.exists())
      file.delete();
    file.createNewFile();

    return file;
  }

}
