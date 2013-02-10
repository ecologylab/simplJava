package simpl.descriptions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_composite_as_scalar;
import simpl.annotations.dbal.simpl_map;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.ElementState;
import simpl.tools.XMLTools;
import simpl.types.TypeRegistry;

/**
 * This class has one simple function:
 * Categorize fields.
 */
public class FieldCategorizer {
	
	
	public FieldType categorizeField(Field thatField)
	{
		FieldType fieldType = FieldType.UNSET_TYPE;
		
		// skip static fields, since we're saving instances,
		// and inclusion w each instance would be redundant.
		if ((thatField.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
			// debug("Skipping " + thatField + " because its static!");
			return FieldType.UNSET_TYPE;
		}
		
		if (FieldCategorizer.isScalar(thatField)) {
			fieldType = FieldType.SCALAR;
		} else if (FieldCategorizer.representAsComposite(thatField)) {
			fieldType = FieldType.COMPOSITE_ELEMENT;
		} else if (FieldCategorizer.representAsCollection(thatField)) {
			// THIS WILL NOT BE A PERMANENT SOLUTION.
			if (FieldCategorizer.isEnumCollection(thatField)) {
				// Enums are scalars at the moment.
				fieldType = FieldType.COLLECTION_ELEMENT;
			} else {
				fieldType = FieldType.COLLECTION_ELEMENT;
			}
		} else if (FieldCategorizer.representAsMap(thatField)) {
			fieldType = FieldType.MAP_ELEMENT;
		}
		
		return fieldType;
	}

	public static boolean isEnumCollection(Field f) {
		if(representAsCollection(f))
		{
			ArrayList<Class<?>> classes = XMLTools.getGenericParameters(f);
			if(classes.isEmpty())
			{
				return false;
			}else{
				return classes.get(0).isEnum();
			}
		}else{
			return false;
		}
	}

	public static boolean isComposite(Class thatClass)
	{
		return ElementState.class.isAssignableFrom(thatClass);
	}

	public static boolean isEnum(Class thatClass)
	{
		return Enum.class.isAssignableFrom(thatClass);
	}

	public static boolean isEnum(Field thatField)
	{
		return FieldCategorizer.isEnum(thatField.getType()) || thatField.getType().isEnum();
	}

	/**
	 * @param field
	 * @return true if the Field is one translated by the Type system.
	 */
	public static boolean isScalarValue(Field field)
	{
		return TypeRegistry.containsScalarTypeFor(field.getType());
	}

	public static boolean representAsCollection(Field field)
	{
		return field.isAnnotationPresent(simpl_collection.class);
	}

	public static boolean representAsMap(Field field)
	{
		return field.isAnnotationPresent(simpl_map.class);
	}

	public static boolean representAsCollectionOrMap(Field field)
	{
		return FieldCategorizer.representAsCollection(field) || FieldCategorizer.representAsMap(field);
	}

	public static boolean representAsComposite(Field field)
	{
		return field.isAnnotationPresent(simpl_composite.class);
	}

	public static boolean isCompositeAsScalarvalue(Field field)
	{
		return field.isAnnotationPresent(simpl_composite_as_scalar.class);
	}

	public static boolean isScalar(Field field)
	{
		return field.isAnnotationPresent(simpl_scalar.class);
	}
}
