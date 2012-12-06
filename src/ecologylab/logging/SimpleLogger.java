package ecologylab.logging;

import java.io.PrintStream;

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
    PrintStream s = null;
    switch (level)
    {
    case DEBUG:
    case INFO:
      s = System.out;
      break;
    case WARNING:
    case ERROR:
    case FATAL:
      s = System.err;
      break;
    }
    s.format("%s %s [%s]: %s", now(), level.toString(), name, msg);
  }

}
