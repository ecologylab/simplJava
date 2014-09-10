package ecologylab.simpl.translators.csharp;

import ecologylab.generic.Debug;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.translators.net.DotNetTranslationUtilities;

public class CSharpLanguageSpecifics
{

  /**
   * @param fieldDescriptor
   * @return true if the name is a keyword, otherwise false.
   */
  public boolean checkForKeywords(FieldDescriptor fieldDescriptor)
  {
    if (DotNetTranslationUtilities.isKeyword(fieldDescriptor.getName()))
    {
      Debug.warning(fieldDescriptor, "Field [" + fieldDescriptor.getName()
          + "]: This is a keyword in C#. Cannot translate.");
      return true;
    }
    return false;
  }

}
