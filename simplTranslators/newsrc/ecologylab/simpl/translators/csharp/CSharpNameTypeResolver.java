package ecologylab.simpl.translators.csharp;

import ecologylab.semantics.html.utils.StringBuilderUtils;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;

public class CSharpNameTypeResolver
{

  public String resolveClassNamespace(ClassDescriptor classDescriptor)
  {
    // TODO built-in namespaces conversion
    String javaPackage = classDescriptor.getDescribedClassPackageName();
    return javaPackage2CSharpNamespace(javaPackage);
  }
  
  public String resolveClassSimpleName(ClassDescriptor classDescriptor)
  {
    // TODO built-in type name conversion
    return classDescriptor.getDescribedClassSimpleName();
  }
  
  public String resolveFieldType(FieldDescriptor fieldDescriptor)
  {
    // TODO built-in type conversion
    String cSharpType = fieldDescriptor.getCSharpType();
    return cSharpType == null ? null : javaType2CSharpTypeWithNamespaceFix(cSharpType);
  }
  
  private String javaType2CSharpTypeWithNamespaceFix(String cSharpType)
  {
    if (cSharpType.contains("."))
    {
      // adapt java style package into C# namespace

      int p0 = cSharpType.indexOf('<');
      int p1 = cSharpType.indexOf('>');
      String prefix = p0 >= 0 ? cSharpType.substring(0, p0 + 1) : "";
      String suffix = p1 >= 0 ? cSharpType.substring(p1) : "";
      String typeStr = p0 >= 0 && p1 >= 0 ? cSharpType.substring(p0 + 1, p1) : cSharpType;

      int p = typeStr.lastIndexOf('.');
      String packageName = typeStr.substring(0, p);
      String className = typeStr.substring(p + 1);

      typeStr = javaPackage2CSharpNamespace(packageName) + "." + className;

      cSharpType = prefix + typeStr + suffix;
    }
    return cSharpType;
  }

  public String javaPackage2CSharpNamespace(String packageName)
  {
    StringBuilder sb = StringBuilderUtils.acquire();
    for (int i = 0; i < packageName.length(); ++i)
    {
      char c = packageName.charAt(i);
      char pc = i == 0 ? 0 : packageName.charAt(i - 1);
      if (c != '_')
        sb.append((i == 0 || pc == '.' || pc == '_') ? Character.toUpperCase(c) : c);
    }
    String result = sb.toString();
    StringBuilderUtils.release(sb);
    return result;
  }

}
