/**
 * 
 */
package ecologylab.serialization.annotations;

/**
 * Usages for SIMPL fields.
 * 
 * @author quyin
 */
public enum FieldUsage
{
	
	CODE_GENERATION,					/* this field is used for code generation. */
	
	SERIALIZATION_IN_STREAM,	/* this field is serialized to streams (XML, JSON, ...). */
	
	PERSISTENCE,							/* this field is persisted in data stores. */

}
