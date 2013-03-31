package ecologylab.serialization;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import simpl.descriptors.ParameterDescriptor;

/**
 * Abstract representation of field / class / method meta-information (e.g. Java Annotations or C#
 * Attributes).
 * 
 * @author quyin
 * 
 */
public class MetaInformation
{

	/**
	 * Abstraction of the argument.
	 * 
	 * @author quyin
	 * 
	 */
	public static class Argument
	{

		public String	name;

		public Object	value;

		public String	typeName;

		public String	simpleTypeName;

	}

	/**
	 * The (simple) type name of the meta-information (annotation/attribute) class.
	 */
	public String					simpleTypeName;

	/**
	 * The (qualified) name of the meta-information (annotation/attribute) class.
	 */
	public String					typeName;

	/**
	 * Arguments.
	 */
	public List<Argument>	args;

	/**
	 * If all arguments should be put in an array. If so, all the values must be of the same type, and
	 * the type name of the first element will be used when necessary.
	 */
	public boolean				argsInArray	= false;
	
	/**
	 * If arguments are named. Typically, if there is only one argument or one array of arguments with
	 * the same type, the name can be omitted. However if there is more than one arguments we will
	 * need names to disambiguate.
	 */
	public boolean				argsNamed = false;

	public MetaInformation()
	{
		super();
	}

	/**
	 * Create a MetaInformation with no arguments from a java annotation. Other languages should
	 * implement their own constructors.
	 * 
	 * @param annotationClass
	 */
	public MetaInformation(Class<? extends Annotation> annotationClass)
	{
		this();
		this.simpleTypeName = annotationClass.getSimpleName();
		this.typeName = annotationClass.getName();
	}

	/**
	 * Create a MetaInformation with homogeneous arguments (e.g. 1 argument or an argument array). In
	 * this case we assume arguments do not need names (there is only one argument or one array of
	 * arguments with the same type).
	 * 
	 * @param annotationClass
	 * @param argsInArray
	 * @param args
	 */
	public MetaInformation(Class<? extends Annotation> annotationClass, boolean argsInArray, Object... args)
	{
		this(annotationClass);
		
		this.argsInArray = argsInArray;
		
		this.args = new ArrayList<Argument>(args.length);
		for (Object arg : args)
		{
			Argument a = new Argument();
			a.value = arg;
			a.typeName = arg.getClass().getName();
			a.simpleTypeName = arg.getClass().getSimpleName();
			this.args.add(a);
		}
	}

	/**
	 * Create a MetaInformation with named heterogeneous arguments.
	 * 
	 * @param annotationClass
	 * @param argNames
	 * @param argValues
	 */
	public MetaInformation(Class<? extends Annotation> annotationClass, String[] argNames, Object[] argValues)
	{
		this(annotationClass);
		
		assert argNames.length == argValues.length : "Different number of argument names and values!";
		
		this.argsNamed = true;
		this.args = new ArrayList<Argument>(argNames.length);
		for (int i = 0; i < argNames.length; ++i)
		{
			Argument a = new Argument();
			a.name = argNames[i];
			a.value = argValues[i];
			a.typeName = a.value.getClass().getName();
			a.simpleTypeName = a.value.getClass().getSimpleName();
			this.args.add(a);
		}
	}

	public List<ParameterDescriptor> getAnnotationParameters() {
		return new LinkedList<ParameterDescriptor>(); // return an empty list; 
		// this isn't supported by the "old" version of S.IM.PL; 
		// will be robustly suppoted soon~! 
	}

}
