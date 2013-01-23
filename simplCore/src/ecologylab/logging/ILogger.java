package ecologylab.logging;

/**
 * A simple logger interface.
 * 
 * @author quyin
 *
 */
public interface ILogger
{

  void debug(String fmt, Object... args);

  void info(String fmt, Object... args);

  void warn(String fmt, Object... args);

  void error(String fmt, Object... args);

  void fatal(String fmt, Object... args);

  void log(LogLevel level, String fmt, Object... args);

}
