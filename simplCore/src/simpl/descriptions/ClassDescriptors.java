package simpl.descriptions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_other_tags;
import simpl.annotations.dbal.simpl_use_equals_equals;
import simpl.tools.XMLTools;
import simpl.types.scalar.CompositeAsScalarType;

public class ClassDescriptors {

	/**
	 * A cache of all field descriptors. 
	 */
	public static Map<String, ClassDescriptor> descriptors = new HashMap<String, ClassDescriptor>(); 
	
	/**
	 * Method to clear the CD cache. Important / used only for testing. (thus the __)
	 */
	public static void __ClearClassDescriptorCache()
	{
		descriptors.clear();
	}
	
	public static boolean containsCD(Class<?> aClass)
	{
		return descriptors.containsKey(aClass.getName());
	}
	
	public static boolean containsCD(ClassDescriptor icd)
	{
		return descriptors.containsKey(icd.getName());
	}
	
	public static void registerClassDescriptor(ClassDescriptor icd)
	{
		if(!ClassDescriptors.containsCD(icd))
		{
			descriptors.put(icd.getName(), icd);
		}
	}
	
	
	public static ClassDescriptor getClassDescriptor(Object object)
	{
		return getClassDescriptor(object.getClass());
	}
	
	public static ClassDescriptor getClassDescriptor(Class<?> aClass)
	{
		// Return a cached CD if we have one. 
		if(containsCD(aClass))
		{
			return descriptors.get(aClass.getName());
		}
		
		//Initialize callback map for class descriptor construction
		ClassDescriptorCallbackMap ourMap = new ClassDescriptorCallbackMap();
		
		ClassDescriptor icd = get(aClass, ourMap); // get the CD for the first class.
		
		// As long as we need to obtain more class descriptors, keep going
		while(!ourMap.isEmpty())
		{
			// Pick the first class descriptor. 
			Class<?> toUpdate = ourMap.getPendingUpdateKeys().iterator().next();
			// Get it...
			ClassDescriptor innerCD = get(toUpdate, ourMap);
			// Resolve all of the update callbacks for classes that needed this class descriptor.
			ourMap.resolveCallbacks(toUpdate, innerCD); //OurMap is smaller after this call.
		}
			
		// Return the class descriptor completely constructed. 
		return icd;
	}
	
	private static boolean classShouldInheritSuperclassCD(Class<?> aClass)
	{
		return aClass.isAnnotationPresent(simpl_inherit.class);
	}
		
	private static ClassDescriptor get(Class<?> aClass, ClassDescriptorCallbackMap updates)
	{
		if(containsCD(aClass))
		{
			return descriptors.get(aClass.getName());
		} 
		
		//Create our class descriptor. 
		// Must be final for callbacks to work. 
		final ClassDescriptorImpl ncd = new ClassDescriptorImpl();
		
		ncd.setJavaClass(aClass);
		ncd.setName(aClass.getName());
		ncd.setSimpleName(aClass.getSimpleName());
		ncd.setNamespace(aClass.getPackage().toString());
		
		
		ncd.setTagName(XMLTools.getXmlTagName(aClass, ""));
		
		descriptors.put(aClass.getName(), ncd);
		
		// Get the generic type information
		
		List<GenericTypeVar> genericTypes = GenericDescriptions.getClassTypeVariables(aClass);
		ncd.setGenericTypeVariables(genericTypes);
		
		// Handle other class specifics: 
		if(aClass.isAnnotationPresent(simpl_use_equals_equals.class))
		{
			ncd.setStrictObjectGraphRequired(true);
		}
		
		// Create all declared fields. 
		// Inherited fields are handled w/ Superclass inheritance
		// (because of the way that simpl_inherit works) 
		for(Field f : aClass.getDeclaredFields())
		{ 
			// Acquires the field, placing any needed updates into the Update Callback map
			acquireField(f, updates, ncd);
		}
		
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
			
			// The general style of this class is to keep most of the processing *outside* of the CD / FD
			// classes, this is a minor exception made for simplicity / niceness's sake. 
			// ClassDescriptorImpl.setSuperClassDescriptor() will add the superclass fields. 
			// This allows us to handle superclass chains correctly, and also makes sure that
			// the simpl_inherit logic works correctly. 
			
			
			if(ClassDescriptors.containsCD(superClass))
			{
				ncd.setSuperClassDescriptor(ClassDescriptors.getClassDescriptor(superClass));
			}
			else
			{
				// We'll use a callback to update the CD for the superclass whenever we have it. 
				updates.insertCallback(new UpdateClassDescriptorCallback() {
					@Override
					public Class<?> getUpdateKey() {
						return superClass;
					}
					
					@Override
					public void runUpdateCallback(ClassDescriptor icd) {
						ncd.setSuperClassDescriptor(icd);
					}
				});
			}
		}
		
		// Composite as Scalar
		if(ncd.fields().CompositesAsScalars.size() > 0)
		{
			CompositeAsScalarType cst = new CompositeAsScalarType(ncd);
			// We create this here, it gets statically added to the typeRegistry. 
		}
		
		
		// Add all meta information:
		AnnotationParser ap = new AnnotationParser();
		Collection<MetaInformation> metaInfo = ap.getAllMetaInformation(aClass);
		
		for(MetaInformation imo : metaInfo)
		{
			ncd.addMetaInformation(imo);
			// TODO: Add requisite callbacks to update any class descriptors in imo.
		}
		
		return ncd;
	}

	private static void acquireField(Field f,
			ClassDescriptorCallbackMap updates, final ClassDescriptorImpl ncd) {
		if(!fieldExcluded(f))
		{
			f.setAccessible(true);
			
			Collection<UpdateClassDescriptorCallback> ucds = new ArrayList<UpdateClassDescriptorCallback>();					
			
			FieldDescriptor ifd = FieldDescriptors.getFieldDescriptor(f, ucds);	
			
			if(!ucds.isEmpty())
			{
				updates.insertCallbacks(ucds);
			}
			
			ncd.addField(ifd);
		}
	}
		
	// exclude some fields
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
