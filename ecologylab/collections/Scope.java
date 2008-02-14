package ecologylab.collections;

import java.util.HashMap;
import java.util.Map;

import ecologylab.generic.Debug;

/**
 * A lexical Scope of bindings from names to values.
 * Lexical Scopes can be nested. However, bind operations are only performed on the current Scope.
 * Lookup operations chain through parents, as necessary, when a binding is not found locally.
 */
public class Scope<T> extends HashMap<String, T>
{
   private		Map<String, T>				parent;
   
   public Scope()
   {
	   
   }
   
   public Scope(Map<String, T> parent)
   {
	   this.parent	= parent;
   }

   /**
	 * Lookup an object in this.
	 * 
	 * @param name
	 * 
	 * @return	The object associated with this name, found in the registry,
	 * 			or null if there is none.
	 */
	public T get(Object name)
	{
		T result = super.get(name);
		return (result != null) ? result : ((parent != null) ? parent.get(name) : null);
	}
    /**
     * Enable this Scope to inherit bindings from a parent.
     * 
     * @param parent
     */
    public void setParent(Map<String, T> parent)
    {
    	if (this.parent != null)
    		Debug.warning(this, "Setting parent to " + parent + " but it was already " + this.parent);
    	
    	this.parent	= parent;
    }
    
	private static final long serialVersionUID = 5840169416933494011L;
}
