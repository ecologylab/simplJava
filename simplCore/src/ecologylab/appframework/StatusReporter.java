package ecologylab.appframework;

/**
 * Interface for objects that report status to the user.
 * 
 * @author andruid
 *
 */
public interface StatusReporter
{
	/**
	 * Submit a message for display in the status line.
	 * If there's nothing showing now, it will be displayed immediately.
	 * Otherwise, it will be queued.
	 * Uses the default minimum longevity level of 1 * 1/4 second.
	 */
	   public void display(String msg);
	   
	   /**
	    * Submit a message for display in the status line.
	    * If there's nothing showing now, it will be displayed immediately.
	    * Otherwise, it will be queued.
	    * 
	    * @param	msg			String to display.
	    * @param	priority	Measure minimum message longevity of message in units 
	    *						of 1/4 second.
	    */
	   public void display(String msg, int priority);
	   
	   
	   public void display(String msg, int longevity, 
				   int progessNumerator, int progessDenominator);
	   

}
