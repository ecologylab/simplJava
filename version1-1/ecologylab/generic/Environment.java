/*
 * Copyright 1996-2002 by Andruid Kerne. All rights reserved.
 * CONFIDENTIAL. Use is subject to license terms.
 */
package cm.generic;

import java.net.*;

/**
 * With an eye toward browser/application portability this interface
 * begins to constitute a transparrent wrapper that provides services
 * that come from the runtime environment, such as parameters, and opening web
 * pages for browsing. 
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

      float		javaVersion	= 1.1f;	// minimum expected
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
	 debug("javaVersion="+ sysJavaVersion+" -> "+ javaVersion);
	 
	 if (javaVersion >= 1.4f)
	    hasXML		= true;
	 else
	    hasXML		= checkFor("org.w3c.dom.Node");

	 debug("javaVersion=" + javaVersion+" hasXML="+hasXML);
      }
      public The(Environment e)
      {
	 set(e);
      }
      public void set(Environment e)
      {
	 environment	= e;
	 frame		= e.parameter("frame");
      }
      public Environment get()
      {
	 return environment;
      }
/**
 * @return	The version of Java we're using (but not the specific release),
 *		as in 1.2, 1.3, 1.4,...
 */
      public float javaVersion()
      {
	 return javaVersion;
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
 * Show the user a message in the status bar at the bottom of the
 * browser, or some other comparable place.
 */
   void		showStatus(String s);
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
 * Get a boolean parameter from the runtime environment. If the value is the
 * string <code>true</code> or <code>yes</code>, the result is 
 * <code>true</code>; else false.
 * 
 * @param	name	The name of the parameter's key.
 */
   boolean parameterBool(String name);
   
/**
 * Get an integer parameter from the runtime environment. The default is 0.
 * 
 * @param	paramName	The name of the parameter's key.
 */
   public int parameterInt(String paramName);
   
/**
 * Get an integer parameter from the runtime environment. 
 * 
 * @param	paramName	The name of the parameter's key.
 * @param	defaultValue	Default integer value, in case param is 
 *				unspecified in the runtime env.
 */
   public int parameterInt(String paramName, int defaultValue);

/**
 * Get a float parameter from the runtime environment.
 * 
 * @param	paramName	The name of the parameter's key.
 * @param	defaultValue	Default floating point value, in case param is 
 *				unspecified in the runtime env.
 */
   public float parameterFloat(String paramName, float defaultValue);


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
 * @return an URL relative to html document
 */   
//   URL rel(String relativeURL);
/**
 * @return an ParsedURL relative to the code base.
 */   
   ParsedURL codeRelativeURL(String relativeURL);
/**
 * @return an ParsedURL relative to the code base.
 */   
   ParsedURL docRelativeURL(String relativeURL);

   public static final int	APPLICATION	= -1;
   public static final int	IE		= 0;
   public static final int	NS		= 1;
   public static final int	PLUGIN		= 2;

   public void go(URL u, String frame);
}
