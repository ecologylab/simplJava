/**
 * 
 */
package ecologylab.xml;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import ecologylab.generic.Debug;
import ecologylab.generic.ReflectionTools;
import ecologylab.xml.types.scalar.ScalarType;
import ecologylab.xml.types.scalar.TypeRegistry;

/**
 * Small data structure used to optimize translation to XML.
 * 
 * Uses Class or Field so that it can dynamically get custom tags.
 * 
 * @author andruid
 */
class FieldToXMLOptimizations
extends Debug
implements OptimizationTypes
{
    private 	String 		startOpenTag;
    
    /**
     * Used for leaf nodes.
     */
    private		String		openTag;

    private		String 		closeTag;
    
    private		String		tagName;
    
    /**
     * This field is used iff type is COLLECTION or MAP
     */
    private		String		childTagName;
    
    private		Field		field;
    
    /**
     * This field is used iff type is COLLECTION or MAP
     */
    private		Class		childClass;
    
    private int				type;
    
    private boolean			isCDATA;
    
    private boolean			needsEscaping;
    
    /**
     * This slot makes sense only when scalarType != null && scalarType.isFloatingPoint()
     */
    private int				floatValuePrecision;
    
    /**
     * This slot makes sense only for attributes and leaf nodes
     */
    private ScalarType		scalarType;

    FieldToXMLOptimizations(FieldToXMLOptimizations parentCollectionEntry)
    {
    	
    }

    /**
     * Construct for a field where the actualClass can override the one in the declaration.
     * 
     * @param field
     * @param actualClass
     */
    FieldToXMLOptimizations(Field field, Class<? extends ElementState> actualClass)
    {
        setTag(field.isAnnotationPresent(ElementState.xml_tag.class) ? field.getAnnotation(
                ElementState.xml_tag.class).value() : XmlTools.getXmlTagName(actualClass, "State"));
        setType(field, actualClass);
        this.field			= field;
    }

    /**
     * Build a FieldToXMLOptimizations for a root element.
     * Use its class name to form the tag name.
     * 
     * @param rootClass
     */
    FieldToXMLOptimizations(Class rootClass)
    {
    	setTag(XmlTools.getXmlTagName(rootClass, "State"));
    	this.type	= ROOT;
    }
    /**
     * Constructor for collection elements (no field).
     * 
     * @param collectionFieldToXMLOptimizations
     * @param actualCollectionElementClass
     */
    FieldToXMLOptimizations(FieldToXMLOptimizations collectionFieldToXMLOptimizations, Class<? extends ElementState> actualCollectionElementClass)
    {
    	//TODO -- is this inheritance good or bad?!
        String tagName	= collectionFieldToXMLOptimizations.childTagName;
        if (actualCollectionElementClass.isAnnotationPresent(ElementState.xml_tag.class))
        {
			ElementState.xml_tag tagAnnotation 	= actualCollectionElementClass.getAnnotation(ElementState.xml_tag.class);
			String tagFromAnnotation			= tagAnnotation.value();
			if (tagFromAnnotation != null && (tagFromAnnotation.length() > 0))
				tagName							= tagFromAnnotation;
        }
        if (tagName == null)
        	// get the tag name from the class of the object in the Collection, if from nowhere else
        	tagName		= XmlTools.getXmlTagName(actualCollectionElementClass, "State");

        setTag(tagName);
        // no field here?!

        //TODO -- do we need to handle scalars here as well?
        this.type		= REGULAR_NESTED_ELEMENT;
    }
    FieldToXMLOptimizations(Field field)
    {
    	final ElementState.xml_collection collectionAnnotationObj	= field.getAnnotation(ElementState.xml_collection.class);
    	final String collectionAnnotation	= (collectionAnnotationObj == null) ? null : collectionAnnotationObj.value();
    	final ElementState.xml_map mapAnnotationObj	= field.getAnnotation(ElementState.xml_map.class);
    	final String mapAnnotation	= (mapAnnotationObj == null) ? null : mapAnnotationObj.value();
    	final ElementState.xml_tag tagAnnotationObj					= field.getAnnotation(ElementState.xml_tag.class);
    	final String tagAnnotation			= (tagAnnotationObj == null) ? null : tagAnnotationObj.value();
    	final String tagName	= 
    		((collectionAnnotation != null) && (collectionAnnotation.length() > 0)) ? collectionAnnotation :
    		((mapAnnotation != null) && (mapAnnotation.length() > 0)) ? mapAnnotation :
    		((tagAnnotation != null) && (tagAnnotation.length() > 0)) ? tagAnnotation :
    			XmlTools.getXmlTagName(field.getName(), null); // generate from class name

        setTag(tagName);
        this.field				= field;
        setType(field, field.getType());
        boolean isLeaf		= (type == LEAF_NODE_VALUE);
        if (isLeaf || (type == REGULAR_ATTRIBUTE))
        {
        	scalarType		= TypeRegistry.getType(field);
        	if (isLeaf)
        	{
	        	isCDATA			= XmlTools.leafIsCDATA(field);
	        	needsEscaping	= scalarType.needsEscaping();
        	}
        }
    }

    @Override public String toString()
    {
        return "FieldToXMLOptimizations" + closeTag;
    }
    
    private void setTag(String tagName)
    {
    	this.tagName		= tagName;
        startOpenTag 		= '<' + tagName;
        openTag				= startOpenTag + '>';
        closeTag			= "</" + tagName + ">";
    }
	
	private void setType(Field field, Class thatClass)
	{
		int	result			= UNSET_TYPE;
		boolean isScalar	= false;
		boolean isCollection= field.isAnnotationPresent(ElementState.xml_collection.class);
		
		// help people who confuse @xml_nested and @xml_collection for ArrayListState
		if (isCollection && (thatClass != null) && ElementState.class.isAssignableFrom(thatClass))
			result			= REGULAR_NESTED_ELEMENT;			
		else if (field.isAnnotationPresent(ElementState.xml_attribute.class))
		{
			result			= REGULAR_ATTRIBUTE;
			isScalar		= true;
		}
		else if (field.isAnnotationPresent(ElementState.xml_leaf.class))
		{
			result			= LEAF_NODE_VALUE;
			isScalar		= true;
		}
		else if (field.isAnnotationPresent(ElementState.xml_nested.class))
			result			= REGULAR_NESTED_ELEMENT;
		else if (isCollection)
		{
			java.lang.reflect.Type[] typeArgs	= ReflectionTools.getParameterizedTypeTokens(field);
			if (typeArgs != null)
			{
				final Type typeArg0					= typeArgs[0];
				if (typeArg0 instanceof Class)
				{	// generic variable is assigned in declaration -- not a field in an ArrayListState or some such
					Class	collectionElementsType	= (Class) typeArg0;
					println("FieldToXMLOptimizations: !!!collection elements are of type: " + collectionElementsType.getName());
					// is collectionElementsType a scalar or a nested element
					ElementState.xml_collection collectionAnnotation		= field.getAnnotation(ElementState.xml_collection.class);
					String	childTagName			= collectionAnnotation.value();
					if (ElementState.class.isAssignableFrom(collectionElementsType))
					{	// nested element
						result						= COLLECTION_ELEMENT;
						//TODO -- is this inheritance good, or should be wait for the actual class object?!
//						if (childTagName == null)
//							childTagName			= XmlTools.getXmlTagName(thatClass, "State");
						println("FieldToXMLOptimizations: !!!collection elements childTagName = " + childTagName);
					}
					else
					{	// scalar
						result						= COLLECTION_SCALAR;
						this.scalarType				= TypeRegistry.getType(collectionElementsType);
					}
					this.childTagName				= childTagName;
				}
				else	//FIXME -- assume that if 
				{
					result							= COLLECTION_ELEMENT;		
				}
					
			}
			else
			{
				println("WARNING: Cant translate  @xml_collection(\"" + field.getName() + 
						  	 "\") because it is not annotating a parameterized generic Collection defined with a <type> token.");
				result						= IGNORED_ELEMENT;
			}
		}
		else if (field.isAnnotationPresent(ElementState.xml_map.class))
		{
			java.lang.reflect.Type[] typeArgs	= ReflectionTools.getParameterizedTypeTokens(field);
			if (typeArgs != null)
			{
				final Type typeArg0				= typeArgs[1];	// 2nd generic type arg -- type of values, not keys
				if (typeArg0 instanceof Class)
				{	// generic variable is assigned in declaration -- not a field in an ArrayListState or some such

					Class	mapElementsType			= (Class) typeArg0;
					println("FieldToXMLOptimizations: !!!map elements are of type: " + mapElementsType.getName());
					ElementState.xml_map mapAnnotation		= field.getAnnotation(ElementState.xml_map.class);
					String	childTagName			= mapAnnotation.value();
					if (ElementState.class.isAssignableFrom(mapElementsType))
					{	// nested element
						result						= MAP_ELEMENT;						
						//TODO -- is this inheritance good, or should be wait for the actual class object?!
//						if (childTagName == null)
//							childTagName			= XmlTools.getXmlTagName(thatClass, "State");
					}
					else
					{	// scalar
						result						= MAP_SCALAR;
						this.scalarType				= TypeRegistry.getType(mapElementsType);
					}
					this.childTagName				= childTagName;
				}
				else
				{
					//FIXME -- assume that if 
					result							= MAP_ELEMENT;		
				}
			}
			else
			{
				println("WARNING: Cant translate  @xml_map(\"" + field.getName() + 
						  	 "\") because it is not annotating a parameterized generic Collection defined with a <type> token.");
				result						= IGNORED_ELEMENT;
			}
		}
		this.type	= result;
		if (isScalar)
		{
			this.scalarType	= TypeRegistry.getType(field);
		}
	}

	public int type()
	{
		return type;
	}

	public boolean isCDATA()
	{
		return isCDATA;
	}

	public boolean isNeedsEscaping()
	{
		return needsEscaping;
	}

	public String closeTag()
	{
		return closeTag;
	}

	public String tagName()
	{
		return tagName;
	}
	public String startOpenTag()
	{
		return startOpenTag;
	}
    
    public ScalarType scalarType()
	{
		return scalarType;
	}
    
    boolean couldNeedEscaping()
    {
    	return (type == REGULAR_ATTRIBUTE) || ((type == LEAF_NODE_VALUE) && !isCDATA);
    }
    
    /**
     * Use this and the context to append an attribute / value pair to the StringBuilder passed in.
     * 
     * @param buffy
     * @param context
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void appendValueAsAttribute(StringBuilder buffy, Object context) 
    throws IllegalArgumentException, IllegalAccessException
    {
        if (context != null)
        {
        	ScalarType scalarType	= this.scalarType;
        	Field field				= this.field;
        	
        	if (!scalarType.isDefaultValue(field, context))
        	{
	            //for this field, generate tags and attach name value pair
	        	
	        	//TODO if type.isFloatingPoint() -- deal with floatValuePrecision here!
        		// (which is an instance variable of this) !!!
	        	
	        	buffy.append(' ');
				buffy.append(this.tagName);
	        	buffy.append('=');
	        	buffy.append('"');
	        	
	        	scalarType.appendValue(buffy, field, context, true);
	        	buffy.append('"');
        	}
        }
    }

    /**
     * Use this and the context to set an attribute (name, value) on the Element DOM Node passed in.
     * 
     * @param element
     * @param context
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void setAttribute(Element element, Object context) 
    throws IllegalArgumentException, IllegalAccessException
    {
        if (context != null)
        {
        	ScalarType scalarType	= this.scalarType;
        	Field field				= this.field;
        	
        	if (!scalarType.isDefaultValue(field, context))
        	{
	            //for this field, generate tags and attach name value pair
	        	
	        	//TODO if type.isFloatingPoint() -- deal with floatValuePrecision here!
        		// (which is an instance variable of this) !!!
        		
        		String value		= scalarType.toString(field, context);
	        	
        		element.setAttribute(tagName, value);
        	}
        }
    }
    
    /**
     * Use this and the context to set an attribute (name, value) on the Element DOM Node passed in.
     * 
     * @param element
     * @param context
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void appendLeaf(Element element, Object context) 
    throws IllegalArgumentException, IllegalAccessException
    {
        if (context != null)
        {
        	ScalarType scalarType	= this.scalarType;
        	Field field				= this.field;
        	
        	if (!scalarType.isDefaultValue(field, context))
        	{
	            //for this field, generate tags and attach name value pair
	        	
	        	//TODO if type.isFloatingPoint() -- deal with floatValuePrecision here!
        		// (which is an instance variable of this) !!!
        		
        		String value		= scalarType.toString(field, context);
        		Document document 	= element.getOwnerDocument();

        		Text textNode		= isCDATA ? document.createCDATASection(value) : document.createTextNode(value);
        		
        		Element leafNode	= document.createElement(tagName);
        		leafNode.appendChild(textNode);
	        	
        		element.appendChild(leafNode);
        	}
        }
    }
    

    /**
     * Use this and the context to append an attribute / value pair to the Appendable passed in.
     * 
     * @param appendable
     * @param context
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public void appendValueAsAttribute(Appendable appendable, Object context) 
    throws IllegalArgumentException, IllegalAccessException, IOException
    {
        if (context != null)
        {
        	ScalarType scalarType	= this.scalarType;
        	Field field				= this.field;
        	
        	if (!scalarType.isDefaultValue(field, context))
        	{
	            //for this field, generate tags and attach name value pair
	        	
	        	//TODO if type.isFloatingPoint() -- deal with floatValuePrecision here!
        		// (which is an instance variable of this) !!!
	        	
	        	appendable.append(' ');
				appendable.append(this.tagName);
	        	appendable.append('=');
	        	appendable.append('"');
	        	
	        	scalarType.appendValue(appendable, field, context, true);
	        	appendable.append('"');
        	}
        }
    }
    static final String START_CDATA	= "<![CDATA[";
    static final String END_CDATA	= "]]>";
    
    /**
     * Use this and the context to append a leaf node with value to the StringBuilder passed in.
     * 
     * @param buffy
     * @param context
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    void appendLeaf(StringBuilder buffy, Object context) 
    throws IllegalArgumentException, IllegalAccessException
    {
        if (context != null)
        {
        	ScalarType scalarType	= this.scalarType;
        	Field field				= this.field;
        	if (!scalarType.isDefaultValue(field, context))
        	{
        		// for this field, generate <tag>value</tag>
        		
        		//TODO if type.isFloatingPoint() -- deal with floatValuePrecision here!
        		// (which is an instance variable of this) !!!
        		
        		buffy.append(openTag);
        		if (isCDATA)
        			buffy.append(START_CDATA);
        		scalarType.appendValue(buffy, field, context, !isCDATA); // escape if not CDATA! :-)
        		if (isCDATA)
        			buffy.append(END_CDATA);
        		
        		buffy.append(this.closeTag);
        	}
        }
    }

    /**
     * Use this and the context to append a leaf node with value to the Appendable passed in.
     * 
     * @param buffy
     * @param context
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    void appendLeaf(Appendable appendable, Object context) 
    throws IllegalArgumentException, IllegalAccessException, IOException
    {
        if (context != null)
        {
        	ScalarType scalarType	= this.scalarType;
        	Field field				= this.field;
        	if (!scalarType.isDefaultValue(field, context))
        	{
        		// for this field, generate <tag>value</tag>
        		
        		//TODO if type.isFloatingPoint() -- deal with floatValuePrecision here!
        		// (which is an instance variable of this) !!!
        		
        		appendable.append(openTag);
        		
        		if (isCDATA)
        			appendable.append(START_CDATA);
        		scalarType.appendValue(appendable, field, context, !isCDATA); // escape if not CDATA! :-)
        		if (isCDATA)
        			appendable.append(END_CDATA);
        		
        		appendable.append(this.closeTag);
        	}
        }
    }

	Field field()
	{
		return field;
	}
}