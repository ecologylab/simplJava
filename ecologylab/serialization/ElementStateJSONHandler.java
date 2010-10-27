package ecologylab.serialization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.types.element.Mappable;

public class ElementStateJSONHandler extends Debug implements ContentHandler, FieldTypes,
		ScalarUnmarshallingContext
{
	final TranslationScope			translationScope;

	ElementState								root;

	ElementState								currentElementState;

	FieldDescriptor							currentFD;

	SIMPLTranslationException		jsonTranslationException;

	ArrayList<FieldDescriptor>	fdStack									= new ArrayList<FieldDescriptor>();

	ParsedURL										purlContext;

	File												fileContext;

	DeserializationHookStrategy	deserializationHookStrategy;

	//int													numOfCollectionElements	= 0;
	
	ArrayList<Integer> 					elementsInCollection = new ArrayList<Integer>();

	public ElementStateJSONHandler(TranslationScope translationScope)
	{
		this.translationScope = translationScope;
	}

	public ElementState parse(CharSequence charSequence)
	{
		try
		{
			
			JSONParser parser = new JSONParser();
			parser.parse(charSequence.toString(), this);
			return root;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean endArray() throws ParseException, IOException
	{
		pop();
		//numOfCollectionElements = 0;
		return true;
	}

	@Override
	public void endJSON() throws ParseException, IOException
	{
		if ((jsonTranslationException == null) && (root != null))
			root.deserializationPostHook();

		ElementState.recycleDeserializationMappings();
	}

	@Override
	public boolean endObject() throws ParseException, IOException
	{

		return true;
	}

	@Override
	public boolean endObjectEntry() throws ParseException, IOException
	{
		if (jsonTranslationException != null)
			return false;

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

		return true;
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
				this.jsonTranslationException = e;
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

	void printStack(String msg)
	{
		currentElementState.debug("Stack -- " + msg + "\t[" + this.currentElementState + "]");
		for (FieldDescriptor thatFD : fdStack)
		{
			println(thatFD.getTagName() + " - 0x" + Integer.toHexString(thatFD.getType()));
		}
		println("");
	}

	StringBuilder	currentTextValue	= new StringBuilder(1024);

	@Override
	public boolean primitive(Object value) throws ParseException, IOException
	{
		if (jsonTranslationException != null)
			return true;

		if (currentFD != null)
		{
			int n2joType = currentFD.getType();
			switch (n2joType)
			{
			case SCALAR:
			case COLLECTION_SCALAR:
				currentTextValue.append(value.toString());
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

		return true;
	}

	@Override
	public boolean startArray() throws ParseException, IOException
	{
		push(0);
		return true;
	}

	@Override
	public void startJSON() throws ParseException, IOException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean startObject() throws ParseException, IOException
	{
		//hack to deal with json arrays with list tag outside the array.
		if (currentFD != null)
			if (currentFD.isCollection() && !currentFD.isPolymorphic())
			{
				if (top() != 0)
				{
					if (currentFD.isWrapped())
					{
						endObjectEntry();
						startObjectEntry(currentFD.getCollectionOrMapTagName());
					}
					else
					{
						FieldDescriptor lastFD = currentFD;
						endObjectEntry();
						startObjectEntry(lastFD.getCollectionOrMapTagName());
					}
				}
				incrementTop();
				//numOfCollectionElements++;
			}

		return true;
	}

	@Override
	public boolean startObjectEntry(String key) throws ParseException, IOException
	{
		if (jsonTranslationException != null)
			return false;

		FieldDescriptor childFD = null;
		final boolean isRoot = (root == null);
		if (isRoot)
		{ // form the root ElementState!
			ClassDescriptor rootClassDescriptor = translationScope.getClassDescriptorByTag(key);
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
						this.jsonTranslationException = new RootElementException(key, translationScope);
						return false;
					}
				}
				catch (SIMPLTranslationException e)
				{
					jsonTranslationException = e;
				}
			}
			else
			{
				// else, we dont translate this element; we ignore it.
				String message = "JSON Translation WARNING: Cant find class object for Root JSON element <"
						+ key + ">: Ignored. ";
				println(message);
				jsonTranslationException = new SIMPLTranslationException(message);
				return false;
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
							.getFieldDescriptorByTag(key, translationScope, currentES);
			if (childFD == null)
			{
				childFD = makeIgnoredFieldDescriptor(key, currentClassDescriptor);
			}
		}
		this.currentFD = childFD;
		// TODO? -- do we need to avoid this if null from an exception in translating root?
		pushFD(childFD);
		// printStack("After push");

		if (isRoot)
			return true;

		ElementState currentElementState = this.currentElementState;
		ElementState childES = null;
		try
		{
			switch (childFD.getType())
			{
			case COMPOSITE_ELEMENT:
				childES = childFD.constructChildElementState(currentElementState, key);

				if (childES == null)
				{
					childFD = makeIgnoredFieldDescriptor(key, currentClassDescriptor());
				}
				else
					childFD.setFieldToComposite(currentElementState, childES);
				
				// maybe we
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
					ElementState childElement = childFD.constructChildElementState(currentElementState, key);
					childES = childElement;

					if (childES == null)
					{
						childFD = makeIgnoredFieldDescriptor(key, currentClassDescriptor());
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
					ElementState childElement = childFD.constructChildElementState(currentElementState, key);

					childES = childElement;
					if (childES == null)
					{
						this.currentFD = makeIgnoredFieldDescriptor(key, currentClassDescriptor());
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
			this.jsonTranslationException = e;
		}

		return true;
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
	
	public Integer pop()
	{
		int num = 0;
		
		if(this.elementsInCollection.size() > 0)
		{
			num = this.elementsInCollection.get(elementsInCollection.size() - 1);
			this.elementsInCollection.remove(elementsInCollection.size() - 1);
		}
		return num;
	}
	
	public void push(Integer num)
	{
		this.elementsInCollection.add(num);
	}
	
	public int top()
	{
		int num = 0;
		
		if(this.elementsInCollection.size() > 0)
		{
			num = this.elementsInCollection.get(elementsInCollection.size() - 1);
		}
		return num;
	}
	
	public void incrementTop()
	{
		int num = pop();
		push(++num);
	}
}
