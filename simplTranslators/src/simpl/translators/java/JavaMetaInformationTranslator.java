package simpl.translators.java;

import java.util.Set;

import ecologylab.serialization.MetaInformation;

import simpl.translation.api.MetaInformationTranslator;
import simpl.translation.api.SourceAppender;
import simpl.translation.api.SourceCodeAppender;

public class JavaMetaInformationTranslator extends MetaInformationTranslator{
	JavaParameterTranslator jpt = new JavaParameterTranslator();
	
	public SourceAppender translateMetaInformation(MetaInformation metaInfo) {

		SourceAppender appender = new SourceCodeAppender();
		
		StringBuilder appendable =new StringBuilder();
		
		appendable.append("@").append(metaInfo.simpleTypeName);
		if (!metaInfo.getAnnotationParameters().isEmpty())
		{
			String parameterList = jpt.translateParameterList(metaInfo.getAnnotationParameters());
			appendable.append(parameterList);
		}
		
		this.addDependency(metaInfo.typeName);
		
		return appender.append(appendable.toString());
	}

	public Set<String> aggregateDependencies() {
		// The parameter translator is our only other class that will have dependencies. 
		return jpt.getDependencies();
	}
}