package simpl.translators.java;


import java.util.HashSet;
import java.util.Set;

import ecologylab.appframework.types.prefs.MetaPrefString;
import ecologylab.serialization.ClassDescriptor;

import simpl.translation.api.ClassTranslator;
import simpl.translation.api.CommentTranslator;
import simpl.translation.api.FieldTranslator;
import simpl.translation.api.MetaInformationTranslator;
import simpl.translation.api.SourceAppender;
import simpl.translation.api.SourceCodeAppender;
import simpl.translation.api.SourceEntry;

public class JavaClassTranslator extends ClassTranslator{

	private FieldTranslator fieldTranslator = new JavaFieldTranslator();
	private CommentTranslator commentTranslator = new JavaCommentTranslator();
	private MetaInformationTranslator metaInfoTranslator = new JavaMetaInformationTranslator();
	
	public SourceAppender translateDefaultConstructor(ClassDescriptor cd)
	{
		SourceAppender appender = new SourceCodeAppender();
		appender.append("public " + cd.getDescribedClassSimpleName() + "()");
		appender.append(SourceEntry.BLOCK_BEGIN);
		appender.append(SourceEntry.BLOCK_END);
		return appender;
	}
	
	@Override
	public SourceAppender translateClass(ClassDescriptor cd) {

		SourceAppender appender = new SourceCodeAppender();
		
		StringBuilder appendable = new StringBuilder();
		
		appender.append(commentTranslator.translateDocComment(cd.getComment()));
		
		appender.append("public class " + cd.getDescribedClassSimpleName());
		appender.append(SourceEntry.BLOCK_BEGIN);
		
		appender.append(fieldTranslator.translateFields(cd));
		
		appender.append(translateDefaultConstructor(cd));

		appender.append(SourceEntry.BLOCK_END);
		
		return appender;
	}

	@Override
	public Set<String> aggregateDependencies() {
		Set<String> ourDependencies = new HashSet<String>();
		
		// Add all dependencies from the FieldTranslator
		ourDependencies.addAll(fieldTranslator.getDependencies());
			 	
		// Add all dependencies from the MetaInfo translator
		ourDependencies.addAll(metaInfoTranslator.getDependencies());
			
		return ourDependencies;	
	}
}
