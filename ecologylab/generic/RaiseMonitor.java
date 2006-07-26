package ecologylab.generic;

import ecologylab.gui.AWTBridge;


/**
 * Extensions to thread provide the service of raising embellishments,
 * and also functionality for starting & stopping this thang.
 */
public abstract class RaiseMonitor extends Thread {

	protected 	boolean			running;

	/**
	 * A montior object used to control access to the wait() call that happens
	 * when a waitThenShow() request is received.
	 */
	protected 	final Object	waitingLock		= new Object();
	/**
	 * 
	 */
	protected 	boolean			waitingToDoRun;
	private		int				raiseDelay;
	
	public RaiseMonitor(String name, int raiseDelay, boolean startNow)
	{
		super(name);
		this.raiseDelay = raiseDelay;
		if (startNow)
			start();
	}
  
	public void start()
	{
		if (!running)
		{
			running		= true;
			super.start();
		}
	}
	
	public synchronized void stopRunning()
	{
		if (running)
		{
			running		= false;
			synchronized (waitingLock)
			{
				notify();
			}
		}
	}
  
	/**
	 * Raise after a suitable delay, unless a cancel comes
	 * in first().
	 */
	public synchronized void waitThenShow()
	{
		//println("RolloverFrame.delayThenShow()");
		notify();
	}
	
	public synchronized void run()
	{
		while (running)
		{
			try
			{  
				AWTBridge.rolloverRaiseStatus	= 3000;
				// wait for the next request
				Debug.println("RolloverFrame.run() wait()");
				wait();
	
				if (!running)
					break;
			    
				AWTBridge.rolloverRaiseStatus = 3001;
				Debug.println("RolloverFrame.run() take waitingLock");
				synchronized (waitingLock)
				{
					  waitingToDoRun	= true;
		//				  println("RolloverFrame.run() waitingLock.wait(RAISE_ABOUT_DELAY)");
					  
					  AWTBridge.rolloverRaiseStatus	= 3002;
					  // wait for a little delay before raising
					  waitingLock.wait(raiseDelay);
					  if (running == false)
						 break;
					  
					  //println("run() waitingToDoRun="+waitingToDoRun);
					  if (waitingToDoRun)
					  {
						 waitingToDoRun	= false;
						 AWTBridge.rolloverRaiseStatus	= 3003;
					     doRaise();
						 AWTBridge.rolloverRaiseStatus	= 3004;
					  }
					  else // waitingToDoRun changed asychronously by cancel()
					  {
						 AWTBridge.rolloverRaiseStatus	= 3005;
						 //cancelRaise();
						 //oneAndOnly.showEmbellishments	= false;
					  }
				 }
			} catch (Exception e)
			{
			   if (running)
				  e.printStackTrace();
			}
		}
	}
	
	protected abstract void doRaise();
	protected abstract void cancelRaise();
	
	/**
	 * If a request to raise the object is pending, cancel it.
	 */
	public boolean cancel()
	{
		 synchronized (waitingLock)
		 {
			boolean	result	= waitingToDoRun;
			if (waitingToDoRun)
			{
			   cancelRaise();

			   waitingToDoRun		= false;
			   waitingLock.notify();
			}
			return result;
		 }
	}
	
	/**
	 * Checks if RaiseMonitor is currently waiting to execute doRaise()
	 * @return true if it is waiting. false if it is not.
	 */
	public boolean waitingToDoRun()
	{
		return waitingToDoRun;
	}
	
	public Object waitingLock()
	{
		return waitingLock;
	}
}
