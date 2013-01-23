package ecologylab.logging;


/**
 * A simple logger factory interface.
 * 
 * @author quyin
 *
 */
public interface ILoggerFactory
{

  ILogger getLogger(String name);

  ILogger getLogger(Class clazz);
  
}
