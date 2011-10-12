/**
 * 
 */
package ecologylab.serialization;

import java.io.File;

import ecologylab.oodss.exceptions.SaveFailedException;
import ecologylab.serialization.annotations.simpl_inherit;

/**
 * This class is configured with a file path that serves as its backing store and provides a save()
 * method. Invoking the save method will cause this object to write itself to the file.
 * 
 * Provides a field for backingFilePath, which is automatically translated to an absolute path.
 * 
 * Overrides translateFrom to automatically set its backing file.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class SaverState extends ElementState
{
	protected static final String	XML_FILE_SUFFIX	= ".xml";

	/**
	 * Translate a file from XML to a strongly typed tree of XML objects.
	 * 
	 * Use SAX or DOM parsing depending on the value of useDOMForTranslateTo.
	 * 
	 * @param xmlFile
	 *          XML source material.
	 * @param translationScope
	 *          Specifies mapping from XML nodes (elements and attributes) to Java types.
	 * 
	 * @return Strongly typed tree of ElementState objects.
	 * @throws SIMPLTranslationException
	 */
	public static SaverState translateFromXML(File xmlFile, SimplTypesScope translationScope)
			throws SIMPLTranslationException
	{
		SaverState saverState = (SaverState) translationScope.deserialize(xmlFile, Format.XML);
		saverState.setBackingFilePath(xmlFile.getAbsolutePath());

		return saverState;
	}

	/**
	 * Translate a file XML to a strongly typed tree of XML objects.
	 * 
	 * Use SAX or DOM parsing depending on the value of useDOMForTranslateTo.
	 * 
	 * @param fileName
	 *          the name of the XML file that needs to be translated.
	 * @param translationScope
	 *          Specifies mapping from XML nodes (elements and attributes) to Java types.
	 * 
	 * @return Strongly typed tree of ElementState objects.
	 * @throws SIMPLTranslationException
	 */
	public static ElementState translateFromXML(String fileName, SimplTypesScope translationScope)
			throws SIMPLTranslationException
	{
		SaverState saverState = (SaverState) translationScope.deserialize(new File(fileName),
				Format.XML);
		saverState.setBackingFilePath(fileName);

		return saverState;
	}

	/**
	 * A path to the backing file for this object. If not specified, the call to save() will fail with
	 * a SaveFailedException. This is specified by providing it in the constructor (for new objects)
	 * or by calling the SaverState.translateFrom methods.
	 */
	private String	backingFilePath;

	/** Lazily instantiated File for storing this object. */
	private File		backingFile;

	/**
	 * No-argument constructor for XML translation.
	 */
	public SaverState()
	{
		super();
	}

	public SaverState(String backingFilePath)
	{
		this.setBackingFilePath(backingFilePath);
	}

	public synchronized void save() throws SaveFailedException
	{
		if (this.backingFilePath == null)
			throw new SaveFailedException("Backing file path not set. Could not save.");

		try
		{
			SimplTypesScope.serialize(this, this.backingFile(), Format.XML);
		}
		catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
			throw new SaveFailedException("Could not write SaverState to file system.", e);
		}
	}

	/**
	 * @return the backingFilePath
	 */
	public String getBackingFilePath()
	{
		return backingFilePath;
	}

	/**
	 * @param backingFilePath
	 *          the backingFilePath to set
	 */
	public void setBackingFilePath(String backingFilePath)
	{
		this.backingFile = null;
		this.backingFilePath = backingFilePath;
	}

	/**
	 * Called when saving to get the file to save to. Can be overridden to provide a specific file, if
	 * desired.
	 * 
	 * @return
	 */
	protected File backingFile()
	{
		if (backingFile == null)
		{
			synchronized (this)
			{
				if (backingFile == null)
				{
					this.backingFile = new File(getBackingFilePath());
				}
			}
		}

		return this.backingFile;
	}
}
