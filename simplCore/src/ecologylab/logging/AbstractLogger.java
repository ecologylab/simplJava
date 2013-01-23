package ecologylab.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * For convenience.
 * 
 * @author quyin
 *
 */
public abstract class AbstractLogger implements ILogger
{

  protected static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");

  @Override
  public void debug(String fmt, Object... args)
  {
    log(LogLevel.DEBUG, fmt, args);
  }

  @Override
  public void info(String fmt, Object... args)
  {
    log(LogLevel.INFO, fmt, args);
  }

  @Override
  public void warn(String fmt, Object... args)
  {
    log(LogLevel.WARNING, fmt, args);
  }

  @Override
  public void error(String fmt, Object... args)
  {
    log(LogLevel.ERROR, fmt, args);
  }

  @Override
  public void fatal(String fmt, Object... args)
  {
    log(LogLevel.FATAL, fmt, args);
  }

  /**
   * Utility method.
   * 
   * @return The current date and time as a string.
   */
  protected String now()
  {
    Date d = new Date();
    return sdf.format(d);
  }
  
}
