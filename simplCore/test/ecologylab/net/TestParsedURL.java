package ecologylab.net;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author quyin
 */
public class TestParsedURL
{

  @Test
  public void testFragment() throws MalformedURLException
  {
    String u1 = "http://example.com/index.html?a=foo&b=bla#bar";

    ParsedURL purl1 = new ParsedURL(new URL(u1));
    Assert.assertEquals("example.com", purl1.domain());
    Assert.assertEquals("/index.html", purl1.pathNoQuery());
    Assert.assertEquals("a=foo&b=bla", purl1.query());
    Assert.assertEquals("bar", purl1.fragment());
    Assert.assertEquals("http://example.com/index.html?a=foo&b=bla#bar", purl1.toString());

    String u2 = "http://example.com/intro.html#overview";
    ParsedURL purl2 = ParsedURL.getAbsolute(u2);
    Assert.assertEquals("example.com", purl2.domain());
    Assert.assertEquals("/intro.html", purl2.pathNoQuery());
    Assert.assertNull(purl2.query());
    Assert.assertEquals("overview", purl2.fragment());
    Assert.assertEquals("http://example.com/intro.html#overview", purl2.toString());

    String relPath = "baz/search.html?q=sometext#frag";
    ParsedURL purl3 = ParsedURL.getRelative(new URL(u1), relPath, "ParsedURL.getRelative()");
    Assert.assertEquals("example.com", purl3.domain());
    Assert.assertEquals("/baz/search.html", purl3.pathNoQuery());
    Assert.assertEquals("q=sometext", purl3.query());
    Assert.assertEquals("frag", purl3.fragment());
    Assert.assertEquals("http://example.com/baz/search.html?q=sometext#frag", purl3.toString());
  }

}
