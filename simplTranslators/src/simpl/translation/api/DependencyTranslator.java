package simpl.translation.api;

import java.util.Collection;
import java.util.List;

/**
 * Translates a list of dependency to the appropriate set of usings / imports for a given language.
 * Also manages global dependencies for a certain language. 
 * @author twhite
 */
public interface DependencyTranslator {
	List<String> getGlobalDependencies();
	SourceAppender translateDependency(String s);
	SourceAppender translateClassDependencies(Collection<String> a);
}
