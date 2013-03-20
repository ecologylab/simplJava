package simpl.interpretation;

/**
 * Identity class, inspired by IDKey in Apache Commons. 
 * @author tom
 *
 */
public class ObjectIdentity {

	final public Object value;
	final public int hashcode;
	
	public ObjectIdentity(Object anObject)
	{
		this.hashcode = System.identityHashCode(anObject);
		this.value = anObject;
	}
	
	@Override 
	public boolean equals(Object otherObject)
	{
		if(otherObject instanceof ObjectIdentity)
		{
			ObjectIdentity otherID = (ObjectIdentity)otherObject;
			
			if(this.hashcode != otherID.hashcode)
			{
				return false;
			}
			return this.value == otherID.value; // compare object references.
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public int hashCode()
	{
		return this.hashcode;
	}
}
