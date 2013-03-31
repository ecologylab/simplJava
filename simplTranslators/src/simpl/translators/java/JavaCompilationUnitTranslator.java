package simpl.translators.java;

import ecologylab.serialization.ClassDescriptor;


import simpl.translation.api.ClassTranslator;
import simpl.translation.api.DependencyTranslator;
import simpl.translation.api.SourceAppender;
import simpl.translation.api.SourceCodeAppender;
import simpl.translation.api.SourceEntry;

public class JavaCompilationUnitTranslator{

	DependencyTranslator dt = new JavaDependencyTranslator();
	ClassTranslator ct = new JavaClassTranslator();
	
	public SourceAppender translatePackage(ClassDescriptor cd)
	{
		SourceAppender sa = new SourceCodeAppender();
		return sa.append("package " + cd.getDescribedClass().getPackage().getName() + ";");
	}
	
	public String translateClass(ClassDescriptor cd)
	{
		SourceAppender classRepr = ct.translateClass(cd);
		
		SourceAppender deps = dt.translateClassDependencies(ct.getDependencies());
		
		SourceAppender packageName = translatePackage(cd);
				
		SourceAppender sa = new SourceCodeAppender();
		sa.append(packageName);
		sa.append(SourceEntry.BREAK);
		sa.append(deps);
		sa.append(classRepr);
		
		return sa.toSource();
	}
}
