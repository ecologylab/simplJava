package ecologylab.concurrent;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import ecologylab.logging.LogEvent;
import ecologylab.logging.LogPost;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.StringFormat;

public class TestDownloadMonitor
{

  DownloadMonitor<FakeDownloadable> dm;

  @Before
  public void setup()
  {
    dm = new DownloadMonitor<FakeDownloadable>("test-dm", 3);
  }

  @Test
  public void testLogEvents() throws InterruptedException, ExecutionException
  {
    FakeDownloadable d = new FakeDownloadable(ParsedURL.getAbsolute("http://example.com/1"));
    DownloadMonitorResult<FakeDownloadable> result = dm.downloadAndWait(d);
    d = result.get();
    DownloadableLogRecord logRecord = d.getLogRecord();
    assertNotNull(logRecord);
    LogPost logPost = logRecord.getLogPost();
    assertNotNull(logPost);
    List<LogEvent> events = logPost.getEvents();
    assertTrue(events.size() >= 2);
    assertTrue(events.get(0) instanceof EnqueueEvent);
    assertTrue(events.get(0).getTimestamp() > 0);
    assertTrue(events.get(events.size() - 1) instanceof DownloadEvent);
    assertTrue(events.get(events.size() - 1).getTimestamp() > 0);
    SimplTypesScope.serializeOut(logRecord, "", StringFormat.XML);
  }

}
