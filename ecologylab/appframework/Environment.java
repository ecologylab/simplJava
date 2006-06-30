/*
 * Copyright 1996-2002 by Andruid Kerne. All rights reserved.
 * CONFIDENTIAL. Use is subject to license terms.
 */
package ecologylab.generic;


/**
 * Provides a mechanism for passing parameters/preferences/properties from
 * diverse runtime environments, including applets (via the param tag), 
 * applications (via a properties file), and sooner or later, servlets.
 * <p>
 * 
 * <b>Cooperative programming</b>: Any class that implements this
 * interface needs to include the following line of code at the top of
 * its initialization, in order to make the services provided by this
 * <code>Environment</code> interface globally accessible.
 * <blockquote><pre>
 *       Environment.the.set(this);
 * </pre></blockquote>
 * 
 * The nested class <code>The</code> serves to keep a global reference
 * to the actual instance of the singleton class that implements Environment.
 * (Don't have more than 1 instance that does!)
 * <p>
 * The <i>raison d'etre</i> of this interface is first to allow
 * programs to utilize services in a uniform way, whether they are
 * applets or applications. The <i>raison d'etre</i> of the nested class
 * is to overcome the existence of a single <code>java.applet.Applet</code>
 * instance in the runtime environment which provides services that
 * conceptually, one expects to get from a static, like
 * <code>java.lang.System</code>. This is a simple mechanism that
 * actually does something quite complex, and for complex reasons.
 * <p>
 * 
 * Services are then available globally through syntax such as:
 * <blockquote><pre>
 *       Environment.the.get().parameter("bgcolor");
 * </pre></blockquote>
 */
public interface Environment
{
/**
 * Holds a  reference to the singleton global instance of
 * {@link Environment Environment}, and simple methods for getting and setting
 * this reference.
 */
   public class The extends Debug
   {
      Environment environment;

      float			javaVersion	= 1.1f;	// minimum expected
      boolean		javaIsBeta;
      
      boolean		hasXML;
      boolean		hasServlet;
      boolean		checkedForServlet;
      boolean		hasQuicktime;
      boolean		checkedForQuicktime;
      
      boolean		hasGL;
      boolean		checkedForGL;
      boolean		hasAgile2D;
      boolean		checkedForAgile2D;
      boolean		checkedForMultivalent;
      boolean		hasMultivalent;
      
      String		frame;
      
      public The()
      {
		 String sysJavaVersion	= System.getProperty("java.version");
		 String floatableJavaVersion = StringTools.remove(sysJavaVersion,'_');

		 int firstDot		= floatableJavaVersion.indexOf('.');
		 int lastDot		= floatableJavaVersion.lastIndexOf('.');
		 if (firstDot != lastDot)
		 {
			String toFirstDot	= floatableJavaVersion.substring(0,firstDot+1);
			String afterFirstDot= floatableJavaVersion.substring(firstDot+1);
			afterFirstDot	= StringTools.remove(afterFirstDot,'.');
			floatableJavaVersion= toFirstDot + afterFirstDot;
		 }
		 int dashBeta		= floatableJavaVersion.indexOf("-beta");
		 
		 if (dashBeta != -1)
		 {
			floatableJavaVersion= floatableJavaVersion.substring(0,dashBeta);
			javaIsBeta		= true;
		 }
		 try
		 {
			javaVersion		= Float.parseFloat(floatableJavaVersion);
		 }
		 catch (NumberFormatException e)
		 {
			debug("PROBLEM parsing javaVersion = " + floatableJavaVersion);
			e.printStackTrace();
		 }
		 //debug("javaVersion="+ sysJavaVersion+" -> "+ javaVersion);
		 
		 if (javaVersion >= 1.4f)
			hasXML		= true;
		 else
			hasXML		= checkFor("org.w3c.dom.Node");

		 //debug("javaVersion=" + javaVersion+" hasXML="+hasXML);
      }
      public The(Environment e)
      {
		 set(e);
      }
      public void set(Environment e)
      {
		 environment	= e;
      }
      public Environment get()
      {
		 return environment;
      }
/**
 * @return	The version of Java we're using.
 */
      public float javaVersion()
      {
		 return javaVersion;
      }
      /**
       * Check to see if we're running on what we consider to be a decent, usable version of Java.
       * For 1.5, this means rel 4 or more; for 1.4, it means 1.42_04 or more.
       * 
       * @return	true if the Java we're running on is good; false if its crap.
       */
      public boolean hasGoodJava()
      {
    	  float javaVersion	= javaVersion();
    	  return (javaVersion >= 1.5004) || ((javaVersion < 1.5) && (javaVersion >= 1.4204));
      }
      public boolean javaIsBeta()
      {
		 return javaIsBeta;
      }
      public boolean hasQuicktime()
      {
		 if (!checkedForQuicktime)
			hasQuicktime	= checkFor("quicktime.std.movies.Movie");
		 return hasQuicktime;
      }
      public boolean hasAgile2D()
      {
		 if (!checkedForAgile2D)
			hasAgile2D	= checkFor("agile2D.AgileJFrame");
		 return hasAgile2D;
      }
      public boolean hasMultivalent()
      {
		 if (!checkedForMultivalent)
			hasMultivalent	= checkFor("multivalent.std.adaptor.pdf.PDFReader");
		 debug("hasMultivalent() = "+hasMultivalent);
		 return hasMultivalent;
      }
      public boolean hasGL()
      {
		 if (!checkedForGL)
			hasGL	= checkFor("gl4java.awt.GLCanvas");
		 return hasGL;
      }
      public boolean hasXML()
      {
		 return hasXML;
      }
      public boolean hasServlet()
      {
		 if (!checkedForServlet)
			hasServlet	= checkFor("javax.servlet.http.HttpServlet");
		 return hasServlet;
      }
      public static boolean checkFor(String className)
      {
		 boolean result	= false;
		 try
		 {
			Class.forName(className);
			result	= true;
		 } catch (ClassNotFoundException e)
		 {
			println("Environment.checkFor("+className+") caught exception "+e);
			//	    e.printStackTrace();
		 } catch (Error e)
		 {
			println("Environment.checkFor("+className+") caught error");
			e.printStackTrace();
		 }
		 return result;
      }
      public String frame()
      {
    	 String result	= frame;
    	 if (result == null)
    	 {
    		 result			= environment.parameter("frame");
    		 frame			= result;
    	 }
		 return frame;
      }

   };
/**
 * Each running entity (be it an applet or an application),
 * should have one and only one instance of an Environment.
 * "the" is that singleton instance.
 */
   static final The the	= new The();

/**
 * Find out which java runtime we're operating in.
 */
   int		runtimeEnv();
/**
 * Find out which browser we're running in.
 */
   int		browser();

/**
 * Show <code>msg</code> in the browser's status bar.
 * 
 * Short form, with for (@link java.cm.applet.Applet#showStatus).
 * Also more robust: avoids breaking when <code>msg</code> is null.
 */
   public void status(String msg);

/**
 * Get a parameter or property, based on a key. Implements a name/value pair.
 * @param	name	The name of the key.
 */
   String	parameter(String name);
   
/**
 * {@link java.applet.Applet#getCodeBase() java.applet.Applet.getCodeBase()}
 * Change type from URL to ParsedURL. 
 */
    ParsedURL codeBase();
/**
 * {@link java.applet.Applet#getDocumentBase() java.applet.Applet.getDocBase()}
 * Change type from URL to ParsedURL.
 */
    ParsedURL docBase();
    
    /**
     * Called at the end of an invocation.
     *
     * @param	code -- 0 for normal. other values are application specific.
     */
    void exit(int code);

/**
 * @return an URL relative to html document
 */   
//   URL rel(String relativeURL);

   public static final int	APPLICATION	= -1;
   public static final int	IE		= 0;
   public static final int	NS		= 1;
   public static final int	PLUGIN		= 2;

   public void go(ParsedURL purl, String frame);
}
