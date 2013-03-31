package simpl.translation.api;

import java.util.Collection;
import java.util.Set;

public interface DependencyTracker {
	void addDependency(String dependency);
	void addDependencies(Collection<String> dependencies);
	
	/**
	 * Aggregate the dependnencies that a given tracker may have. 
	 * (For instance, a field has a metainformationtranslator. 
	 * Grab the dependencies from the metainformation translator in the aggregateDependencies call
	 * @return
	 */
	Set<String> aggregateDependencies(); 
	/**
	 * Aggregates the depedencies that other trackers have, and then combines them with the dependencies
	 * tracked by this tracker. 
	 * @return
	 */
	Set<String> getDependencies();
}
