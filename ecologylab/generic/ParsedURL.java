/*
 * Modified by Eunyee Koh
 */

package cm.generic;

import java.net.*;
import java.util.*;

/**
 * New class for manipulating and displaying URLs.
 * 
 * Uses lazy evaluation to minimize storage allocation.
 */
public class ParsedURL
extends Debug
{
   protected URL		url = null;
   protected String	string = null;
   
   /* lower case of the url string */
   protected String	lc = null;
   
   /* suffix string of the url */
   protected String	suffix = null;
   
   /* domain value string of the ulr */
   protected String	domain = null;
   
   public ParsedURL()
   {
   }
   
   public ParsedURL(URL url)
   {
      this.url	= url;     
   }
   
   /* 
    * Constructor with a url string parameter. 
    * get absolute URL with getURLAbsolute() method. 
    */
   /*
   public ParsedURL(String urlString)
   {
   		this.url = getAbsolute(urlString, "").url();
   }
   */
   ///////////////////////////////////////////////////////////////////////
   
   /* 
    * Create Absolute URL
    * 
    * @param	webAddr	url string
    * @param	errorDescriptor	which will be printed out in the trace file if there is something happen
    * 			converting from the url string to URL.	
    * @return URL from url string parameter named webAddr.
    */
   public static ParsedURL getAbsolute(String webAddr, String errorDescriptor)
   {
       try
      {
      	URL url		= new URL(webAddr);
      	return new ParsedURL(url);
      }
      catch (MalformedURLException e)
      {
      	Debug.println(urlErrorMsg(webAddr, errorDescriptor));
      	return null;
      }
    }
/*   
   public URL getURL(String webAddr)
   {
      return getURL(webAddr, "");
   }
*/   
/**
 * Uses an absolute URL, if the String parameter looks like that,
 * or one that's relative to docBase, if it looks a relative URL.
 */
   public static ParsedURL getRelativeOrAbsolute(String webAddr, String errorDescriptor)
   {
      if (webAddr == null)
      	return null;
      
      // if its a relative address string, make it that way
      ParsedURL result	= getRelativeToDocBase(webAddr, errorDescriptor);
      // otherwise, try forming it absolutely
      if (result == null)
      {
      	result		= getAbsolute(webAddr, errorDescriptor);
      }
      return result;
   }
   
   public final ParsedURL getRelative(String relativeURLPath, String errorDescriptor)
   {
   	 return getRelative(url, relativeURLPath, errorDescriptor);
   }
   
/**
 * Form a new ParsedURL, relative from a supplied base URL.
 * @param relativeURLPath
 * @param errorDescriptor
 * @return
 */ 
   public static ParsedURL getRelative(URL base, String relativeURLPath, String errorDescriptor)
   {
      if (relativeURLPath == null)
      	return null;
      
      ParsedURL result	= null;
       if (!relativeURLPath.startsWith("http://") && !relativeURLPath.startsWith("ftp://"))
      {
      	try
		{
      		Debug.println("Forming relative URL from docBase="+
      				Generic.docBase());
      		URL url		= new URL(base, relativeURLPath);
          	result			= new ParsedURL(url);
		}
      	catch (MalformedURLException e)
		{
      		Debug.println(urlErrorMsg(relativeURLPath, errorDescriptor));
		}
      }      
      return result;
   }
   
   public static URL getURL(URL base, String path, String error)
   {
      // ??? might want to allow this default behaviour ???
      if (path == null)
	 return null;
      try 
      {
		//System.err.println("\nGENERIC - base, path, error = \n" + base + "\n" + path);
		URL newURL = new URL(base,path);
		//System.err.println("\nNEW URL = " + newURL);
	 return newURL;
      } catch (MalformedURLException e) 
      {
	 if (error != null)
	    throw new Error(e + "\n" + error + " " + base + " -> " + path);
	 return null;
      }
   }
   
   public static ParsedURL getRelativeToDocBase(String relativeURLPath, String errorDescriptor)
   {
   		return Generic.docBase().getRelative(relativeURLPath, errorDescriptor);
   } 
   
   static String urlErrorMsg(String webAddr, String errorDescriptor)
   {
      return "CANT open " + errorDescriptor + " " + webAddr +
	      " because it doesn't look like a web address.";
   }    

/**
 * Uses lazy evaluation to minimize storage allocation.
 * 
 * @return The URL as a String.
 */
  public String toString()
   {
      String result	= string;
      if (result == null)
      {
      	result		= StringTools.pageString(url);
      	string		= result;
      }
      return result;
   }
/**
 * Uses lazy evaluation to minimize storage allocation.
 * 
 * @return Lower case rendition of the URL String.
 */
   public String lc()
   {
      String result	= lc;
      if (result == null)
      {
      	result		= toString().toLowerCase();
      	lc		= result;
      }
      return result;
   }
/**
 * Uses lazy evaluation to minimize storage allocation.
 * 
 * @return	The suffix of the filename, in lower case.
 */
   public String suffix()
   {
      String result	= suffix;
      if (result == null)
      {
      	result		= suffix(lc());
      	suffix		= result;
      }
      return result;
   }
   
   /**
    * Uses lazy evaluation to minimize storage allocation.
    * 
    * @return	The domain of the URL.
    */
   public String domain()
   {
      String result	= domain;
      if (result == null)
      {
      	result		= StringTools.domain(url);
      	domain		= result;
      }
      return result;
   }
/**
 * @return	The suffix of the filename, in whatever case is found in the input string.
 */
  public String suffix(String lc)
   {
      int afterDot	= lc.lastIndexOf('.') + 1;
      int lastSlash	= lc.lastIndexOf('/');
      String result	= 
	 ((afterDot == 0) || (afterDot < lastSlash)) ? "" 
	    : lc.substring(afterDot);
      return result;
   }
/*
   public String toString()
   {
//      return super.toString() + "[" +string() + "]";
      return string();
   }
*/   
   /*
    * @return URL 
    */
   public final URL url()
   {
   	if( url == null )
   		return null;
   	return url;
   }
   
   /*
    * Return no anchor ParsedURL.
    */
   public ParsedURL noAnchorParsedURL()
   {
      if (url.getRef() == null)
      	return this;
      // else
      return new ParsedURL(StringTools.urlNoAnchor(url));
   }
   
   /*
    * Return no anchor URL.
    */
   public URL noAnchorURL()
   {
    if (url.getRef() == null)
   	 return this.url();
         // else
    return StringTools.urlNoAnchor(url);   	
   }
   
   /*
    * return noAnchor no query page string 
    */
   public String noAnchorNoQueryPageString()
   {
   	return StringTools.noAnchorNoQueryPageString(url);
   }
   
   /*
    * return no anchor no page string.
    */
   public String noAnchorPageString()
   {
   	return StringTools.noAnchorPageString(url);
   }
/**
 * @return true if the suffix of this is equal to that of the argument.
 */
   public final boolean hasSuffix(String s)
   {
   	  return lc().endsWith(s);
 //     return suffix().equals(s);
   }

   public ParsedURL relativeURL(String s)
   {
      return relativeURL( s, null, false, false);
   }
   
   public ParsedURL relativeURL(String s, boolean fromSearchPage)
   {
      return relativeURL(s, null, false, fromSearchPage);      
   }

   final static String unsupportedMimeStrings[]	=
   {
      "ai", "bmp", "eps", "ps", "psd", "svg", "tif", "vrml",
      "doc", "xls", "pps", "ppt", "adp", "rtf", 
      "vbs", "vsd", "wht", 
      "aif", "aiff", "aifc", "au", "mp3", "wav", "ra", "ram", 
      "wm", "wma", "wmf", "wmp", "wms", "wmv", "wmx", "wmz",
      "avi", "mov", "mpa", "mpeg", "mpg", "ppj",
      "swf", "spl", 
//	delete "pdf" Eunyee
//	  "swf", "sql",  
      "qdb", 
      "cab", "chm", "gzip", "hqx", "jar", "lzh", "tar", "zip", 
      "xml", "xsl",
   };
   final static HashMap unsupportedMimes	= 
      Generic.buildHashMapFromStrings(unsupportedMimeStrings);

   static final String[] unsupportedProtocolStrings = 
   {
      "mailto", "vbscript", "news", "rtsp", "https",
   };
   static final HashMap unsupportedProtocols = 
      Generic.buildHashMapFromStrings(unsupportedProtocolStrings);

   static final String[] imgMimeStrings	=
   {
      "jpg", "jpeg", "pjpg", "pjpeg", "gif", "png", 
   };
   static final HashMap imgMimes = 
      Generic.buildHashMapFromStrings(imgMimeStrings);
   static final String[] htmlMimeStrings	=
   {
      "html", "htm", "stm", "php", "jhtml", "jsp", "asp", "txt", "shtml",
      "pl", "plx", "exe"
   };
   static final HashMap htmlMimes = 
      Generic.buildHashMapFromStrings(htmlMimeStrings);

   /**
    * @param	search -- does
    * skipSlashifying	Slashifying is generally necessary to
    * make links to directories work (when they dont end in /).
    * This breaks Yahoo search, so we can parameterize it.
    * Also peels off all arguments cause search engines like to add their
    * own. ??? this is a disaster for creatingmedia.com when its listed ???
    * When this comes up, consider putting in a special exception, or
    * look real hard at the structure of them URLs!!!
    */
   public ParsedURL relativeURL(String s, String errorIdString,
			    boolean search, boolean tossArgsAndHash)
   {
      if ((s == null) || (s.length() == 0))
	 return null;
	 
	 if(url!=null)
	 {
		 String urlString = url.toString();	 				 
	
		 //if the base url is a folder but not ended with "/", we need to add one "/"
		 if ((urlString.charAt(urlString.length()-1))!='/')
		 {
		 	//println(utlString.lastIndexOf('/')+ " : " + utlString.length());
		 	String lastPart = urlString.substring(urlString.lastIndexOf("/"), urlString.length());
		 		
		 	if(lastPart.indexOf(".")!=-1)	 	
		 	lastPart =  urlString.substring(urlString.lastIndexOf(".")+1);	 		 	
		 	
		 	if ((!imgMimes.containsKey(lastPart))&&(!htmlMimes.containsKey(lastPart)))		 	
				// use new ParsedURL constructor.
		 		url = getAbsolute(urlString, "").url();
	
		 }
	 }
	 
      
      URL newUrl = null;
      String	lc	= s.toLowerCase();
      boolean javascript	= lc.startsWith("javascript:");
      // special: seeking to data mine urls from javascript quoted strings
      if (javascript)
      {
		 // !!! Could do an even better job here of mining quoted
		 // !!! javascript strings.
	//	 println("Container.newURL("+s);
		 int http	= lc.lastIndexOf("http://");
		 int html	= lc.lastIndexOf(".html");
//		 println("Container.newURL() checking javascript url:="+s+
//			 " http="+http+" html="+html);
		 if ((http > -1) && (html > -1) && (http < html))
		 {
		    s		= s.substring(http, html + 5);
//		    println("Container.newURL fixed javascript:= " + s);
		    lc		= lc.substring(http, html + 5);
		    javascript	= false;
		 }
		 // !!! What we should really do here is find quoted strings
		 // (usually with single quote, but perhaps double as well)
		 // (use regular expressions?? - are they fast enough?)
		 // and look at each one to see if either protocol is supported
		 // or suffix is htmlMime or imgMime.
      }
      if (javascript)
      {
	 // println("rejecting " + s); 
		 return null;
      }
      // used to check for mime here!!! now we do that where the crawler is 
      // if !crawlable(lc) ...

      // handle embedded http://
      int lastHttp	= s.lastIndexOf("http://");
      char argDelim	= '?';
      if (lastHttp > 0)
      {
	 // this is search engine crap
	 s		= s.substring(lastHttp);
	 // handle any embedded args (for google mess)
	 argDelim		= '&';
      }
      if (!search)
      {
	 // 1) peel off hash
	 int hashPos	= s.indexOf('#'); 
	 String hashString= StringTools.EMPTY_STRING;
	 if (hashPos > -1)
	 {
	    hashString	= s.substring(hashPos);
	    s		= s.substring(0, hashPos);
	 }
	 // 2) peel off args
	 int argPos	= s.indexOf(argDelim);
	 String argString	= StringTools.EMPTY_STRING;
	 if (argPos > -1)
	 {
	    argString	= s.substring(argPos);
	    s		= s.substring(0, argPos);
	 }
	 else
	 {
	    // 3) if what's left is a directory (w/o a mime type),add slash
	    int endingSlash	= s.lastIndexOf('/');
	    int lastChar	= s.length() - 1;
	    if (endingSlash == -1)
	       endingSlash++;
	    if ((lastChar > 0) &&
		(lastChar != endingSlash) &&
		(s.substring(endingSlash).indexOf('.') == -1))
	       s	       += '/';
	 }
	 if (!tossArgsAndHash)
	    // 4) put back what we peeled off
	    s	       += argString + hashString;
      }
      
      ParsedURL parsedUrl;
      try
      {
	 if (url == null)	   
	    newUrl = new URL(s);
	 else
	 {
	    newUrl = new URL(url, s);	       
	 }	      
	 
	 parsedUrl		= new ParsedURL(newUrl);	 
      }
      catch (MalformedURLException e)
      {
	 parsedUrl		= null;
	 println("Container.parseURL() cant access this malformed url:\n\t" +
	      url +"/"+ s + "\n\t" + e + "\n");
      }
            
      return parsedUrl;
   }
/**
 * 
 * @return A String version of the URL path, in which all punctuation characters have been changed into spaces.
 */
   public String removePunctuation()
   {
   	  return StringTools.removePunctuation(toString());
   }
 
   /*
    * return true if they have same domains. 
    * return false if they have different domains. 
    */
   public boolean sameDomain(ParsedURL other)
   {
   		return (other != null) && domain().equals(other.domain());
   }
   
   /*
    * return true if they have same hosts.
    * return false if they have different hosts.
    */
   public boolean sameHost(ParsedURL other)
   {
   		return (other != null) && url.getHost().equals(other.url().getHost());
   }
   
  
   /**
    * NB: this should be using ParsedURL!
    * 
    * @param lc	String form of a url, already converted to lower case.
    * 
    * @return true if this seems to be a web addr we can crawl to.
    * 			(currently that means html).
    **/
   	public boolean crawlableLc()
    {
        if (!protocolIsSupported(url.getProtocol()))
         	return false;
 
        if( lc == null )
        	lc = lc();
  
         // check for an unsupported mime extension so we dont
         // useless binary data    
        int lastSlash	= lc.lastIndexOf("/");
        if (lastSlash == -1)
         	lastSlash	= 0;
        String fileName	= lc.substring(lastSlash);
        int dot		= fileName.lastIndexOf(".");
//       println("crawlable()lastSlash="+lastSlash+" dot="+dot+ fileName+" "+lc);
   	  	boolean result	= true;
         if (dot > 0)
         {
         	String mime	= fileName.substring(dot + 1);
         	result		= !unsupportedMimes.containsKey(mime);	 
         }
         return result;
     }  

   	/*
   	 * check whether the protocol is supported or not from unsupportedProtocols.
   	 */
   public boolean protocolIsSupported(String urlPath)
      {
         boolean result	= true;
         int separator	= urlPath.indexOf("://");
         if (separator == -1)
         	separator	= urlPath.indexOf(":");
         if (separator > 0)			    // if not a relative path
         {
         	String protocol	= urlPath.substring(0, separator);
//   	 println("Container.protocolIsSupported.(testing " + protocol);
         	result		= !unsupportedProtocols.containsKey(protocol);
         }
         return result;
      }
   
   /**
    * @param	suffix	file name suffix in lower case.
    */
    public  boolean isImg()
    {
    	if( suffix == null )
    		suffix = suffix();
        return (suffix != null) && (suffix.length() != 0) && 
		 	imgMimes.containsKey(suffix);
    }
    
   /**
    * @param	suffix	file name suffix in lower case.
    */
   public boolean isHTML()
      {
   		if( suffix == null )
   			suffix = suffix();
        return htmlMimes.containsKey(suffix);
      }
   
   /*
    * Check the suffix whether it is in the unsupportedMimes or not. 
    * If it is in the unsupportedMimes, return true, and if it is not, return false.
    */
   public boolean isUnsupported()
    {
   		if( suffix == null )
   			suffix = suffix();
        return unsupportedMimes.containsKey(suffix);
    }
   
   /*
    * return the inverse of isUnsupported().
    * Then, if the suffix is in the unsupportedMimes, return false, and if it is not, return true.
    */
   public boolean supportedMime()
   {
      return !isUnsupported();
   }
   /**
    * @return		the path to the directory assosciated w this URL.
    */
   public String directoryString()
   {
         int port		= url.getPort();
         String portStr	= (port == -1) ? "" : ":" + port;
         String protocol	= url.getProtocol() + "://";
         String path	= url.getFile();
         int	args		= path.indexOf("?");

         if (args > -1)
         	path		= path.substring(0,args);
         int	lastSlash	= path.lastIndexOf("/");
         int	lastDot		= path.lastIndexOf(".");
         if (lastDot > lastSlash)
         	path		= path.substring(0,lastSlash); 
         return protocol + url.getHost() + portStr + path;
   }

}
