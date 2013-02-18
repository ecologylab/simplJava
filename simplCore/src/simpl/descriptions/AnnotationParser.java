package ecologylab.simpl.descriptors.utilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import ecologylab.simpl.descriptors.ParameterDescriptor;

public class AnnotationParser
{
	/**
	 * Gets ParameterDescriptors that describe the parameters used for this particular annotation.
	 * Used primarily for getting the information for code generation
	 * @param a The Annotation Class to parse
	 * @return ParameterDescriptors for all entries
	 */
	public <T extends Annotation> List<ParameterDescriptor> getParametersFromAnnotation(T a) 
	{
		List<ParameterDescriptor> ourList = Lists.newArrayList();
						
		Class<? extends Annotation> annotClass = a.getClass();
		
		Method[] methods = annotClass.getDeclaredMethods();
		
		List<String> ourMethods = Lists.newArrayList();
		
		for(Method m : methods)
		{
			if(MethodMayBeAnnotationName(m))
				ourMethods.add(m.getName());
		}
	

		
		Set<String> ourMethodSet = 	Sets.newHashSet(ourMethods);		
		Set<String> defaultMethodSet = getDefaultAnnotationProxyMethods();

		// Get the difference. What isn't in the default method set will be a value in the attribute
		Set<String> result = Sets.symmetricDifference(ourMethodSet, defaultMethodSet);
		
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
			
				ourList.add(new ParameterDescriptor(name,paramType,value));
			}
			catch(Throwable t)
			{
				Throwables.propagate(t);
			}
		}
		
		return ourList;
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
				hackAttack = HackTacular.class.getField("SoWrong").getDeclaredAnnotations()[0];
			} 
			catch (Throwable t)
			{
				Throwables.propagate(t);
			}
			
			List<String> defaultMethods = Lists.newArrayList();
			
			for(Method m : hackAttack.getClass().getDeclaredMethods())
			{
				if(MethodMayBeAnnotationName(m))
					defaultMethods.add(m.getName());
			}
			
			cacheSet = Sets.newHashSet(defaultMethods);
		}
		return cacheSet;
	}

	
}