package simpl.descriptions.beiber;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import simpl.annotations.dbal.simpl_classes;
import simpl.annotations.dbal.simpl_other_tags;
import simpl.annotations.dbal.simpl_scope;
import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScope;
import simpl.descriptions.AnnotationParser;
import simpl.descriptions.FieldCategorizer;
import simpl.descriptions.FieldType;
import simpl.types.TypeRegistry;

public class FieldDescriptors {

	
	public Class<?> ourClass;
	
	public FieldDescriptors(Class<?> parentClass)
	{
		ourClass = parentClass;
	}
	
	private static boolean classIsScalar(Class<?> aClass)
	{
		return TypeRegistry.containsScalarTypeFor(aClass);
	}
	
	public static IFieldDescriptor getFieldDescriptor(Field toDescribe, Collection<UpdateClassDescriptorCallback> classAccumulator){
		
		classAccumulator.clear();
		
		final NewFieldDescriptor nfd = new NewFieldDescriptor();
		 
		nfd.setName(toDescribe.getName());
		
		Class<?> declaringClass = toDescribe.getDeclaringClass();
		nfd.setDeclaringClass(declaringClass);
		
		assert(ClassDescriptors.containsCD(declaringClass)); //If declaringClass isn't there, we've got a problem. 
		nfd.setDeclaringClassDescriptor(ClassDescriptors.get(declaringClass));
		
		FieldType fd = new FieldCategorizer().categorizeField(toDescribe);
		
		nfd.setFieldType(fd);
				
		
		if(toDescribe.isAnnotationPresent(simpl_other_tags.class))
		{
			final simpl_other_tags otherTags = toDescribe.getAnnotation(simpl_other_tags.class);
			for(String s: otherTags.value())
			{
				nfd.addOtherTags(s);
			}
		}
		
		
		// Handle scalar type / or composite types
		if(classIsScalar(toDescribe.getType()))
		{
			// handle this. :) 
		}else{
			// check to see if simpl type. ;) 
			// if we already have it...
			// if not, updaayyyte. ;D
			
			final Class<?> classToUpdate = toDescribe.getType();
			
			if(ClassDescriptors.containsCD(classToUpdate))
			{
				// good. GIT IT. :) 
				nfd.setFieldClassDescriptor(ClassDescriptors.get(classToUpdate));
			}else{
				// We need to update it later when we have it. 
				classAccumulator.add(new UpdateClassDescriptorCallback() {
					
					@Override
					public Class<?> getClassToUpdate() {
						// TODO Auto-generated method stub
						return classToUpdate;
					}
					
					@Override
					public void updateWithCD(IClassDescriptor icd) {
						nfd.setFieldClassDescriptor(icd);
					}
				});
			}
			
			
		}
		
		
		AnnotationParser ap = new AnnotationParser();
		Collection<IMetaInformation> metaInfo = ap.getAllMetaInformation(toDescribe);
		
		for(IMetaInformation imo : metaInfo)
		{
			nfd.addMetaInformation(imo);
			// TODO: Add requisite callbacks to update any class descriptors in imo.
		}
		
		// handle polymorphing w/ simpl_classes
		if(toDescribe.isAnnotationPresent(simpl_classes.class))
		{
			final simpl_classes classesAnnotationObj = toDescribe.getAnnotation(simpl_classes.class);
			
			final Class<?>[] classesAnnotation = classesAnnotationObj.value();
			
			if ((classesAnnotation != null) && (classesAnnotation.length > 0))
			{
				for(Class<?> polymorph : classesAnnotation)
				{
					if(ClassDescriptors.containsCD(polymorph))
					{
						nfd.addPolymoprhicFieldDescriptor(ClassDescriptors.get(polymorph));
					}else{
						final Class<?> polymorphToUpdate = polymorph;
						classAccumulator.add(new UpdateClassDescriptorCallback() {
							
							@Override
							public void updateWithCD(IClassDescriptor icd) {
								nfd.addPolymoprhicFieldDescriptor(icd);
							}
							
							@Override
							public Class<?> getClassToUpdate() {
								return polymorphToUpdate;
							}
						});
					}
				}
			}
		}
		
		if(toDescribe.isAnnotationPresent(simpl_scope.class))
		{
			final simpl_scope scopeAnnotationObj = toDescribe.getAnnotation(simpl_scope.class);
			String scopeToResolve = scopeAnnotationObj.value();
			ISimplTypesScope s = SimplTypesScope.get(scopeToResolve);
			
			if(s == null)
			{
				throw new RuntimeException("Simpl Types Scope named ["
							+scopeToResolve == null ? "NULL" : scopeToResolve +
						"] is not created. Please make sure scope has been created " +
						"and that static initialization happens in the proper order.");
			}
			else
			{
				if(toDescribe.getType().isEnum())
				{
					throw new RuntimeException("Polymorphic enumerations do not exist!");
				}
				else
				{
					// We have a valid type to polymorph! Let's do it:
					// TODO: Put in when simpl types scope returns IClassDescriptor
					// Commented code like this is a cardinal sin: Doing this because I need to make the IClassDescriptor refactor on STS which will be gnarly. 
/*					int added = 0;
 * 					for(IClassDescriptor icd : s.getAllClassDescriptors())
					{
						if(ncd.isSuperClass(icd))
						{
							ncd.addPolymorphicFieldDescriptor(icd);
							added = added + 1;
						}
					}
					
					if(added == 0)
					{
						throw new RuntimeException("No simplClasses added to polymorphic field descriptor; did you mean to reference a sts with types that were a supertype of the declared class? Check your code and STS and try again.");
					}
					
					*/
				}
			}
		}
		
		
		
		
		
		return nfd;
	}
}
