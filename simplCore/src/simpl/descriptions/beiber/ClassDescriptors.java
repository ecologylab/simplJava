package simpl.descriptions.beiber;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_other_tags;
import simpl.descriptions.FieldCategorizer;
import simpl.descriptions.FieldType;

public class ClassDescriptors {

	/**
	 * This class holds a collection of callbacks to update class descriptor references.
	 * Allows us to avoid recursing beyond a single level...
	 * Makes things a bit more verbose at spots, but also cleans up a lot of danging logic from the past. 
	 *
	 */
	private class UpdateMap
	{		
		Map<Class<?>, Collection<UpdateClassDescriptorCallback>> ourMap;
		
		public UpdateMap()
		{
			ourMap = new HashMap<Class<?>, Collection<UpdateClassDescriptorCallback>>();
		}
		
		public void insertUCDs(Collection<UpdateClassDescriptorCallback> collection)
		{
			this._insertUCDs(ourMap, collection);
		}
		
		public void insertUDC(UpdateClassDescriptorCallback callback)
		{
			this._insertUCD(ourMap, callback);
		}
		
		public boolean isEmpty()
		{
			return ourMap.keySet().isEmpty();
		}
		
		public void resolveUpdates(Class<?> someClass, IClassDescriptor descriptor)
		{
			for(UpdateClassDescriptorCallback ucd : ourMap.get(someClass))
			{
				ucd.updateWithCD(descriptor);
			}
			
			ourMap.remove(someClass);
		}
		
		public Collection<Class<?>> getClassesPendingUpdate()
		{
			return ourMap.keySet();
		}
		
		private void _insertUCDs(Map<Class<?>, Collection<UpdateClassDescriptorCallback>> ourMap, Collection<UpdateClassDescriptorCallback> ucds)
		{
			for(UpdateClassDescriptorCallback ucd : ucds)
			{
				_insertUCD(ourMap, ucd);
			}
		}
		
		private void _insertUCD(Map<Class<?>, Collection<UpdateClassDescriptorCallback>> ourMap, UpdateClassDescriptorCallback ucd)
		{
			if(!ourMap.containsKey(ucd.getClassToUpdate()))
			{
				ourMap.put(ucd.getClassToUpdate(), new LinkedList<UpdateClassDescriptorCallback>());
			}
			ourMap.get(ucd.getClassToUpdate()).add(ucd);
		}
		
	}
	
	/**
	 * A cache of all field descriptors. 
	 */
	public static Map<String, IClassDescriptor> descriptors = new HashMap<String, IClassDescriptor>(); 
	
	/**
	 * Method to clear the CD cache. Important / used only for esting. (thus the __)
	 */
	public static void __ClearClassDescriptorCache()
	{
		descriptors.clear();
	}
	
	public static boolean containsCD(Class<?> aClass)
	{
		return descriptors.containsKey(aClass.getName());
	}
	
	public static boolean containsCD(IClassDescriptor icd)
	{
		return descriptors.containsKey(icd.getName());
	}
	
	public static void registerClassDescriptor(IClassDescriptor icd)
	{
		if(!ClassDescriptors.containsCD(icd))
		{
			descriptors.put(icd.getName(), icd);
		}
	}
	
	public static IClassDescriptor get(Class<?> aClass)
	{
		if(containsCD(aClass))
		{
			return descriptors.get(aClass.getName());
		}
		
		UpdateMap ourMap = new ClassDescriptors().new UpdateMap();
		
		IClassDescriptor icd = get(aClass, ourMap); // get the CD for the first class.
		
		// As long as we need to obtain more class descriptors, keep going
		while(!ourMap.isEmpty())
		{
			// Pick the first class descriptor. 
			Class<?> toUpdate = ourMap.getClassesPendingUpdate().iterator().next();
			// Get it...
			IClassDescriptor innerCD = get(toUpdate, ourMap);
			// Resolve all of the update callbacks for classes that needed this class descriptor.
			ourMap.resolveUpdates(toUpdate, innerCD); //OurMap is smaller after this call.
		}
			
		// Return the class descriptor completely constructed. 
		return icd;
	}
	
	private static boolean classShouldInheritSuperclassCD(Class<?> aClass)
	{
		return aClass.isAnnotationPresent(simpl_inherit.class);
	}
		
	private static IClassDescriptor get(Class<?> aClass, UpdateMap updates)
	{
		if(containsCD(aClass))
		{
			return descriptors.get(aClass.getName());
		} // we're going to double hti this until I restructure
		// for now, fine. :) 
		
		//Create our class descriptor. 
		// Must be final for callbacks to work. 
		final NewClassDescriptor ncd = new NewClassDescriptor();
		
		ncd.setJavaClass(aClass);
		ncd.setName(aClass.getName());
		
		descriptors.put(aClass.getName(), ncd);
		
		// Handle other class specifics: 
		
		
		if(aClass.isAnnotationPresent(simpl_other_tags.class))
		{
			final simpl_other_tags otherTags = aClass.getAnnotation(simpl_other_tags.class);
			for(String s: otherTags.value())
			{
				ncd.addOtherTag(s);
			}
		}
		
		
		
		
		//Inheritance w/ superclasses and such. 
		if(classShouldInheritSuperclassCD(aClass))
		{
			// Reference the superclass, must be final for callbacks.
			final Class<?> superClass = aClass.getSuperclass();
			
			if(ClassDescriptors.containsCD(superClass))
			{
				ncd.setSuperClassDescriptor(ClassDescriptors.get(superClass));
			}else{
				// We'll use a callback to update the CD for the superclass whenever we have it. 
				updates.insertUDC(new UpdateClassDescriptorCallback() {
					@Override
					public Class<?> getClassToUpdate() {
						return superClass;
					}
					
					@Override
					public void updateWithCD(IClassDescriptor icd) {
						ncd.setSuperClassDescriptor(icd);
					}
				});
			}
		}
		
		
		
		

		// Create all fields!
		for(Field f : aClass.getDeclaredFields())
		{ 
			if(!fieldExcluded(f))
			{
				Collection<UpdateClassDescriptorCallback> ucds = new ArrayList<UpdateClassDescriptorCallback>();					
				
				IFieldDescriptor ifd = FieldDescriptors.getFieldDescriptor(f, ucds);	
				
				if(!ucds.isEmpty())
				{
					updates.insertUCDs(ucds);
				}
				
				ncd.addField(ifd);
			}
		}
		
		return ncd;
	}
		
	// exclude some fields yo
		private static boolean fieldExcluded(Field f)
		{
			// Exclude "this" instances on inner classes
			if(f.getName().equals("this$0"))
			{
				return true;
			}
			
			// exclude synthetic fields.
			if(f.isSynthetic())
			{
				return true;
			}
			
			// Exclude static fields. 
			if(Modifier.isStatic(f.getModifiers()))
			{
				return true;
			}
			
			// Exclude "unset" types
			FieldType ft = new FieldCategorizer().categorizeField(f);
			if(ft == FieldType.UNSET_TYPE)
			{
				return true;
			}
			
			return false;
			

		}
	
}
