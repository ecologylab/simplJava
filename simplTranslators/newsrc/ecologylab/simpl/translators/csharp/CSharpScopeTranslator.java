package ecologylab.simpl.translators.csharp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ecologylab.generic.Debug;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.simpl.translators.DependencyTracker;
import ecologylab.simpl.translators.DefaultDependencyTracker;
import ecologylab.simpl.translators.EventDispatcher;
import ecologylab.translators.CodeTranslatorConfig;
import ecologylab.translators.net.DotNetTranslationException;

/**
 * 
 * @author quyin
 * 
 */
public class CSharpScopeTranslator extends Debug implements
    EventDispatcher<ClassTranslatedEventHandler>
{

  private Set<String>                         excludedClassNames = new HashSet<String>();

  private CSharpNameTypeResolver              nameTypeResolver;

  private CSharpCommentTranslator             commentTranslator;

  private CSharpMetaInformationTranslator     metaInfoTranslator;

  private CSharpGenericTypeVariableTranslator gtvTranslator;

  private CSharpLanguageSpecifics             langSpec;

  private CSharpFieldTranslator               fieldTranslator;

  private CSharpClassTranslator               classTranslator;

  private List<ClassTranslatedEventHandler>   classTranslatedEventHandlers;

  public CSharpScopeTranslator()
  {
    super();

    nameTypeResolver = new CSharpNameTypeResolver();
    commentTranslator = new CSharpCommentTranslator();
    metaInfoTranslator = new CSharpMetaInformationTranslator();
    gtvTranslator = new CSharpGenericTypeVariableTranslator();
    langSpec = new CSharpLanguageSpecifics();
    
    fieldTranslator = new CSharpFieldTranslator(nameTypeResolver,
                                                langSpec,
                                                commentTranslator,
                                                metaInfoTranslator,
                                                gtvTranslator);
    
    classTranslator = new CSharpClassTranslator(langSpec,
                                                nameTypeResolver,
                                                fieldTranslator,
                                                metaInfoTranslator,
                                                gtvTranslator,
                                                commentTranslator);
  }

  /**
   * 
   * @param typeScope
   * @param config
   * @return
   * @throws IOException
   * @throws SIMPLTranslationException
   * @throws DotNetTranslationException
   */
  public void translate(SimplTypesScope typeScope, CodeTranslatorConfig config)
      throws IOException, SIMPLTranslationException, DotNetTranslationException
  {
    debug("Generating C# classes ...");

    Collection<ClassDescriptor<? extends FieldDescriptor>> classes =
        typeScope.entriesByClassName().values();
    for (ClassDescriptor classDescriptor : classes)
    {
      String name = classDescriptor.getName();
      if (excludedClassNames.contains(name))
      {
        debug("Excluding " + classDescriptor + "from translation as requested.");
        continue;
      }
      DependencyTracker dependencyTracker = new DefaultDependencyTracker();
      classTranslator.setDependencyTracker(dependencyTracker);
      String classDef = classTranslator.translate(classDescriptor);
      if (classDef != null && classDef.length() > 0)
      {
        String classNamespace = nameTypeResolver.resolveClassNamespace(classDescriptor);
        String classSimpleName = nameTypeResolver.resolveClassSimpleName(classDescriptor);
        dispatchEvent(classNamespace,
                      classSimpleName,
                      classDef,
                      dependencyTracker.getDependencies());
      }
    }

    debug("DONE !");
  }

  /**
   * @param classDescriptor
   */
  public void excludeClassFromTranslation(ClassDescriptor classDescriptor)
  {
    if (classDescriptor != null)
      excludeClassFromTranslation(classDescriptor.getName());
  }

  /**
   * @param className
   */
  public void excludeClassFromTranslation(String className)
  {
    if (className != null && className.length() > 0)
      excludedClassNames.add(className);
  }

  @Override
  public void addEventHandler(ClassTranslatedEventHandler eventHandler)
  {
    if (classTranslatedEventHandlers == null)
      classTranslatedEventHandlers = new ArrayList<ClassTranslatedEventHandler>();
    classTranslatedEventHandlers.add(eventHandler);
  }

  @Override
  public void removeEventHandler(ClassTranslatedEventHandler eventHandler)
  {
    if (classTranslatedEventHandlers != null)
      classTranslatedEventHandlers.remove(eventHandler);
  }

  @Override
  public Collection<ClassTranslatedEventHandler> getEventHandlers()
  {
    return classTranslatedEventHandlers;
  }

  @Override
  public void dispatchEvent(Object... args)
  {
    if (classTranslatedEventHandlers != null)
      if (args != null && args.length == 4)
      {
        String classNamespace = (String) args[0];
        String classSimpleName = (String) args[1];
        String classDef = (String) args[2];
        Set<String> dependencies = null;
        if (args[3] instanceof Set<?>)
          dependencies = (Set<String>) args[3];

        for (ClassTranslatedEventHandler eventHandler : classTranslatedEventHandlers)
          eventHandler.classTranslated(classNamespace,
                                       classSimpleName,
                                       classDef,
                                       dependencies);
      }
  }

}
