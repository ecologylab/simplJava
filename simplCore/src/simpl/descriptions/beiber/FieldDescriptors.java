package simpl.descriptions.beiber;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
		
		assert(ClassDescriptors.containsCD(declaringClass));
		nfd.setDeclaringClassDescriptor(ClassDescriptors.get(declaringClass));
		
		
		FieldType fd = new FieldCategorizer().categorizeField(toDescribe);
		
		nfd.setFieldType(fd);
		
		if(classIsScalar(toDescribe.getType()))
		{
			// set up scalar type
		}else{
			// check to see if simpl type. ;) 
			// if we already have it...
			// if not, updaayyyte. ;D
			
			
			
			final Class<?> classToUpdate = toDescribe.getType();
			
			if(ClassDescriptors.containsCD(classToUpdate))
			{
				// good. GIT IT. :) 
				nfd.setDeclaringClassDescriptor(ClassDescriptors.get(classToUpdate));
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
						// TODO Auto-generated method stub
						// THIS IS WRONG. :) 
						// TODO FIX: 
						nfd.setFieldClassDescriptor(icd);
					}
				});
			}
		}
		
		return nfd;
	}
}
