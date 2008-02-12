/**
 * 
 */
package ecologylab.appframework;

/**
 *
 * @author andruid
 */
public class ClassRegistry<U> extends Scope<Class<U>>
{

	/**
	 * 
	 */
	public ClassRegistry()
	{
		super();

	}

    public U lookupInstance(String key)
    {
    	return getInstance(this.lookup(key));
    }
    
    /**
     * Get a DocumentType from a generic parameterized Class object.
     * 
     * @param thatClass
     * @return
     */
  	public U getInstance(Class<? extends U> thatClass)
   	{
  		U result		= null;
   		if (thatClass != null)
   		{
   			try
   			{
   				result        	= thatClass.newInstance();
   			} catch (InstantiationException e)
   			{
   				e.printStackTrace();
   			} catch (IllegalAccessException e)
   			{
   				e.printStackTrace();
   			}
   		}
   		return result;
   	}
	
	
}
