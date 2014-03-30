package ecologylab.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ecologylab.generic.Continuation;

/**
 * A utility that allows returning a Future object from a DownloadMonitor, which can be handy in
 * some use cases.
 * 
 * @author quyin
 */
public class DownloadMonitorResult<T extends Downloadable> implements Future<T>, Continuation<T>
{

  private DownloadMonitor<T> downloadMonitor;

  private T                  downloadable;

  private boolean            done;

  private boolean            cancelled;

  public DownloadMonitorResult(DownloadMonitor<T> downloadMonitor, T downloadable)
  {
    this.downloadMonitor = downloadMonitor;
    this.downloadable = downloadable;
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning)
  {
    if (!done && !cancelled)
    {
      downloadMonitor.cancelDownload(downloadable);
      cancelled = true;
      return true;
    }
    return false;
  }

  @Override
  public boolean isCancelled()
  {
    return cancelled;
  }

  @Override
  public boolean isDone()
  {
    return done;
  }

  @Override
  public T get() throws InterruptedException, ExecutionException
  {
    try
    {
      return get(10, TimeUnit.MINUTES);
    }
    catch (TimeoutException e)
    {
      return null;
    }
  }

  @Override
  public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
      TimeoutException
  {
    synchronized (this)
    {
      if (!done)
      {
        this.wait(unit.toMillis(timeout));
      }
      return downloadable;
    }
  }

  @Override
  public void callback(T o)
  {
    synchronized (this)
    {
      done = true;
      downloadable = o;
      this.notifyAll();
    }
  }

}
