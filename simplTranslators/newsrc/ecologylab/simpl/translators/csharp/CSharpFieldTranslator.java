package ecologylab.simpl.translators.csharp;

import java.io.IOException;
import java.util.List;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.MetaInformation;
import ecologylab.simpl.translators.DependencyTracker;
import ecologylab.translators.net.DotNetTranslationException;
import ecologylab.translators.net.DotNetTranslationUtilities;

public class CSharpFieldTranslator
{

  private CSharpNameTypeResolver              nameTypeResolver;

  private CSharpLanguageSpecifics             langSpec;

  private CSharpCommentTranslator             commentTranslator;

  private CSharpMetaInformationTranslator     metaInfoTranslator;

  private CSharpGenericTypeVariableTranslator gtvTranslator;

  private DependencyTracker                   dependencyTracker;

  public CSharpFieldTranslator(CSharpNameTypeResolver nameTypeResolver,
                               CSharpLanguageSpecifics langSpec,
                               CSharpCommentTranslator commentTranslator,
                               CSharpMetaInformationTranslator metaInfoTranslator,
                               CSharpGenericTypeVariableTranslator gtvTranslator)
  {
    super();
    this.nameTypeResolver = nameTypeResolver;
    this.langSpec = langSpec;
    this.commentTranslator = commentTranslator;
    this.metaInfoTranslator = metaInfoTranslator;
    this.gtvTranslator = gtvTranslator;
  }

  /**
   * Append a field to the translated class source.
   * 
   * @param context
   * @param fieldDescriptor
   * @param appendable
   * @throws IOException
   * @throws DotNetTranslationException
   */
  public void translate(ClassDescriptor context,
                        FieldDescriptor fieldDescriptor,
                        Appendable appendable)
      throws IOException, DotNetTranslationException
  {
    String cSharpType = nameTypeResolver.resolveFieldType(fieldDescriptor);
    if (cSharpType == null)
    {
      System.out.println("ERROR, no valid CSharpType found for : " + fieldDescriptor);
      return;
    }

    boolean isKeyword = langSpec.checkForKeywords(fieldDescriptor);
    if (isKeyword)
      appendable.append("/*\n");
    appendFieldComments(fieldDescriptor, appendable);
    appendFieldMetaInformation(context, fieldDescriptor, appendable);
    appendable.append(String.format("    private %s %s;\n", cSharpType, fieldDescriptor.getName()));
    if (isKeyword)
      appendable.append("*/\n");

    appendGettersAndSetters(context, fieldDescriptor, cSharpType, appendable);
  }

  protected void appendFieldGenericTypeVars(ClassDescriptor contextCd,
                                            FieldDescriptor fieldDescriptor,
                                            Appendable appendable)
      throws IOException
  {
    // TODO Auto-generated method stub

  }

  /**
   * @param fieldDescriptor
   * @param appendable
   * @throws IOException
   */
  protected void appendFieldComments(FieldDescriptor fieldDescriptor, Appendable appendable)
      throws IOException
  {
    String comment = fieldDescriptor.getComment();
    if (comment != null && comment.length() > 0)
      appendable.append(commentTranslator.translateComment("    ", comment));
  }

  protected void appendFieldMetaInformation(ClassDescriptor contextCd,
                                            FieldDescriptor fieldDescriptor,
                                            Appendable appendable) throws IOException
  {
    List<MetaInformation> metaInfo = fieldDescriptor.getMetaInformation();
    appendFieldMetaInformationHook(contextCd, fieldDescriptor, appendable);
    if (metaInfo != null)
      for (MetaInformation piece : metaInfo)
      {
        String metaInfoStr = metaInfoTranslator.translateMetaInformation(piece);
        if (metaInfoStr != null && metaInfoStr.length() > 0)
          appendable.append("    ").append(metaInfoStr);
      }
  }

  protected void appendFieldMetaInformationHook(ClassDescriptor contextCd,
                                                FieldDescriptor fieldDesc,
                                                Appendable appendable) throws IOException
  {
    // for derived classes to use.
  }

  /**
   * @param context
   * @param fieldDescriptor
   * @param appendable
   * @throws IOException
   */
  protected void appendGettersAndSetters(ClassDescriptor context,
                                         FieldDescriptor fieldDescriptor,
                                         String type,
                                         Appendable appendable)
      throws IOException
  {
    String cSharpType = type;

    appendable.append("\n");

    String fieldName = fieldDescriptor.getName();
    String propertyName = DotNetTranslationUtilities.getPropertyName(context, fieldDescriptor);
    boolean isKeyword = langSpec.checkForKeywords(fieldDescriptor);
    if (isKeyword)
      appendable.append("/*\n");
    appendable.append(String.format("    public %s %s\n", cSharpType, propertyName));
    appendable.append(String.format("    {\n"));
    appendable.append(String.format("      get { return %s; }\n", fieldName));
    appendable.append(String.format("      set\n"));
    appendable.append(String.format("      {\n"));
    appendable.append(String.format("        if (this.%s != value)\n", fieldName));
    appendable.append(String.format("        {\n"));
    appendable.append(String.format("          this.%s = value;\n", fieldName));
    appendable.append(String.format("        }\n"));
    appendable.append(String.format("      }\n"));
    appendable.append(String.format("    }\n"));
    if (isKeyword)
      appendable.append("*/\n");

    appendGettersAndSettersHook(context, fieldDescriptor, appendable);
  }

  protected void appendGettersAndSettersHook(ClassDescriptor context,
                                             FieldDescriptor fieldDescriptor, Appendable appendable)
  {
    // TODO Auto-generated method stub

  }

  void setDependencyTracker(DependencyTracker dependencyTracker)
  {
    this.dependencyTracker = dependencyTracker;
  }

}
