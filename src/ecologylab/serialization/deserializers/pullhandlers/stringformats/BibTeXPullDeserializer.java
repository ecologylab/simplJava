package ecologylab.serialization.deserializers.pullhandlers.stringformats;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.deserializers.parsers.bibtex.BibTeXEvents;

public class BibTeXPullDeserializer extends StringPullDeserializer implements BibTeXEvents,
		ScalarUnmarshallingContext
{

	ParsedURL						purlContext;

	File								fileContext;

	Object							root	= null;

	String							currentTag;

	FieldDescriptor			currentFD;

	public BibTeXPullDeserializer(SimplTypesScope tscope)
	{
		this(tscope, null);
	}

	public BibTeXPullDeserializer(SimplTypesScope tscope, TranslationContext tcontext)
	{
		super(tscope, tcontext);
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
			ClassDescriptor rootClassD = translationScope.getClassDescriptorByBibTeXType(typeName.toLowerCase());
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
			FieldDescriptor keyFD = ClassDescriptor.getClassDescriptor(root)
					.getFieldDescriptorForBibTeXKey();
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
			currentFD = ClassDescriptor.getClassDescriptor(root).getFieldDescriptorByBibTeXTag(tagName);
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
					String[] items = value.split(translationContext.getDelimiter());
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
	public Object getBibTeXObject()
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

	@Override
	public Object parse(CharSequence charSequence) throws SIMPLTranslationException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object parse(InputStream inputStream) throws SIMPLTranslationException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object parse(InputStream inputStream, Charset charSet) throws SIMPLTranslationException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
