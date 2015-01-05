package ecologylab.logging;

import ecologylab.serialization.SimplTypesScope;

/**
 * Type scope for log events 
 * 
 * @author quyin
 */
@SuppressWarnings("rawtypes")
public class LogEventTypeScope
{

  public static final String     NAME    = "logging_events_translations";

  private static Class[]         classes = {
                                             LogEvent.class,
                                             LogPost.class,
                                           };

  private static SimplTypesScope scope;

  static
  {
    scope = SimplTypesScope.get(NAME, classes);
  }

  public static SimplTypesScope get()
  {
    return scope;
  }

  public static void addEventClass(Class<? extends LogEvent> eventClass)
  {
    scope.addTranslation(eventClass);
  }

}
