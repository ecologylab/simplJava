package simpl.translators.java;

import java.io.IOException;
import java.util.Set;

import ecologylab.generic.Debug;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.MetaInformation;

import simpl.translation.api.CommentTranslator;
import simpl.translation.api.FieldTranslator;
import simpl.translation.api.LanguageCore;
import simpl.translation.api.MetaInformationTranslator;
import simpl.translation.api.SourceAppender;
import simpl.translation.api.SourceCodeAppender;
import simpl.translation.api.SourceEntry;

public class JavaFieldTranslator extends FieldTranslator{

	private final CommentTranslator commentTranslator = new JavaCommentTranslator();
	private final MetaInformationTranslator metaInformationTranslator = new JavaMetaInformationTranslator();
	private final LanguageCore javaCore = new JavaLanguageCore();
	
	public JavaFieldTranslator() {
		// TODO Auto-generated constructor stub
	}

	public SourceAppender translateField(ClassDescriptor context, FieldDescriptor fieldDescriptor)
	{

		SourceAppender appender = new SourceCodeAppender();
		
		StringBuilder appendable = new StringBuilder();
		
		String javaType = fieldDescriptor.getJavaTypeName();
		String simpleJavaTypeName = fieldDescriptor.getFieldTypeSimpleName();
		
		addDependency(fieldDescriptor.getJavaTypeName());
		
		if (javaType == null)
		{
			throw new RuntimeException("Invalid type for FieldDescriptor: " + fieldDescriptor.getName());	
		}
		
		boolean isKeyword = checkForKeywords(fieldDescriptor);
		if (isKeyword)
			return appender; // We didn't make a change to it; so just return an empty appender
		
		// If it has comments...
		if(fieldDescriptor.getComment() != null)
		{
			SourceAppender comments = commentTranslator.translateDocComment(fieldDescriptor.getComment());
			appender.append(comments);
		}
		
		// if it has metainfo
		if(!fieldDescriptor.getMetaInformation().isEmpty())
		{
			for(MetaInformation m : fieldDescriptor.getMetaInformation())
			{
				appender.append(metaInformationTranslator.translateMetaInformation(m));
			}	
		}
		
		
		// private simpleJavaTypeName name
		appendable.append("private ").append(simpleJavaTypeName).append(" ").append(fieldDescriptor.getName()).append(";");
		
		
		return appender.append(appendable.toString()).append(SourceEntry.BREAK);
		
	}
	
	private String capitalizeFirst(String s)
	{
		String firstchar = s.substring(0,1);
		firstchar = firstchar.toUpperCase();
		
		return firstchar + s.substring(1,s.length());
	}
	
	private SourceAppender translateGetterSetter(ClassDescriptor context, FieldDescriptor fieldDescriptor)
	{
		SourceAppender appender = new SourceCodeAppender();
		
		if(fieldDescriptor.hasGetter()) // By default, always true. 
		{
			String methodTitle = "public " + fieldDescriptor.getFieldTypeSimpleName() + " get"  + capitalizeFirst(fieldDescriptor.getName()) +"()";
			appender.append(methodTitle);
			appender.append(SourceEntry.BLOCK_BEGIN);
			String methodContents = "return this." + fieldDescriptor.getName() + ";";
			appender.append(methodContents);
			appender.append(SourceEntry.BLOCK_END);
			appender.append(SourceEntry.BREAK);
		}
		
		if(fieldDescriptor.hasSetter()) // By default, always true. 
		{
			String methodTitle = "public void set"  + capitalizeFirst(fieldDescriptor.getName()) +"("+fieldDescriptor.getFieldTypeSimpleName() +" value)";
			appender.append(methodTitle);
			appender.append(SourceEntry.BLOCK_BEGIN);
			String methodContents = "this." + fieldDescriptor.getName() + " = value;";
			appender.append(methodContents);
			appender.append(SourceEntry.BLOCK_END);
			appender.append(SourceEntry.BREAK);
		}
		
		return appender;
	}
	
	public SourceAppender translateFields(ClassDescriptor context)
	{
		SourceAppender appender = new SourceCodeAppender();
	
		for(Object fd : context.allFieldDescriptors())
		{
			appender.append(this.translateField(context, (FieldDescriptor)fd));
			// This ugly cast is here because the API is changing.
			// Dont' worry! Translation code you write today will still work soon!
		}
				
		for(Object fd : context.allFieldDescriptors())
		{
			appender.append(this.translateGetterSetter(context, (FieldDescriptor)fd));
		}
		
		return appender;		
	}
	
	/**
	 * A method to test whether fieldAccessor is a java keyword
	 * 
	 * @param fieldDescriptor
	 * @return
	 * @throws IOException
	 */
	protected boolean checkForKeywords(FieldDescriptor fieldDescriptor)
	{
		if (javaCore.isKeyword(fieldDescriptor.getName()))
		{
			Debug.warning(fieldDescriptor, " Field Name: [" + fieldDescriptor.getName()
					+ "]. This is a keyword in Java. Cannot translate.");
			return true;
		}
		return false;
	}

	@Override
	public Set<String> aggregateDependencies() {
		// TODO Auto-generated method stub
		return metaInformationTranslator.getDependencies();
	}
}
