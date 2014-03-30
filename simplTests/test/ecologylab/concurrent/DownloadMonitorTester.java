package ecologylab.concurrent;

import java.io.IOException;
import java.util.Random;

import ecologylab.generic.Continuation;
import ecologylab.net.ParsedURL;

/**
 * Run fake tasks through DownloadMonitor to check for problems.
 * 
 * @author quyin
 */
public class DownloadMonitorTester
{

  static String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz ";

  static Random r     = new Random();

  static class Task implements Downloadable
  {

    ParsedURL location;

    char[]    buf;

    public Task(ParsedURL location)
    {
      this.location = location;
    }

    @Override
    public void performDownload() throws IOException
    {
      buf = new char[10 * 1024 * 1024];
      for (int i = 0; i < buf.length; ++i)
      {
        buf[i] = CHARS.charAt(r.nextInt(CHARS.length()));
      }
    }

    @Override
    public void handleIoError(Throwable e)
    {
      System.err.println(e);
    }

    @Override
    public boolean isRecycled()
    {
      return false;
    }

    @Override
    public void recycle()
    {
    }

    @Override
    public Site getSite()
    {
      return null;
    }

    @Override
    public Site getDownloadSite()
    {
      return null;
    }

    @Override
    public ParsedURL location()
    {
      return location;
    }

    @Override
    public ParsedURL getDownloadLocation()
    {
      return location;
    }

    @Override
    public boolean isImage()
    {
      return false;
    }

    @Override
    public String message()
    {
      return null;
    }

    @Override
    public boolean isCached()
    {
      return false;
    }

    @Override
    public DownloadableLogRecord getLogRecord()
    {
      return null;
    }

  }

  public void runFakeTasks()
  {
    DownloadMonitor<Task> dm = new DownloadMonitor<Task>("test", 2);
    for (int i = 0; i < 10; ++i)
    {
      dm.download(new Task(ParsedURL.getAbsolute("http://example.com/pages/" + i)),
                  new Continuation<Task>()
                  {
                    @Override
                    public void callback(Task o)
                    {
                      System.out.println(o.location() + " downloaded.");
                    }
                  });
    }
    dm.requestStop();
  }

  public static void main(String[] args)
  {
    System.out.println("Run this program with heap size <=100M (-Xms100m -Xmx100m).");
    DownloadMonitorTester dmt = new DownloadMonitorTester();
    dmt.runFakeTasks();
  }

}
