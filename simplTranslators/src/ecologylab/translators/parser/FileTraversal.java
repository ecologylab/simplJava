package ecologylab.translators.parser;

import java.io.File;
import java.io.IOException;

import ecologylab.serialization.ElementState;

/**
 * Class file to search for java source files. Using the Class type objects this class searches for
 * source files inside a root directory recursively. This class is being used by the JavaDocParser
 * to fetch java doc comments from source files.
 * 
 * @author nabeel
 */
public class FileTraversal
{
	/**
	 * Object which persists during recursive file search.
	 */
	private File													searchedFile	= null;

	/**
	 * inputClass which is set when searching for the file.
	 */
	private Class<?>	inputClass;

	/**
	 * Default constructor. does nothing
	 */
	public FileTraversal()
	{

	}

	/**
	 * Searches for a given inside a root directory.
	 * 
	 * @param thatClass
	 * @param rootLocation
	 * @return File null if the search did not yield any result. 
	 * @throws IOException
	 */
	public File searchForFile(Class<?> thatClass, File rootLocation)
			throws IOException
	{
		this.searchedFile = null;
		this.inputClass = thatClass;
		this.traverse(rootLocation);

		return searchedFile;
	}

	/**
	 * Returns the search file. Use
	 * <code> File searchForFile(Class<? extends ElementState> thatClass, File rootLocation) </code>
	 * before using this method. If this value is null than the file was not found.
	 * 
	 * @return File
	 */
	public File getSearchedFile()
	{
		return searchedFile;
	}

	/**
	 * Recursive method to traverse through each file in the root directory.
	 * 
	 * @param file
	 * @throws IOException
	 */
	private final void traverse(final File file) throws IOException
	{
		if (file.isDirectory())
		{
			onDirectory(file);
			final File[] childs = file.listFiles();
			for (File child : childs)
			{
				traverse(child);
			}
			return;
		}
		onFile(file);
	}

	/**
	 * This method is invoked if the search passes through a directory.
	 * 
	 * @param directory
	 */
	private void onDirectory(final File directory)
	{
	}

	/**
	 * This method is invoked if the search passes through a file.
	 * 
	 * @param targetFile
	 */
	private void onFile(final File targetFile)
	{
		if (targetFile.getName().equals(inputClass.getSimpleName() + ".java"))
			if (belongsToSamePackage(inputClass, targetFile))
				searchedFile = targetFile;
	}

	/**
	 * Utility method to check if the found source file also belongs to the same package as input
	 * class.
	 * 
	 * @param thatClass
	 * @param targetFile
	 * @return boolean
	 */
	private boolean belongsToSamePackage(Class<?> thatClass, File targetFile)
	{
		boolean result = true;

		File lastParentFile = targetFile.getParentFile();
		String[] packageSpecifierArray = thatClass.getPackage().getName().split("\\.");

		for (int i = packageSpecifierArray.length - 1; i >= 0; i--)
		{
			String packageName = packageSpecifierArray[i];
			if (packageName.equals(lastParentFile.getName()))
			{
				lastParentFile = lastParentFile.getParentFile();
			}
			else
			{
				result = false;
				break;
			}
		}

		return result;
	}
}
