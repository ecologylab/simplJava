package ecologylab.serialization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.xml.sax.Attributes;

import ecologylab.generic.Debug;
import ecologylab.serialization.TranslationScope.GRAPH_SWITCH;

/**
 * This class is the heart of the <code>ecologylab.serialization</code> translation framework.
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
	private static final String										SIMPL_ID									= "simpl_id";

	private static final String										SIMPL_REF									= "simpl_ref";

	private static HashMap<Integer, ElementState>	marshalledObjects					= new HashMap<Integer, ElementState>();

	private static HashMap<Integer, ElementState>	visitedElements						= new HashMap<Integer, ElementState>();

	private static HashMap<Integer, ElementState>	needsAttributeHashCode		= new HashMap<Integer, ElementState>();

	public static HashMap<String, ElementState>		unmarshalledObjects				= new HashMap<String, ElementState>();

	// --------//

	/**
	 * Link for a DOM tree.
	 */
	transient ElementState												parent;

	/**
	 * Just-in time look-up tables to make translation be efficient. Allocated on a per class basis.
	 */
	transient private ClassDescriptor							classDescriptor;

	/**
	 * Use for resolving getElementById()
	 */
	transient HashMap<String, ElementState>				elementByIdMap;

	transient HashMap<String, ElementState>				nestedNameSpaces;

	static protected final int										ESTIMATE_CHARS_PER_FIELD	= 80;

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
	 * @throws SIMPLTranslationException
	 *           if there is a problem with the structure. Specifically, in each ElementState object,
	 *           fields for attributes must be declared before all fields for nested elements (those
	 *           derived from ElementState). If there is any public field which is not derived from
	 *           ElementState declared after the declaration for 1 or more ElementState instance
	 *           variables, this exception will be thrown.
	 */
	public StringBuilder serialize() throws SIMPLTranslationException
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
	 * @throws SIMPLTranslationException
	 *           if a problem arises during translation. Problems with Field access are possible, but
	 *           very unlikely.
	 */
	public StringBuilder serialize(StringBuilder buffy) throws SIMPLTranslationException
	{
		if (buffy == null)
			buffy = allocStringBuilder();

		try
		{
			// first-pass of the two pass algorithm. resolves cyclic pointers by creating appropriate data
			// structures.
			resolveGraph(this);

			serializeToBuilder(classDescriptor().pseudoFieldDescriptor(), buffy);

			// clear all datastructures used by two-pass algorithm.
			recycleSerializationMappings();

			return buffy;
		}
		catch (IOException e)
		{
			throw new SIMPLTranslationException("IO", e);
		}
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
	 * @throws SIMPLTranslationException
	 *           if a problem arises during translation. Problems with Field access are possible, but
	 *           very unlikely.
	 * @throws IOException
	 *           If there are problems with the file.
	 */
	public void serialize(File outputFile) throws SIMPLTranslationException, IOException
	{
		XMLTools.createParentDirs(outputFile);

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
		serialize(bufferedWriter);
		bufferedWriter.close();
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
	 * @throws SIMPLTranslationException
	 *           if a problem arises during translation. The most likely cause is an IOException.
	 * 
	 *           <p/>
	 *           Problems with Field access are possible, but very unlikely.
	 */
	public void serialize(Appendable appendable) throws SIMPLTranslationException
	{
		if (appendable == null)
			throw new SIMPLTranslationException("Appendable is null");

		try
		{
			// first-pass of the two pass algorithm. resolves cyclic pointers by creating appropriate data
			// structures.
			resolveGraph(this);

			serializeToAppendable(classDescriptor().pseudoFieldDescriptor(), appendable);

			// clear all data structures used for two pass-algorithm
			recycleSerializationMappings();
		}
		catch (IOException e)
		{
			throw new SIMPLTranslationException("IO", e);
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
	 * @throws SIMPLTranslationException
	 *           if a problem arises during translation. Problems with Field access are possible, but
	 *           very unlikely.
	 * @throws IOException
	 */
	private void serializeToBuilder(FieldDescriptor fieldDescriptor, StringBuilder buffy)
			throws SIMPLTranslationException, IOException
	{

		// To handle cyclic pointers. map marshalled ElementState Objects.
		mapCurrentElementState();

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
				throw new SIMPLTranslationException("TranslateToXML for attribute " + this, e);
			}
		}

		// To handle cyclic graphs append simpl id as an attribute.
		appendSimplIdIfRequired(buffy);

		ArrayList<FieldDescriptor> elementFieldDescriptors = classDescriptor()
				.elementFieldDescriptors();
		int numElements = elementFieldDescriptors.size();

		boolean hasXmlText = fieldDescriptor.hasXmlText();
		if ((numElements == 0) && !hasXmlText)
		{
			buffy.append('/').append('>'); // done! completely close element behind attributes
		}
		else
		{
			if (!fieldDescriptor.isXmlNsDecl())
				buffy.append('>'); // close open tag behind attributes

			if (hasXmlText)
			{
				try
				{
					classDescriptor().getScalarTextFD().appendXMLScalarText(buffy, this);
				}
				catch (Exception e)
				{
					throw new SIMPLTranslationException("TranslateToXML for @xml_field " + this, e);
				}
			}

			for (int i = 0; i < numElements; i++)
			{
				FieldDescriptor childFD = elementFieldDescriptors.get(i);
				final int childFdType = childFD.getType();
				if (childFD.getType() == SCALAR)
				{
					try
					{
						childFD.appendLeaf(buffy, this);
					}
					catch (Exception e)
					{
						throw new SIMPLTranslationException("TranslateToXML for leaf node " + this, e);
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
									throw new SIMPLTranslationException("TranslateToXML for collection leaf " + this,
											e);
								}
								catch (IllegalAccessException e)
								{
									throw new SIMPLTranslationException("TranslateToXML for collection leaf " + this,
											e);
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

								// inside handles cyclic pointers by translating only the simpl id if already
								// serialized.
								serializeCompositeElements(buffy, collectionSubElementState, collectionElementFD);
								// collectionSubElementState.serializeToBuilder(collectionElementFD, buffy);
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

						// inside handles cyclic pointers by translating only the simpl id if already
						// serialized.
						serializeCompositeElements(buffy, nestedES, nestedFD);
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
	private SIMPLTranslationException collectionElementTypeException(Object thatReferenceObject)
	{
		return new SIMPLTranslationException("Collections MUST contain "
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
	 * @throws SIMPLTranslationException
	 *           if a problem arises during translation. Problems with Field access are possible, but
	 *           very unlikely.
	 * @throws IOException
	 */
	private void serializeToAppendable(FieldDescriptor fieldDescriptor, Appendable appendable)
			throws SIMPLTranslationException, IOException
	{

		// To handle cyclic pointers. map marshalled ElementState Objects.
		mapCurrentElementState();

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
				throw new SIMPLTranslationException("TranslateToXML for attribute " + this, e);
			}
		}

		// To handle cyclic graphs append simpl id as an attribute.
		appendSimplIdIfRequired(appendable);

		// ArrayList<Field> elementFields = optimizations.elementFields();
		ArrayList<FieldDescriptor> elementFieldDescriptors = classDescriptor()
				.elementFieldDescriptors();
		int numElements = elementFieldDescriptors.size();

		boolean hasXmlText = classDescriptor().hasScalarFD();
		if ((numElements == 0) && !hasXmlText)
		{
			appendable.append('/').append('>'); // done! completely close element behind attributes
		}
		else
		{
			if (!fieldDescriptor.isXmlNsDecl())
				appendable.append('>'); // close open tag behind attributes unless in a nested namespace
			// root

			if (hasXmlText)
			{
				try
				{
					classDescriptor().getScalarTextFD().appendXMLScalarText(appendable, this);
				}
				catch (Exception e)
				{
					throw new SIMPLTranslationException("TranslateToXML for @xml_field " + this, e);
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
						throw new SIMPLTranslationException("TranslateToXML for leaf node " + this, e);
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
									throw new SIMPLTranslationException("TranslateToXML for collection leaf " + this,
											e);
								}
								catch (IllegalAccessException e)
								{
									throw new SIMPLTranslationException("TranslateToXML for collection leaf " + this,
											e);
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

								// inside handles cyclic pointers by translating only the simpl id if already
								// serialized.
								serializeCompositeElements(appendable, collectionSubElementState,
										collectionElementFD);

								// collectionSubElementState.serializeToAppendable(collectionElementFD, appendable);
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
						FieldDescriptor nestedFD = childFD.isPolymorphic() ? nestedES.classDescriptor()
								.pseudoFieldDescriptor() : childFD;

						// inside handles cyclic pointers by translating only the simpl id if already
						// serialized.
						serializeCompositeElements(appendable, nestedES, nestedFD);
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
	 * Link new born root element to its Optimizations and create an elementByIdMap for it.
	 */
	void setupRoot()
	{
		elementByIdMap = new HashMap<String, ElementState>();
	}

	/**
	 * Used in SAX parsing to unmarshall attributes into fields.
	 * 
	 * @param translationScope
	 * @param attributes
	 * @param scalarUnmarshallingContext
	 *          TODO
	 */
	void translateAttributes(TranslationScope translationScope, Attributes attributes,
			ScalarUnmarshallingContext scalarUnmarshallingContext, ElementState context)
	{
		int numAttributes = attributes.getLength();
		for (int i = 0; i < numAttributes; i++)
		{
			// TODO -- figure out what we're doing if there's a colon and a namespace
			final String tag = attributes.getQName(i);
			final String value = attributes.getValue(i);

			if (handleSimplIds(tag, value))
				continue;

			// TODO String attrType = getType()?!
			if (value != null)
			{
				ClassDescriptor classDescriptor = classDescriptor();
				FieldDescriptor fd = classDescriptor
						.getFieldDescriptorByTag(tag, translationScope, context);
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

	private boolean handleSimplIds(final String tag, final String value)
	{
		if (TranslationScope.graphSwitch == GRAPH_SWITCH.ON)
		{
			if (tag.equals(ElementState.SIMPL_ID))
			{
				unmarshalledObjects.put(value, this);
				return true;
			}
			else
			{
				if (tag.equals(ElementState.SIMPL_REF))
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Translate to XML, then write the result to a file.
	 * 
	 * @param outputFileName
	 * @throws SIMPLTranslationException
	 */
	public void serialize(String outputFileName) throws SIMPLTranslationException, IOException
	{
		if (!outputFileName.endsWith(".xml") && !outputFileName.endsWith(".XML"))
		{
			outputFileName = outputFileName + ".xml";
		}
		serialize(new File(outputFileName));
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
	 * Metalanguage declaration that tells simpl serialization that each Field it is applied to as an
	 * annotation is a scalar-value.
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
	 * S.IM.PL declaration for hints that precisely define the syntactic structure of serialization.
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
	 * S.IM.PL declaration for scalar fields.
	 * <p/>
	 * Specifies filtering a scalar value on input, using a regex, before marshalling by a ScalarType.
	 * Only activated when you call on your TranslationScope instance, setPerformFilters(), before
	 * calling deserialize(Stream).
	 * 
	 * @author andruid
	 * 
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@Inherited
	public @interface simpl_filter
	{
		String regex();

		String replace() default "";
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
	 * Metalanguage declaration that tells ecologylab.serialization translators that each Field it is
	 * applied to as an annotation is represented in XML by a (non-leaf) nested child element. The
	 * field must be a subclass of ElementState.
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

	static final String	NULL_TAG	= "";

	/**
	 * Metalanguage declaration that tells ecologylab.serialization translators that each Field it is
	 * applied to as an annotation is of type Collection. An argument may be passed to declare the tag
	 * name of the child elements. The XML may define any number of child elements with this tag. In
	 * this case, the class of the elements will be dervied from the instantiated generic type
	 * declaration of the children. For example,
	 * <code>@xml_collection("item")    ArrayList&lt;Item&gt;	items;</code>
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
	 * Metalanguage declaration that tells ecologylab.serialization translators that each Field it is
	 * applied to as an annotation is of type Map. An argument may be passed to declare the tag name
	 * of the child elements. The XML may define any number of child elements with this tag. In this
	 * case, the class of the elements will be dervied from the instantiated generic type declaration
	 * of the children.
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
	 * Annotation that tells ecologylab.serialization translators that instead of generating a name
	 * for XML elements corresponding to the field or class using camel case conversion, one is
	 * specified explicitly. This name is specified by the value of this annotation.
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
		PRIMARY_KEY, NOT_NULL, NULL, UNIQUE
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
		 * @return database constraints defined in 'DbHint' and name of reference table
		 */
		DbHint[] value();

		String references() default "null";
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
	 * Set-up referential chains for a newly born child of this.
	 * 
	 * @param newParent
	 * @param ourClassDescriptor
	 *          TODO
	 */
	void setupInParent(ElementState newParent, ClassDescriptor ourClassDescriptor)
	{
		this.elementByIdMap = newParent.elementByIdMap;
		this.parent = newParent;
		this.classDescriptor = ourClassDescriptor;
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
	 * Lookup an ElementState subclass representing the scope of the nested XML Namespace in this.
	 * 
	 * @param id
	 * @return The ElementState subclass associated with xmlns:id, if there is one. Otherwise, null.
	 */
	public ElementState lookupNestedNameSpace(String id)
	{
		return (nestedNameSpaces == null) ? null : nestedNameSpaces.get(id);
	}

	/*
	 * Cyclic graph related functions
	 */
	private void resolveGraph(ElementState elementState)
	{
		if (TranslationScope.graphSwitch == GRAPH_SWITCH.ON)
		{
			visitedElements.put(elementState.hashCode(), elementState);

			ArrayList<FieldDescriptor> elementFieldDescriptors = elementState.classDescriptor()
					.elementFieldDescriptors();

			for (FieldDescriptor elementFieldDescriptor : elementFieldDescriptors)
			{
				Object thatReferenceObject = null;
				Field childField = elementFieldDescriptor.getField();
				try
				{
					thatReferenceObject = childField.get(elementState);
				}
				catch (IllegalAccessException e)
				{
					debugA("WARNING re-trying access! " + e.getStackTrace()[0]);
					childField.setAccessible(true);
					try
					{
						thatReferenceObject = childField.get(elementState);
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

				int childFdType = elementFieldDescriptor.getType();

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
					for (Object next : thatCollection)
					{
						if (next instanceof ElementState)
						{
							ElementState compositeElement = (ElementState) next;

							if (alreadyVisited(compositeElement))
							{
								needsAttributeHashCode.put(compositeElement.hashCode(), compositeElement);
							}
							else
							{
								resolveGraph(compositeElement);
							}
						}
					}
				}
				else if (thatReferenceObject instanceof ElementState)
				{
					ElementState compositeElement = (ElementState) thatReferenceObject;

					if (alreadyVisited(compositeElement))
					{
						needsAttributeHashCode.put(compositeElement.hashCode(), compositeElement);
					}
					else
					{
						resolveGraph(compositeElement);
					}
				}
			}
		}
	}

	private boolean alreadyVisited(ElementState elementState)
	{
		return visitedElements.containsKey(elementState.hashCode());
	}

	private void mapCurrentElementState()
	{
		if (TranslationScope.graphSwitch == GRAPH_SWITCH.ON)
		{
			marshalledObjects.put(this.hashCode(), this);
		}
	}

	private void serializeCompositeElements(Appendable appendable, ElementState nestedES,
			FieldDescriptor nestedF2XO) throws IOException, SIMPLTranslationException
	{
		if (TranslationScope.graphSwitch == GRAPH_SWITCH.ON && alreadyMarshalled(nestedES))
		{
			appendSimplRefId(appendable, nestedES, nestedF2XO);
		}
		else
		{
			nestedES.serializeToAppendable(nestedF2XO, appendable);
		}
	}

	private void appendSimplIdIfRequired(Appendable appendable) throws IOException
	{
		if (TranslationScope.graphSwitch == GRAPH_SWITCH.ON && needsHashCode())
		{
			appendSimplIdAttribute(appendable, this);
		}
	}

	private boolean alreadyMarshalled(ElementState compositeElementState)
	{
		return marshalledObjects.containsKey(compositeElementState.hashCode());
	}

	private void appendSimplRefId(Appendable appendable, ElementState elementState,
			FieldDescriptor compositeElementFD) throws IOException
	{
		compositeElementFD.writeElementStart(appendable);
		appendSimplIdAttributeWithTagName(appendable, SIMPL_REF, elementState);
		appendable.append("/>");
	}

	private void appendSimplIdAttributeWithTagName(Appendable appendable, String tagName,
			ElementState elementState) throws IOException
	{
		appendable.append(' ');
		appendable.append(tagName);
		appendable.append('=');
		appendable.append('"');
		appendable.append(((Integer) elementState.hashCode()).toString());
		appendable.append('"');
	}

	private void appendSimplIdAttribute(Appendable appendable, ElementState elementState)
			throws IOException
	{
		appendSimplIdAttributeWithTagName(appendable, SIMPL_ID, elementState);
	}

	private boolean needsHashCode()
	{
		return needsAttributeHashCode.containsKey(this.hashCode());
	}

	public static ElementState getFromMap(Attributes attributes)
	{
		ElementState unMarshalledObject = null;

		int numAttributes = attributes.getLength();
		for (int i = 0; i < numAttributes; i++)
		{
			final String tag = attributes.getQName(i);
			final String value = attributes.getValue(i);

			if (tag.equals(ElementState.SIMPL_REF))
			{
				unMarshalledObject = unmarshalledObjects.get(value);
			}
		}

		return unMarshalledObject;
	}

	public static void recycleSerializationMappings()
	{
		if (TranslationScope.graphSwitch == GRAPH_SWITCH.ON)
		{
			marshalledObjects.clear();
			visitedElements.clear();
			needsAttributeHashCode.clear();
		}
	}

	public static void recycleDeserializationMappings()
	{
		if (TranslationScope.graphSwitch == GRAPH_SWITCH.ON)
		{
			unmarshalledObjects.clear();
		}
	}
}
