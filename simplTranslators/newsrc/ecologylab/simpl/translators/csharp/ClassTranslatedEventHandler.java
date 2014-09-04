package ecologylab.simpl.translators.csharp;

import java.util.Set;

/**
 * 
 * @author quyin
 *
 */
public interface ClassTranslatedEventHandler
{

  /**
   * 
   * @param classNamespace
   * @param classSimpleName
   * @param classDef
   * @param dependencies
   */
  void classTranslated(String classNamespace,
                       String classSimpleName,
                       String classDef,
                       Set<String> dependencies);

}
