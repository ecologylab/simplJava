package simpl.descriptions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * A class that parses annotations in java and creates the according IMetaInformation classes
 * @author tom
 *
 */
public class AnnotationParser
{
	
	
	/**
	 * Returns all metainformation correlated to a given class
	 * @param fromAClass A class that has meta information
	 * @return The collection of meta information associated with the CLASS
	 */
	public Collection<MetaInformation> getAllMetaInformation(Class<?> fromAClass)
	{
		
		return getAllMetaInfo(fromAClass.getDeclaredAnnotations());
	}

	/**
	 * Returns all metainformation correlated to a given Field
	 * @param fromAField The Field to obtain meta information for
	 * @return The collection of meta information associated with the FIELD
	 */
	public Collection<MetaInformation> getAllMetaInformation(Field fromAField)
	{
		
		return getAllMetaInfo(fromAField.getDeclaredAnnotations());
	}
	
	/**
	 * Returns all metainformation associated with a given method
	 * @param fromAMethod the method to obtain meta information from
	 * @return Thhe collection of meta information associated with the METHOD
	 */
	public Collection<MetaInformation> getAllMetaInformation(Method fromAMethod)
	{			
		return getAllMetaInfo(fromAMethod.getDeclaredAnnotations());
	}
	
	private Collection<MetaInformation> getAllMetaInfo(Annotation[] annotations)
	{
		List<MetaInformation> ourMetaInfo = new LinkedList<MetaInformation>();
		
		for(Annotation a: annotations)
		{
			ourMetaInfo.add(getMetaInformationFromAnnotation(a));
		}
		
		return ourMetaInfo;
	}
	
	
	
	/**
	 * Gets ParameterDescriptors that describe the parameters used for this particular annotation.
	 * Used primarily for getting the information for code generation
	 * @param a The Annotation Class to parse
	 * @return ParameterDescriptors for all entries
	 */
	public <T extends Annotation> MetaInformation getMetaInformationFromAnnotation(T a)
	{
		Class<? extends Annotation> annotationClass = a.getClass();
		
		//We get the first interface of the annotation class b/c it's the only interface it should have. 
		// This class instance will be a Proxy; so that class name won't give us the name we want! 
		MetaInformationImpl ourMetaInfo = new MetaInformationImpl(annotationClass.getInterfaces()[0].getSimpleName());
		
		for(ParameterDescriptor param : getParametersFromAnnotation(a))
		{
			ourMetaInfo.addParameter(param);
		}
		
		return ourMetaInfo;
	}
	
	public <T extends Annotation> List<ParameterDescriptor> getParametersFromAnnotation(T a) 
	{
		List<ParameterDescriptor> ourList = new LinkedList<ParameterDescriptor>();
						
		Class<? extends Annotation> annotClass = a.getClass();
		
		Method[] methods = annotClass.getDeclaredMethods();
		
		List<String> ourMethods = new LinkedList<String>();
		
		for(Method m : methods)
		{
			if(MethodMayBeAnnotationName(m))
				ourMethods.add(m.getName());
		}
	
		Set<String> ourMethodSet = new HashSet<String>();
		ourMethodSet.addAll(ourMethods);
		
		Set<String> defaultMethodSet = getDefaultAnnotationProxyMethods();

		// Get the difference. What isn't in the default method set will be a value in the attribute
		Set<String> result = symmetricDifference(ourMethodSet, defaultMethodSet);
		
		for(String s: result)
		{
			Method m;
			try 
			{	
				m = a.getClass().getMethod(s);
			
				// Hold onto the name
				String name = m.getName();
			
				// Get the values
				Object value = null;
			

				// Invoke A's method to get the value
				value = m.invoke(a);
			
				// Get the return type
				Class<?> paramType = m.getReturnType();
			
				ourList.add(new ParameterDescriptorImpl(name,paramType,value));
			}
			catch(Exception t)
			{
				throw new RuntimeException(t);
			}
		}
		
		return ourList;
	}
	
	public Set<String> symmetricDifference(Set<String> ourMethodSet,
			Set<String> defaultMethodSet) {
		Set<String> toReturn= new HashSet<String>();
		toReturn.addAll(ourMethodSet);
		
		boolean changed = toReturn.removeAll(defaultMethodSet);
		
		if(!changed)
		{
			throw new RuntimeException("The set should have changed! Something is amiss!");
		}		
		
		return toReturn;
	}

	private boolean MethodMayBeAnnotationName(Method m)
	{
		return m.getParameterTypes().length== 0;
	}
	
	/**
	 * A cache set for the default annotation proxy methods.
	 */
	private Set<String> cacheSet = null;
	
	/**
	 * Does a very spiffy hack to make sure that we get all of the default annotation proxy methods.
	 * 
	 *  An annotation is an awkward proxy object in Java. Attribute values are methods() that return the value. 
	 * This is a cool property for consumers, allows for some other stuff that's great, but doesn't make 
	 * any sort of reflective understanding of the annotation easy.
	 * 
	 * If I have an empty instance of an annotation, I have some methods that the proxying system defines.
	 * I can take the symmetric difference between these methods and any other annotation's methods, and those will give 
	 * me the methods that represent annotation values. Those can then be executed to get the value. 
	 * 
	 * This is a dirty dirty thing, but it works. 
	 * @return
	 */
	private Set<String> getDefaultAnnotationProxyMethods()
	{
		if(cacheSet == null)
		{
			final class HackTacular
			{
				@EmptyAnnotationHack
				public int SoWrong;
			}
			
			Annotation hackAttack = null;
			
			try 
			{
				// We get the annotation on the field b/c it'll be a proxy object we can use
				hackAttack = HackTacular.class.getField("SoWrong").getDeclaredAnnotations()[0];
			} 
			catch (Exception t)
			{
				throw new RuntimeException(t);
			}
			
			List<String> defaultMethods = new LinkedList<String>();
			
			for(Method m : hackAttack.getClass().getDeclaredMethods())
			{
				if(MethodMayBeAnnotationName(m))
				{
					// Add everything that can be a method
					defaultMethods.add(m.getName());
				}
			}
			
			// Add it to our cache
			cacheSet = new HashSet<String>();
			cacheSet.addAll(defaultMethods);
		}
		return cacheSet;
	}
}