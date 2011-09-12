/**
 * 
 */
package ecologylab.serialization;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import ecologylab.generic.ResourcePool;

/**
 * @author toupsz
 * 
 */
public class XMLReaderPool extends ResourcePool<XMLReader>
{

	/**
	 * @param instantiateResourcesInPool
	 * @param initialPoolSize
	 * @param minimumPoolSize
	 */
	public XMLReaderPool(boolean instantiateResourcesInPool, int initialPoolSize, int minimumPoolSize)
	{
		super(instantiateResourcesInPool, initialPoolSize, minimumPoolSize, false);
	}

	/**
	 * @param initialPoolSize
	 * @param minimumPoolSize
	 */
	public XMLReaderPool(int initialPoolSize, int minimumPoolSize)
	{
		super(initialPoolSize, minimumPoolSize);
	}

	/**
	 * Does nothing; XMLReaders CANNOT be cleaned.
	 * 
	 * @see ecologylab.generic.ResourcePool#clean(java.lang.Object)
	 */
	@Override
	protected void clean(XMLReader objectToClean)
	{
	}

	@Override
	protected XMLReader generateNewResource()
	{
		// TODO Auto-generated method stub
		return null;
	}

	// /**
	// * @see ecologylab.generic.ResourcePool#generateNewResource()
	// */
	// @Override protected XMLReader generateNewResource()
	// {
	// try
	// {
	// return ElementStateSAXHandler.createXMLReader();
	// }
	// catch (SAXException e)
	// {
	// e.printStackTrace();
	// }
	//		
	// return null;
	// }
}
