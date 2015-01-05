package ecologylab.simpl.translators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Default implementation of DependencyTracker.
 * 
 * @author quyin
 * @author twhite
 */
public class DefaultDependencyTracker implements DependencyTracker
{

	private Set<String>	trackedDependencies	= new HashSet<String>();

	public void addDependency(String dependency)
	{
		if (dependency == null)
		{
			throw new NullPointerException("Dependency cannot be null!");
		}
		this.trackedDependencies.add(dependency);
	}

	public void addDependencies(Collection<String> dependencies)
	{
		if (dependencies == null)
		{
			throw new NullPointerException("Dependencies cannot be null!");
		}
		this.trackedDependencies.addAll(dependencies);
	}

	public Set<String> getDependencies()
	{
		return trackedDependencies;
	}

	@Override
	public List<String> getOrderedDependencies()
	{
		List<String> result = new ArrayList<String>(trackedDependencies);
		Collections.sort(result);
		return result;
	}

}
