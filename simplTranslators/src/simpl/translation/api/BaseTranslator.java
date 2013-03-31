package simpl.translation.api;

import java.util.HashSet;
import java.util.Set;
import java.util.Collection;


public abstract class BaseTranslator implements DependencyTracker {
	/**
	 * Holds onto the dependencies tracked by this translator.
	 */
	private Set<String> trackedDependencies = new HashSet<String>(); 
	
	@Override
	public void addDependency(String dependency) {
		this.trackedDependencies.add(dependency);
	}

	@Override
	public void addDependencies(Collection<String> dependencies) {
		this.trackedDependencies.addAll(dependencies);
	}
	
	/**
	 * If a translator has other depdendency trackers, get those dependencies. 
	 */
	public abstract Set<String> aggregateDependencies();

	@Override
	public Set<String> getDependencies() {
		this.trackedDependencies.addAll(aggregateDependencies());
		Set<String> toReturn = new HashSet<String>();
		toReturn.addAll(this.trackedDependencies);
		
		//reset this trackedDependencies for the next getDependency() call. 
		this.trackedDependencies = new HashSet<String>();
		
		// return the dependencies we had. 
		return toReturn;
	}
}
