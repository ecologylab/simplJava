package ecologylab.simpl.translators.csharp;

import java.io.IOException;

import ecologylab.semantics.html.utils.StringBuilderUtils;
import ecologylab.serialization.MetaInformation;
import ecologylab.serialization.MetaInformation.Argument;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.simpl.translators.DependencyTracker;
import ecologylab.translators.net.DotNetTranslationUtilities;

public class CSharpMetaInformationTranslator
{

  private DependencyTracker dependencyTracker;

  public String translateMetaInformation(MetaInformation metaInfo) throws IOException
  {
    if (isAttributeHint(metaInfo))
      return "";

    String metaInfoPackage = metaInfo.typeName.substring(0, metaInfo.typeName.lastIndexOf('.'));
    String metaInfoDependency = DotNetTranslationUtilities
        .translateAnnotationPackage(metaInfoPackage);
    if (metaInfoDependency != null)
      dependencyTracker.addDependency(metaInfoDependency);

    StringBuilder sb = StringBuilderUtils.acquire();

    sb.append("[");
    sb.append(DotNetTranslationUtilities.translateAnnotationName(metaInfo.simpleTypeName));
    if (metaInfo.args != null && metaInfo.args.size() > 0)
    {
      sb.append("(");
      if (metaInfo.argsInArray)
      {
        String argType = metaInfo.args.get(0).simpleTypeName;
        if (argType.equals("Class"))
          argType = "Type";
        sb.append("new ").append(argType).append("[] {");
        for (int i = 0; i < metaInfo.args.size(); ++i)
          sb.append(i == 0 ? "" : ", ")
              .append(translateMetaInfoArgValue(metaInfo.args.get(i).value));
        sb.append("}");
      }
      else
      {
        for (int i = 0; i < metaInfo.args.size(); ++i)
        {
          Argument a = metaInfo.args.get(i);
          sb.append(i == 0 ? "" : ", ")
              .append(metaInfo.args.size() > 1 ? a.name + " = " : "")
              .append(translateMetaInfoArgValue(a.value));
        }
      }
      sb.append(")");
    }
    sb.append("]");
    sb.append("\n");

    String metaInfoStr = sb.toString();
    StringBuilderUtils.release(sb);
    return metaInfoStr;
  }

  private boolean isAttributeHint(MetaInformation metaInfo)
  {
    if (simpl_hints.class.getName().equals(metaInfo.typeName)
        && metaInfo.argsInArray
        && metaInfo.args != null
        && metaInfo.args.size() == 1
        && metaInfo.args.get(0).value != null
        && metaInfo.args.get(0).value.equals(Hint.XML_ATTRIBUTE))
      return true;
    return false;
  }

  protected String translateMetaInfoArgValue(Object argValue)
  {
    // TODO to make this extendible, use an interface MetaInfoArgValueTranslator and allow users
    // to inject new ones to handle different kind of cases.
    if (argValue instanceof String)
    {
      return "\"" + argValue.toString() + "\"";
    }
    else if (argValue instanceof Hint)
    {
      switch ((Hint) argValue)
      {
      case XML_ATTRIBUTE:
        return "Hint.XmlAttribute";
      case XML_LEAF:
        return "Hint.XmlLeaf";
      case XML_LEAF_CDATA:
        return "Hint.XmlLeafCdata";
      case XML_TEXT:
        return "Hint.XmlText";
      case XML_TEXT_CDATA:
        return "Hint.XmlTextCdata";
      default:
        return "Hint.Undefined";
      }
    }
    else if (argValue instanceof Class)
    {
      return "typeof(" + ((Class) argValue).getSimpleName() + ")";
    }
    // eles if (argValue instanceof ClassDescriptor)
    return null;
  }

  void setDependencyTracker(DependencyTracker dependencyTracker)
  {
    this.dependencyTracker = dependencyTracker;
  }

}
