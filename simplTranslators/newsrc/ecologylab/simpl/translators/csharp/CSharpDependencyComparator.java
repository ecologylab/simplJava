package ecologylab.simpl.translators.csharp;

import java.util.Comparator;

/**
 * 
 * @author quyin
 * 
 */
public class CSharpDependencyComparator implements Comparator<String>
{

  private static String[] BUILTIN_NAMESPACE_PREFIXES;

  static
  {
    BUILTIN_NAMESPACE_PREFIXES = new String[]
    {
      "System",
      "System.",
      "Ecologylab",
      "Ecologylab.",
      "Simpl",
      "Simpl.",
    };
  }

  /**
   * 
   */
  @Override
  public int compare(String dep1, String dep2)
  {
    if (dep1 == null || dep2 == null)
      throw new NullPointerException("Non-null argument expected.");

    int p1 = getBuiltinNamespacePrefixIndex(dep1);
    int p2 = getBuiltinNamespacePrefixIndex(dep2);

    if (p1 == p2)
      return dep1.compareTo(dep2);
    else
      return p1 - p2;
  }

  /**
   * 
   * @return
   */
  protected String[] getBuiltinNamespacePrefixes()
  {
    return BUILTIN_NAMESPACE_PREFIXES;
  }

  /**
   * 
   * @param dep
   * @return
   */
  protected int getBuiltinNamespacePrefixIndex(String dep)
  {
    if (dep == null)
      throw new NullPointerException("Non-null argument expected.");

    String[] builtinNamespacePrefixes = getBuiltinNamespacePrefixes();
    for (int i = 0; i < builtinNamespacePrefixes.length; ++i)
      if (dep.startsWith(builtinNamespacePrefixes[i]))
        return i;
    return Integer.MAX_VALUE;
  }
}
