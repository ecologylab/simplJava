package ecologylab.simpl.translators;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Tracking dependencies.
 * 
 * @author quyin
 * @author twhite
 */
public interface DependencyTracker
{

	/**
	 * Add a single dependency.
	 * 
	 * @param dependency
	 */
	void addDependency(String dependency);

	/**
	 * Add a collection of dependencies.
	 * 
	 * @param dependencies
	 */
	void addDependencies(Collection<String> dependencies);

	/**
	 * @return All dependencies tracked by this dependency tracker.
	 */
	Set<String> getDependencies();

	/**
	 * @return All dependencies tracked by this depedency tracker, ordered alphabetically.
	 */
	List<String> getOrderedDependencies();

}
