package ecologylab.generic;

import java.util.*;

/**
 * A DownloadMonitor that processes text.
 * It makes one NewPorterStemmer's available to each thread.
 */
public class TextDownloadMonitor
extends
DownloadMonitor
{
   static HashMap	stemmersHash	= new HashMap();
   
   public TextDownloadMonitor(String name, int numDownloadThreads, 
			  int priorityBoost)
   {
      super(name, numDownloadThreads, priorityBoost);
   }
   /**
    * In addition to calling super to create a new Thread for performing downloads,
    * create a stemmer for the new Thread.
    * 
    * @param i
    * @param s
    * @return
    */
   protected Thread newPerformDownloadsThread(int i, String s)
   {
      Thread result = super.newPerformDownloadsThread(i, s);
      debug("makeDownloadThread() creating stemmer for " + result);
      
      addStemmer(result);

      return result;
   }
   public static void addStemmer()
   {
      addStemmer(Thread.currentThread());
   }
   static void addStemmer(Thread thread)
   {
      NewPorterStemmer stemmer			= new NewPorterStemmer();
      stemmersHash.put(thread, stemmer);
   }

   public static NewPorterStemmer getStemmer()
   {
      Thread currentThread		= Thread.currentThread();
      NewPorterStemmer stemmer		= 
	 (NewPorterStemmer) stemmersHash.get(currentThread);
      return stemmer;
   }
}

