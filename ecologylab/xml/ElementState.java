package ecologylab.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.xml.sax.Attributes;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;

/**
 * This class is the heart of the <code>ecologylab.xml</code> translation framework.
 * 
 * <p/>
 * To use the framework, the programmer must define a tree of objects derived from this class. The
 * public fields in each of these derived objects correspond to the XML DOM. The declarations of
 * attribute fields must preceed thos for nested XML elements. Attributes are built directly from
 * Strings, using classes derived from
 * 
 * @link ecologylab.types.Type ecologylab.types.Type}.
 * 
 *       <p/>
 *       The framework proceeds automatically through the application of rules. In the standard
 *       case, the rules are based on the automatic mapping of XML element names (aka tags), to
 *       ElementState class names. An mechanism for supplying additional translations may also be
 *       provided.
 * 
 *       <p/>
 *       <code>ElementState</code> is based on 2 methods, each of which employs Java reflection and
 *       recursive descent.
 * 
 *       <li><code>translateToXML(...)</code> translates a tree of these <code>ElementState</code>
 *       objects into XML.</li>
 * 
 *       <li><code>translateFromXML(...)</code> translates an XML DOM into a tree of these
 *       <code>ElementState</code> objects</li>
 * 
 * @author Andruid Kerne
 * @author Madhur Khandelwal
 * 
 * @version 2.9
 */
public class ElementState extends Debug implements FieldTypes, XMLTranslationExceptionTypes
{
	/**
	 * Link for a DOM tree.
	 */
	transient ElementState									parent;

	/**
	 * Enables storage of a single text node child. This facility is meager and rarely used, since the
	 * leaf nodes facility does the same thing but better.
	 * <p/>
	 * We might want to implement the ability to store multiple text nodes here some time in the
	 * future.
	 */
	/**
	 * The following 'transient' markers are necessary to keep terracotta from sharing these fields.
	 * TODO Find a better way to do this!
	 */
	transient private StringBuilder					textNodeBuffy;

	/**
	 * Just-in time look-up tables to make translation be efficient. Allocated on a per class basis.
	 */
	transient private ClassDescriptor				classDescriptor;

	/**
	 * Use for resolving getElementById()
	 */
	transient HashMap<String, ElementState>	elementByIdMap;

	transient HashMap<String, ElementState>	nestedNameSpaces;

	public static final int									UTF16_LE	= 0;

	public static final int									UTF16			= 1;

	public static final int									UTF8			= 2;

	/**
	 * xml header
	 */
	static protected final String		XML_FILE_HEADER						= "<?xml version=" + "\"1.0\""
																																+ " encoding=" + "\"UTF-8\""
																																+ "?>\n";

	// static protected final String XML_FILE_HEADER = "<?xml version=" + "\"1.0\"" + " encoding=" +
	// "\"US-ASCII\"" + "?>";

	static protected final int			ESTIMATE_CHARS_PER_FIELD	= 80;

	static final int								TOP_LEVEL_NODE						= 1;

	/**
	 * Used for argument marshalling with reflection to access a set method that takes a String as an
	 * argument.
	 */
	protected static Class<?>[]			MARSHALLING_PARAMS				=
																														{ String.class };

	/**
	 * Construct. Create a link to a root optimizations object.
	 */
	public ElementState()
	{
	}

	/**
	 * Translates a tree of ElementState objects into an equivalent XML string.
	 * 
	 * Uses Java reflection to iterate through the public fields of the object. When primitive types
	 * are found, they are translated into attributes. When objects derived from ElementState are
	 * found, they are recursively translated into nested elements.
	 * <p/>
	 * Note: in the declaration of <code>this</code>, all nested elements must be after all
	 * attributes.
	 * <p/>
	 * The result is a hierarchichal XML structure.
	 * <p/>
	 * Note: to keep XML files from growing unduly large, there is a default value for each type.
	 * Attributes which are set to the default value (for that type), are not emitted.
	 * 
	 * @return the generated xml string, in a Reusable SBtringBuilder
	 * 
	 * @throws XMLTranslationException
	 *           if there is a problem with the structure. Specifically, in each ElementState object,
	 *           fields for attributes must be declared before all fields for nested elements (those
	 *           derived from ElementState). If there is any public field which is not derived from
	 *           ElementState declared after the declaration for 1 or more ElementState instance
	 *           variables, this exception will be thrown.
	 */
	public StringBuilder serialize() throws XMLTranslationException
	{
		return serialize((StringBuilder) null);
	}

	/**
	 * Allocated a StringBuilder for translateToXML(), based on a rough guess of how many fields there
	 * are to translate.
	 * 
	 * @return
	 */
	private StringBuilder allocStringBuilder()
	{
		return new StringBuilder(classDescriptor().numFields() * ESTIMATE_CHARS_PER_FIELD);
	}

	/**
	 * Translates a tree of ElementState objects into equivalent XML in a StringBuilder.
	 * 
	 * Uses Java reflection to iterate through the public fields of the object. When primitive types
	 * are found, they are translated into attributes. When objects derived from ElementState are
	 * found, they are recursively translated into nested elements -- if doRecursiveDescent is true).
	 * <p/>
	 * Note: in the declaration of <code>this</code>, all nested elements must be after all
	 * attributes.
	 * <p/>
	 * The result is a hierarchichal XML structure.
	 * <p/>
	 * Note: to keep XML files from growing unduly large, there is a default value for each type.
	 * Attributes which are set to the default value (for that type), are not emitted.
	 * 
	 * @param buffy
	 *          StringBuilder to translate into, or null if you want one created for you.
	 * 
	 * @return the generated xml string
	 * 
	 * @throws XMLTranslationException
	 *           if a problem arises during translation. Problems with Field access are possible, but
	 *           very unlikely.
	 */
	public StringBuilder serialize(StringBuilder buffy) throws XMLTranslationException
	{
		if (buffy == null)
			buffy = allocStringBuilder();

		serializeToBuilder(classDescriptor().pseudoFieldDescriptor(), buffy);

		return buffy;
	}

	/**
	 * Translates a tree of ElementState objects, and writes the output to the File passed in.
	 * <p/>
	 * Uses Java reflection to iterate through the public fields of the object. When primitive types
	 * are found, they are translated into attributes. When objects derived from ElementState are
	 * found, they are recursively translated into nested elements.
	 * <p/>
	 * The result is a hierarchichal XML structure.
	 * <p/>
	 * Note: to keep XML files from growing unduly large, there is a default value for each type.
	 * Attributes which are set to the default value (for that type), are not emitted.
	 * <p/>
	 * Makes directories if necessary.
	 * 
	 * @param outputFile
	 *          File to write the XML to.
	 * 
	 * @throws XMLTranslationException
	 *           if a problem arises during translation. Problems with Field access are possible, but
	 *           very unlikely.
	 * @throws IOException
	 *           If there are problems with the file.
	 */
	public void serialize(File outputFile) throws XMLTranslationException, IOException
	{
		createParentDirs(outputFile);

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
		serialize(bufferedWriter);
		bufferedWriter.close();
	}

	/**
	 * Assumes that outputFile should be a file, whose parent directories may not exist. Creates the
	 * parent directories for the given File, and throws an XMLTranslationException if outputFile is a
	 * directory and not a file.
	 * 
	 * @param outputFile
	 * @throws XMLTranslationException
	 */
	public static void createParentDirs(File outputFile) throws XMLTranslationException
	{
		if (outputFile.isDirectory())
			throw new XMLTranslationException(
					"Output path is already a directory, so it can't be a file: "
							+ outputFile.getAbsolutePath());

		String outputDirName = outputFile.getParent();
		if (outputDirName != null) // if no parent dir exist, don't make dirs.
		{
			File outputDir = new File(outputDirName);
			outputDir.mkdirs();
		}

	}

	/**
	 * Translates a tree of ElementState objects, and writes the output to the Appendable passed in.
	 * <p/>
	 * Uses Java reflection to iterate through the public fields of the object. When primitive types
	 * are found, they are translated into attributes. When objects derived from ElementState are
	 * found, they are recursively translated into nested elements.
	 * <p/>
	 * The result is a hierarchichal XML structure.
	 * <p/>
	 * Note: to keep XML files from growing unduly large, there is a default value for each type.
	 * Attributes which are set to the default value (for that type), are not emitted.
	 * 
	 * @param appendable
	 *          Appendable to translate into. Must be non-null. Can be a Writer, OutputStream, ...
	 * 
	 * @throws XMLTranslationException
	 *           if a problem arises during translation. The most likely cause is an IOException.
	 * 
	 *           <p/>
	 *           Problems with Field access are possible, but very unlikely.
	 */
	public void serialize(Appendable appendable) throws XMLTranslationException
	{
		if (appendable == null)
			throw new XMLTranslationException("Appendable is null");

		try
		{
			serializeToAppendable(classDescriptor().pseudoFieldDescriptor(), appendable);
		}
		catch (IOException e)
		{
			throw new XMLTranslationException("IO", e);
		}
	}

	/**
	 * Translates a tree of ElementState objects into an equivalent XML string.
	 * 
	 * Uses Java reflection to iterate through the public fields of the object. When primitive types
	 * are found, they are translated into attributes. When objects derived from ElementState are
	 * found, they are recursively translated into nested elements -- if doRecursiveDescent is true).
	 * <p/>
	 * Note: in the declaration of <code>this</code>, all nested elements must be after all
	 * attributes.
	 * <p/>
	 * The result is a hierarchichal XML structure.
	 * <p/>
	 * Note: to keep XML files from growing unduly large, there is a default value for each type.
	 * Attributes which are set to the default value (for that type), are not emitted.
	 * 
	 * @return the generated xml string
	 * 
	 * @throws XMLTranslationException
	 *           if a problem arises during translation. Problems with Field access are possible, but
	 *           very unlikely.
	 */
	private void serializeToBuilder(FieldDescriptor fieldDescriptor, StringBuilder buffy)
			throws XMLTranslationException
	{
		this.preTranslationProcessingHook();

		fieldDescriptor.writeElementStart(buffy);

		// TODO -- namespace support
		// ArrayList<FieldToXMLOptimizations> xmlnsF2XOs =
		// classDescriptor().xmlnsAttributeOptimizations();
		// int numXmlnsAttributes = (xmlnsF2XOs == null) ? 0 : xmlnsF2XOs.size();
		// if (numXmlnsAttributes > 0)
		// {
		// for (int i=0; i<numXmlnsAttributes; i++)
		// {
		// FieldToXMLOptimizations xmlnsF2Xo = xmlnsF2XOs.get(i);
		// xmlnsF2Xo.xmlnsAttr(buffy);
		// }
		// }

		ArrayList<FieldDescriptor> attributeFieldDescriptors = classDescriptor()
				.attributeFieldDescriptors();
		int numAttributes = attributeFieldDescriptors.size();

		if (numAttributes > 0)
		{
			try
			{
				for (int i = 0; i < numAttributes; i++)
				{
					// iterate through fields
					FieldDescriptor childFD = attributeFieldDescriptors.get(i);
					childFD.appendValueAsAttribute(buffy, this);
				}
			}
			catch (Exception e)
			{
				// IllegalArgumentException, IllegalAccessException
				throw new XMLTranslationException("TranslateToXML for attribute " + this, e);
			}
		}

		ArrayList<FieldDescriptor> elementFieldDescriptors = classDescriptor()
				.elementFieldDescriptors();
		int numElements = elementFieldDescriptors.size();

		StringBuilder textNode = this.textNodeBuffy;

		boolean hasXmlText = fieldDescriptor.hasXmlText();
		if ((numElements == 0) && ((textNode == null) || (textNode.length() == 0)) && !hasXmlText)
		{
			buffy.append('/').append('>'); // done! completely close element behind attributes
		}
		else
		{
			if (!fieldDescriptor.isXmlNsDecl())
				buffy.append('>'); // close open tag behind attributes

			// FIXME -- drop old style text node processing
			if (textNode != null)
			{
				XMLTools.escapeXML(buffy, textNode);
			}

			if (hasXmlText)
			{
				try
				{
					fieldDescriptor.appendXmlText(buffy, this);
				}
				catch (Exception e)
				{
					throw new XMLTranslationException("TranslateToXML for @xml_field " + this, e);
				}
			}

			for (int i = 0; i < numElements; i++)
			{
				FieldDescriptor childFD = elementFieldDescriptors.get(i);
				final int childFdType 	= childFD.getType();
				if (childFD.getType() == SCALAR)
				{
					try
					{
						childFD.appendLeaf(buffy, this);
					}
					catch (Exception e)
					{
						throw new XMLTranslationException("TranslateToXML for leaf node " + this, e);
					}
				}
				else
				{
					Object thatReferenceObject = null;
					Field childField = childFD.getField();
					try
					{
						thatReferenceObject = childField.get(this);
					}
					catch (IllegalAccessException e)
					{
						debugA("WARNING re-trying access! " + e.getStackTrace()[0]);
						childField.setAccessible(true);
						try
						{
							thatReferenceObject = childField.get(this);
						}
						catch (IllegalAccessException e1)
						{
							error("Can't access " + childField.getName());
							e1.printStackTrace();
						}
					}
					// ignore null reference objects
					if (thatReferenceObject == null)
						continue;

					final boolean isScalar = (childFdType == COLLECTION_SCALAR)
							|| (childFdType == MAP_SCALAR);
					// gets Collection object directly or through Map.values()
					Collection thatCollection;
					switch (childFdType)
					{
					case COLLECTION_ELEMENT:
					case COLLECTION_SCALAR:
					case MAP_ELEMENT:
					case MAP_SCALAR:
						thatCollection = XMLTools.getCollection(thatReferenceObject);
						break;
					default:
						thatCollection = null;
						break;
					}

					if (thatCollection != null && (thatCollection.size() > 0))
					{
						// if the object is a collection,
						// iterate thru the collection and emit XML from each element
						if (childFD.isWrapped())
							childFD.writeWrap(buffy, false);
						for (Object next : thatCollection)
						{
							if (isScalar) // leaf node!
							{
								try
								{
									childFD.appendCollectionLeaf(buffy, next);
								}
								catch (IllegalArgumentException e)
								{
									throw new XMLTranslationException("TranslateToXML for collection leaf " + this, e);
								}
								catch (IllegalAccessException e)
								{
									throw new XMLTranslationException("TranslateToXML for collection leaf " + this, e);
								}
							}
							else if (next instanceof ElementState)
							{
								ElementState collectionSubElementState = (ElementState) next;

								// FIXME -- uses class instead of field to get F2XO
								// does this work correctly with @xml_classes ?
								final Class<? extends ElementState> collectionElementClass = collectionSubElementState
										.getClass();
								// TODO -- changed by andruid 7/21/08 -- not sure if this breaks anything else, but
								// it seems correct,
								// and it fixes @simpl_scalar @simpl_hints(Hint.XML_TEXT) output

								FieldDescriptor collectionElementFD = childFD.isPolymorphic() ? collectionSubElementState
										.classDescriptor().pseudoFieldDescriptor()
										: childFD;

								collectionSubElementState.serializeToBuilder(collectionElementFD, buffy);
							}
							else
								throw collectionElementTypeException(thatReferenceObject);
						}
						if (childFD.isWrapped())
							childFD.writeWrap(buffy, true);
					}
					else if (thatReferenceObject instanceof ElementState)
					{ // one of our nested elements, so recurse
						ElementState nestedES = (ElementState) thatReferenceObject;
						// if the field type is the same type of the instance (that is, if no subclassing),
						// then use the field name to determine the XML tag name.
						// if the field object is an instance of a subclass that extends the declared type of
						// the
						// field, use the instance's type to determine the XML tag name.
						FieldDescriptor nestedFD = childFD.isPolymorphic() ? nestedES.classDescriptor()
								.pseudoFieldDescriptor() : childFD;

						nestedES.serializeToBuilder(nestedFD, buffy);
						// buffy.append('\n');
					}
				}
			} // end of for each element child

			// TODO -- namespace support!
			// HashMap<String, ElementState> nestedNameSpaces = this.nestedNameSpaces;
			// if (nestedNameSpaces != null)
			// {
			// for (ElementState nestedNSE : nestedNameSpaces.values())
			// {
			// //TODO -- where do we get optimizations for nested namespace elements?
			// FieldToXMLOptimizations nestedNsF2XO =
			// nestedNSE.classDescriptor().pseudoFieldDescriptor();
			// nestedNSE.translateToXMLBuilder(nestedNsF2XO, buffy);
			// }
			// }
			// end the element
			fieldDescriptor.writeCloseTag(buffy);
		} // end if no nested elements or text node
	}

	/**
	 * Generate an exception during translateToXML() when a collection contains elements that are not
	 * ElementState subclasses, or ScalarType leafs.
	 * 
	 * @param thatReferenceObject
	 * @return
	 */
	private XMLTranslationException collectionElementTypeException(Object thatReferenceObject)
	{
		return new XMLTranslationException("Collections MUST contain "
				+ "objects of class derived from ElementState or Scalars, but " + thatReferenceObject
				+ " contains some that aren't.");
	}

	/**
	 * /** Translates a tree of ElementState objects, and writes the output to the Appendable passed
	 * in
	 * <p/>
	 * Uses Java reflection to iterate through the public fields of the object. When primitive types
	 * are found, they are translated into attributes. When objects derived from ElementState are
	 * found, they are recursively translated into nested elements.
	 * <p/>
	 * The result is a hierarchichal XML structure.
	 * <p/>
	 * Note: to keep XML files from growing unduly large, there is a default value for each type.
	 * Attributes which are set to the default value (for that type), are not emitted.
	 * 
	 * @param fieldDescriptor
	 * @param appendable
	 *          Appendable to translate into. Must be non-null. Can be a Writer, OutputStream, ...
	 * 
	 * @throws XMLTranslationException
	 *           if a problem arises during translation. Problems with Field access are possible, but
	 *           very unlikely.
	 * @throws IOException
	 */
	private void serializeToAppendable(FieldDescriptor fieldDescriptor, Appendable appendable)
			throws XMLTranslationException, IOException
	{
		this.preTranslationProcessingHook();

		fieldDescriptor.writeElementStart(appendable);

		// TODO -- namespace support
		// ArrayList<FieldToXMLOptimizations> xmlnsF2XOs =
		// classDescriptor().xmlnsAttributeOptimizations();
		// int numXmlnsAttributes = (xmlnsF2XOs == null) ? 0 : xmlnsF2XOs.size();
		// if (numXmlnsAttributes > 0)
		// {
		// for (int i=0; i<numXmlnsAttributes; i++)
		// {
		// FieldToXMLOptimizations xmlnsF2Xo = xmlnsF2XOs.get(i);
		// xmlnsF2Xo.xmlnsAttr(appendable);
		// }
		// }
		ArrayList<FieldDescriptor> attributeFieldDescriptors = classDescriptor()
				.attributeFieldDescriptors();
		int numAttributes = attributeFieldDescriptors.size();

		if (numAttributes > 0)
		{
			try
			{
				for (int i = 0; i < numAttributes; i++)
				{
					// iterate through fields
					FieldDescriptor childFD = attributeFieldDescriptors.get(i);
					childFD.appendValueAsAttribute(appendable, this);
				}
			}
			catch (Exception e)
			{
				// IllegalArgumentException, IllegalAccessException
				throw new XMLTranslationException("TranslateToXML for attribute " + this, e);
			}
		}
		// ArrayList<Field> elementFields = optimizations.elementFields();
		ArrayList<FieldDescriptor> elementFieldDescriptors = classDescriptor()
				.elementFieldDescriptors();
		int numElements = elementFieldDescriptors.size();

		// FIXME -- get rid of old textNode stuff. it doesnt even work
		StringBuilder textNode = this.textNodeBuffy;
		boolean hasXmlText = fieldDescriptor.hasXmlText();
		if ((numElements == 0) && (textNode == null) && !hasXmlText)
		{
			appendable.append('/').append('>'); // done! completely close element behind attributes
		}
		else
		{
			if (!fieldDescriptor.isXmlNsDecl())
				appendable.append('>'); // close open tag behind attributes unless in a nested namespace
																// root

			// FIXME get rid of this block, because this method of dealing with text nodes is obsolete
			if (textNode != null)
			{
				// TODO -- might need to trim the buffy here!
				// if (textNode.length() > 0 -- not needed with current impl, which doesnt do append to text
				// node if trim -> empty string
				// if (textNode.length() > 0)
				XMLTools.escapeXML(appendable, textNode);
			}

			if (hasXmlText)
			{
				try
				{
					fieldDescriptor.appendXmlText(appendable, this);
				}
				catch (Exception e)
				{
					throw new XMLTranslationException("TranslateToXML for @xml_field " + this, e);
				}
			}

			for (int i = 0; i < numElements; i++)
			{
				// NodeToJavaOptimizations pte = optimizations.getPTEByFieldName(thatFieldName);
				FieldDescriptor childFD = elementFieldDescriptors.get(i);
				final int childFdType = childFD.getType();
				if (childFdType == SCALAR)
				{
					try
					{
						childFD.appendLeaf(appendable, this);
					}
					catch (Exception e)
					{
						throw new XMLTranslationException("TranslateToXML for leaf node " + this, e);
					}
				}
				else
				{
					Object thatReferenceObject = null;
					Field childField = childFD.getField();
					try
					{
						thatReferenceObject = childField.get(this);
					}
					catch (IllegalAccessException e)
					{
						debugA("WARNING re-trying access! " + e.getStackTrace()[0]);
						childField.setAccessible(true);
						try
						{
							thatReferenceObject = childField.get(this);
						}
						catch (IllegalAccessException e1)
						{
							error("Can't access " + childField.getName());
							e1.printStackTrace();
						}
					}
					// ignore null reference objects
					if (thatReferenceObject == null)
						continue;

					final boolean isScalar = (childFdType == COLLECTION_SCALAR)
							|| (childFdType == MAP_SCALAR);
					// gets Collection object directly or through Map.values()
					Collection thatCollection;
					switch (childFdType)
					{
					case COLLECTION_ELEMENT:
					case COLLECTION_SCALAR:
					case MAP_ELEMENT:
					case MAP_SCALAR:
						thatCollection = XMLTools.getCollection(thatReferenceObject);
						break;
					default:
						thatCollection = null;
						break;
					}

					if (thatCollection != null && (thatCollection.size() > 0))
					{ // if the object is a collection, iterate thru the collection and emit XML for each
						// element
						if (childFD.isWrapped())
							childFD.writeWrap(appendable, false);
						for (Object next : thatCollection)
						{
							if (isScalar) // leaf node!
							{
								try
								{
									childFD.appendCollectionLeaf(appendable, next);
								}
								catch (IllegalArgumentException e)
								{
									throw new XMLTranslationException("TranslateToXML for collection leaf " + this, e);
								}
								catch (IllegalAccessException e)
								{
									throw new XMLTranslationException("TranslateToXML for collection leaf " + this, e);
								}
							}
							else if (next instanceof ElementState)
							{
								ElementState collectionSubElementState = (ElementState) next;
								final Class<? extends ElementState> collectionElementClass = collectionSubElementState
										.getClass();

								FieldDescriptor collectionElementFD = childFD.isPolymorphic() ?
								// tag by class
								collectionSubElementState.classDescriptor().pseudoFieldDescriptor()
										: childFD; // tag by annotation

								collectionSubElementState.serializeToAppendable(collectionElementFD, appendable);
							}
							else
								throw collectionElementTypeException(thatReferenceObject);
						}
						if (childFD.isWrapped())
							childFD.writeWrap(appendable, true);
					}
					else if (thatReferenceObject instanceof ElementState)
					{ // one of our nested elements, so recurse
						ElementState nestedES = (ElementState) thatReferenceObject;
						// if the field type is the same type of the instance (that is, if no subclassing),
						// then use the field name to determine the XML tag name.
						// if the field object is an instance of a subclass that extends the declared type of
						// the
						// field, use the instance's type to determine the XML tag name.
						FieldDescriptor nestedF2XO = childFD.isPolymorphic() ? nestedES.classDescriptor()
								.pseudoFieldDescriptor() : childFD;

						nestedES.serializeToAppendable(nestedF2XO, appendable);
					}
				}
			} // end of for each element child
			// FIXME -- Add namespace support!
			// HashMap<String, ElementState> nestedNameSpaces = this.nestedNameSpaces;
			// if (nestedNameSpaces != null)
			// {
			// for (ElementState nestedNSE : nestedNameSpaces.values())
			// {
			// // translate nested namespace root
			// FieldToXMLOptimizations nestedNsF2XO =
			// nestedNSE.classDescriptor().pseudoFieldDescriptor();
			// nestedNSE.translateToXMLAppendable(nestedNsF2XO, appendable);
			// }
			// }
			// end the element
			fieldDescriptor.writeCloseTag(appendable);
		} // end if no nested elements or text node
	}

	/**
	 * Translate a file XML to a strongly typed tree of XML objects.
	 * 
	 * Use SAX or DOM parsing depending on the value of useDOMForTranslateTo.
	 * 
	 * @param fileName
	 *          the name of the XML file that needs to be translated.
	 * @param translationSpace
	 *          Specifies mapping from XML nodes (elements and attributes) to Java types.
	 * 
	 * @return Strongly typed tree of ElementState objects.
	 * @throws XMLTranslationException
	 */
	public static ElementState translateFromXML(String fileName, TranslationScope translationSpace)
			throws XMLTranslationException
	{
		File xmlFile = new File(fileName);
		if (!xmlFile.exists() && !xmlFile.canRead())
			throw new XMLTranslationException("Can't access " + xmlFile.getAbsolutePath(), FILE_NOT_FOUND);

		return translateFromXML(xmlFile, translationSpace);
	}
	/**
	 * Link new born root element to its Optimizations and create an elementByIdMap for it.
	 */
	void setupRoot()
	{
		elementByIdMap = new HashMap<String, ElementState>();
	}

	/**
	 * Use the (faster!) SAX parser to form a strongly typed tree of ElementState objects from XML.
	 * 
	 * @param charSequence
	 * @param translationSpace
	 * @return
	 * @throws XMLTranslationException
	 */
	public static ElementState translateFromXMLCharSequence(CharSequence charSequence,
			TranslationScope translationSpace) throws XMLTranslationException
	{
		ElementStateSAXHandler saxHandler = new ElementStateSAXHandler(translationSpace);
		return saxHandler.parse(charSequence);
	}

	/**
	 * Use the (faster!) SAX parser to form a strongly typed tree of ElementState objects from XML.
	 * 
	 * @param purl
	 * @param translationScope
	 * @return
	 * @throws XMLTranslationException
	 */
	public static ElementState translateFromXML(ParsedURL purl, TranslationScope translationScope)
			throws XMLTranslationException
	{
		if (purl == null)
			throw new XMLTranslationException("Null PURL", NULL_PURL);

		if (!purl.isNotFileOrExists())
			throw new XMLTranslationException("Can't find " + purl.toString(), FILE_NOT_FOUND);

		ElementStateSAXHandler saxHandler = new ElementStateSAXHandler(translationScope);
		return saxHandler.parse(purl);
	}

	/**
	 * Use the (faster!) SAX parser to form a strongly typed tree of ElementState objects from XML.
	 * 
	 * @param url
	 * @param translationSpace
	 * @return
	 * @throws XMLTranslationException
	 */
	public static ElementState translateFromXML(URL url, TranslationScope translationSpace)
			throws XMLTranslationException
	{
		ElementStateSAXHandler saxHandler = new ElementStateSAXHandler(translationSpace);
		return saxHandler.parse(url);
	}

	/**
	 * Use the (faster!) SAX parser to form a strongly typed tree of ElementState objects from XML.
	 * 
	 * @param file
	 * @param translationSpace
	 * @return
	 * @throws XMLTranslationException
	 */
	public static ElementState translateFromXML(File file, TranslationScope translationSpace)
			throws XMLTranslationException
	{
		ElementStateSAXHandler saxHandler = new ElementStateSAXHandler(translationSpace);
		return saxHandler.parse(file);
	}

	/**
	 * Use the (faster!) SAX parser to form a strongly typed tree of ElementState objects from XML.
	 * 
	 * @param inputStream
	 *          An InputStream to the XML that needs to be translated.
	 * @param translationSpace
	 * @return
	 * @throws XMLTranslationException
	 */
	public static ElementState translateFromXML(InputStream inputStream,
			TranslationScope translationSpace) throws XMLTranslationException
	{
		ElementStateSAXHandler saxHandler = new ElementStateSAXHandler(translationSpace);
		return saxHandler.parse(inputStream);
	}

	/**
	 * Used in SAX parsing to unmarshall attributes into fields.
	 * 
	 * @param translationSpace
	 * @param attributes
	 * @param scalarUnmarshallingContext
	 *          TODO
	 */
	void translateAttributes(TranslationScope translationSpace, Attributes attributes,
			ScalarUnmarshallingContext scalarUnmarshallingContext, ElementState context)
	{
		int numAttributes = attributes.getLength();
		for (int i = 0; i < numAttributes; i++)
		{
			// TODO -- figure out what we're doing if there's a colon and a namespace
			final String tag = attributes.getQName(i);
			final String value = attributes.getValue(i);
			// TODO String attrType = getType()?!
			if (value != null)
			{
				ClassDescriptor classDescriptor = classDescriptor();
				FieldDescriptor fd = classDescriptor
						.getFieldDescriptorByTag(tag, translationSpace, context);
				if (fd == null)
					classDescriptor.warning(" FieldDescriptor not found for tag " + tag);
				else
				{
					try
					{
						// don't enforce hints -- if there is an attribute, use it.
						fd.setFieldToScalar(this, value, scalarUnmarshallingContext);
						// the value can become a unique id for looking up this
						// TODO -- could support the ID type for the node here!
						if ("id".equals(fd.getTagName()))
							this.elementByIdMap.put(value, this);
					}
					catch (Throwable ex)
					{
						classDescriptor.error(" processing FieldDescriptor for tag " + tag);
						ex.printStackTrace();
					}
				}
			}
		}
	}

/**
 * Translate to XML, then write the result to a file.
 * 
 * @param xmlFileName
 * @throws XMLTranslationException
 */
	public void serializeAsFile(String xmlFileName) throws XMLTranslationException, IOException
	{
		if (!xmlFileName.endsWith(".xml") && !xmlFileName.endsWith(".XML"))
		{
			xmlFileName = xmlFileName + ".xml";
		}
		serialize(new File(xmlFileName));
	}

	// ////////////// helper methods used by translateToXML() //////////////////

	static final int			HAVENT_TRIED_ADDING	= 0;

	static final int			DONT_NEED_WARNING		= 1;

	static final int			NEED_WARNING				= -1;

	transient private int	considerWarning			= HAVENT_TRIED_ADDING;

	/**
	 * This is the hook that enables programmers to do something special when handling a nested XML
	 * element and its associate ElementState (subclass), by overriding this method and providing a
	 * custom implementation.
	 * <p/>
	 * The default implementation is a no-op. fields that get here are ignored.
	 * 
	 * @param elementState
	 * @throws XMLTranslationException
	 */
	protected void addNestedElement(ElementState elementState)
	{
		if (considerWarning == HAVENT_TRIED_ADDING)
			considerWarning = NEED_WARNING;
	}

	/**
	 * Called during translateFromXML(). If the textNodeString is currently null, assign to.
	 * Otherwise, append to it.
	 * 
	 * @param newText
	 *          Text Node value just found parsing the XML.
	 */
	protected void appendTextNodeString(String newText)
	{
		if ((newText != null) && (newText.length() > 0))
		{
			// TODO -- hopefully we can get away with this speed up
			String trimmed = newText.trim();
			if (trimmed.length() > 0)
			{
				String unescapedString = XMLTools.unescapeXML(newText);
				if (this.textNodeBuffy == null)
					textNodeBuffy = new StringBuilder(unescapedString);
				else
					textNodeBuffy.append(unescapedString);
			}
		}
	}

	/**
	 * @deprecated should use @simpl_scalar @simpl_hints(Hint.XML_TEXT) or @simpl_scalar @simpl_hints(Hint.XML_LEAF) to specify text child
	 * @return
	 */
	@Deprecated
	public String getTextNodeString()
	{
		return (textNodeBuffy == null) ? null : textNodeBuffy.toString();
		// return (textNodeString == null) ? null : XmlTools.unescapeXML(textNodeString);
	}

	// ///////////////////////// other methods //////////////////////////

	/**
	 * The DOM classic accessor method.
	 * 
	 * @return element in the tree rooted from this, whose id attrribute is as in the parameter.
	 * 
	 */
	public ElementState getElementStateById(String id)
	{
		return this.elementByIdMap.get(id);
	}

	/**
	 * @return the parent
	 */
	public ElementState parent()
	{
		return parent;
	}

	/**
	 * Set the parent of this, to create the tree structure.
	 * 
	 * @param parent
	 */
	public void setParent(ElementState parent)
	{
		this.parent = parent;
	}

	public ElementState getRoot()
	{
		return parent == null ? this : parent.getRoot();
	}

	/**
	 * Metalanguage declaration that tells simpl serialization that each Field it is applied to
	 * as an annotation is a scalar-value.
	 * <p/>
	 * The attribute name will be derived from the field name, using camel case conversion, unless @xml_tag
	 * is used.
	 * 
	 * @author andruid
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@Inherited
	public @interface simpl_scalar
	{
	}

	/**
	 * S.IM.PL	declaration for hints that precisely define the syntactic structure of serialization.
	 * 
	 * @author andruid
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@Inherited
	public @interface simpl_hints
	{
		Hint[] value() default
		{ Hint.XML_ATTRIBUTE };
	}
	/**
	 * Optional metalanguage declaration. Enables specificaition of one or more formatting strings.
	 * Only affects ScalarTyped Fields (ignored otherwise). The format string will be passed to the
	 * ScalarType for type-specific interpretation.
	 * <p/>
	 * An example of use is to pass DateFormat info to the DateType.
	 * 
	 * @author andruid
	 * @author toupsz
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@Inherited
	public @interface simpl_format
	{
		String[] value();
	}

	/**
	 * Metalanguage declaration that tells ecologylab.xml translators that each Field it is applied to
	 * as an annotation is represented in XML by a (non-leaf) nested child element. The field must be
	 * a subclass of ElementState.
	 * <p/>
	 * The nested child element name will be derived from the field name, using camel case conversion,
	 * unless @xml_tag is used.
	 * 
	 * @author andruid
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@Inherited
	public @interface simpl_composite
	{

	}

	static final String		NULL_TAG		= "";

	static final Class[]	NO_CLASSES	= {

																		};

	/**
	 * Metalanguage declaration that tells ecologylab.xml translators that each Field it is applied to
	 * as an annotation is of type Collection. An argument may be passed to declare the tag name of
	 * the child elements. The XML may define any number of child elements with this tag. In this
	 * case, the class of the elements will be dervied from the instantiated generic type declaration
	 * of the children. For example, <code>@xml_collection("item")    ArrayList&lt;Item&gt;	items;</code>
	 * <p/>
	 * For that formulation, the type of the children may be a subclass of ElementState, for full
	 * nested elements, or it may be a ScalarType, for leaf nodes.
	 * <p/>
	 * Without the tag name declaration, the tag name will be derived from the class name of the
	 * children, and in translate from XML, the class name will be derived from the tag name, and then
	 * resolved in the TranslationSpace.
	 * <p/>
	 * Alternatively, to achieve polymorphism, for children subclassed from ElementState only, this
	 * declaration can be combined with @xml_classes. In such cases, items of the various classes will
	 * be collected together in the declared Collection. Then, the tag names for these elements will
	 * be derived from their class declarations.
	 * 
	 * @author andruid
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@Inherited
	public @interface simpl_collection
	{
		String value() default NULL_TAG;
	}

	/**
	 * Metalanguage declaration that tells ecologylab.xml translators that each Field it is applied to
	 * as an annotation is of type Map. An argument may be passed to declare the tag name of the child
	 * elements. The XML may define any number of child elements with this tag. In this case, the
	 * class of the elements will be dervied from the instantiated generic type declaration of the
	 * children.
	 * <p/>
	 * For example, <code>@xml_map("foo")    HashMap&lt;String, FooFoo&gt;	items;</code><br/>
	 * The values of the Map must implement the Mappable interface, to supply a key which matches the
	 * key declaration in the Map's instantiated generic types.
	 * <p/>
	 * Without the tag name declaration, the tag name will be derived from the class name of the
	 * children, and in translate from XML, the class name will be derived from the tag name, and then
	 * resolved in the TranslationSpace.
	 * 
	 * @author andruid
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@Inherited
	public @interface simpl_map
	{
		String value() default NULL_TAG;
	}

	/**
	 * Metalanguage declaration that can be applied either to field or to class declarations.
	 * 
	 * Annotation that tells ecologylab.xml translators that instead of generating a name for XML
	 * elements corresponding to the field or class using camel case conversion, one is specified
	 * explicitly. This name is specified by the value of this annotation.
	 * <p/>
	 * Note that programmers should be careful when specifying an xml_tag, to ensure that there are no
	 * collisions with other names. Note that when an xml_tag is specified for a field or class, it
	 * will ALWAYS EMIT AND TRANSLATE FROM USING THAT NAME.
	 * 
	 * xml_tag's should typically be something that cannot be represented using camel case name
	 * conversion, such as utilizing characters that are not normally allowed in field names, but that
	 * are allowed in XML names. This can be particularly useful for building ElementState objects out
	 * of XML from the wild.
	 * <p/>
	 * You cannot use XML-forbidden characters or constructs in an xml_tag!
	 * 
	 * When using @xml_tag, you MUST create your corresponding TranslationSpace entry using a Class
	 * object, instead of using a default package name.
	 * 
	 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface xml_tag
	{
		String value();
	}

	/**
	 * This optional metalanguage declaration is used to add extra tags to a field or class, in order
	 * to enable backwards compatability with a previous dialect of XML. It affects only translate
	 * from XML; translateToXML() never uses these entries.
	 * 
	 * @author andruid
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface xml_other_tags
	{
		String[] value();
	}

	/**
	 * Supplementary metalanguage declaration that can be applied only to a field. The argument is the
	 * name of a TranslationScope.
	 * <p/>
	 * Annotation uses the argument to lookup a TranslationScope. If there is none, a warning is
	 * provided. Otherwise, mappings are created for tag names associated with each class in the
	 * TranslationScope. It then creates a mapping from the tag and class names to the field it is
	 * applied to, so that translateFromXML(...) will set a value based on an element with the tags,
	 * if field is also declared with @xml_nested, or collect values when elements have the tags, if
	 * the field is declared with @xml_collection.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	public @interface simpl_scope
	{
		String value();
	}

	/**
	 * Supplementary metalanguage declaration that can be applied only to a field. The argument is an
	 * array of Class objects.
	 * <p/>
	 * Annotation forms tag names from each of the class names, using camel case conversion. It then
	 * creates a mapping from the tag and class names to the field it is applied to, so that
	 * translateFromXML(...) will set a value based on an element with the tags, if field is also
	 * declared with @xml_nested, or collect values when elements have the tags, if the field is
	 * declared with @xml_collection.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface simpl_classes
	{
		Class<? extends ElementState>[] value();
	}

	/**
	 * Used to specify that the elements of a collection or map should not be wrapped by an outer tag
	 * corresponding to their field name.
	 * 
	 * @author andruid
	 * 
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	public @interface simpl_nowrap
	{
	}

	/**
	 * Source of bindings that will be mapped to an xml_bind_from declaration inside an inheriting
	 * object or one referenced through a field.
	 * 
	 * @author andruid
	 * 
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	public @interface simpl_bind_to
	{
		/**
		 * @return This is the name of this binding site. It must match the name of the bind_from site.
		 *         Common static final constants can be used across @xml_bind_to and @xml_bind_from,
		 *         ensuring robust consistency through re-factoring.
		 */
		String name();

		String scopeName();

		Class<? extends ElementState>[] classes();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	public @interface simpl_bind_from
	{
		/**
		 * @return This is the name of this binding site. It must match the name of the bind_to site.
		 *         Common static final constants can be used across @xml_bind_to and @xml_bind_from,
		 *         ensuring robust consistency through re-factoring.
		 */
		String value();
	}

	public enum DbHint
	{
		PRIMARY_KEY, ALLOW_NOT_NULL, UNIQUE
	}

	/**
	 * Supplementary metalanguage declaration that can be applied only to a field. This is used for
	 * assigning database constraints to a field, which are referenced in creating correponding sql
	 * table schema. Database constraints are defined in 'DbHint'
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@Inherited
	public @interface simpl_db
	{
		/**
		 * @return assigned database constraints that are defined in 'DbHint'
		 */
		DbHint[] value();
	}

	/**
	 * @return Returns the optimizations.
	 */

	public ClassDescriptor classDescriptor()
	{
		ClassDescriptor result = classDescriptor;
		if (result == null)
		{
			result = ClassDescriptor.getClassDescriptor(this);
			this.classDescriptor = result;
		}
		return result;
	}

	/**
	 * Perform custom processing on the newly created child node, just before it is added to this.
	 * <p/>
	 * This is part of depth-first traversal during translateFromXML().
	 * <p/>
	 * This, the default implementation, does nothing. Sub-classes may wish to override.
	 * 
	 * @param child
	 */
	protected void createChildHook(ElementState child)
	{

	}

	/**
	 * Perform custom processing immediately before translating this to XML.
	 * <p/>
	 * This, the default implementation, does nothing. Sub-classes may wish to override.
	 * 
	 */
	protected void preTranslationProcessingHook()
	{

	}

	/**
	 * Perform custom processing immediately after all translation from XML is completed. This allows
	 * a newly-created ElementState object to perform any post processing with all the data it will
	 * have from XML.
	 * <p/>
	 * This method is called by NodeToJavaOptimizations.createChildElement() or translateToXML
	 * depending on whether the element in question is a child or the top-level parent.
	 * <p/>
	 * This, the default implementation, does nothing. Sub-classes may wish to override.
	 * 
	 */
	protected void postTranslationProcessingHook()
	{

	}

	/**
	 * Clear data structures and references to enable garbage collecting of resources associated with
	 * this.
	 */
	public void recycle()
	{
		if (parent == null)
		{ // root state!
			if (elementByIdMap != null)
			{
				elementByIdMap.clear();
				elementByIdMap = null;
			}
		}
		else
			parent = null;

		elementByIdMap = null;
		textNodeBuffy = null;
		classDescriptor = null;
		if (nestedNameSpaces != null)
		{
			for (ElementState nns : nestedNameSpaces.values())
			{
				if (nns != null)
					nns.recycle();
			}
			nestedNameSpaces.clear();
			nestedNameSpaces = null;
		}
	}

	/**
	 * Add a NestedNameSpace object to this.
	 * 
	 * @param urn
	 * @param nns
	 */
	private void nestNameSpace(String urn, ElementState nns)
	{
		if (nestedNameSpaces == null)
			nestedNameSpaces = new HashMap<String, ElementState>(2);

		nestedNameSpaces.put(urn, nns);
	}

	/**
	 * Set-up referential chains for a newly born child of this.
	 * 
	 * @param newChildES
	 */
	void setupChildElementState(ElementState newChildES)
	{
		newChildES.elementByIdMap = elementByIdMap;
		newChildES.parent = this;
		ClassDescriptor parentOptimizations = classDescriptor();
		// ClassDescriptor childOptimizations =
		// parentOptimizations.lookupChildOptimizations(newChildES);
		newChildES.classDescriptor = ClassDescriptor.getClassDescriptor(newChildES);
		// childOptimizations.setParent(parentOptimizations);
	}

	/**
	 * Either lookup an existing Nested Namespace object, or form a new one, map it, and return it.
	 * This lazy evaluation type call is invoked either in translateFromXML(), or, when procedurally
	 * building an element with Namespace children.
	 * 
	 * @param id
	 * @param esClass
	 * @return Namespace ElementState object associated with urn.
	 */
	public ElementState getNestedNameSpace(String id)
	{
		/*
		 * ElementState result = (nestedNameSpaces == null) ? null : nestedNameSpaces.get(id); if
		 * (result == null) { Class<? extends ElementState> esClass =
		 * classDescriptor.lookupNameSpaceClassById(id); if (esClass != null) { try { result =
		 * XMLTools.getInstance(esClass); this.setupChildElementState(result);
		 * result.classDescriptor.setNameSpaceID(id); // result.parent = this; nestNameSpace(id,
		 * result); // debug("WOW! Created nested Namespace xmlns:"+id+'\n'); } catch
		 * (XMLTranslationException e) { e.printStackTrace(); } } } return result;
		 */
		return null;
	}

	/**
	 * Lookup an ElementState subclass representing the scope of the nested XML Namespace in this.
	 * 
	 * @param id
	 * @return The ElementState subclass associated with xmlns:id, if there is one. Otherwise, null.
	 */
	public ElementState lookupNestedNameSpace(String id)
	{
		return (nestedNameSpaces == null) ? null : nestedNameSpaces.get(id);
	}

	/**
	 * If the element associated with this is annotated with a field for @simpl_scalar @simpl_hints(Hint.XML_TEXT), make that
	 * available here.
	 * 
	 * @return
	 */
	FieldDescriptor scalarTextChildFD()
	{
		return classDescriptor().scalarTextFD();
	}

	public boolean hasScalarTextField()
	{
		return classDescriptor().hasScalarTextField();
	}

}
