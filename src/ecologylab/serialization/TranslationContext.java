package ecologylab.serialization;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import ecologylab.collections.MultiMap;
import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.SimplTypesScope.GRAPH_SWITCH;

/**
 * 
 * @author nabeelshahzad
 * 
 */
public class TranslationContext extends Debug implements ScalarUnmarshallingContext
{

	public static final String				SIMPL_NAMESPACE	= " xmlns:simpl=\"http://ecologylab.net/research/simplGuide/serialization/index.html\"";

	public static final String				SIMPL						= "simpl";

	public static final String				REF							= "ref";

	public static final String				ID							= "id";

	public static final String				SIMPL_ID				= "simpl:id";

	public static final String				SIMPL_REF				= "simpl:ref";

	public static final String				JSON_SIMPL_ID		= "simpl.id";

	public static final String				JSON_SIMPL_REF	= "simpl.ref";

	private MultiMap<Integer, Object>	marshalledObjects;

	private MultiMap<Integer, Object>	visitedElements;

	private MultiMap<Integer, Object>	needsAttributeHashCode;

	private HashMap<String, Object>		unmarshalledObjects;

	protected ParsedURL								baseDirPurl;

	protected File										baseDirFile;

	protected String									delimiter				= ",";
	
	/**
	 * 
	 */
	public TranslationContext()
	{

	}

	/**
	 * 
	 * @param fileDirContext
	 */
	public TranslationContext(File fileDirContext)
	{
		if (fileDirContext != null)
			setBaseDirFile(fileDirContext);
	}

	/**
	 * 
	 * @param purlContext
	 */
	public TranslationContext(ParsedURL purlContext)
	{
		this.baseDirPurl = purlContext;
		if (purlContext.isFile())
			this.baseDirFile = purlContext.file();
	}

	/**
	 * 
	 * @param fileDirContext
	 */
	public void setBaseDirFile(File fileDirContext)
	{
		this.baseDirFile = fileDirContext;
		this.baseDirPurl = new ParsedURL(fileDirContext);
	}

	public void initializeMultiMaps()
	{
		marshalledObjects = new MultiMap<Integer, Object>();
		visitedElements = new MultiMap<Integer, Object>();
		needsAttributeHashCode = new MultiMap<Integer, Object>();
		unmarshalledObjects = new HashMap<String, Object>();
	}

	/**
	 * 
	 * @param value
	 * @param elementState
	 */
	public void markAsUnmarshalled(String value, Object elementState)
	{
		if (unmarshalledObjects == null)
			initializeMultiMaps();
		this.unmarshalledObjects.put(value, elementState);
	}

	public void resolveGraph(Object object)
	{
		if (visitedElements == null)
			initializeMultiMaps();

		resolveGraphRecursive(object);
	}

	/**
	 * 
	 * @param elementState
	 */
	public void resolveGraphRecursive(Object elementState)
	{
		if (SimplTypesScope.graphSwitch == GRAPH_SWITCH.ON)
		{
			// this.visitedElements.put(System.identityHashCode(elementState), elementState);
			this.visitedElements.put(elementState.hashCode(), elementState);

			ClassDescriptor.getClassDescriptor(elementState);

			ArrayList<? extends FieldDescriptor> elementFieldDescriptors = ClassDescriptor
					.getClassDescriptor(elementState).elementFieldDescriptors();

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
				catch (Exception e)
				{
					System.out.println(e);
				}
				// ignore null reference objects
				if (thatReferenceObject == null)
					continue;
				
				FieldType childFdType = elementFieldDescriptor.getType();

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
						if (next instanceof Object)
						{
							Object compositeElement = next;
							
							if (this.alreadyVisited(compositeElement))
							{
								// this.needsAttributeHashCode.put(System.identityHashCode(compositeElement),
								// compositeElement);
								this.needsAttributeHashCode.put(compositeElement.hashCode(), compositeElement);
							}
							else
							{
								this.resolveGraph(compositeElement);
							}
						}
					}
				}
				else if (thatReferenceObject instanceof Object)
				{
					Object compositeElement = thatReferenceObject;

					if (this.alreadyVisited(compositeElement))
					{
						// this.needsAttributeHashCode.put(System.identityHashCode(compositeElement),
						// compositeElement);
						this.needsAttributeHashCode.put(compositeElement.hashCode(), compositeElement);
					}
					else
					{
						resolveGraph(compositeElement);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param elementState
	 * @return
	 */
	public boolean alreadyVisited(Object elementState)
	{
		if (unmarshalledObjects == null)
			initializeMultiMaps();

		return this.visitedElements.contains(elementState.hashCode(), elementState) != -1;
	}

	/**
	 * 
	 * @param object
	 */
	public void mapObject(Object object)
	{
		if (SimplTypesScope.graphSwitch == GRAPH_SWITCH.ON)
		{
			if (object != null)
				this.marshalledObjects.put(object.hashCode(), object);
		}
	}

	/**
	 * 
	 * @param compositeObject
	 * @return
	 */
	public boolean alreadyMarshalled(Object compositeObject)
	{
		if (compositeObject == null)
			return false;

		return this.marshalledObjects.contains(compositeObject.hashCode(), compositeObject) != -1;
	}

	/**
	 * 
	 * @param elementState
	 * @return
	 */
	public boolean needsHashCode(Object elementState)
	{
		return this.needsAttributeHashCode.contains(elementState.hashCode(), elementState) != -1;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isGraph()
	{
		return this.needsAttributeHashCode.size() > 0;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public Object getFromMap(String value)
	{

		return this.unmarshalledObjects.get(value);
	}

	/**
	 * 
	 */
	@Override
	public ParsedURL purlContext()
	{
		return (baseDirPurl != null) ? baseDirPurl : (baseDirFile != null) ? new ParsedURL(baseDirFile)
				: null;
	}

	public String getSimplId(Object object)
	{
		Integer objectHashCode = object.hashCode();
		Integer orderedIndex = marshalledObjects.contains(objectHashCode, object);

		if (orderedIndex > 0)
			return objectHashCode.toString() + "," + orderedIndex.toString();
		else
			return objectHashCode.toString();
	}

	/**
	 * 
	 */
	@Override
	public File fileContext()
	{
		return (baseDirFile != null) ? baseDirFile : (baseDirPurl != null) ? baseDirPurl.file() : null;
	}

	/**
	 * 
	 * @return
	 */
	public String getDelimiter()
	{
		return delimiter;
	}

	void clean()
	{
		if (marshalledObjects != null)
			marshalledObjects.clear();
		if (visitedElements != null)
			visitedElements.clear();
		if (needsAttributeHashCode != null)
			needsAttributeHashCode.clear();
		if (needsAttributeHashCode != null)
			unmarshalledObjects.clear();

		baseDirPurl = null;
		baseDirFile = null;
		delimiter = ",";
	}

}