package ecologylab.logging;

import java.util.ArrayList;
import java.util.List;

import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_scope;

/**
 * A list of log events that share common app / username / hashkey.
 * 
 * It is thread safe to add events from multiple threads.
 * 
 * @author quyin
 */
public class LogPost
{

  @simpl_scalar
  private String         app;

  @simpl_scalar
  private String         username;

  @simpl_scalar
  private String         hashKey;

  @simpl_collection
  @simpl_scope(LogEventTypeScope.NAME)
  private List<LogEvent> events;

  public LogPost()
  {
    // Default constructor must be empty, for deserialization.
  }

  public String getApp()
  {
    return app;
  }

  public void setApp(String app)
  {
    this.app = app;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

  public String getHashKey()
  {
    return hashKey;
  }

  public void setHashKey(String hashKey)
  {
    this.hashKey = hashKey;
  }

  public List<LogEvent> getEvents()
  {
    return events;
  }

  public void addEvent(LogEvent event)
  {
    if (events == null)
    {
      synchronized (this)
      {
        if (events == null)
        {
          events = new ArrayList<LogEvent>();
        }
      }
    }

    synchronized (events)
    {
      events.add(event);
    }
  }

  public void addEventNow(LogEvent event)
  {
    event.setTimestamp(System.currentTimeMillis());
    addEvent(event);
  }

  public void addEvents(LogPost logPost)
  {
    if (logPost != null)
    {
      List<LogEvent> eventsToAdd = logPost.getEvents();
      addEvents(eventsToAdd);
    }
  }

  public void addEvents(List<LogEvent> eventsToAdd)
  {
    if (eventsToAdd != null)
    {
      for (LogEvent eventToAdd : eventsToAdd)
      {
        addEvent(eventToAdd);
      }
    }
  }

}
