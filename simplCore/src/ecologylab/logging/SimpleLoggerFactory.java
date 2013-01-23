package ecologylab.logging;

/**
 * A factory that produces BasicLoggers.
 * 
 * @author quyin
 *
 */
public class SimpleLoggerFactory implements ILoggerFactory
{

  @Override
  public ILogger getLogger(String name)
  {
    return new SimpleLogger(name);
  }

  @Override
  public ILogger getLogger(Class clazz)
  {
    return getLogger(clazz.getName());
  }

}
