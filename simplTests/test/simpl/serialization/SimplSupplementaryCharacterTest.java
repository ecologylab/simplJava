package simpl.serialization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.StringFormat;

public class SimplSupplementaryCharacterTest
{

  @Test
  public void testSupplementaryCharacter() throws SIMPLTranslationException
  {
    Article article = new Article();
    article.title = "\uD800\uDF40 and \uD803\uDC22";

    String xml = SimplTypesScope.serialize(article, StringFormat.XML).toString();
    System.out.println(xml);

    SimplTypesScope tscope = SimplTypesScope.get("test-supplementary-chars", Article.class);
    Article a1 = (Article) tscope.deserialize(xml, StringFormat.XML);
    assertEquals(article.title, a1.title);
  }

}
