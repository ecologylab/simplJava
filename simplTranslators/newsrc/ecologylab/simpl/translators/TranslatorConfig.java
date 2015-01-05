package ecologylab.simpl.translators;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * Necessary configurations of a code translator.
 * 
 * @author quyin
 */
public class TranslatorConfig
{

	@simpl_scalar
	private String				typeLibraryClassNamespace;

	@simpl_scalar
	private String				typeLibraryClassSimpleName;

	@simpl_scalar
	private File					targetDir;

	@simpl_collection("excluded_class_name")
	private List<String>	excludedClassNames;

	public String getTypeLibraryClassNamespace()
	{
		return typeLibraryClassNamespace;
	}

	public void setTypeLibraryClassNamespace(String typeLibraryClassNamespace)
	{
		this.typeLibraryClassNamespace = typeLibraryClassNamespace;
	}

	public String getTypeLibraryClassSimpleName()
	{
		return typeLibraryClassSimpleName;
	}

	public void setTypeLibraryClassSimpleName(String typeLibraryClassSimpleName)
	{
		this.typeLibraryClassSimpleName = typeLibraryClassSimpleName;
	}

	public File getTargetDir()
	{
		return targetDir;
	}

	public void setTargetDir(File targetDir)
	{
		this.targetDir = targetDir;
	}

	public List<String> getExcludedClassNames()
	{
		return excludedClassNames;
	}

	public void setExcludedClassNames(List<String> excludedClassNames)
	{
		this.excludedClassNames = excludedClassNames;
	}

	public void addExcludedClassName(String excludedClassName)
	{
		if (excludedClassNames == null)
		{
			excludedClassNames = new ArrayList<String>();
		}
		if (!excludedClassNames.contains(excludedClassName))
		{
			excludedClassNames.add(excludedClassName);
		}
	}

}
