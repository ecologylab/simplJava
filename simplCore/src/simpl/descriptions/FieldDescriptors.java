package simpl.descriptions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import simpl.annotations.dbal.simpl_classes;
import simpl.annotations.dbal.simpl_other_tags;
import simpl.annotations.dbal.simpl_scope;
import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScope;
import simpl.exceptions.SIMPLDescriptionException;
import simpl.tools.ReflectionTools;
import simpl.types.ListType;
import simpl.types.MapType;
import simpl.types.TypeRegistry;

public class FieldDescriptors {
	public Class<?> ourClass;
	
	public FieldDescriptors(Class<?> parentClass)
	{
		ourClass = parentClass;
	}
	
	private static boolean classIsEnum(Class<?> aClass)
	{
		return aClass.isEnum();
	}
	private static boolean classIsScalar(Class<?> aClass)
	{
		return TypeRegistry.containsScalarTypeFor(aClass);
	}
	
	public static FieldDescriptor getFieldDescriptor(Field toDescribe, Collection<UpdateClassDescriptorCallback> classAccumulator){
		
		classAccumulator.clear();
		
		final FieldDescriptorImpl nfd = new FieldDescriptorImpl();
		 
		nfd.setName(toDescribe.getName());
		
		Class<?> declaringClass = toDescribe.getDeclaringClass();
		nfd.setDeclaringClass(declaringClass);
		
		assert(ClassDescriptors.containsCD(declaringClass)); //If declaringClass isn't there, we've got a problem. 
		nfd.setDeclaringClassDescriptor(ClassDescriptors.getClassDescriptor(declaringClass));
		
		
		
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
		if(classIsEnum(toDescribe.getType()))
		{
			try {
				nfd.setEnumerationDescriptor(EnumerationDescriptor.get(toDescribe.getType()));
			} catch (SIMPLDescriptionException e) {
				throw new RuntimeException(e);
			}
		}
		else if(classIsScalar(toDescribe.getType()))
		{
			nfd.setScalarType(TypeRegistry.getScalarType(toDescribe.getType()));
		}
		else if(classIsMap(toDescribe.getType()))
		{
			Class<?> collectionType = toDescribe.getType();
			if(classIsInterfaceOrAbstract(collectionType))
			{
				// we need to obtain the type via creating an instance of the CD. 
				
				try {
					Object o = declaringClass.newInstance();
					Object instance = ReflectionTools.getFieldValue(nfd.getField(), o);
					
					if(instance == null)
					{
						throw new RuntimeException("Fields which are defined with an interface must have an instance initialized by their public constructor in order to be simpl serialized!");
					}
					
					collectionType = instance.getClass();
					
				} catch (Exception e)
				{
					throw new RuntimeException(e);
				}
				
			}
			
			nfd.setMapType(new MapType(collectionType));
		}
		else if(classIsCollection(toDescribe.getType()))
		{
			Class<?> listType = toDescribe.getType();
			if(classIsInterfaceOrAbstract(listType))
			{
				// we need to obtain the type via creating an instance of the CD.
				try 
				{
					Object o = declaringClass.newInstance();
					Object instance = ReflectionTools.getFieldValue(nfd.getField(), o);
					
					if(instance == null)
					{
						throw new RuntimeException("Fields which are defined with an interface must have an instance initialized by their public constructor in order to be simpl serialized!");
					}
					
					listType = instance.getClass();
				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}
			}
			
			nfd.setListType(new ListType(listType));
		}
		else
		{
			// check to see if simpl type. ;) 
			// if we already have it...
			// if not, updaayyyte. ;D
			
			final Class<?> classToUpdate = toDescribe.getType();
			
			if(ClassDescriptors.containsCD(classToUpdate))
			{
				// good. GIT IT. :) 
				nfd.setFieldClassDescriptor(ClassDescriptors.getClassDescriptor(classToUpdate));
			}else{
				// We need to update it later when we have it. 
				classAccumulator.add(new UpdateClassDescriptorCallback() {
					
					@Override
					public Class<?> getUpdateKey() {
						// TODO Auto-generated method stub
						return classToUpdate;
					}
					
					@Override
					public void runUpdateCallback(ClassDescriptor icd) {
						nfd.setFieldClassDescriptor(icd);
					}
				});
			}
			
			
		}
		
		
		AnnotationParser ap = new AnnotationParser();
		Collection<MetaInformation> metaInfo = ap.getAllMetaInformation(toDescribe);
		
		for(MetaInformation imo : metaInfo)
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
						nfd.addPolymoprhicFieldDescriptor(ClassDescriptors.getClassDescriptor(polymorph));
					}else{
						final Class<?> polymorphToUpdate = polymorph;
						classAccumulator.add(new UpdateClassDescriptorCallback() {
							
							@Override
							public void runUpdateCallback(ClassDescriptor icd) {
								nfd.addPolymoprhicFieldDescriptor(icd);
							}
							
							@Override
							public Class<?> getUpdateKey() {
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
			nfd.addPolymorphicScope(scopeToResolve);	
		}
		
		return nfd;
	}
	
	private static boolean classIsInterfaceOrAbstract(Class<?> toDescribe)
	{
		return (toDescribe.isInterface() || Modifier.isAbstract(toDescribe.getModifiers()));
	}

	private static boolean classIsMap(Class<?> type) {
		return Map.class.isAssignableFrom(type);
	}

	private static boolean classIsCollection(Class<?> type) {
		//type.
		return Collection.class.isAssignableFrom(type) && !Map.class.isAssignableFrom(type);
	}
}
