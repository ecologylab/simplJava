package ecologylab.xml;

import java.lang.reflect.*;
import java.util.*;
import java.io.*;
import java.net.*;

import javax.xml.parsers.*;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.w3c.dom.Attr; 
import org.xml.sax.*;

/**
 * This class is the heart of the translation framework. All classes which 
 * need to be translated to xml and back MUST extend this class. It has the actual
 * methods which emit the xml and build the java class back from xml.    
 *  
 * @author      Andruid Kerne
 * @author      Madhur Khandelwal
 * @version     0.5
 */
abstract public class ElementState extends IO
{
	/**
	 * xml header
	 */
	protected String xml = "<?xml version=" + "\"1.0\"" + " encoding=" + "\"US-ASCII\"" + "?>";
	
	/**
	 * whether the generated XML should be in compressed form or not
	 */
	protected static boolean compressed = false;
	int nodeNumber = 1;
	
	/**
	 * package name of the class
	 */ 
	protected String	packageName;
	
	/**
	 * controls if the public fields of the parent class will be emitted or not. 
	 */
	protected boolean emitParentFields = false;
	
	public String tag()
	{
		String tagName	= XmlTools.elementNameFromObject(this, "State", compressed);
		return tagName;
	}

	public String startOpenTag()
	{	
		String openTagName	= "<" + this.tag();
		return openTagName;
	}
	public String closeTag()
	{
		String closeTagName = "</" + this.tag() + ">";
		return closeTagName;
	}

	/**
	 * Emits xml from a given state object. Recursively goes through the object until
	 * it finds the primitive types. As a result it creates a hierarchichal xml structure.
	 * @param compression	if the emitted xml needs to be compressed
	 * @return 				emitted xml string
	 */
	public String emitXml(boolean compression) throws XmlTranslationException
	{
		//nodeNumber is just to indicate which node number(#1 is the root node of the DOM)
		//is being processed. compression attr is emitted only for node number 1
		return emitXml(compression, true, nodeNumber);
	}
	
	public String emitXml(boolean compression, boolean doRecursiveDescent) throws XmlTranslationException
	{
		return emitXml(compression, doRecursiveDescent, nodeNumber);
	}
	
	/**
	 * emits xml from a given state object
	 */
	String emitXml(boolean compression, boolean doRecursiveDescent, int nodeNumber) throws XmlTranslationException
	{
		compressed = compression;
		String result	= "";
		result += startOpenTag();
		
		//emit compresseion = true only for the top node, so this dirty hack
		//so if the nodeNumber is 1 (top node) then emit the compression attribute
		if(nodeNumber == 1 && compressed)
		{
			String compressionAttr = " " + "compressed" + " = " + "\"" + compressed + "\"" + " ";
			result += compressionAttr;
		}
		nodeNumber++;
		
		try
		{
			Field[] fields	= getClass().getFields();
			//arrange the fields such that all primitive types occur before the reference types
			arrangeFields(fields);
			boolean	hasRecursiveElements	= false;
			
			for (int i=0; i<fields.length; i++)
			{
				// iterate through fields
				Field thatField		= fields[i];
				Class thatFieldType	= thatField.getType();
				
				if (isExtendedPrimitive(thatFieldType)){
				   	
				   	//controls comes here if a primitive type is found AFTER a reference type
				   	//which is not allowed in our framework
					if(thatField.getDeclaringClass().getName() == getClass().getName())
				   		if(hasRecursiveElements)
				   			throw new XmlTranslationException("Primitive type " + thatFieldType.getName() + 
				   					" found after Reference type " + fields[i-1].getType().getName());
					
					//emit only if the field is present in this classs
					//parent class fields should not be emitted, coz thats confusing
				    if(!emitParentFields)
				    {
						if(thatField.getDeclaringClass().getName() == getClass().getName())		
							result	+= XmlTools.generateNameVal(thatField, this);
				    }
				    else
				    {
				    	result	+= XmlTools.generateNameVal(thatField, this);
				    }
				}
								
				else if (doRecursiveDescent)	// recursive descent
				{	
					boolean isCollection = false;
					
					if (!hasRecursiveElements)
					{	// found *first* recursive element
						result	+= ">";	// close element tag behind attributes
						hasRecursiveElements	= true;
					}

					Object thatReferenceObject = null;
					try
					{
						thatReferenceObject	= thatField.get(this);
						if (thatReferenceObject ==null)
						    continue;
					}
					catch(IllegalAccessException e){
						e.printStackTrace();
					}
					
					//if these conditions are true, the object is a Collection
					if (isCollection(thatFieldType))
					{
					
						//if the object is a collection, we handle it in a different way
						//basically iterate thru the collection and emit Xml from each element
						Collection elements = getCollection();
						if(elements == null)
							throw new XmlTranslationException("Class containing collections such as " +								"Vector and Hashtable MUST define a method called " +								"getCollection() which returns a type Collection");

						Iterator elementIterator = elements.iterator();
					
						while( elementIterator.hasNext() )
						{
							ElementState element;
							try{
								element = (ElementState)elementIterator.next();
							}catch(ClassCastException e)
							{
								throw new XmlTranslationException("Collections MUST " +									"contain objects of class derived from \"ElementState\" ");
							}
							
							result += element.emitXml(compressed, true, nodeNumber);		
						}
						
						isCollection = true;
				
					}//end of if getName == Vector || Hashtable
				
					if(!isCollection){	
						if (thatReferenceObject instanceof ElementState)
						{
							ElementState thatElementState	= (ElementState) thatReferenceObject;
							//if the field has already been emitted in the parent object, no need of emitting here
							//so we emit ONLY IF the field is in the *same* object, NOT the parent object
							if(thatField.getDeclaringClass().getName() == getClass().getName())
							{
								result += thatElementState.emitXml(compressed, true, nodeNumber);			
							}
						}
					}//end of isCollection
				}//end of else
			}//end of for loop
			
			if (!doRecursiveDescent)
				result += ">"; // dont close it
			else if (hasRecursiveElements)
				result	+= closeTag();
			else
				result	+= "/>";	// simple element w attrs but no embedded elements
				
		} catch (SecurityException e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Arranges fields so that all primitive types occur first and then the reference types.
	 * @param fields
	 */
	void arrangeFields(Field[] fields)
	{
		int primitivePos = 0;
		Vector refTypes = new Vector();
		
		for(int i = 0; i < fields.length; i++)
		{
			if(isExtendedPrimitive(fields[i].getType()))
			{
				if(i > primitivePos)
				{
					fields[primitivePos] = fields[i];
				}
				primitivePos++;
			}
			else
			{
				refTypes.add(fields[i]);
			}
		}
		
		//copy the ref types at the end of the primitive types
		int j = 0;
		for(int i = fields.length - refTypes.size(); i < fields.length; i++)
		{
			fields[i]	=	(Field)refTypes.elementAt(j++);			
		}
	}
   public static final HashMap buildHashMapFromStrings(String[] strings)
   {
      HashMap hashMap	= new HashMap(strings.length);
      buildMapFromStrings(hashMap, strings);
      return hashMap;
   }
   public static final void buildMapFromStrings(Map map, String[] strings)
   {
      for (int i=0; i<strings.length; i++)
      {
	 String thatString	= strings[i];
	 map.put(thatString, thatString);
      }
   }
	
	static final String[] collectionTypeStrings = 
	{
	   "java.util.Vector", "java.util.Hashtable",
	   "java.util.HashMap, java.util.ArrayList", 
	   "LinkedHashMap", "LinkedHashSet",
	};
	static final HashMap collectionTypes = 
	   buildHashMapFromStrings(collectionTypeStrings);
	/**
	 * determines if a field is of type collection
	 * @param thatFieldType
	 * @return
	 */
	protected boolean isCollection(Class thatFieldType)
	{
	   String thatFieldTypeName = thatFieldType.getName();
	   return (thatFieldTypeName != null) &&
	      collectionTypes.containsKey(thatFieldTypeName);
	}
	
	/**
	 * determines if the field is to considered as a primitive type.
	 * i.e. if you want to emit this field as an XML attribute, rather than an element
	 * @param thatFieldType
	 * @return
	 */
	protected boolean isExtendedPrimitive(Class thatFieldType)
	{
		return thatFieldType.isPrimitive() || 
		   thatFieldType.getName() == "java.lang.String" ||
		   thatFieldType.getName() == "java.net.URL" ||
		   thatFieldType.getName() == "java.awt.Color";
	}
	
/**
 * 	Emits xml using the recursive descent algorithm, and saves it to a file.
 * 	@param filePath		the file in which the xml needs to be saved
 * 	@param prettyXml	whether the xml should be written in an indented fashion
 *  @param compression	whether the xml should be compressed while being emitted
 */	
	public void saveXmlFile(String filePath, boolean prettyXml, boolean compression) throws XmlTranslationException
	{
		//initialize the xml string 
		xml = "<?xml version=" + "\"1.0\"" + " encoding=" + "\"US-ASCII\"" + "?>";

		//generate the xml of the taken snap		
		xml += emitXml(compression);
		
		//write the Xml in the file		
		try
		{
			String xmlFileName = filePath;
			if(!filePath.endsWith(".xml") && !filePath.endsWith(".XML"))
			{
				xmlFileName = filePath + ".xml";
			}
			else
			{
				filePath	=	filePath.substring(0,filePath.indexOf(".xml"));
			}
		 if(prettyXml)
		 {
		    XmlTools.writePrettyXml(xml, new StreamResult(new
								  File(xmlFileName)));
		 }
		 
			else
			{
				FileOutputStream out = new FileOutputStream(new File(xmlFileName));
				PrintStream p = new PrintStream(out);
				p.println(xml);
				p.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
	
/**
 * Emits xml, and appends it to a file. This method is used when xml should be emitted
 * and written to a file incrementally. In other words, it does not wait for the complete
 * xml to be emitted before saving it to a file. This is useful in cases such as logging.
 * @see  <code>saveXmlFile</code>
 * 	@param filePath		the file in which the xml needs to be saved
 * 	@param prettyXml	whether the xml should be written in an indented fashion
 *  @param compression	whether the xml should be compressed while being emitted
 */	
	public void appendXmlFile(String filePath, boolean prettyXml, boolean compression)
	{
		//write the Xml in the file		
		try
		{
			String xmlFileName = filePath;
			if(!filePath.endsWith(".xml") && !filePath.endsWith(".XML"))
			{
				xmlFileName = filePath + ".xml";
			}
			else
			{
				filePath	=	filePath.substring(0,filePath.indexOf(".xml"));
			}
			if(prettyXml)
				XmlTools.writePrettyXml(xml, new StreamResult(new File(xmlFileName)));
			else
			{
				BufferedWriter writer = IO.openWriter(xmlFileName, true);
				File temp = new File(xmlFileName);
				if(!temp.exists())
				IO.writeLine(writer,xml);								
				IO.writeLine(writer,emitXml(compression));
				IO.closeWriter(writer);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected Document buildDOM(URL fileURL)
	{
		return buildDOM(fileURL.toString());
	}
	protected Document buildDOM(File file)
	{
		return buildDOM(file.toString());
	}
	
	/**
	 * This method creates a DOM from a given file.
	 * @param fileName	the file from which the DOM is to be created
	 * @return			the Document object
	 */
	protected Document buildDOM(String fileName)
	{		       
		Document document	= null;
		try
		{
    	  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	  DocumentBuilder builder = factory.newDocumentBuilder();
    	  createErrorHandler(builder);
    	  
  		  document = builder.parse(fileName);
		} 
		
		catch (SAXParseException spe) {
			// Error generated by the parser
		    debug("\n** Parsing error" + ", line " + spe.getLineNumber() + ", uri " + spe.getSystemId());
		    println("   " + spe.getMessage());
		  
		    // Use the contained exception, if any
		    Exception  x = spe;
		    if (spe.getException() != null)
		   	   x = spe.getException();
		    x.printStackTrace();
	  	}
	  	
	  	catch (SAXException sxe) {
		    // Error generated during parsing
		    Exception  x = sxe;
		    if (sxe.getException() != null)
		      x = sxe.getException();
		    x.printStackTrace();
	   	}
	   	
	   	catch (ParserConfigurationException pce) {
		    // Parser with specified options can't be built
		    pce.printStackTrace();
	   	}
	   	
	   	catch (IOException ioe) {
		    // I/O error
		    ioe.printStackTrace();
	  	}
	  	
	  	catch(FactoryConfigurationError fce){
	  		fce.printStackTrace();
	  	}
	  	catch(Exception e){
	  		e.printStackTrace();
	  	}
		return document;
	}

  	private void createErrorHandler(DocumentBuilder builder){
  		
  		builder.setErrorHandler(
	  	new org.xml.sax.ErrorHandler() {
	    	// ignore fatal errors (an exception is guaranteed)
		    public void fatalError(SAXParseException exception)
		    throws SAXException {
		    }
		    // treat validation errors as fatal
		    public void error(SAXParseException e)
		    throws SAXParseException
		    {
		      throw e;
		    }
		
		     // dump warnings too
		    public void warning(SAXParseException err)
		    throws SAXParseException
		    {
		      debug("** Warning"
		        + ", line " + err.getLineNumber()
		        + ", uri " + err.getSystemId());
		      println("   " + err.getMessage());
		    }
	    
	  	}  
		); 
  	}

	/**
	 * Builds a state object from a given XML file. This is essentially creating Java
	 * class from xml. The programmer needs can pass the name of the xml file 
	 * to this method to create the corresponding Java hierarchy.
	 * @param fileName	the file from which the Java hierarchy is to be created
	 * @return 			the parent class of the Java hierarchy 
	 */

	public ElementState buildStateObject(String fileName) throws XmlTranslationException
	{
		Document document	= buildDOM(fileName);
		return buildStateObject(document);
	}
	
	/**
	 * Builds a state object from a given DOM. This is essentially creating Java
	 * class from xml. The programmer needs to create a DOM of the xml file and pass it 
	 * to this method to create a Java hierarchy similar to the DOM.
	 * @param doc	the DOM from which the Java hierarchy is to be created
	 * @return 		the parent class of the Java hierarchy 
	 */
	public  ElementState buildStateObject(Document doc) throws XmlTranslationException
	{
        
		Node parentNode = (Node)doc.getDocumentElement();
		ElementState elementState = buildStateObject(parentNode);
		
		return elementState;
	}
	
	/**
	 * Called by the ElementState <code>buildStateObject(Document doc)</code>.
	 * Recursively goes thru the nodes in DFS order and builds state-objects
	 * @param parentNode	the root node of the DOM from which the Java hierarchy is to be created
	 * @return 				the parent class of the Java hierarchy 

	 */
	public  ElementState buildStateObject(Node parentNode) throws XmlTranslationException
	{
		Class stateClass = null;
		ElementState elementState = null;
		try
		{									
//    		String packageName = XmlTools.getPackageName(this) + ".";
                		
			stateClass = XmlTools.getStateClass(parentNode.getNodeName());
			
			elementState	=	(ElementState)stateClass.newInstance();
		}
		catch(Exception e)
		{
			if(e instanceof InstantiationException)
				throw new XmlTranslationException("All the classes that are translated MUST " +					"contain an empty constructor");
			e.printStackTrace();
		}

		
		//take the attribute values and fill them up in the object
		if( parentNode.hasAttributes() )
		{
			
			NamedNodeMap attributes = parentNode.getAttributes();
			
			for (int i = 0; i < attributes.getLength(); i++) 
			{
          	   Node attr = attributes.item(i);
               
               if(attr.getNodeValue() != null)
               {
	              
	              //create the method name from the tag name
	              //for example, for the attr bias, methodName = setBias
	              String methodName = XmlTools.methodNameFromTagName(attr.getNodeName());
	              
	              //search for the method with the name created above 
	              //for this u have to create an array of class indicating the parameters to the method
	              //in our case, all the methods have a single parameter, String
	              //which holds the value of the attribute and then that object is responsible
	              //for converting it to appropriate type from the string
	              Class[] parameters = new Class[1];
	              Method attrMethod = null;
	              
	              try
	              {
		              parameters[0] = Class.forName("java.lang.String");		              
		              attrMethod = stateClass.getMethod(methodName,parameters);
	              }
	              catch(Exception e)
	              {
	              	  if(e instanceof NoSuchMethodException)
	              	  	throw new
				   XmlTranslationException("setter method not found for variable " + attr.getNodeName() + " in "+stateClass+
", please create a method that takes a String as parameter and sets the value of " + attr.getNodeName());
						e.printStackTrace();
	              }
	              
	              //if the method is found
	              //invoke the method
	              //fill the String value with the value of the attr node
	              //args is the array of objects containing the arguments to the method to be invoked
	              //in our case, methods have only one arg which is the String, "value"
	              String value = attr.getNodeValue();
	              Object[] args = new Object[1];
	              args[0] = value;
	              
	              try
	              {
	              		attrMethod.invoke(elementState,args);
	              }
	              catch(Exception e)
	              {
	              		e.printStackTrace();
	              }	              
	              //done, if the control reaches here, the value has been filled	              	              
               }//end of if
          	}//end of for loop
			
		}//end of if hasAttributes

		// loop through nested elements, build them, and add them to ourself
		NodeList childElements = parentNode.getChildNodes();
		for(int i = 0; i < childElements.getLength(); i++)
		{
			Node thisChild = childElements.item(i);
			
			if (thisChild.getNodeType() != Node.TEXT_NODE)
				elementState.addElement(buildStateObject(thisChild));
//			else
//			   parentStateObj.setAttribute(this.className,
//						       thisChild.getValue());
		}
				
		return elementState;
		
	}//end of buildStateObject
	
	/**
	 * This method MUST be overridden by all the state-objects which have 
	 * collections (e.g Vector, Hashtable etc.) of other state-objects inside of them. 
	 * It is used to add composite elements inside the state-object. It is called when the 
	 * Java classes are being built from the xml to add a state-object to the collection.  
	 * @param elementState	the nested state-object to be added
	 */
	protected void addElement(ElementState elementState) throws XmlTranslationException
	{
		String fieldName = XmlTools.fieldNameFromObject(elementState);
//		IO.println("<<<<<<<<<<<<<<<<<<<<<<<<fieldName is: " + fieldName);		
		try
		{
			Field field = getClass().getField(fieldName);
			field.set(this,elementState);
		}
		catch(Exception e)
		{
			if(e instanceof NoSuchFieldException)
				throw new XmlTranslationException("Class containing collections such as " +
					"Vector and Hashtable MUST define a method called " +
					"addElement() which adds a given object to the collection");
			e.printStackTrace();
		}
	}
	
/**
 * Set the field of name in <code>this</code> to the value of the nested state-object.
 * @param fieldName 	the name of the field to be set
 * @param nestedObj		the state-object which needs to be added to the parent object 
 */
	protected void setField(String fieldName, Object nestedObj)
	{
		try
		{
			Field field = getClass().getField(fieldName);
			setField(field, nestedObj);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	   
	}
/**
 * Set the field represented by @param field in <code>this</code> to the
 * value of @param nestedObj. 
 */
	void setField(Field field, Object nestedObj)
	{
		try
		{
			field.set(this, nestedObj); 
		} catch (IllegalAccessException e)
		{
			Class fieldType	= field.getType();
			println("IllegalAccessException setting field " +
					fieldType.getName() + " in " +
					nestedObj);
		}					
	}
	
	/**
	 * This method MUST be overridden by all the state-objects which have 
	 * collections (e.g Vector, Hashtable etc.) of other state-objects inside of them. 
	 * It is called by the xml emitter to iterate thru the collection
	 * and emit xml for each of the object in the collection
	 */	
	protected Collection getCollection()
	{
		return null;
	}
	
	protected void fillAttributeValues(Vector attributeVector)
	{
	   int n = attributeVector.size();
	   for (int i=0; i<n; i++)
	      fillValues((Attr) attributeVector.elementAt(i));
	}
	
	void fillValues(Vector vector)
	{
	   int n = vector.size();
	   for (int i=0; i<n; i++)
	      fillValues((Attr) vector.elementAt(i));
	}
	
	protected void fillValues(Attr attr)
	{	  
	  setField(attr.getName(), attr.getValue());
	}
	
	void fillValues(String fieldName, String value)
	{	   
	   setField(fieldName, value);
	}
	
	protected void createAndCollectElements(Vector attrs,
					     String className)
	{
	   try
	   {
	      createAndCollectElements(attrs, Class.forName(className));
	   } catch (ClassNotFoundException e)
	   {
	      e.printStackTrace();
	   }
	}
	
	void createAndCollectElements(Vector attrs, Class elementType)
	{
	   for(int i=0; i<attrs.size(); i++)
	   {
	      Attr thatAttr	= (Attr) attrs.elementAt(i);
	      try
	      {
			 ElementState newElement = 
			    (ElementState) elementType.newInstance();
			 newElement.fillValues(thatAttr); 
			 add(newElement); 
	      } catch (Exception e)
		  {
				e.printStackTrace();
		  }
	   }    	
	}
	
	void createAndCollectElements(String fieldName,
					     Vector values,
					     String className)
	{
	   try
	   {
	      createAndCollectElements(fieldName, values,
				       Class.forName(className));
	   } catch (ClassNotFoundException e)
	   {
	      e.printStackTrace();
	   }
	}
	
	void createAndCollectElements(String fieldName,
					     Vector values,
					     Class elementType)
	{
	   for(int i=0; i<values.size(); i++)
	   {
	      String thatValue	= (String) values.elementAt(i);
	      try
	      {	      	
			 ElementState newElement = 
			    (ElementState) elementType.newInstance();
			 newElement.fillValues(fieldName, thatValue); 
			 add(newElement); 
	      } catch (Exception e)
		  {
			   e.printStackTrace();
		  }
	   }    	
	}
 
	protected void add(ElementState elementToAdd)
	{
	}
	
	public void setCompressed(String value)
	{
		if(value.equals("true"))
			compressed	=	true;
	}
}
