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
   
   /**
    * Create a Scope with no parent, using default HashMap size and loadFactor.
    */
   public Scope()
   {
	   
   }
   
   /**
    * Create a lexical Scope, chained to a parent Scope for resolving lookup/get.
    * Use the default HashMap size and loadFactor.
    * 
    * @param parent
    */
   public Scope(Map<String, T> parent)
   {
	   this.parent	= parent;
   }

   /**
    * Create a Scope with no parent, using the specified HashMap size and default loadFactor.
    * 
    * @param size
    */
   public Scope(int size)
   {
	   super(size);
   }

   /**
    * Create a lexical Scope, chained to a parent Scope for resolving lookup/get.
    * Use the specified HashMap size and default loadFactor.
    * 
    * @param size
    */
   public Scope(Map<String, T> parent, int size)
   {
	   super(size);
	   this.parent	= parent;
  }

   /**
    * Create a Scope with no parent, using the specified HashMap size and loadFactor.
    * 
    * @param size
    * @param loadFactor
    */
   public Scope(int size, float loadFactor)
   {
	   super(size, loadFactor);
   }

   /**
    * Create a lexical Scope, chained to a parent Scope for resolving lookup/get.
    * Use the specified HashMap size and loadFactor.
    * 
    * @param parent
    * @param size
    * @param loadFactor
    */
   public Scope(Map<String, T> parent, int size, float loadFactor)
   {
	   super(size, loadFactor);
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
		T result	= super.get(name);
		Map<String, T> operativeParent = parent;
		return (result != null) ? result : ((operativeParent != null) ? operativeParent.get(name) : null);
	}
    /**
     * Enable this Scope to inherit bindings from a parent, by setting the parent instance variable in this.
     * 
     * @param newParent
     */
    public void setParent(Map<String, T> newParent)
    {
    	Map<String, T> thisParent = this.parent;
		if (thisParent != null)
    		Debug.warning(this, "Setting parent to " + newParent + " but it was already " + thisParent);

    	this.parent		= newParent;
    }
    
    /**
     * The chained parent Map used for resolving lookup/get operations, if they cannot be resolved in this.
     * 
     * @return		The parent instance variable in this.
     */
    public Map<String, T> operativeParent()
    {
    	return this.parent;
    }

	private static final long serialVersionUID = 5840169416933494011L;
}
