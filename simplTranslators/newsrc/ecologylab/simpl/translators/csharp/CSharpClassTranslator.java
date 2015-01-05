package ecologylab.simpl.translators.csharp;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ecologylab.generic.Debug;
import ecologylab.generic.HashMapArrayList;
import ecologylab.semantics.html.utils.StringBuilderUtils;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.MetaInformation;
import ecologylab.serialization.types.CollectionType;
import ecologylab.serialization.types.ScalarType;
import ecologylab.simpl.translators.DependencyTracker;
import ecologylab.translators.net.DotNetTranslationException;

/**
 * 
 * @author quyin
 * 
 */
public class CSharpClassTranslator extends Debug
{
  
  private CSharpLanguageSpecifics             langSpec;

  private CSharpNameTypeResolver              nameTypeResolver;

  private CSharpFieldTranslator               fieldTranslator;

  private CSharpMetaInformationTranslator     metaInfoTranslator;

  private CSharpGenericTypeVariableTranslator gtvTranslator;

  private CSharpCommentTranslator             commentTranslator;
  
  private DependencyTracker                   dependencyTracker;

  public CSharpClassTranslator(CSharpLanguageSpecifics langSpec,
                               CSharpNameTypeResolver nameTypeResolver,
                               CSharpFieldTranslator fieldTranslator,
                               CSharpMetaInformationTranslator metaInfoTranslator,
                               CSharpGenericTypeVariableTranslator gtvTranslator,
                               CSharpCommentTranslator commentTranslator)
  {
    super();
    this.langSpec = langSpec;
    this.nameTypeResolver = nameTypeResolver;
    this.fieldTranslator = fieldTranslator;
    this.metaInfoTranslator = metaInfoTranslator;
    this.gtvTranslator = gtvTranslator;
    this.commentTranslator = commentTranslator;
  }

  public DependencyTracker getDependencyTracker()
  {
    return dependencyTracker;
  }

  void setDependencyTracker(DependencyTracker dependencyTracker)
  {
    this.dependencyTracker = dependencyTracker;
  }

  /**
   * The actual method converting a class descriptor into C# codes.
   * 
   * @param inputClass
   * @throws IOException
   * @throws DotNetTranslationException
   */
  public String translate(ClassDescriptor inputClass) throws IOException,
      DotNetTranslationException
  {
    HashMapArrayList<String, ? extends FieldDescriptor> fieldDescriptors =
        inputClass.getDeclaredFieldDescriptorsByFieldName();
    inputClass.resolvePolymorphicAnnotations();

    String namespace = nameTypeResolver.resolveClassNamespace(inputClass);
    String classSimpleName = nameTypeResolver.resolveClassSimpleName(inputClass);

    StringBuilder classDef = StringBuilderUtils.acquire();

    // // file header
    // StringBuilder header = StringBuilderUtils.acquire();
    // appendHeaderComments(inputClass.getDescribedClassSimpleName(),
    // SINGLE_LINE_COMMENT,
    // FILE_EXTENSION,
    // header);

    // namespace
    classDef.append("namespace ").append(namespace).append("\n");
    classDef.append("{\n");
    classDef.append("\n");

    // class
    // class: opening
    classDef.append(translateClassComment(inputClass));
    appendClassMetaInformation(inputClass, classDef);
    appendClassDeclaration(inputClass, classDef);
    // class: fields
    fieldTranslator.setDependencyTracker(dependencyTracker);
    for (FieldDescriptor fieldDescriptor : fieldDescriptors)
    {
      if (fieldDescriptor.belongsTo(inputClass)
          || inputClass.isCloned() && fieldDescriptor.belongsTo(inputClass.getClonedFrom()))
      {
        classDef.append("\n");
        fieldTranslator.translate(inputClass, fieldDescriptor, classDef);
        // FIXME dependencies!!!
      }
    }
    // class: constructor(s)
    appendConstructor(inputClass, classDef, null);
    // class: closing
    classDef.append("\t");
    classDef.append("}");
    classDef.append("\n");

    // unit scope: closing
    classDef.append("}");
    classDef.append("\n");

    // dependencies
    // for (String dependency : deriveDependencies(inputClass))
    // {
    // currentClassDependencies.add(javaPackage2CSharpNamespace(dependency));
    // }
    // appendDependencies(currentClassDependencies, header);
    // currentClassDependencies.clear();

    // output
    String rst = classDef.toString();
    StringBuilderUtils.release(classDef);
    return rst;
  }

  /**
   * opens the c# class
   * 
   * @param inputClass
   * @param appendable
   * @throws IOException
   */
  protected void appendClassDeclaration(ClassDescriptor inputClass, Appendable appendable)
      throws IOException
  {
    String classSimpleName = inputClass.getDescribedClassSimpleName();
    appendable.append(String.format("  public class %s", classSimpleName));
    appendClassGenericTypeVariables(appendable, inputClass);

    ClassDescriptor superCD = inputClass.getSuperClass();
    if (superCD != null)
    {
      appendable.append(" ");
      appendable.append(":");
      appendable.append(" ");
      appendable.append(superCD.getDescribedClassSimpleName());
      appendSuperClassGenericTypeVariables(appendable, inputClass);
      String superClassDependency =
          nameTypeResolver.javaPackage2CSharpNamespace(superCD.getCSharpNamespace());
      dependencyTracker.addDependency(superClassDependency);
    }

    appendable.append("\n");
    appendable.append("  {\n");
  }

  /**
   * Append class comments.
   * 
   * @param inputClass
   * @param appendable
   * @throws IOException
   */
  protected String translateClassComment(ClassDescriptor inputClass)
      throws IOException
  {
    String comment = inputClass.getComment();
    if (comment != null && comment.length() > 0)
      return commentTranslator.translateComment("  ", comment);
    return "";
  }

  protected void appendClassMetaInformation(ClassDescriptor inputClass, Appendable appendable)
      throws IOException
  {
    List<MetaInformation> metaInfo = inputClass.getMetaInformation();
    appendClassMetaInformationHook(inputClass, appendable);
    metaInfoTranslator.setDependencyTracker(dependencyTracker);
    if (metaInfo != null)
      for (MetaInformation piece : metaInfo)
      {
        String metaInfoStr = metaInfoTranslator.translateMetaInformation(piece);
        if (metaInfoStr != null && metaInfoStr.length() > 0)
          appendable.append("  ").append(metaInfoStr);
      }
  }

  protected void appendClassMetaInformationHook(ClassDescriptor inputClass, Appendable appendable)
  {
    // empty implementation
  }

  /**
   * @param appendable
   * @param inputClass
   * @throws IOException
   */
  protected void appendClassGenericTypeVariables(Appendable appendable, ClassDescriptor inputClass)
      throws IOException
  {
    appendable.append(gtvTranslator.toDefinitionWithGenerics(inputClass));
  }

  protected void appendSuperClassGenericTypeVariables(Appendable appendable,
                                                      ClassDescriptor inputClass)
      throws IOException
  {
    // StringBuilder sb = new StringBuilder();
    // List<GenericTypeVar> genericTypeVars = inputClass.getGenericTypeVars();
    // if (genericTypeVars != null && genericTypeVars.size() > 0)
    // {
    // sb.append(" where ");
    // int len = sb.length();
    // for (int i = 0; i < genericTypeVars.size(); ++i)
    // {
    // GenericTypeVar genericTypeVar = genericTypeVars.get(i);
    // sb.append(i == 0 ? "" : ", ");
    // if (genericTypeVar.getConstraintGenericTypeVar() != null)
    // {
    // sb.append(genericTypeVar.getName()).append(" : ")
    // .append(genericTypeVar.getConstraintGenericTypeVar().getName());
    // }
    // else if (genericTypeVar.getConstraintClassDescriptor() != null)
    // {
    // sb.append(genericTypeVar.getName()).append(" : ")
    // .append(genericTypeVar.getConstraintClassDescriptor().getDescribedClassSimpleName());
    // List<GenericTypeVar> CGTVargs = genericTypeVar.getConstraintGenericTypeVarArgs();
    // if (CGTVargs != null && CGTVargs.size() > 0)
    // {
    // sb.append('<');
    // for (int j = 0; j < CGTVargs.size(); ++j)
    // {
    // GenericTypeVar cgtv = CGTVargs.get(j);
    // sb.append(j == 0 ? "" : ", ").append(cgtv.getName());
    // }
    // sb.append('>');
    // }
    // }
    // else
    // {
    // warning("Unprocessed generic type var: " + genericTypeVar);
    // }
    // }
    // if (sb.length() > len)
    // appendable.append(sb);
    // }
  }

  private Set<String> deriveDependencies(ClassDescriptor inputClass)
  {
    Set<String> dependencies = new HashSet<String>();

    Set<ScalarType> scalars = inputClass.deriveScalarDependencies();
    for (ScalarType scalar : scalars)
      dependencies.add(scalar.getCSharpNamespace());

    Set<ClassDescriptor> composites = inputClass.deriveCompositeDependencies();
    for (ClassDescriptor composite : composites)
      dependencies.add(composite.getCSharpNamespace());

    Set<CollectionType> collections = inputClass.deriveCollectionDependencies();
    for (CollectionType collection : collections)
      dependencies.add(collection.getCSharpNamespace());

    return dependencies;
  }

  /**
   * @param inputClass
   * @param appendable
   * @throws IOException
   */
  protected void appendConstructor(ClassDescriptor inputClass,
                                   Appendable appendable,
                                   String className)
      throws IOException
  {
    appendDefaultConstructor(inputClass.getDescribedClassSimpleName(), appendable);

    appendConstructorHook(inputClass, appendable, null);
  }

  protected void appendConstructorHook(ClassDescriptor inputClass,
                                       Appendable appendable,
                                       String classSimpleName) throws IOException
  {
    // for derived classes to use.
  }

  /**
   * @param className
   * @param appendable
   * @throws IOException
   */
  protected void appendDefaultConstructor(String className, Appendable appendable)
      throws IOException
  {
    appendable.append("\n");
    appendable.append(String.format("    public %s()\n", className));
    appendable.append("    { }\n");
  }

  /**
   * Append class header comment.
   * 
   * @param className
   *          The target class name.
   * @param singleLineComment
   *          The single line comment leading pattern.
   * @param fileExtension
   *          The extension of generated source code file (with dot).
   * @param appendable
   *          The appendable target.
   * @throws IOException
   */
  protected void appendHeaderComments(String className,
                                      String singleLineComment,
                                      String fileExtension,
                                      Appendable appendable) throws IOException
  {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
    DateFormat yearFormat = new SimpleDateFormat("yyyy");

    Date date = new Date();

    appendable.append(singleLineComment + "\n"
        + singleLineComment + " " + className + fileExtension + "\n"
        + singleLineComment + " s.im.pl serialization\n"
        + singleLineComment + "\n"
        + singleLineComment + " Generated by " + this.getClass().getSimpleName()
        + ".\n"
        + singleLineComment + " Copyright " + yearFormat.format(date)
        + " Interface Ecology Lab. \n"
        + singleLineComment + "\n\n"
        );
  }
  
}
