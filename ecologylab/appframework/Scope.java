package ecologylab.appframework;

import java.util.Collection;
import java.util.HashMap;

/**
 * A lexical Scope of bindings from names to values.
 * Lexical Scopes can be nested. However, bind operations are only performed on the current Scope.
 * Lookup operations chain through parents, as necessary, when a binding is not found locally.
 * <p/>
 * These replace statics and are much more flexible.
 */
public class Scope<T>
{
   /**
    * Map of bindings from names to values.
    */
   private		HashMap<String, T>		bindingsMap	= new HashMap<String, T>();
   
   private		Scope<T>				parent;
   
   public Scope()
   {
	   
   }
   
   public Scope(Scope<T> parent)
   {
	   this.parent	= parent;
   }
   
   
	/**
	 * Bind an object to a name.
     * 
	 * @param name
	 * @param value
	 * 
	 * @return Previous value, or null if there wasn't one.
	 */
	public T bind(String name, T value)
	{
		return bindingsMap.put(name, value);
	}
	/**
	 * Lookup an object in this.
	 * 
	 * @param name
	 * 
	 * @return	The object associated with this name, found in the registry,
	 * 			or null if there is none.
	 */
	public T lookup(String name)
	{
		T result = bindingsMap.get(name);
		return (result != null) ? result : ((parent != null) ? parent.lookup(name) : null);
	}
   
	/**
	 * 
	 * @param key
	 * @return	true if there is already a binding for key in this Scope.
	 */
    public boolean isBound(String key)
    {
    	return bindingsMap.containsKey(key);
    }
    
    /**
     * 
     * @return	A Collection of all the bound values in this.
     */
    public Collection<T> values()
    {
    	return bindingsMap.values();
    }
 
    /**
     * 
     * @return	A Collection of all the names to which values are bound in this.
     */
    public Collection<String> keySet()
    {
    	return bindingsMap.keySet();
    }
}
