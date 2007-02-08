package ecologylab.xml;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.w3c.dom.Node;

import ecologylab.generic.Debug;
import ecologylab.generic.ReflectionTools;
import ecologylab.xml.types.scalar.Type;
import ecologylab.xml.types.scalar.TypeRegistry;

/**
 * Holds optimizations for translating from XML a single tag or attribute
 * within a contextualizing ElementState subclass.
 *
 * @author andruid
 */
class ParseTableEntry extends Debug
implements ParseTableEntryTypes
{
	private final String		tag;
	
	private boolean				isID;
	
	private int					type;
	
	private String				nameSpaceName;
	
	/**
	 * The Field object that we should set. 
	 * Usually this is defined in the context of the original ElementState object passed to
	 * the constructor. It corresponds to the tag originally passed in.
	 * However, in the case of an XML Namespace, it may be defined in the context of a nested field.
	 */
	private Field				field;
	
	private Method				setMethod;
	
	private Type				fieldType;
	
	/**
	 * true for LEAF_NODE_VALUE entries. Value not used for other types.
	 */
//	private	boolean				isCDATA;
	
	/**
	 * Usually the class of the field corresponding to the tag in the context of the original ElementState 
	 * object passed to the constructor. However, n the case of an XML Namespace, it may be the class
	 * of a nested Namespace object.
	 */
	private Class				classOp;
	
	private TranslationSpace	translationSpace;
	
	private ParseTableEntry		nestedPTE;
	
	/**
	 * 
	 * @param translationSpace
	 * @param optimizations
	 * @param context
	 * @param tag		Tag corresponding to the value in the XML that we're translating.
	 * @param isAttribute TODO
	 */
	ParseTableEntry(TranslationSpace translationSpace, Optimizations optimizations, ElementState context, String tag, boolean isAttribute)
	{
		super();
		this.tag				= tag;
		this.translationSpace	= translationSpace;
		
		Class contextClass 		= context.getClass();
		int colonIndex			= tag.indexOf(':');
		
		if (isAttribute)
		{
			if ("id".equals(tag))
				isID			= true;
			
			if (colonIndex > -1)
			{	//TODO -- make this do the right thing
				this.type		= IGNORED_ATTRIBUTE;
				return;
			}
			
			setupScalarValue(tag, optimizations, contextClass, true);
			return;
		}
		
		// element, not attribute
		if (colonIndex > 0)
		{	// there is an XML namespace specified in the XML!
			nameSpaceName		= tag.substring(0, colonIndex);
			translationSpace	= TranslationSpace.get(nameSpaceName);
			String subTag		= tag.substring(colonIndex+1);
			// is there a field called nameSpaceName?
			Field nameSpaceField= optimizations.getField(nameSpaceName);
			if (nameSpaceField != null)
			{	// o.k. we know we're working in the object of namespace fields
				// create a dummy object to get its ParseTableEntry
				Class nsFieldClass			= nameSpaceField.getType();
				Object dummy				= ReflectionTools.getInstance(nsFieldClass);
				if (dummy instanceof ElementState)
				{
					this.field				= nameSpaceField;
					ElementState dummyES 	= (ElementState) dummy;
					Optimizations nsOptimizations	= Optimizations.lookup(dummyES);
					TranslationSpace nameSpaceTranslations	= TranslationSpace.get(nameSpaceName);
					ParseTableEntry	 nsPTE	= nsOptimizations.parseTableEntry(nameSpaceTranslations, dummyES, subTag);
					this.classOp			= nsFieldClass;
					fillValues(nsPTE);
					this.nestedPTE			= nsPTE;
				}
				else
				{
					println("ERROR: there is a field called " + nameSpaceName + " in " + contextClass +
							" but it is not of type ElementState!");
					this.type		= BAD_FIELD;
					return;
				}
			}
			
		}
		else
		{	// no XML namespace; life is simpler.
			
			// try as leaf node
			int diganosedType		= setupScalarValue(tag, optimizations, contextClass, false);
			switch (diganosedType)
			{
			case LEAF_NODE_VALUE:
				this.classOp		= contextClass;
/*				ElementState.xml_leaf leafAnnotation		= field.getAnnotation(ElementState.xml_leaf.class);
				if ((leafAnnotation != null) && (leafAnnotation.value() == ElementState.CDATA))
						this.isCDATA= true;
 */				return;
			case IGNORED_ELEMENT:
				if (this.field != null)
				{
					// this is actually a regular nested element
					this.type	= REGULAR_NESTED_ELEMENT;
//					this.classOp	= field.getType();
					// changed 12/2/06 by andruid -- use the type in the TranslationSpace!
					Class thatClass	= translationSpace.getClassByTag(tag);
					if (thatClass == null)
						thatClass	= field.getType();
					this.classOp	= thatClass;
					return;
				}
				// no field object, so we must continue to check stuff out!
				break;
			default:
				printError("Unknown case in element type assignment switch " + 
						    diganosedType + ".");
				return;
			}

			// else there is no Field to resolve. but there must a class!
			Class classOp	= translationSpace.xmlTagToClass(tag);
			if (classOp != null)
			{
				Collection collection = context.getCollection(classOp);
				this.type	= (collection != null) ?
						COLLECTION_ELEMENT : OTHER_NESTED_ELEMENT;
				this.classOp= classOp;
			}
			else
			{
				context.debugA("WARNING - ignoring <" + tag() +"/>");
				this.type	= IGNORED_ELEMENT;
			}
		}
	}

/**
 * Set-up PTE for scalar valued field (attribute or leaf node).
 * First look for a set method.
 * Then, look in the type registry.
 * Else, we must ignore the field.
 * 
 * @param tag
 * @param optimizations TODO
 * @param contextClass
 * @param isAttribute		true for attribute; false for leaf node.
 */
	private int setupScalarValue(String tag, Optimizations optimizations, Class contextClass, boolean isAttribute)
	{
		int type			= UNSET_TYPE;
		String methodName	= XmlTools.methodNameFromTagName(tag);
		Method setMethod	= 
			ReflectionTools.getMethod(contextClass, methodName, ElementState.MARSHALLING_PARAMS);
		if (setMethod != null)
		{
			this.setMethod	= setMethod;
			type			= isAttribute ? REGULAR_ATTRIBUTE : LEAF_NODE_VALUE;
			// set method is custom code on a per field basis, and so doesnt need field object
		}
		else
		{
			String fieldName= XmlTools.fieldNameFromElementName(tag);
			Field field		= optimizations.getField(fieldName);
			if (field != null)
			{
				Type fieldType		= TypeRegistry.getType(field);
				if (fieldType != null)
				{
					this.fieldType	= fieldType;
					type			= isAttribute ? REGULAR_ATTRIBUTE : LEAF_NODE_VALUE;
					this.field		= field;
				}
				else if (!isAttribute)
					this.field	= field; // for leaf node seekers that can be nested elements
			}
		}
		if (type == UNSET_TYPE)
		{
			type					= isAttribute ? IGNORED_ATTRIBUTE : IGNORED_ELEMENT;
			if (isAttribute)
				printError("no set method or type to set value for this tag in " + 
						contextClass.getName() + ".");
		}
		this.type				= type;
		return type;
	}
	
	/**
	 * Use this as the spec for obtaining a new ElementState (subclass),
	 * springing forth from the parent, and based on the XML Node.
	 * 
	 * @param parent
	 * @param node
	 * @return
	 * @throws XmlTranslationException
	 */
	ElementState getChildElementState(ElementState parent, Node node)
	throws XmlTranslationException
	{
		return parent.getChildElementState(node, classOp, translationSpace);
	}
	
	/**
	 * Use a set method or the type system to set our field in the context to the value.
	 * 
	 * @param context
	 * @param value
	 */
	void setAttribute(Object context, String value)
	{
		if (setMethod != null)
		{
			// if the method is found, invoke the method
			// fill the String value with the value of the attr node
			// args is the array of objects containing arguments to the method to be invoked
			// in our case, methods have only one arg: the value String
			Object[] args = new Object[1];
			args[0]		  = value;
			try
			{
				setMethod.invoke(context, args); // run set method!
			}
			catch (InvocationTargetException e)
			{
				printWeird("couldnt run set method for " + tag +
						  " even though we found it");
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				printWeird("couldnt run set method for " + tag +
						  " even though we found it");
				e.printStackTrace();
			}	  
			
		}
		else if (fieldType != null)
		{
			fieldType.setField(context, field, value);
		}
	}
	
	void printError(String msg)
	{
		printMessage("ERROR", msg); 		
	}
	void printWarn(String msg)
	{
		printMessage("WARNING", msg); 		
	}
	void printWeird(String msg)
	{
		printMessage("WEIRD", msg); 		
	}
	private void printMessage(String prefix, String msg)
	{
		String tag	= this.tag;
		if (tag == null)	//TODO if this happens find a way to show a better error
			tag		= "NO TAG?";
		println(tag + ": " + prefix + " - " + msg); 		
	}
	/**
	 * Set a scalar value using the textElementChild Node as the source,
	 * the stateClass as the template for where the field is located, 
	 * the childFieldName as the name of the field to select in the template,
	 * and this as the object to do the set in.
	 * 
	 * @param Object			The object in which we are setting the field's value.
	 * @param textElementChild	The leaf node with the text element value.
	 */
	void setLeafNodeValue(Object context, Node textElementChild)
	{
		if (textElementChild != null)
		{
			String textNodeValue	= textElementChild.getNodeValue();
			if (textNodeValue != null)
			{
				textNodeValue		= textNodeValue.trim();
				if ((fieldType != null) && fieldType.needsEscaping())
					textNodeValue	= XmlTools.unescapeXML(textNodeValue);
				//debug("setting special text node " +childFieldName +"="+textNodeValue);
				if (textNodeValue.length() > 0)
				{
					setAttribute(context, textNodeValue);
				}
			}
		}
	}
	
	/**
	 * Used to set a field in this to a nested ElementState object.
	 * 
	 * his method is called during translateFromXML(...).
	 *
	 * @param nestedElementState	the nested state-object to be added
	 */
	protected void setFieldToNestedElement(ElementState context, Node childNode)
		throws XmlTranslationException
	{
		Object nestedElementState	= getChildElementState(context, childNode);
		try
		{
			field.set(context, nestedElementState);
		}
		catch (Exception e)
		{
		   throw new XmlTranslationException(
					"Object / Field set mismatch -- unexpected. This should never happen.\n\t"+
					"with Field = " + field +"\n\tin object " + this +"\n\tbeing set to " + nestedElementState.getClass(), e);
		}
	}
	
/**
 * Add element derived from the Node to a Collection.
 * 
 * @param activeES
 * @param childNode
 * @throws XmlTranslationException
 */
	void addToCollection(ElementState activeES, Node childNode)
	throws XmlTranslationException
	{
		Collection collection		= activeES.getCollection(classOp());
		// the sleek new way to add elements to collections
		collection.add(getChildElementState(activeES, childNode));
	}
	
	private void fillValues(ParseTableEntry other)
	{
		//this.classOp			= other.classOp;
		this.type				= other.type;
		// i think this field could be gotten from here (the child) or be propagated from us
		// no difference, cause the current Field has to appear in both TranslationSpaces!
		this.translationSpace	= other.translationSpace;
	}

	/**
	 * @return the Class operand that we need to work with in translation from XML.
	 */
	Class classOp()
	{
		return classOp;
	}

	/**
	 * @return the field
	 */
	Field field()
	{
		return field;
	}

	/**
	 * @return the nameSpaceName
	 */
	String nameSpaceName()
	{
		return nameSpaceName;
	}

	/**
	 * @return the nestedNameSpaceParseTableEntry
	 */
	ParseTableEntry nestedPTE()
	{
		return nestedPTE;
	}

	/**
	 * @return the type
	 */
	int type()
	{
		return type;
	}

	/**
	 * @return the translationSpace
	 */
	TranslationSpace translationSpace()
	{
		return translationSpace;
	}

	/**
	 * @return the tag
	 */
	String tag()
	{
		return tag;
	}

	/**
	 * @return the isID
	 */
	boolean isID()
	{
		return isID;
	}

}
