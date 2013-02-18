package simpl.descriptions.beiber;
import java.util.Collection;

/**
 * Provides meta information about a given described entity.
 */
public interface IMetaInformationProvider {
	/**
	 * Adds an entry of metadata to the provider
	 */
	public void addMetaInformation(IMetaInformation imo);
	/**
	 * Gets a collection of meta information 
	 * @return
	 */
	public Collection<IMetaInformation> getMetaInformation();
	
	/**
	 * Returns true if the meta information is contained in the getMetaInformation collection, marshalled by name
	 * @param name
	 * @return
	 */
	public boolean containsMetaInformation(String name);
	
	/**
	 * Returns the MetaInformation at "name", or null if it does not exist. 
	 * @param name
	 * @return
	 */
	public IMetaInformation getMetaInformation(String name);
}
 