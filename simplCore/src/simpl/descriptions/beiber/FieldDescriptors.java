package simpl.descriptions.beiber;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import simpl.annotations.dbal.simpl_classes;
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
		
		
		
		
		
		return nfd;
	}
}
