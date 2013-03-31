package simpl.translators.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import simpl.translation.api.DependencyTranslator;
import simpl.translation.api.SourceAppender;
import simpl.translation.api.SourceCodeAppender;

public class JavaDependencyTranslator implements DependencyTranslator {	
	/**
	 * All dependencies that every file should include. 
	 */
	@Override
	public List<String> getGlobalDependencies() {
			return new ArrayList<String>();///List.class.getName(), Map.class.getName());
	}

	/**
	 * Translates a dependency for a given class to an import statement
	 */
	@Override
	public SourceAppender translateDependency(String s) {
		SourceAppender sa = new SourceCodeAppender();
		
		if(shouldExcludeDependency(s))
		{
			return sa;
		}
		
		return sa.append("import " + s + ";");
	}
	
	public boolean shouldExcludeDependency(String s)
	{
		return s.startsWith("java.lang.");
	}

	@Override
	public SourceAppender translateClassDependencies(Collection<String> classDependencies) {
		
		SourceAppender sa = new SourceCodeAppender();
		
		for(String dep : getGlobalDependencies())
		{
			sa.append(translateDependency(dep));
		}
		
		for(String dep : classDependencies)
		{
			sa.append(translateDependency(dep));
		}
			
		return sa;
	}
}
