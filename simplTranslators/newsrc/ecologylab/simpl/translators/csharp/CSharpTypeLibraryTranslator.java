package ecologylab.simpl.translators.csharp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import ecologylab.semantics.html.utils.StringBuilderUtils;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.simpl.translators.DependencyTracker;
import ecologylab.translators.net.DotNetTranslationConstants;

public class CSharpTypeLibraryTranslator implements DotNetTranslationConstants
{

  Set<String>            excludedClassNames;

  DependencyTracker      dependencyTracker;

  CSharpNameTypeResolver nameTypeResolver;

  /**
   * 
   * @param typeScope
   * @param libraryTypeScopeName
   * @param libraryNamespace
   * @param libraryClassName
   * @param parentDir
   * @throws IOException
   */
  public String generateTypeLibraryClass(SimplTypesScope typeScope,
                                         String libraryTypeScopeName,
                                         String libraryNamespace,
                                         String libraryClassName,
                                         File parentDir) throws IOException
  {
    StringBuilder sb = StringBuilderUtils.acquire();

    // append notice information
    sb.append("\n");
    sb.append("// Developer should proof-read this TranslationScope before using it "
        + "for production.\n");
    sb.append("\n");

    // header
    sb.append(NAMESPACE);
    sb.append(SPACE);
    sb.append(libraryNamespace);
    sb.append(SPACE);
    sb.append(SINGLE_LINE_BREAK);
    sb.append(OPENING_CURLY_BRACE);
    sb.append(SINGLE_LINE_BREAK);

    // class: header
    sb.append("  ");
    sb.append(PUBLIC);
    sb.append(SPACE);
    sb.append(CLASS);
    sb.append(SPACE);
    sb.append(libraryClassName);
    sb.append(SINGLE_LINE_BREAK);
    sb.append("  ");
    sb.append(OPENING_CURLY_BRACE);
    sb.append(SINGLE_LINE_BREAK);

    // class: constructor
    appendDefaultConstructor(libraryClassName, sb);

    // class: the Get() method
    generateLibraryTScopeGetter(sb, typeScope, libraryTypeScopeName);

    sb.append("\n  }\n");
    sb.append("\n}\n");

    String result = sb.toString();
    StringBuilderUtils.release(sb);
    return result;
  }

  protected void generateLibraryTScopeGetter(Appendable appendable,
                                             SimplTypesScope tScope,
                                             String typeScopeName)
      throws IOException
  {
    appendable.append(SINGLE_LINE_BREAK);
    appendable.append("    ");
    appendable.append(PUBLIC);
    appendable.append(SPACE);
    appendable.append(STATIC);
    appendable.append(SPACE);
    appendable.append(DOTNET_TRANSLATION_SCOPE);
    appendable.append(SPACE);
    appendable.append("Get()\n");
    appendable.append("    ");
    appendable.append(OPENING_CURLY_BRACE);
    appendable.append(SINGLE_LINE_BREAK);

    appendable.append("      ");
    appendable.append(RETURN);
    appendable.append(SPACE);
    appendable.append(DOTNET_TRANSLATION_SCOPE);
    appendable.append(DOT);
    appendable.append(FGET);
    appendable.append(OPENING_BRACE);
    appendable.append("\n        ");
    appendable.append(QUOTE);
    appendable.append(tScope.getName());
    appendable.append(QUOTE);
    appendTranslatedClassList(tScope, appendable);
    appendable.append("\n      ");
    appendable.append(CLOSING_BRACE);
    appendable.append(END_LINE);
    appendable.append(SINGLE_LINE_BREAK);
    appendable.append("    ");
    appendable.append(CLOSING_CURLY_BRACE);
    appendable.append(SINGLE_LINE_BREAK);
  }

  protected void appendTranslatedClassList(SimplTypesScope tScope, Appendable appendable)
      throws IOException
  {
    List<String> lines = new ArrayList<String>();

    Collection<ClassDescriptor<? extends FieldDescriptor>> allClasses =
        tScope.entriesByClassName().values();
    for (ClassDescriptor<? extends FieldDescriptor> oneClass : allClasses)
    {
      if (excludedClassNames.contains(oneClass))
        continue;
      String packageName = oneClass.getDescribedClassPackageName();
      String namespace = nameTypeResolver.javaPackage2CSharpNamespace(packageName);
      lines.add(String.format(",\n        typeof(%s.%s%s)",
                              namespace,
                              oneClass.getDescribedClassSimpleName(),
                              oneClass.isGenericClass() ? "<>" : ""));
    }
    Collections.sort(lines);
    for (String line : lines)
      appendable.append(line);
  }

  protected void appendDefaultConstructor(String className, Appendable appendable)
      throws IOException
  {
    appendable.append("\n");
    appendable.append(String.format("    public %s()\n", className));
    appendable.append("    { }\n");
  }

}
