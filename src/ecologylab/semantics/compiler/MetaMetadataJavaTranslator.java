package ecologylab.semantics.compiler;

import java.io.IOException;

import ecologylab.semantics.metadata.MetadataFieldDescriptor;
import ecologylab.semantics.metametadata.MetaMetadataCompositeField;
import ecologylab.semantics.metametadata.MetaMetadataField;
import ecologylab.semantics.metametadata.MetaMetadataScalarField;
import ecologylab.semantics.metametadata.exceptions.MetaMetadataException;
import ecologylab.serialization.ElementState.xml_other_tags;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.FieldTypes;
import ecologylab.serialization.Hint;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.types.ScalarType;
import ecologylab.translators.java.JavaTranslator;

public class MetaMetadataJavaTranslator extends JavaTranslator
{
	
	private static String[] metaMetadataDefaultImports = {
		MetaMetadataCompositeField.class.getName(),
		Hint.class.getName(),
		xml_tag.class.getName().replace('$', '.'),
		xml_other_tags.class.getName().replace('$', '.'),
	};

	public MetaMetadataJavaTranslator()
	{
		for (String importTarget : metaMetadataDefaultImports)
			this.addGlobalImportDependency(importTarget);
	}

	@Override
	protected void appendFieldAnnotationsHook(Appendable appendable, FieldDescriptor fieldDesc) throws IOException
	{
		super.appendFieldAnnotationsHook(appendable, fieldDesc);
		
		MetadataFieldDescriptor fd = (MetadataFieldDescriptor) fieldDesc;
		MetaMetadataField f = fd.getDefiningMmdField();
		String annotations = f.getAdditionalAnnotationsInJava();
		appendable.append(annotations);
	}

	@Override
	protected void appendConstructorHook(String className, Appendable appendable) throws IOException
	{
		appendable.append("\n");
		appendable.append("\tpublic ").append(className).append("(MetaMetadataCompositeField mmd) {\n");
		appendable.append("\t\tsuper(mmd);\n");
		appendable.append("\t}\n");
		appendable.append("\n");
	}
	
	protected void appendLazyEvaluation(String fieldName, String typeName, Appendable appendable) throws IOException
	{
		// TODO write comments?
		
		// first line. Start of method name
		appendable.append("\tpublic ").append(typeName).append("\t").append(fieldName).append("()\n\t{\n");
	
		// second line. Declaration of result variable.
		appendable.append("\t\t").append(typeName).append("\t").append("result = this.").append(fieldName).append(";\n");
	
		// third line. Start of if statement
		appendable.append("\t\tif (result == null)\n\t\t{\n");
	
		// fourth line. creation of new result object.
		appendable.append("\t\t\tresult = new ").append(typeName).append("();\n");
	
		// fifth line. end of if statement
		appendable.append("\t\t\tthis.").append(fieldName).append(" = result;\n\t\t}\n");
	
		// sixth line. return statement and end of method.
		appendable.append("\t\treturn result;\n\t}\n\n");
	}

	@Override
	protected void appendGetters(FieldDescriptor fieldDescriptor, Appendable appendable)
			throws IOException
	{
		if (fieldDescriptor.getType() == FieldTypes.SCALAR)
		{
			MetaMetadataScalarField scalar = (MetaMetadataScalarField) ((MetadataFieldDescriptor) fieldDescriptor).getDefiningMmdField();
			
			// metadata scalar types!
			ScalarType<?> scalarType = fieldDescriptor.getScalarType();
			if (scalarType == null)
				throw new MetaMetadataException("scalar type not specified!");
			
			String fieldName = fieldDescriptor.getName();
			String capFieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
			String typeName = scalarType.getJavaTypeName();
			
			appendLazyEvaluation(fieldName, typeName, appendable);
			
			String javaPrimitiveTypeName = scalarType.operativeScalarType().getJavaTypeName();
			appendable.append("\tpublic ").append(javaPrimitiveTypeName).append(" get").append(capFieldName).append("()\n");
			appendable.append("\t{\n");
			appendable.append("\t\treturn this.").append(fieldName).append("().getValue();\n");
			appendable.append("\t}\n\n");
		}
		else
		{
			super.appendGetters(fieldDescriptor, appendable);
		}
	}

	@Override
	protected void appendSetters(FieldDescriptor fieldDescriptor, Appendable appendable)
			throws IOException
	{
		if (fieldDescriptor.getType() == FieldTypes.SCALAR)
		{
			// metadata scalar types!
			ScalarType<?> scalarType = fieldDescriptor.getScalarType();
			if (scalarType == null)
				throw new MetaMetadataException("scalar type not specified!");
			
			String fieldName = fieldDescriptor.getName();
			String capFieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
			
			String javaPrimitiveTypeName = scalarType.operativeScalarType().getJavaTypeName();
			appendable.append("\tpublic void set").append(capFieldName).append("(").append(javaPrimitiveTypeName).append(" ").append(fieldName).append(")\n");
			appendable.append("\t{\n");
			appendable.append("\t\tthis.").append(fieldName).append("().setValue(").append(fieldName).append(");\n");
			appendable.append("\t}\n\n");
		}
		else
		{
			super.appendSetters(fieldDescriptor, appendable);
		}
	}
	
}
