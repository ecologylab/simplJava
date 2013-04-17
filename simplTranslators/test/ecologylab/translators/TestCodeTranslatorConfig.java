package ecologylab.translators;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class TestCodeTranslatorConfig
{

  CodeTranslatorConfig config;

  @Before
  public void init()
  {
    config = new CodeTranslatorConfig();
  }

  @Test
  public void testConstruction()
  {
    assertNotNull(config);
  }

}
