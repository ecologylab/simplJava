package ecologylab.simpl.translators.csharp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ecologylab.simpl.translators.DependencyTranslator;

/**
 * 
 * @author quyin
 *
 */
public class CSharpDependencyTranslator implements DependencyTranslator
{

  private static Comparator<String> depComparator      = new CSharpDependencyComparator();

  private static Set<String>        globalDependencies = new HashSet<String>();

  static
  {
    globalDependencies.add("System");
    globalDependencies.add("System.Collections");
    globalDependencies.add("System.Collections.Generic");
    globalDependencies.add("Simpl.Serialization");
    globalDependencies.add("Simpl.Serialization.Attributes");
    globalDependencies.add("Simpl.Fundamental.Generic");
    globalDependencies.add("Ecologylab.Collections");
  }

  @Override
  public Set<String> getGlobalDependencies()
  {
    return globalDependencies;
  }

  /**
   * 
   * @return
   */
  protected Comparator<String> getDependencyComparator()
  {
    return depComparator;
  }

  @Override
  public String translateDependency(String dependency)
  {
    return dependency == null ? "" : "using " + dependency + ";\n";
  }

  @Override
  public String translateDependencies(Set<String> dependencies)
  {
    List<String> sortedDeps = new ArrayList<String>();

    Set<String> allDeps = new HashSet<String>();
    allDeps.addAll(getGlobalDependencies());
    if (dependencies != null)
      allDeps.addAll(dependencies);

    for (String dep : allDeps)
      sortedDeps.add(dep);
    Collections.sort(sortedDeps, getDependencyComparator());

    StringBuilder sb = new StringBuilder();
    for (String dep : sortedDeps)
      sb.append(translateDependency(dep));
    return sb.toString();
  }

}