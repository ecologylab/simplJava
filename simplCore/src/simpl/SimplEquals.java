package simpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import simpl.core.TranslationContext;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;

/**
 * Implementation class to contain the implementation of Simpl.Equals.
 * Ideally, someone should call through simpl.equals. You could call through this too, if you wanted. 
 */
public class SimplEquals {
	
	
	static SimplEquals impl = new SimplEquals();
	
	final class CycleContext{
		List<Object> context;
		public CycleContext(){
			context = new ArrayList<Object>();
		}
		
		public void add(Object o)
		{
			context.add(o);
		}
		
		public boolean visited(Object o)
		{
			return context.contains(o);
		}
	}
	
	/**
	 * Implementation of simpl Equals (Preferably, call the static version at Simpl.equals()
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static boolean equals(Object lhs, Object rhs)
	{
		// resolve the nulls: 
		if(lhs == null && rhs == null)
		{
			return true;
		}
		else if(lhs==null || rhs==null)
		{
			return false;
		}
		
		// Actually delve in and calculate the simplEquals for the two objects.
		return innerEquals(lhs, rhs, impl.new CycleContext());
	}
	
	
	
	private static boolean innerEquals(Object lhs, Object rhs, CycleContext context)
	{
		/*
		if(!context.visited(lhs))
		{
			if(sameClass(lhs, rhs))
			{
				ClassDescriptor<?> cd = ClassDescriptors.getClassDescriptor(lhs);
				
				if(hasFields(cd))
				{
					Collection<IFieldDescriptor> fds; 
					for(IFieldDescriptor fd : fds)
					{
						Object lhsVal = fd.getValue(lhs);
						Object rhsVal = fd.getValue(rhs);
						
						// Validate nullity between the two values. 
						if(lhsVal == null && rhsVal == null)
						{
							return true;
						}
						
						if(lhsVal == null)
						{
							if(rhsVal != null)
							{
								return false;
							}
						}
						
						if(rhsVal == null)
						{
							if(lhsVal != null)
							{
								return false;
							}
						}
						
						if(!((lhsVal == null)&&(rhsVal == null)))
						{
							// So, I'm committing a mortal sin here...
							// But I'm going to do it anyways, for now, because most of the 
							// Represented Strings we deal with actually have a good .ToString() method. :3 
							
							// Using the string representation feels a bit skeevy to me,
							// however! Insofar as the string representation represents the value that we should convert it to, 
							// and insofar as the same string representations will deserialize to the same values...
							// AND insofar as this means that I don't have to worry about adding additional equality 
							// functionality to the simpl scalar interface...
							// This should work for now. :) 
							
							// If perf  / etc of comparing strings becomes too much; this can change. 
							
							// ALSO NOTE: I'm not even using the scalar type for the first pass implementation.
							// TODO: Fix this. Maybe. 
							
							String lhsStr = getValueString(fd, lhs);
							String rhsStr = getValueString(fd, rhs);
							
							if(!lhsStr.equals(rhsStr))
							{
								// When we make an assertion mode, this can go here. ;3
								// TODO: Assert.fail();
								return false;
							}
						}
					}
				}else{
					// this type isn't a simpl type; fall through to java.equals.
					// TODO: Assert.fail() 
					return lhs.equals(rhs);
				}
			
			}
			else
			{
				return false;
			}
		}
		*/
		return true;
	}
	
	private static String getValueString(FieldDescriptor fd, Object o)
	{
		try {
			return fd.getScalarType().getFieldString(fd.getField(), o);
		} catch (SIMPLTranslationException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	private static boolean sameClass(Object lhs, Object rhs)
	{
		return lhs.getClass().equals(rhs.getClass());
	}
	
	private static boolean hasFields(ClassDescriptor<?> cd)
	{
		return !cd.allFieldDescriptors().isEmpty();
	}
}
