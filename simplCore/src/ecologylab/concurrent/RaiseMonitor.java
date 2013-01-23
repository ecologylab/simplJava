package ecologylab.concurrent;

import ecologylab.appframework.TraceSlots;



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
  
	@Override
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
	
	@Override
	public synchronized void run()
	{
		while (running)
		{
			try
			{  
				//TraceSlots.rolloverRaiseStatus	= 3000;
				// wait for the next request
				//Debug.println("RaiseMonitor.run() wait()");
				wait();
	
				if (!running)
					break;
			    
				TraceSlots.rolloverRaiseStatus = 3001;
				//Debug.println("RaiseMonitor.run() take waitingLock");
				synchronized (waitingLock)
				{
					waitingToDoRun	= true;
					//				  println("RolloverFrame.run() waitingLock.wait(RAISE_ABOUT_DELAY)");
					
					TraceSlots.rolloverRaiseStatus	= 3002;
					// wait for a little delay before raising
					waitingLock.wait(raiseDelay);
					if (running == false)
						break;
					
					//println("run() waitingToDoRun="+waitingToDoRun);
					if (waitingToDoRun)
					{
						waitingToDoRun	= false;
						TraceSlots.rolloverRaiseStatus	= 3003;
						doRaise();
						TraceSlots.rolloverRaiseStatus	= 3004;
					}
					else // waitingToDoRun changed asychronously by cancel()
					{
						TraceSlots.rolloverRaiseStatus	= 3005;
						//cancelRaise();
						//oneAndOnly.showEmbellishments	= false;
					}
				}
				//Debug.println("RaiseMonitor.run() released waitingLock");
			} catch (Exception e)
			{
			   if (running)
				  e.printStackTrace();
			}
		}
	}
	
	protected abstract void doRaise();
	protected abstract void cancelRaise();
	
	public void setRaiseDelay(int raiseDelay)
	{
		this.raiseDelay		= raiseDelay;
	}
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
	
	public boolean isRunning()
	{
		return running;
	}
}
