/**
 * 
 */
package ecologylab.collections;


/**
 * Make a generic set element that stores an object in it.
 * 
 * @author andruid
 *
 */
public class GenericElement<GO> extends SetElement
{
	GO			genericObject;
	/**
	 * 
	 */
	public GenericElement(GO genericObject)
	{
		this.genericObject	= genericObject;
	}
	/**
	 * @return the clipping
	 */
	public GO getGeneric()
	{
		return genericObject;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof GenericElement)
		{
			GenericElement otherElement	= (GenericElement) other;
			return otherElement.genericObject.equals(genericObject);
		}
		return false;
	}

}
