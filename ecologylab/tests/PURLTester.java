package ecologylab.tests;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.Hint;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.SIMPLTranslationException;

public class PURLTester extends ElementState {
	
	/*
	 * You'll need to edit these to make sure they exist
	 * on your system.
	 */
	String absolutePath = "C:\\test\\testfile.txt";
	String relativePath = "ecologylab\\net\\ParsedURL.java";
	String uncPath = "\\\\hostname\\rest\\of\\path";
	String fileProtocol = "file://";
	
	File absoluteFile = new File(absolutePath);
	File relativeFile = new File(relativePath);
	File uncFile = new File(uncPath);
	
	URL absoluteURL;
	URL relativeURL;
	URL uncURL;
	
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL absolutePURLfromFile;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL relativePURLfromFile;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL uncPURLfromFILE;
	
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL absolutePURLfromURL;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL relativePURLfromURL;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL uncPURLfromURL;

	
	public PURLTester()
	{
		// empty just in case deserialization tried to cheat.
	}
	
	public PURLTester(boolean notDefault)
	{
		try
		{
		absoluteURL = new URL(fileProtocol+absolutePath);
		relativeURL = new URL(fileProtocol+relativePath);
		uncURL = new URL(fileProtocol+uncPath);
		
		absolutePURLfromFile = new ParsedURL(absoluteFile);
		relativePURLfromFile = new ParsedURL(relativeFile);
		uncPURLfromFILE = new ParsedURL(uncFile);
		
		absolutePURLfromURL = new ParsedURL(absoluteURL);
		relativePURLfromURL = new ParsedURL(relativeURL);
		uncPURLfromURL = new ParsedURL(uncURL);
		
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Run a few simple tests to make sure ParsedURLs are working
	 * correctly.
	 */
	public void main()
	{
		try
		{
			/* test files created from the paths -- to check the paths */

			System.out.println("absolute file exists: " + absoluteFile.exists() + " relative file exists: " + relativeFile.exists());
			
			/* test PURLs created from above Files */

			System.out.print("absPURLfromFile.toString ");
			System.out.println(absolutePURLfromFile.toString());
			System.out.print("absPURLfromFile ");
			System.out.println(absolutePURLfromFile.connect().inputStream().read());
			

			System.out.print("relPURLfromFile ");
			System.out.println(relativePURLfromFile.connect().inputStream().read());
			
			/* test the URLs */
			System.out.print("absoluteURL ");
			System.out.println(absoluteURL.toString());
			
			/* test PURLs created from URLs */

			System.out.print("absPURLfromURL (should match absPURLfromFile) ");
			System.out.println(absolutePURLfromURL.connect().inputStream().read());
			

			System.out.print("relPURLfromURL (should match relPURLfromFile) ");
			System.out.println(relativePURLfromURL.connect().inputStream().read());
			
			/* Windows UNC names: \\\\hostanme\\rest\\of\\path 
			 * Turns out we can handle them just like normal...
			 * */

			URI uncURI = uncFile.toURI();
			URL uncURL = new URL(fileProtocol+uncPath);
			File fromURL = new File(uncURL.toString().substring(7));
			
			System.out.println("path " + uncPath);
			System.out.println("file " + uncFile.toString());
			System.out.println("URI " + uncURI.toString());
			System.out.println("URI->URL " + uncURI.toURL().toString());
			System.out.println("URL " + uncURL.toString());
			System.out.println("URL->file " + fromURL.toString() + " exists? " + fromURL.exists());
			
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		PURLTester tester = new PURLTester(true);
		tester.main();
		
		TranslationScope ts = TranslationScope.get("testerscope", PURLTester.class, ParsedURL.class);
		try {
			tester.serialize("tester.xml");
			PURLTester other = (PURLTester) TranslationScope.translateFromXML("tester.xml", ts);
			/* I put a breakpoint here to manually inspect other
			 * to make sure it was read back in correctly -- Marty
			 */
			System.out.println(other);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
