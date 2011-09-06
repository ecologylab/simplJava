package ecologylab.serialization.deserializers.pushhandlers;

import java.io.File;
import java.util.Collection;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.deserializers.parsers.bibtex.BibTeXEvents;

public class ElementStateBibTeXHandler extends Debug implements BibTeXEvents,
		ScalarUnmarshallingContext
{

	ParsedURL						purlContext;

	File								fileContext;

	TranslationScope		tscope;

	TranslationContext	tcontext;

	ElementState				root	= null;

	String							currentTag;

	FieldDescriptor			currentFD;

	public ElementStateBibTeXHandler(TranslationScope tscope)
	{
		this(tscope, null);
	}

	public ElementStateBibTeXHandler(TranslationScope tscope, TranslationContext tcontext)
	{
		this.tscope = tscope;
		this.tcontext = (tcontext == null) ? new TranslationContext() : tcontext;
	}

	@Override
	public void startBibTeX()
	{
		// System.out.println("started:");
		root = null;
	}

	@Override
	public void startEntity(String typeName)
	{
		// System.out.println("entity: " + typeName);
		if (root == null && typeName != null)
		{
			ClassDescriptor rootClassD = tscope.getClassDescriptorByBibTeXType(typeName.toLowerCase());
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
		// System.out.println("key: " + key);
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
		// System.out.println("tag: " + tagName);
		if (root != null && tagName != null)
		{
			currentTag = tagName.toLowerCase();
			currentFD = root.classDescriptor().getFieldDescriptorByBibTeXTag(tagName);
			if (currentFD == null)
			{
				warning("ignoring bibTeX tag: " + tagName);
			}
		}
	}

	@Override
	public void endTag()
	{
		// System.out.println("endtag;");
		currentFD = null;
		currentTag = null;
	}

	@Override
	public void value(String value)
	{
		// System.out.println("value: " + value);
		if (currentFD != null && root != null)
		{
			int type = currentFD.getType();

			switch (type)
			{
			case FieldDescriptor.SCALAR:
				currentFD.setFieldToScalar(root, value, this);
				break;
			case FieldDescriptor.COLLECTION_SCALAR:
				Collection collection = (Collection) currentFD.automaticLazyGetCollectionOrMap(root);
				if ("author".equals(currentTag))
				{
					String[] authorNames = value.split("and");
					for (String authorName : authorNames)
					{
						collection.add(authorName.trim());
					}
				}
				else
				{
					String[] items = value.split(tcontext.getDelimiter());
					for (String item : items)
					{
						collection.add(item);
					}
				}
				break;
			}
		}
	}

	@Override
	public void endEntity()
	{
		// System.out.println("endentity;");
		root = null;
	}

	@Override
	public void endBibTeX()
	{
		// System.out.println("end.");
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
