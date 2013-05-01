/**
 * 
 */
package simpl.descriptions;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;

import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_scalar;
import simpl.platformspecifics.SimplPlatformSpecifics;

import ecologylab.generic.Debug;

/**
 * This class encapsulates generic type variables declarations on classes and fields.
 * 
 * Different uses of this class:
 * 
 * <p>
 * When used for definition before 'extends' (in ClassDescriptor): name + constraintClassDescriptor
 * (+ constraintGenericTypeVarArgs) | constraintGenericTypeVar:<br>
 * name: the name of the new generic type var,<br>
 * constraintClassDescriptor: when the constraint is a concrete class, this holds the class
 * descriptor;<br>
 * constraintGenericTypeVar: when the constraint is another generic type var, this refers to the
 * definition of that generic type var;<br>
 * constraintGenericTypeVarArgs: when the constraint is parameterized, this holds type arguments.
 * </p>
 * 
 * <p>
 * When used with field types (in FieldDescriptor):
 * <ul>
 * <li>if the field is purely generic, name + referredGenericTypeVar:<br>
 * name: the generic type var name used as the type,<br>
 * referredGenericTypeVar: refers to the definition of this generic type var in class definition.</li>
 * <li>if the field is parameterized, the FieldDescriptor should already have the class part of the
 * field type, and its genericTypeVars field should have a list of type arguments. each type
 * argument has classDescriptor / referredGenericTypeVar:<br>
 * classDescriptor: if the argument is a concrete class,<br>
 * referredGenericTypeVar: if the argument is parameterized or another generic type var. it should
 * refer to the definition of that generic type var in class definition.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * When used with base type after 'extends' (in ClassDescriptor): similar to the parameterized case
 * when it is used for field type.
 * </p>
 * 
 * @author quyin
 * 
 */
public class GenericTypeVar extends Debug
{
	
	// The declared name of the generic type variable. such as 'M' for Media<M> test;
	// Wild card operator '?' will also be populated in this field. 
	// The classDescriptor will be null if this parameter is populated. 
	@simpl_scalar
	String										name;

	// This variable holds the ClassDecriptor of the class declared as a constraint to the generic type variable. 
	// e.g. for M, this holds ClassDescriptor<Media> in class MediaSearchResult<M extends Media>.
	@simpl_composite
	ClassDescriptor						constraintClassDescriptor				= null;
	
	// This variable holds the generic type variable as the constraint.
	// e.g. for T1, this holds a reference to the definition of T in class MyClass<T1 extends T>. 
	@simpl_composite
	GenericTypeVar						constraintGenericTypeVar    		= null;

	// This variable holds the args of generic type variables of a parameterized constraint. 
	// e.g. for M, this holds references to definitions of R & S in class MediaSearchResult<M extends Media<R,S>>.
	@simpl_collection("generic_type_var")
	ArrayList<GenericTypeVar>	constraintGenericTypeVarArgs	= null;

	// ClassDescriptor of the type arg. Not used for defining a new generic type var.
	// e.g. ClassDescriptor<Media> in MediaSearchResult<Media>; 
	@simpl_composite
	ClassDescriptor						classDescriptor									= null;

	// If the type arg is parameterized, this holds the type arguments. each element in this collection
	// should have a name and a reference to the definition of that generic type var used as arg.
	// e.g. M & T in MediaSearchResult<Media<M,T>> (in this case the field classDescriptor should be ClassDescriptor<MediaSearchResult>) 
	@simpl_collection("generic_type_var")
	ArrayList<GenericTypeVar>	genericTypeVarArgs							= null;

	// Refers to another generic type var, typically the definition. 
	// e.g. the 2nd M in class MediaSearchResult<M extends Media, M> (that GenericTypeVar object will have name=M and referredGenericTypeVar to the 1st M) 
	// may be used in other cases. see the javadoc of this class.
	@simpl_composite
	GenericTypeVar						referredGenericTypeVar					= null;
	
	/**
	 * A list of GenericTypeVars that can be referred to recursively inside this GenericTypeVar.
	 */
	ArrayList<GenericTypeVar> scope;

	public GenericTypeVar()
	{
			//for simpl de/serialzation
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public ClassDescriptor getConstraintClassDescriptor()
	{
		return constraintClassDescriptor;
	}

	public void setConstraintClassDescriptor(ClassDescriptor constraintClassDescriptor)
	{
		this.constraintClassDescriptor = constraintClassDescriptor;
	}

	public GenericTypeVar getConstraintGenericTypeVar()
	{
		return constraintGenericTypeVar;
	}

	public void setConstraintGenericTypeVar(GenericTypeVar constraintGenericTypeVar)
	{
		this.constraintGenericTypeVar = constraintGenericTypeVar;
	}

	public ArrayList<GenericTypeVar> getConstraintGenericTypeVarArgs()
	{
		return constraintGenericTypeVarArgs;
	}

	public void addContraintGenericTypeVarArg(GenericTypeVar g)
	{
		if (constraintGenericTypeVarArgs == null)
			constraintGenericTypeVarArgs = new ArrayList<GenericTypeVar>();
		constraintGenericTypeVarArgs.add(g);
	}

	public ClassDescriptor getClassDescriptor()
	{
		return classDescriptor;
	}
	
	public void setClassDescriptor(ClassDescriptor classDescriptor)
	{
		this.classDescriptor = classDescriptor;
	}
	
	public ArrayList<GenericTypeVar> getGenericTypeVarArgs()
	{
		return genericTypeVarArgs;
	}

	public void addGenericTypeVarArg(GenericTypeVar arg)
	{
		if (genericTypeVarArgs == null)
			genericTypeVarArgs = new ArrayList<GenericTypeVar>();
		genericTypeVarArgs.add(arg);
	}

	public GenericTypeVar getReferredGenericTypeVar()
	{
		return referredGenericTypeVar;
	}

	public void setReferredGenericTypeVar(GenericTypeVar referredGenericTypeVar)
	{
		this.referredGenericTypeVar = referredGenericTypeVar;
	}
	
	public ArrayList<GenericTypeVar> getScope()
	{
		return scope;
	}

	/**
	 * Creates a GenericTypeVar object as the definition of a new generic type var, from a java
	 * reflection TypeVariable object.
	 * 
	 * @param typeVariable
	 * @param scope
	 *          the scope of current generic type vars; used to resolve generic type var names.
	 * @return
	 */
	public static GenericTypeVar getGenericTypeVarDef(TypeVariable<?> typeVariable, ArrayList<GenericTypeVar> scope)
	{
		GenericTypeVar g = new GenericTypeVar();
		g.scope = scope;
		g.name = typeVariable.getName();
		
		// resolve constraints
		resolveGenericTypeVarDefinitionConstraints(g, typeVariable.getBounds());

		g.scope = null;
		return g;
	}
	
	/**
	 * Creates a GenericTypeVar object as in a type reference (usage), from a java reflection Type
	 * object.
	 * 
	 * @param type
	 * @param scope
	 * @return
	 */
	public static GenericTypeVar getGenericTypeVarRef(Type type, ArrayList<GenericTypeVar> scope)
	{
		GenericTypeVar g = new GenericTypeVar();
		g.scope = scope;

		// case 1: arg is a concrete class
		if (type instanceof Class<?>)
		{
			Class<?> typeClass = (Class<?>) type;
			g.classDescriptor = ClassDescriptors.getClassDescriptor(typeClass);
			return g;
		}

		// case 2: arg is another generic type var
		if (type instanceof TypeVariable<?>)
		{
			TypeVariable<?> typeVar = (TypeVariable<?>) type;
			String argName = typeVar.getName();
			if (argName != null && scope != null)
			{
				for (GenericTypeVar var : scope)
					if (argName.equals(var.getName()))
					{
						g.name = var.getName();
						g.referredGenericTypeVar = var;
						break;
					}
			}
		}

		// case 3: arg is a wildcard
		checkTypeWildcardTypeImpl(g, type);

		// case 4: arg is parameterized
		checkTypeParameterizedTypeImpl(g, type);
		
		g.scope = null;
		return g;
	}

	/**
	 * Resolves constraints on the definition of a generic type var.
	 * 
	 * @param g
	 * @param bounds
	 */
	public static void resolveGenericTypeVarDefinitionConstraints(GenericTypeVar g, Type[] bounds)
	{
		if (bounds == null)
			return;

		Type bound = bounds[0];

		// case 1: constraint is a concrete class
		if (bound instanceof Class<?>)
		{
			Class<?> boundClass = (Class<?>) bound;
			if (Object.class != boundClass)
				g.constraintClassDescriptor = ClassDescriptors.getClassDescriptor(boundClass);
		}

		// case 2: constraint is another generic type var
		if (bound instanceof TypeVariable<?>)
		{
			TypeVariable<?> boundTypeVar = (TypeVariable<?>) bound;
			// look up the scope to find the bound generic type var (must have been defined)
			String boundName = boundTypeVar.getName();
			if (boundName != null && g.scope != null)
			{
				for (GenericTypeVar var : g.scope)
					if (boundName.equals(var.getName()))
					{
						g.setConstraintGenericTypeVar(var);
						break;
					}
			}
//			g.addContraintGenericTypeVarArg(getGenericTypeVar(boundTypeVar));
		}
		
		// case 3: constraint is parameterized -- the most complicated case
		checkBoundParameterizedTypeImpl(g, bound);
	}

	/**
	 * Resolves constraints on a generic type var that is used in a type reference (usage).
	 * 
	 * @param g
	 * @param bounds
	 */
	public static void resolveGenericTypeVarReferenceConstraints(GenericTypeVar g, Type[] bounds)
	{
		if (bounds == null)
			return;

		Type bound = bounds[0];

		// case 1: constraint is a concrete class
		if (bound instanceof Class<?>)
		{
			Class<?> boundClass = (Class<?>) bound;
			if (Object.class != boundClass)
				g.classDescriptor = ClassDescriptors.getClassDescriptor(boundClass);
		}

		// case 2: constraint is another generic type var
		if (bound instanceof TypeVariable<?>)
		{
			TypeVariable<?> boundTypeVar = (TypeVariable<?>) bound;
			// look up the scope to find the bound generic type var (must have been defined)
			String boundName = boundTypeVar.getName();
			if (boundName != null && g.scope != null)
			{
				for (GenericTypeVar var : g.scope)
					if (boundName.equals(var.getName()))
					{
						g.setConstraintGenericTypeVar(var);
						break;
					}
			}
		}
		
		// case 3: constraint is parameterized -- the most complicated case
		checkBoundParameterizedTypeImpl(g, bound);
	}
	
	public static void checkBoundParameterizedTypeImpl (GenericTypeVar g, Type bounds)
	{
		SimplPlatformSpecifics.get().checkBoundParameterizedTypeImpl(g, bounds);
	}
	
	// added two helper functions for GenericTypeVar
	public static void checkTypeWildcardTypeImpl(GenericTypeVar g, Type type)
	{
		SimplPlatformSpecifics.get().checkTypeWildcardTypeImpl(g, type);
	}
	
	public static void checkTypeParameterizedTypeImpl(GenericTypeVar g, Type type)
	{
		SimplPlatformSpecifics.get().checkTypeParameterizedTypeImpl(g, type);
	}
	
	public boolean isDef()
	{
		return name != null && name.length() > 0 && (constraintClassDescriptor != null || constraintGenericTypeVar != null) && referredGenericTypeVar == null;
	}
	
	/**
	 * Return a string representation of this GenericTypeVar object. The syntax is like in Java.
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		if (isDef())
		{
			sb.append(name);
			if (constraintGenericTypeVar != null)
			{
				sb.append(" extends ").append(constraintGenericTypeVar.name);
			}
			else if (constraintClassDescriptor != null)
			{
				sb.append(" extends ").append(constraintClassDescriptor.getSimpleName());
				if (constraintGenericTypeVarArgs != null && constraintGenericTypeVarArgs.size() > 0)
				{
					sb.append("<");
					for (int i = 0; i < constraintGenericTypeVarArgs.size(); ++i)
					{
						GenericTypeVar g = constraintGenericTypeVarArgs.get(i);
						sb.append(i == 0 ? "" : ",").append(g.toString());
					}
					sb.append(">");
				}
			}
		}
		else
		{
			if (name != null || referredGenericTypeVar != null)
			{
				sb.append(name);
			}
			else if (classDescriptor != null)
			{
				sb.append(classDescriptor.getSimpleName());
				if (genericTypeVarArgs != null && genericTypeVarArgs.size() > 0)
				{
					sb.append("<");
					for (int i = 0; i < genericTypeVarArgs.size(); ++i)
					{
						GenericTypeVar g = genericTypeVarArgs.get(i);
						sb.append(i == 0 ? "" : ",").append(g.toString());
					}
					sb.append(">");
				}
			}
		}

		return sb.toString();
	}
	
	// these methods has been moved to the platform specific package in the corresponding project:
//	public static ArrayList<GenericTypeVar> getGenericTypeVars(Type parameterizedType);
//	public static ArrayList<GenericTypeVar> getGenericTypeVars(ParameterizedTypeImpl parameterizedType);
	
}
