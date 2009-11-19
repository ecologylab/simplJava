/**
 * 
 */
package ecologylab.xml;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import ecologylab.generic.Debug;
import ecologylab.generic.ReflectionTools;
import ecologylab.xml.types.scalar.ScalarType;
import ecologylab.xml.types.scalar.TypeRegistry;

//FIXME -- will be merged with and called FieldDescriptor in the Next Generation (TNG :-)
/**
 * Small data structure used to optimize translation to XML.
 * 
 * Uses Class or Field so that it can dynamically get custom tags.
 * 
 * @author andruid
 */
public class FieldToXMLOptimizations
extends Debug
implements ClassTypes
{
    public static final String XMLNS_URN = "http://www.w3.org/2000/xmlns/";

    private		String		tag;
    
    private		String		nameSpacePrefix;
    
    /**
     * This field is used iff type is COLLECTION or MAP.
     * 
     * We also borrow it for use for the URN in for type XMLNS_ATTRIBUTE.
     */
    private		String		childTagName;
    
    private		Field		field;
    
    /**
     * For noting that the object of this root or @xml_nested field has, within it, a field declared with @xml_text.
     */
    private		Field		xmlTextField;
    
    private		ScalarType	xmlTextScalarType;
    
    /**
     * This field is used iff type is COLLECTION or MAP
     */
    private		Class		childClass;
    
    /**
     * Optimizations object for the ElementState object that was the context when
     * the field represented by this was processed.
     */
    private		ClassDescriptor	declaringClassDescriptor;
    
    private int				type;
    
    private boolean			isCDATA;
    
    private boolean			needsEscaping;
    
    /**
     * true if this is a collection or map field annotated with the name of child elements.
     */
    private	boolean			hasCollectionOrMapTag;
    
    private boolean			hasXmlClasses;
    
    /**
     * This slot makes sense only for attributes and leaf nodes
     */
    private ScalarType		scalarType;
  
    /**
     * An option for scalar formatting.
     */
    private String[]		format;

    /**
     * Construct for a field where the actualClass can override the one in the declaration, 
     * that is, for an @xml_nested child.
     * 
     * @param optimizations TODO
     * @param field
     * @param actualClass
     */
    FieldToXMLOptimizations(ClassDescriptor declaringClassDescriptor, Field field, Class<? extends ElementState> actualClass)
    {
    	this.declaringClassDescriptor	= declaringClassDescriptor;
    	// this looks wrong to me -- andruid 11/12/09
    	// why use tag from the field, then the class name, not the field name????
    	
    	this.tag				= field.isAnnotationPresent(ElementState.xml_tag.class) ? 
    			field.getAnnotation(ElementState.xml_tag.class).value() : XMLTools.getXmlTagName(actualClass, "State");
    			
    	setType(field, actualClass);
    	this.field			= field;

    	setupXmlText(declaringClassDescriptor);
    }

    /**
     * Build a FieldToXMLOptimizations for a root element.
     * There is no field!
     * Use its class name to form the tag name.
     * @param optimizations TODO
     */
    FieldToXMLOptimizations(ClassDescriptor declaringClassDescriptor)
    {
    	this.declaringClassDescriptor	= declaringClassDescriptor;
    	setupXmlText(declaringClassDescriptor);
    	this.tag	= XMLTools.getXmlTagName(declaringClassDescriptor.describedClass(), "State");	// by class name, not by field name :-)
    	this.type	= ROOT;
//    	if (nameSpaceID != null)
//    	{
//    		this.nameSpacePrefix	= nameSpaceID + ":";	// set-up the prefix
//    		// with the prefix, we dont create an enclosing element!
//    		this.startOpenTag		= "";
//    		this.openTag			= "";
//    		this.closeTag			= "";
//    	}
    }

    /**
     * If this is nested, and has a corresponding ScalarTyped field declared with @xml_text,
     * set-up associated optimizations.
     * 
     * @param classDescriptor
     */
	private void setupXmlText(ClassDescriptor classDescriptor)
	{
		Field optimizationsScalarTextField = classDescriptor.getScalarTextField();
		if (optimizationsScalarTextField != null)
		{
			this.xmlTextField					= optimizationsScalarTextField;
			this.xmlTextScalarType		= TypeRegistry.getType(optimizationsScalarTextField);
		}
	}
    
	boolean isXmlNsDecl()
	{
		return false;	// for some hacking reason, in ElementState, this test was startOpenTag().length() > 0
	}
	
	String prefixedXmlNs;
	String xmlNsDecl;
	String urn;
    /**
     * Build a FieldToXMLOptimizations pseudo-object for an XML Namespace scope declaration
     * attribute.
     * We stuff the entire xmlns:id="http://blah" into the tagName for efficiency's sake.
     * 
     * @param nameSpaceID
     */
    FieldToXMLOptimizations(String nameSpaceID, String urn, boolean ignored)
    {
    	this.tag				= nameSpaceID;
    	String prefixedXMLNS		= "xmlns:" + tag;
    	this.prefixedXmlNs			= prefixedXMLNS;
    	this.xmlNsDecl					= " " + prefixedXMLNS + "=\"" + urn + "\"";
    	this.urn								= urn;
    	this.type								= ignored ? XMLNS_IGNORED : XMLNS_ATTRIBUTE;
    }
    
    /**
     * Return a well-formed xmlns: attribute with namespace id and urn.
     * Or, if this is not an XMLNS_ATTRIBUTE, do nothing.
     * 
     * @return
     * @throws IOException 
     */
    void xmlnsAttr(StringBuilder buffy)
    {
    	if (type == XMLNS_ATTRIBUTE)
    	{
    		buffy.append(xmlNsDecl()).append(prefixedXmlNs).append('=');
    		buffy.append('"').append(urn).append('"');  
    	}
    }
    
    String xmlnsURN()
    {
    	return childTagName;
    }
    
    String prefixedXmlNs()
    {
    	return prefixedXmlNs;
    }
    
    String xmlNsDecl()
    {
    	return xmlNsDecl;
    }
    /**
     * Append to the DOM a well-formed xmlns: attribute with namespace id and urn.
     * Or, if this is not an XMLNS_ATTRIBUTE, do nothing.
     * 
     * @return
     * @throws XMLTranslationException 
     * @throws IOException 
     */
    void xmlnsAttr(Element elementNode, Document dom) throws XMLTranslationException
    {
    	if (type == XMLNS_ATTRIBUTE)
    	{
    		String xmlnsURN				= xmlnsURN();
    		String prefixedNSDecl = prefixedXmlNs();

    		Attr attr 						= dom.createAttribute(prefixedNSDecl);
    		attr.setValue(xmlnsURN);
    		elementNode.setAttributeNode(attr);
    	}
    	else
    		throw new XMLTranslationException("Trying to output XMLNS_ATTRIBUTE, but FieldToXMLOptimizations.type = "+type);
    	//    		element.setAttribute(this.tagName, this.childTagName);
    }
    /**
     * Return a well-formed xmlns: attribute with namespace id and urn.
     * Or, if this is not an XMLNS_ATTRIBUTE, do nothing.
     * 
     * @return
     * @throws IOException 
     */
    void xmlnsAttr(Appendable appendable) throws IOException
    {
    	if (type == XMLNS_ATTRIBUTE)
    	{
    		appendable.append(xmlNsDecl());
//    		appendable.append(this.startOpenTag);
//    		appendable.append('=');
//    		appendable.append('"');
//    		appendable.append(this.childTagName);
//    		appendable.append('"');  
    	}
    }
    /**
     * Constructor for collection elements (no field).
     * @param optimizations TODO
     * @param collectionFieldToXMLOptimizations
     * @param actualCollectionElementClass
     */
    FieldToXMLOptimizations(ClassDescriptor optimizations, FieldToXMLOptimizations collectionFieldToXMLOptimizations, Class<? extends ElementState> actualCollectionElementClass)
    {
    	this.declaringClassDescriptor	=  optimizations;

    	//TODO -- is this inheritance good or bad?!
    	String tag	= collectionFieldToXMLOptimizations.childTagName;

    	if (tag == null)
    		// get the tag name from the class of the object in the Collection, if from nowhere else
    		tag		= XMLTools.getXmlTagName(actualCollectionElementClass, "State");

    	this.tag		= tag;
    	// no field here?!

    	//TODO -- do we need to handle scalars here as well?
    	this.type		= REGULAR_NESTED_ELEMENT;
    }
    
    /**
     * Usual constructor.
     * 
     * @param optimizations
     * @param field
     * @param nameSpacePrefix
     */
    FieldToXMLOptimizations(ClassDescriptor optimizations, Field field, String nameSpacePrefix)
    {
    	this.declaringClassDescriptor					= optimizations;

    	String tag = deriveTag(field);

    	setupXmlClasses(field);
    	//TODO XmlNs
//    	if (nameSpacePrefix != null)
//    	{
//    		tagName				= nameSpacePrefix + tagName;
//    	}
    	this.tag					= tag;
    	this.field				= field;
    	setType(field, field.getType());
    	boolean isLeaf		= (type == LEAF_NODE_VALUE);
    	if (isLeaf || (type == REGULAR_ATTRIBUTE))
    	{
    		scalarType		= TypeRegistry.getType(field);
    		if (isLeaf)
    		{
    			isCDATA			= XMLTools.leafIsCDATA(field);
    			needsEscaping	= scalarType.needsEscaping();
    		}
    		format			= XMLTools.getFormatAnnotation(field);
    	}

    	if (XMLTools.isNested(field))
    		setupXmlText(ClassDescriptor.getClassDescriptor((Class<ElementState>) field.getType()));
    }

		private void setupXmlClasses(Field field)
		{
			final ElementState.xml_class classAnnotationObj		= field.getAnnotation(ElementState.xml_class.class);
    	final Class classAnnotation			= (classAnnotationObj == null) ? null : classAnnotationObj.value();
    	final ElementState.xml_classes classesAnnotationObj		= field.getAnnotation(ElementState.xml_classes.class);
    	final Class[] classesAnnotation			= (classesAnnotationObj == null) ? null : classesAnnotationObj.value();
    	final ElementState.xml_scope scopeAnnotationObj		= field.getAnnotation(ElementState.xml_scope.class);
    	final String scopeAnnotation			= (scopeAnnotationObj == null) ? null : scopeAnnotationObj.value();
    	if ((classAnnotation != null) || (scopeAnnotation != null) ||
    			((classesAnnotation != null) && (classesAnnotation.length > 0)))
    	{
    		hasXmlClasses								= true;
    	}
		}

		private String deriveTag(Field field)
		{
			final ElementState.xml_collection collectionAnnotationObj	= field.getAnnotation(ElementState.xml_collection.class);
    	final String collectionAnnotation	= (collectionAnnotationObj == null) ? null : collectionAnnotationObj.value();
    	final ElementState.xml_map mapAnnotationObj	= field.getAnnotation(ElementState.xml_map.class);
    	final String mapAnnotation	= (mapAnnotationObj == null) ? null : mapAnnotationObj.value();
    	final ElementState.xml_tag tagAnnotationObj					= field.getAnnotation(ElementState.xml_tag.class);
    	final String tagAnnotation			= (tagAnnotationObj == null) ? null : tagAnnotationObj.value();
    	String tag;
    	if ((collectionAnnotation != null) && (collectionAnnotation.length() > 0))
    	{
    		tag											= collectionAnnotation;
    		hasCollectionOrMapTag	= true;
    	}
    	else if ((mapAnnotation != null) && (mapAnnotation.length() > 0))
    	{
    		tag											= mapAnnotation;
    		hasCollectionOrMapTag	= true;
    	}
    	else
    	{
    		tag											= XMLTools.getXmlTagName(field);
    	}
			return tag;
		}
    
    /**
     * If this Optimizations object represents a NameSpace, then return the prefix for it, in the current context.
     * 
     * @return
     */
    public String nameSpacePrefix()
    {
    	return this.nameSpacePrefix;
    }

    String toString;
    @Override public String toString()
    {
    	if (toString == null)
    		toString = "FieldToXMLOptimizations<" + tag + ">";
    	return toString;
    }
    
    private void setTag(String tagName)
    {
    	this.tag		= tagName;
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
//					println("FieldToXMLOptimizations: !!!collection elements are of type: " + collectionElementsType.getName());
					// is collectionElementsType a scalar or a nested element
					ElementState.xml_collection collectionAnnotation		= field.getAnnotation(ElementState.xml_collection.class);
					String	childTagName			= collectionAnnotation.value();
					if (ElementState.class.isAssignableFrom(collectionElementsType))
					{	// nested element
						result						= COLLECTION_ELEMENT;
						//TODO -- is this inheritance good, or should we wait for the actual class object?!
//						if (childTagName == null)
//							childTagName			= XmlTools.getXmlTagName(thatClass, "State");
//						println("FieldToXMLOptimizations: !!!collection elements childTagName = " + childTagName);
					}
					else
					{	// scalar
						result						= COLLECTION_SCALAR;
						this.scalarType				= TypeRegistry.getType(collectionElementsType);
						format						= XMLTools.getFormatAnnotation(field);
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

	public String tag()
	{
		return tag;
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
        	
        	if (scalarType == null)
        	{
        		weird("scalarType = null!");
        	}      	
        	else if (!scalarType.isDefaultValue(field, context))
        	{
	            //for this field, generate tags and attach name value pair
	        	
	        	//TODO if type.isFloatingPoint() -- deal with floatValuePrecision here!
        		// (which is an instance variable of this) !!!
	        	
	        	buffy.append(' ');
				buffy.append(this.tag);
	        	buffy.append('=');
	        	buffy.append('"');
	        	
	        	scalarType.appendValue(buffy, this, context);
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
        	
        	if (scalarType == null)
        		weird("YO! setAttribute() scalarType == null!!!");
        	else
        	if (!scalarType.isDefaultValue(field, context))
        	{
	            //for this field, generate tags and attach name value pair
	        	
	        	//TODO if type.isFloatingPoint() -- deal with floatValuePrecision here!
        		// (which is an instance variable of this) !!!
        		
        		String value		= scalarType.toString(field, context);
	        	
        		element.setAttribute(tag, value);
        	}
        }
    }
    
    /**
     * Use this and the context to set an attribute (name, value) on the Element DOM Node passed in.
     * 
     * @param element
     * @param instance
     * @param isAtXMLText TODO
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void appendLeaf(Element element, Object instance) 
    throws IllegalArgumentException, IllegalAccessException
    {
        if (instance != null)
        {
        	ScalarType scalarType	= this.scalarType;
        	
        	Document document 		= element.getOwnerDocument();
        	
        	Object fieldInstance 	= field.get(instance); 
        	if(fieldInstance != null)
        	{

        		String  fieldValueString= fieldInstance.toString();

        		Text textNode			= isCDATA ? document.createCDATASection(fieldValueString) : document.createTextNode(fieldValueString);
        		
        		Element leafNode		= document.createElement(tag);
        		leafNode.appendChild(textNode);

        		element.appendChild(leafNode);
        	}
         }
    }
    
    public void appendXmlText(Element element, Object instance) 
    throws IllegalArgumentException, IllegalAccessException
    {
        if (instance != null)
        {
//        	ScalarType scalarType	= this.scalarType;
        	ScalarType scalarType	= this.xmlTextScalarType;
        	Field field 			= this.xmlTextField;
        	Document document 		= element.getOwnerDocument();
        	
        	Object fieldInstance 	= field.get(instance); 
        	if (fieldInstance != null)
        	{

        		String  fieldValueString= fieldInstance.toString();

        		Text textNode			= isCDATA ? document.createCDATASection(fieldValueString) : document.createTextNode(fieldValueString);
        		
        		element.appendChild(textNode);
        	}
         }
    }
    
    public void appendCollectionLeaf(Element element, Object instance) 
    throws IllegalArgumentException, IllegalAccessException
    {
        if (instance != null)
        {
        	ScalarType scalarType	= this.scalarType;
        	
        	Document document 		= element.getOwnerDocument();
        	
        	String  instanceString	= instance.toString();

//            Object fieldInstance 	= field.get(instance);        	
//        	String  fieldValueString= fieldInstance.toString();

        	Text textNode			= isCDATA ? document.createCDATASection(instanceString) : document.createTextNode(instanceString);

        	Element leafNode		= document.createElement(tag);
        	leafNode.appendChild(textNode);

        	element.appendChild(leafNode);
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
				appendable.append(this.tag);
	        	appendable.append('=');
	        	appendable.append('"');
	        	
	        	scalarType.appendValue(appendable, this, context);
	        	appendable.append('"');
        	}
        }
    }
    static final String START_CDATA	= "<![CDATA[";
    static final String END_CDATA	= "]]>";
    
    /**
     * Use this and the context to append a leaf node with value to the StringBuilder passed in,
     * unless it turns out that the value is the default.
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
    			appendOpenTag(buffy);

    			if (isCDATA)
    				buffy.append(START_CDATA);
    			scalarType.appendValue(buffy, this, context); // escape if not CDATA! :-)
    			if (isCDATA)
    				buffy.append(END_CDATA);

    			appendCloseTag(buffy);
    		}
    	}
    }

    /**
     * Use this and the context to append a text node  value to the StringBuilder passed in,
     * unless it turns out that the value is the default.
     * 
     * @param buffy
     * @param context
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    void appendXmlText(StringBuilder buffy, Object context) 
    throws IllegalArgumentException, IllegalAccessException
    {
        if (context != null)
        {
        	ScalarType scalarType	= this.xmlTextScalarType;
        	Field field 			= this.xmlTextField;

        	if (!scalarType.isDefaultValue(field, context))
        	{
        		// for this field, generate <tag>value</tag>
        		
        		if (isCDATA)
        			buffy.append(START_CDATA);
// TODO        			lkkljhkhj
        		scalarType.appendValue(buffy, this, context); // escape if not CDATA! :-)
        		if (isCDATA)
        			buffy.append(END_CDATA);
        	}
        }
    }

    void appendOpenTag(StringBuilder buffy)
    {
    	buffy.append('<').append(tag).append('>');
    }
    void appendCloseTag(StringBuilder buffy)
    {
    	buffy.append('<').append('/').append(tag).append('>');
    }
    void appendOpenTag(Appendable buffy) throws IOException
    {
    	buffy.append('<').append(tag).append('>');
    }
    void appendCloseTag(Appendable buffy) throws IOException
    {
    	buffy.append('<').append('/').append(tag).append('>');
    }
    /**
     * Use this and the context to append a leaf node with value to the StringBuilder passed in.
     * Consideration of default values is not evaluated.
     * 
     * @param buffy
     * @param context
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    void appendCollectionLeaf(StringBuilder buffy, Object instance) 
    throws IllegalArgumentException, IllegalAccessException
    {
    	if (instance != null)
    	{
    		ScalarType scalarType	= this.scalarType;

    		appendOpenTag(buffy);
    		if (isCDATA)
    			buffy.append(START_CDATA);
    		scalarType.appendValue(instance, buffy, !isCDATA); // escape if not CDATA! :-)
    		if (isCDATA)
    			buffy.append(END_CDATA);

    		appendCloseTag(buffy);
    	}
    }

    /**
     * Use this and the context to append a leaf node with value to the Appendable passed in.
     * Consideration of default values is not evaluated.
     * 
     * @param appendable
     * @param context
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException 
     */
    void appendCollectionLeaf(Appendable appendable, Object instance) 
    throws IllegalArgumentException, IllegalAccessException, IOException
    {
    	if (instance != null)
    	{
    		ScalarType scalarType	= this.scalarType;

    		appendOpenTag(appendable);
    		if (isCDATA)
    			appendable.append(START_CDATA);
    		scalarType.appendValue(instance, appendable, !isCDATA); // escape if not CDATA! :-)
    		if (isCDATA)
    			appendable.append(END_CDATA);

    		appendCloseTag(appendable);
     	}
    }
   
    /**
     * Use this and the context to append a leaf node with value to the Appendable passed in.
     * 
     * @param buffy
     * @param context
     * @param isAtXMLText 
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
        		
           	appendOpenTag(appendable);
        		
        		if (isCDATA)
        			appendable.append(START_CDATA);
        		scalarType.appendValue(appendable, this, context); // escape if not CDATA! :-)
        		if (isCDATA)
        			appendable.append(END_CDATA);
        		
        		appendCloseTag(appendable);
        	}
        }
    }

    void appendXmlText(Appendable appendable, Object context) 
    throws IllegalArgumentException, IllegalAccessException, IOException
    {
    	if (context != null)
    	{
    		ScalarType scalarType	= this.xmlTextScalarType;
    		Field field				= this.xmlTextField;
    		if (!scalarType.isDefaultValue(field, context))
    		{
    			// for this field, generate <tag>value</tag>

    			if (isCDATA)
    				appendable.append(START_CDATA);

    			scalarType.appendValue(appendable, this, context); // escape if not CDATA! :-)
    			if (isCDATA)
    				appendable.append(END_CDATA);
    		}
    	}
    }
	Field field()
	{
		return field;
	}
	
	public static void main(String[] a)
	{
		DocumentBuilderFactory factory	= DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder docBuilder 	= factory.newDocumentBuilder();
			Document doc				= docBuilder.newDocument();
			
//			String nsURN				= "urn:users";
			String nsURN				= "http://rssnamespace.org/feedburner/ext/1.0";
//			Element root 				= doc.createElementNS("urn:users", "root");
			Element root 				= doc.createElementNS(nsURN, "rss");
			doc.appendChild(root);
			
			Attr attr 					= doc.createAttribute("xmlns:feedburner");
			attr.setValue(nsURN);
			root.setAttributeNode(attr);
			println("yo!");
		} catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}

	}
	
	public boolean hasXmlText()
	{
		return xmlTextField != null;
	}
	
	/**
	 * Get the class that this operates on.
	 * It is either the class of the Field, or the class of the Collection element
	 * represented by this.
	 * 
	 * @return
	 */
	public Class getOperativeClass()
	{
		return childClass != null ? childClass : field.getType();
	}

	/**
	 * Get the optimizations object for the parent of this field.
	 * 
	 * @return
	 */
	public ClassDescriptor getContextOptimizations()
	{
		return declaringClassDescriptor;
	}

	/**
	 * @return the field
	 */
	public Field getField()
	{
		if (xmlTextField == null)
			return field;
		else
			return xmlTextField;
	}

	/**
	 * @return the format
	 */
	public String[] getFormat()
	{
		return format;
	}

	/**
	 * For most fields in an ElementState, object, we generate XML with the tag name mapped to the
	 * field name. However for some -- collections, maps, and those that use @xml_classes --
	 * we must generate XML with tag name mapped to class name (w camel case conversion).
	 * 
	 * @return	true for collection & map fields, and those annotated with @xml_classes or @xml_scope.
	 */
	public boolean hasCollectionOrMapTag()
	{
		return hasCollectionOrMapTag;
	}

	/**
	 * @return the hasXMLClasses
	 */
	public boolean hasXMLClasses()
	{
		return hasXmlClasses;
	}
}