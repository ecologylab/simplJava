package simpl.core;

import java.io.File;

import ecologylab.net.ParsedURL;

public class ImageSerializationContext extends TranslationContext
{
	int index = 0;
	protected String parentFolder;

	public ImageSerializationContext()
	{
		// TODO Auto-generated constructor stub
	}

	public ImageSerializationContext(File fileDirContext)
	{
		super(fileDirContext);
		// TODO Auto-generated constructor stub
	}

	public ImageSerializationContext(ParsedURL purlContext)
	{
		super(purlContext);
		// TODO Auto-generated constructor stub
	}
	
	public File getPastedImageFileLocation()
	{
		File result = new File(parentFolder + "/pasted-" + index + ".png");
		return result;
	}
	
	public void iterate()
	{
		index++;
	}
	
	public int getIndex()
	{
		return index;
	}

}
