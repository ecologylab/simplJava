package ecologylab.logging;

/**
 * A basic logger that logs to console output.
 * 
 * @author quyin
 *
 */
public class SimpleLogger extends AbstractLogger
{
  
  String name;
  
  SimpleLogger(String name)
  {
    this.name = name;
  }

  @Override
  public void log(LogLevel level, String fmt, Object... args)
  {
    String msg = String.format(fmt, args);
    switch (level)
    {
    case DEBUG:
    case INFO:
      System.out.format("%s\t%s\t%s", now(), name, msg);
      break;
    case WARNING:
    case ERROR:
    case FATAL:
      System.err.format("%s\t%s\t%s", now(), name, msg);
      break;
    }
  }

}
