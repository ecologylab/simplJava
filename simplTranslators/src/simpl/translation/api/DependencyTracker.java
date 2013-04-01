package simpl.translation.api;

import java.util.Collection;
import java.util.Set;

/**
 * An interface that represents an entity that tracks source dependencies.
 */
public interface DependencyTracker {
	/**
	 * Adds a dependency to this DependencyTracker
	 * @param dependency The full path/package name to the dependency
	 */
	void addDependency(String dependency);
	
	/**
	 * Adds a collection of dependencies to this DependencyTracker
	 * @param dependencies A collection of full path/package names to dependencies
	 */
	void addDependencies(Collection<String> dependencies);
	
	/**
	 * Aggregate the dependencies that a given tracker may have. 
	 * (For instance, a field has a metainformationtranslator. 
	 * Grab the dependencies from the metainformation translator in the aggregateDependencies call
	 * @return
	 */
	Set<String> aggregateDependencies(); 
	/**
	 * Aggregates the dependencies that other trackers have, and then combines them with the dependencies
	 * tracked by this tracker. 
	 * @return
	 */
	Set<String> getDependencies();
}
