package ecologylab.generic;

import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * @author vikrams
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ThreadDebugger extends Debug
{
	static     Hashtable 	threadEntriesByName		= new Hashtable();
	static     HashMap 		threadEntriesByThread	= new HashMap();
	
	static 	int nThreads;

	static     JFrame threadControlFrame = new JFrame("Thread Debugger");
	static     JPanel threadControlPanel =  new JPanel();
	static     Box verticalBox = new Box(BoxLayout.Y_AXIS);
	static     ActionListener threadToggler;
	
	static	WindowObservable windowObservable = new WindowObservable();

	static
	{
		nThreads = 0;	

		threadToggler = new ActionListener()
  		{
  			public void actionPerformed(ActionEvent e)
  			{
  				String action = e.getActionCommand();
  				// ignore "start " / "pause " - we have the thread name as the key into the hashtable
  				
		 	 	String threadName = action.substring(6,action.length());
				ThreadEntry ttd = (ThreadEntry)threadEntriesByName.get(threadName);	
				if (ttd == null)
				{
					// System.err.println("\nthreadName = " + threadName);
					return;
				}
							  			
  				boolean paused = toggleAndReturnNewState(ttd); 				
  			
  				if (!paused)
  				{
					resume(ttd);
  				}
  			}			
  		};	
  		
  		threadControlPanel.add(verticalBox);
		threadControlFrame.getContentPane().add(threadControlPanel);
		threadControlFrame.pack();
		setPosition();		

		threadControlFrame.addWindowListener(windowObservable);
	}	
	
	public ThreadDebugger()
	{
		System.err.println("\nThreadDebugger constructor");
	}
	
	public static void registerMyself(Thread thread)
	{
		synchronized (threadEntriesByName)
		{
			final String threadName = thread.getName();
			if (threadEntriesByName.get(threadName) != null)
			{
				return;	
			}
			ThreadEntry threadEntry = new ThreadEntry(thread);
			threadEntriesByName.put(threadName, threadEntry);	
			threadEntriesByThread.put(thread, threadEntry);
			nThreads++;
			//println("ThreadDebugger.register("+thread+" COUNT = " +nThreads);
			verticalBox.add(threadEntry.button);
			threadControlFrame.pack();
			setPosition();			
		}
	}
	
	public	static boolean toggleAndReturnNewState(ThreadEntry threadEntry)
	{
		threadEntry.button.setBackground(Color.yellow);
		return threadEntry.toggleAndReturnNewState();
	}
		
	public static void waitIfPaused(Thread thread)
	{
		ThreadEntry threadEntry = (ThreadEntry)threadEntriesByThread.get(thread);
		if( threadEntry != null)
		{	
			Object mylock = threadEntry.lock;
			synchronized (mylock)
			{
				boolean paused = threadEntry.paused;
				if (paused)
				{
					try
					{
						println("\nPAUSING THREAD " + thread.getName());
						threadEntry.button.setBackground(Color.red);
						mylock.wait();	
					}catch (InterruptedException e)
					{
					}
				}		
			}	
		}
	}		
	
	public	static void resume(ThreadEntry threadEntry)
	{
		Object mylock = threadEntry.lock;
		
		synchronized (mylock)
		{
			println("\nRESTARTING THREAD " + threadEntry.thread.getName());					
			mylock.notify();
			threadEntry.button.setBackground(Color.green);
		}				
	}	
	
	public static void removeMyself(Thread thread)
	{
		ThreadEntry removedTtd = (ThreadEntry)threadEntriesByThread.remove(thread.getName());
		threadEntriesByName.remove(thread);
		verticalBox.remove(removedTtd.button);
		threadControlFrame.pack();						
		setPosition();
	}
	
	/**
	 * Clear all the thread collections -- eunyee
	 *
	 */
	public static void clear()
	{
		threadEntriesByName.clear();
		threadEntriesByThread.clear();
		nThreads = 0;
	}
	
	static int xOriginal, yOriginal;
		
	public static void setPosition(int x, int y)
	{
		xOriginal = x;
		yOriginal = y;
		threadControlFrame.setLocation(x - currentWidth(),y - currentHeight());	
	}
	
	static void setPosition()
	{
		threadControlFrame.setLocation(xOriginal - currentWidth(),yOriginal - currentHeight());					
	}
	
	protected static int currentWidth()
	{
		return threadControlFrame.getWidth();	
	}
	
	protected static int currentHeight()
	{
		return threadControlFrame.getHeight();		
	}
	public static void show()
	{
	   println("ThreadDebugger.show()");
		threadControlFrame.setVisible(true);	
	}
	public static void hide()
	{
	   println("ThreadDebugger.hide()");
		threadControlFrame.setVisible(false);	
	}

	public static void addObserver(Observer o)
	{
	   windowObservable.addObserver(o);
	}

	static class WindowObservable extends ObservableDebug
	   implements WindowListener
	{
	   public void windowClosing(WindowEvent e)
	   {
	      println("ThreadDebugger.windowClosing()");
	      setChanged();
	      notifyObservers("thread_debugger_close");
	   }
	   public void windowOpened(WindowEvent e)
	   {
	   }
	   public void windowClosed(WindowEvent e)
	   {
	   }
	   public void windowIconified(WindowEvent e)
	   {
	   }
	   public void windowDeiconified(WindowEvent e)
	   {
	   }
	   public void windowActivated(WindowEvent e)
	   {
	   }
	   public void windowDeactivated(WindowEvent e)
	   {
	   }
	}
}	
/**
 * An entry in the Hash of debuggable threads.
 * 
 * @author andruid
 */

class ThreadEntry
{
   Object	lock	= new Object();
   JButton	button;
   Thread	thread;
   boolean paused = false;
   
   ThreadEntry(Thread thread)
   {
      this.thread	= thread;
      button = new JButton("Pause " + thread.getName());
      button.addActionListener(ThreadDebugger.threadToggler);						
   }
   
   public	boolean toggleAndReturnNewState()
   {
      paused = !paused;
      
      if (!paused)
      {
	 button.setText("Pause " + thread.getName());
      }
      else
      {
	 button.setText("Start " + thread.getName());			
      }
      return paused;						
   }		
}

