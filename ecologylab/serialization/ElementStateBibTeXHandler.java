package ecologylab.serialization;

import java.io.File;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.bibtex.BibTeXEvents;

public class ElementStateBibTeXHandler extends Debug implements BibTeXEvents,
		ScalarUnmarshallingContext
{

	ParsedURL					purlContext;

	File							fileContext;

	TranslationScope	tscope;

	ElementState			root	= null;

	FieldDescriptor		currentFD;

	public ElementStateBibTeXHandler(TranslationScope tscope)
	{
		this.tscope = tscope;
	}

	@Override
	public void startBibTeX()
	{
//		System.out.println("started:");
		root = null;
	}

	@Override
	public void startEntity(String typeName)
	{
//		System.out.println("entity: " + typeName);
		if (root == null)
		{
			ClassDescriptor rootClassD = tscope.getClassDescriptorByBibTeXType(typeName);
			if (rootClassD != null)
			{
				try
				{
					root = rootClassD.getInstance();
				}
				catch (SIMPLTranslationException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				warning("no ClassDescriptor found for bibTeX type " + typeName);
			}
		}
	}

	@Override
	public void key(String key)
	{
//		System.out.println("key: " + key);
		if (root != null)
		{
			FieldDescriptor keyFD = root.classDescriptor().getFieldDescriptorForBibTeXKey();
			if (keyFD != null)
			{
				keyFD.setFieldToScalar(root, key, this);
			}
		}
	}

	@Override
	public void startTag(String tagName)
	{
//		System.out.println("tag: " + tagName);
		if (root != null)
		{
			currentFD = root.classDescriptor().getFieldDescriptorByBibTeXTag(tagName);
			if (currentFD == null)
				warning("ignoring bibTeX tag: " + tagName);
		}
	}

	@Override
	public void endTag()
	{
//		System.out.println("endtag;");
		currentFD = null;
	}

	@Override
	public void value(String value)
	{
//		System.out.println("value: " + value);
		if (currentFD != null && root != null)
		{
			currentFD.setFieldToScalar(root, value, this);
		}
	}

	@Override
	public void endEntity()
	{
//		System.out.println("endentity;");
		root = null;
	}

	@Override
	public void endBibTeX()
	{
//		System.out.println("end.");
	}
	
	@Override
	public ElementState getBibTeXObject()
	{
		return root;
	}

	@Override
	public ParsedURL purlContext()
	{
		return purlContext;
	}

	@Override
	public File fileContext()
	{
		if (fileContext != null)
			return fileContext;
		ParsedURL purlContext = purlContext();
		if (purlContext != null)
		{
			return purlContext.file();
		}
		return null;
	}

}
