package simpl.descriptions.beiber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClassDescriptors {

	
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
	
	public static IClassDescriptor get(Class<?> aClass)
	{
		if(containsCD(aClass))
		{
			return descriptors.get(aClass.getName());
		}
		
		UpdateMap ourMap = new ClassDescriptors().new UpdateMap();
		
		IClassDescriptor icd = get(aClass, ourMap);
		
		while(!ourMap.isEmpty())
		{
			Class<?> toUpdate = ourMap.getClassesPendingUpdate().iterator().next();
			IClassDescriptor innerCD = get(toUpdate, ourMap);
			ourMap.resolveUpdates(toUpdate, innerCD);
		}
			
		return icd;
	}
	
	private static IClassDescriptor get(Class<?> aClass, UpdateMap updates)
	{
		if(containsCD(aClass))
		{
			return descriptors.get(aClass.getName());
		} // we're going to double hti this until I restructure
		// for now, fine. :) 
		
		NewClassDescriptor ncd = new NewClassDescriptor();
		
		ncd.setJavaClass(aClass);
		ncd.setName(aClass.getSimpleName());
		
		descriptors.put(aClass.getName(), ncd);
		
		// Handle other class specifics: 
		
				
		
		
		
		
		
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
			if(f.getName().equals("this$0"))
			{
				return true;
			}

			//TODO: 
			/// FUCK CONTEXT SWITCHING :
				/// MAKE THIS LOGIC. 
			
			return false;
			

		}
	
}
