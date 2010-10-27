package ecologylab.serialization;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.tlv.TLVEvents;
import ecologylab.serialization.tlv.TLVParser;
import ecologylab.serialization.types.element.Mappable;

public class ElementStateTLVHandler extends Debug implements TLVEvents, FieldTypes,
		ScalarUnmarshallingContext
{
	TranslationScope									translationScope;

	TLVParser													tlvParser								= null;

	ElementState											root;

	ElementState											currentElementState;

	FieldDescriptor										currentFD;

	private SIMPLTranslationException	tlvTranslationException;

	ArrayList<FieldDescriptor>				fdStack									= new ArrayList<FieldDescriptor>();

	ParsedURL													purlContext;

	File															fileContext;

	DeserializationHookStrategy				deserializationHookStrategy;


	public ElementStateTLVHandler(TranslationScope translationScope)
	{
		this.translationScope = translationScope;
		tlvParser = new TLVParser(this, translationScope);
	}

	public ElementState parse(CharSequence charSequence)
	{
		return parse(charSequence.toString().getBytes());
	}

	public ElementState parse(byte[] byteArray)
	{
		tlvParser.parse(byteArray);
		return root;
	}

	@Override
	public void endObject(String objectName)
	{
		if (tlvTranslationException != null)
			return;

		FieldDescriptor currentFD = this.currentFD;
		final int curentFdType = currentFD.getType();

		ElementState currentES = this.currentElementState;
		processPendingScalar(curentFdType, currentES);

		final ElementState parentES = currentES.parent;

		switch (curentFdType)
		// every good push deserves a pop :-) (and othertimes, not!)
		{
		case MAP_ELEMENT:
			if (currentES instanceof Mappable)
			{
				final Object key = ((Mappable) currentES).key();
				Map map = (Map) currentFD.automaticLazyGetCollectionOrMap(parentES);
				// Map map = currentFD.getMap(parentES);
				map.put(key, currentES);
			}
		case COMPOSITE_ELEMENT:
		case COLLECTION_ELEMENT:
		case NAME_SPACE_NESTED_ELEMENT:
			if (parentES != null)
				parentES.createChildHook(currentES);
			else
				debug("cool - post ns element");
			currentES.deserializationPostHook();
			if (deserializationHookStrategy != null)
				deserializationHookStrategy.deserializationPostHook(currentES, currentFD);
			this.currentElementState = currentES.parent;
		case NAME_SPACE_SCALAR:
			// case WRAPPER:
			this.currentElementState = parentES; // restore context!
			break;
		default:
			break;
		}
		// end of the Namespace object, so we gotta pop it off, too.
		// if (curentN2JOType == NAME_SPACE_NESTED_ELEMENT)
		// this.currentElementState = this.currentElementState.parent;
		popAndPeekFD();

		// if (this.startElementPushed) // every good push deserves a pop :-) (and othertimes, not!)
	}

	@Override
	public void endTLV()
	{
		if ((tlvTranslationException == null) && (root != null))
			root.deserializationPostHook();

		ElementState.recycleDeserializationMappings();

	}

	StringBuilder	currentTextValue	= new StringBuilder(1024);

	@Override
	public void primitive(String value)
	{
		if (tlvTranslationException != null)
			return;

		if (currentFD != null)
		{
			int n2joType = currentFD.getType();
			switch (n2joType)
			{
			case SCALAR:
			case COLLECTION_SCALAR:
				currentTextValue.append(value);
				processPendingScalar(n2joType, currentElementState);
				// TODO -- unmarshall to set field with scalar type
				break;
			case COMPOSITE_ELEMENT:
			case COLLECTION_ELEMENT:
			case PSEUDO_FIELD_DESCRIPTOR:
				// optimizations in currentN2JO are for its parent (they were in scope when it was
				// constructed)
				// so we get the optimizations we need from the currentElementState
				if (currentElementState.classDescriptor().hasScalarFD())
					currentTextValue.append(value.toString());
				break;
			default:
				// TODO ?! can we dump characters in this case, or should we append to textNode?!
				// currentElementState.appendLeafXML(buffy, leafElementName, leafValue, needsEscaping,
				// isCDATA)
				break;
			}
		}

	}

	@Override
	public void startObject(String objectName)
	{
		if (tlvTranslationException != null)
			return;

		FieldDescriptor childFD = null;
		final boolean isRoot = (root == null);
		if (isRoot)
		{ // form the root ElementState!
			ClassDescriptor rootClassDescriptor = translationScope.getClassDescriptorByTag(objectName);
			if (rootClassDescriptor != null)
			{
				try
				{
					ElementState root = null;
					if (root == null)
						root = rootClassDescriptor.getInstance();
					if (root != null)
					{
						root.setupRoot();
						setRoot(root);
						if (deserializationHookStrategy != null)
							deserializationHookStrategy.deserializationPreHook(root, null);

						childFD = rootClassDescriptor.pseudoFieldDescriptor();
					}
					else
					{
						this.tlvTranslationException = new RootElementException(objectName, translationScope);
						return;
					}
				}
				catch (SIMPLTranslationException e)
				{
					tlvTranslationException = e;
				}
			}
			else
			{
				// else, we dont translate this element; we ignore it.
				String message = "TLV Translation WARNING: Cant find class object for Root JSON element <"
						+ objectName + ">: Ignored. ";
				println(message);
				tlvTranslationException = new SIMPLTranslationException(message);
				return;
			}
		}
		else
		// not root
		{
			final int currentType = currentFD.getType();
			ElementState currentES = this.currentElementState;
			// if there is a pending text node, assign it somehow!
			processPendingScalar(currentType, currentES);

			ClassDescriptor currentClassDescriptor = currentClassDescriptor();
			childFD = (currentFD != null) && (currentType == IGNORED_ELEMENT) ?
			// new NodeToJavaOptimizations(tagName) : // (nice for debugging; slows us down)
			FieldDescriptor.IGNORED_ELEMENT_FIELD_DESCRIPTOR
					: (currentType == WRAPPER) ? currentFD.getWrappedFD() : currentClassDescriptor
							.getFieldDescriptorByTag(objectName, translationScope, currentES);
			if (childFD == null)
			{
				childFD = makeIgnoredFieldDescriptor(objectName, currentClassDescriptor);
			}
		}
		this.currentFD = childFD;
		// TODO? -- do we need to avoid this if null from an exception in translating root?
		pushFD(childFD);
		// printStack("After push");

		if (isRoot)
			return;

		ElementState currentElementState = this.currentElementState;
		ElementState childES = null;
		try
		{
			switch (childFD.getType())
			{
			case COMPOSITE_ELEMENT:
				childES = childFD.constructChildElementState(currentElementState, objectName);

				if (childES == null)
				{
					childFD = makeIgnoredFieldDescriptor(objectName, currentClassDescriptor());
				}
				else
					childFD.setFieldToComposite(currentElementState, childES); // maybe we
				// should do
				// this on close
				// element
				break;
			case SCALAR:
				// wait for characters to set scalar field
				// activeN2JO.setScalarFieldWithLeafNode(activeES, childNode);
				break;
			case COLLECTION_ELEMENT:

				Collection collection = (Collection) childFD
						.automaticLazyGetCollectionOrMap(currentElementState);
				if (collection != null)
				{
					ElementState childElement = childFD.constructChildElementState(currentElementState,
							objectName);
					childES = childElement;

					if (childES == null)
					{
						childFD = makeIgnoredFieldDescriptor(objectName, currentClassDescriptor());
					}

					collection.add(childES);
				}
				// activeNJO.formElementAndAddToCollection(activeES, childNode);
				break;
			case COLLECTION_SCALAR:
				// wait for characters to create scalar reference type and add to collection
				// activeN2JO.addLeafNodeToCollection(activeES, childNode);
				break;
			case MAP_ELEMENT:
				Map map = (Map) childFD.automaticLazyGetCollectionOrMap(currentElementState);
				if (map != null)
				{
					ElementState childElement = childFD.constructChildElementState(currentElementState,
							objectName);

					childES = childElement;
					if (childES == null)
					{
						this.currentFD = makeIgnoredFieldDescriptor(objectName, currentClassDescriptor());
					}
				}
				// Map map = activeFieldDescriptor.getMap(currentElementState);
				// if (map != null)
				// {
				// childES = activeFieldDescriptor.constructChildElementState(currentElementState, tagName);
				// }
				break;
			case IGNORED_ELEMENT:
				// should get a set of Optimizations for this, to represent its subfields
			case BAD_FIELD:
			case WRAPPER:
			default:
				break;

			}
			if (childES != null)
			{
				// fill in its attributes
				if (deserializationHookStrategy != null)
					deserializationHookStrategy.deserializationPreHook(childES, childFD);

				this.currentElementState = childES; // childES.parent = old currentElementState
				this.currentFD = childFD;
			}
		}
		catch (SIMPLTranslationException e)
		{
			this.tlvTranslationException = e;
		}

	}

	private void setRoot(ElementState root)
	{
		this.root = root;
		this.currentElementState = root;
	}

	private ClassDescriptor currentClassDescriptor()
	{
		if (currentElementState != null)
			return this.currentElementState.classDescriptor();
		else
			return null;
	}

	private void pushFD(FieldDescriptor fd)
	{
		this.fdStack.add(fd);
	}

	private void popAndPeekFD()
	{
		ArrayList<FieldDescriptor> stack = this.fdStack;
		int last = stack.size() - 1;
		if (last >= 0)
		{
			FieldDescriptor result = stack.remove(last--);
			if (last >= 0)
				result = stack.get(last);
			this.currentFD = result;
			// printStack("After Pop");
		}
	}

	/**
	 * Assign pending value to a @simpl_scalar
	 * 
	 * @param curentN2JOType
	 * @param currentES
	 */
	private void processPendingScalar(final int curentN2JOType, ElementState currentES)
	{
		final int length = currentTextValue.length();
		if (length > 0)
		{
			try
			{
				switch (curentN2JOType)
				{
				case NAME_SPACE_SCALAR:
				case SCALAR:
					// TODO -- unmarshall to set field with scalar type
					// copy from the StringBuilder
					String value = stringToDeserializeAsScalar(length);
					currentFD.setFieldToScalar(currentES, value, this);
					break;
				case COLLECTION_SCALAR:
					value = stringToDeserializeAsScalar(length);
					currentFD.addLeafNodeToCollection(currentES, value, this);
					break;
				case COMPOSITE_ELEMENT:
				case COLLECTION_ELEMENT:
				case PSEUDO_FIELD_DESCRIPTOR:
					// TODO - is this code used????
					// optimizations in currentN2JO are for its parent (they were in scope when it was
					// constructed)
					// so we get the optimizations we need from the currentElementState
					// FIXME -- implement this!!!
					FieldDescriptor scalarTextFD = currentElementState.classDescriptor().getScalarTextFD();
					if (scalarTextFD != null)
					{
						value = stringToDeserializeAsScalar(length);
						scalarTextFD.setFieldToScalar(currentES, value, this);
					}
					break;
				default:
					break;
				}
			}
			catch (SIMPLTranslationException e)
			{
				this.tlvTranslationException = e;
			}

			currentTextValue.setLength(0);
		}
	}

	private FieldDescriptor makeIgnoredFieldDescriptor(String key,
			ClassDescriptor currentClassDescriptor)
	{
		FieldDescriptor activeFieldDescriptor;
		currentClassDescriptor.warning(" Ignoring key <" + key + ">");
		activeFieldDescriptor = new FieldDescriptor(key); // TODO -- should we record
		// declaringClass in here??!
		if (activeFieldDescriptor.getTagName() != null)
			currentClassDescriptor.addFieldDescriptorMapping(activeFieldDescriptor);
		return activeFieldDescriptor;
	}

	/**
	 * Get the String that will be marshalled into the value with a ScalarType, using the
	 * currentTextValue state variable from the parse, and the length parameter. If appropriate, use
	 * the currentFD to perform a regex filter on the value before passing it to the appropriate
	 * scalar marshalling and field or collection element setter.
	 * 
	 * @param length
	 * @return
	 */
	private String stringToDeserializeAsScalar(final int length)
	{
		String result = new String(currentTextValue.substring(0, length));
		if (translationScope.isPerformFilters())
			result = currentFD.filterValue(result);
		return result;
	}

	@Override
	public void startTLV()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @return the root
	 */
	public ElementState root()
	{
		return root;
	}

	public File fileContext()
	{
		return (fileContext != null) ? fileContext : (purlContext != null) ? purlContext.file() : null;
	}

	public ParsedURL purlContext()
	{
		return purlContext;
	}
	//	
	// @Override
	// public void endObject(String objectName)
	// {
	// System.out.println("END   of object : " + objectName);
	//		
	// }
	//
	// @Override
	// public void endTLV()
	// {
	// System.out.println("end of tlv message ");
	//		
	// }
	//
	// @Override
	// public void primitive(String value)
	// {
	// System.out.println("value of object : " + value);
	//		
	// }
	//
	// @Override
	// public void startObject(String objectName)
	// {
	// System.out.println("START of object : " + objectName);
	//		
	// }
	//
	// @Override
	// public void startTLV()
	// {
	// System.out.println("start of tlv message ");
	//		
	// }
	//
	// @Override
	// public File fileContext()
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public ParsedURL purlContext()
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }
}