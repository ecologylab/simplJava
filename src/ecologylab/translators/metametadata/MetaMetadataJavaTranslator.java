package ecologylab.translators.metametadata;

import java.io.IOException;

import ecologylab.semantics.metadata.MetadataFieldDescriptor;
import ecologylab.semantics.metametadata.MetaMetadataField;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.translators.java.JavaTranslator;

public class MetaMetadataJavaTranslator extends JavaTranslator
{

	public MetaMetadataJavaTranslator()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void appendClassAnnotationsHook(Appendable appendable, ClassDescriptor classDesc,
			String tabSpacing)
	{
		// TODO Auto-generated method stub
		super.appendClassAnnotationsHook(appendable, classDesc, tabSpacing);
	}

	@Override
	protected void appendFieldAnnotationsHook(Appendable appendable, FieldDescriptor fieldDesc) throws IOException
	{
		super.appendFieldAnnotationsHook(appendable, fieldDesc);
		
		MetadataFieldDescriptor fd = (MetadataFieldDescriptor) fieldDesc;
		MetaMetadataField f = fd.getDefiningMmdField();
		String annotations = f.getAnnotationsInJava();
		appendable.append(annotations);
	}

}
