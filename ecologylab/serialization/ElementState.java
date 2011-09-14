package ecologylab.serialization;

import java.util.HashMap;
import java.util.Stack;

import ecologylab.generic.Debug;
import ecologylab.serialization.deserializers.ISimplDeserializationPre;
import ecologylab.serialization.deserializers.ISimplDeserializatonPost;
import ecologylab.serialization.serializers.ISimplSerializationPost;
import ecologylab.serialization.serializers.ISimplSerializationPre;

public class ElementState<PES extends ElementState> extends Debug implements FieldTypes,
		XMLTranslationExceptionTypes, ISimplSerializationPre, ISimplSerializationPost,
		ISimplDeserializationPre, ISimplDeserializatonPost
{

	private boolean													isRoot										= false;

	/**
	 * Link for a DOM tree. should be removed
	 */
	transient PES														parent;

	/**
	 * to handle objects with multiple parents this variable helps keep track of parents in
	 * deserializing graph
	 */
	Stack<PES>															parents										= null;

	/**
	 * Use for resolving getElementById()
	 */
	transient HashMap<String, ElementState>	elementByIdMap;

	transient HashMap<String, ElementState>	nestedNameSpaces;

	static protected final int							ESTIMATE_CHARS_PER_FIELD	= 80;

	/**
	 * Just-in time look-up tables to make translation be efficient. Allocated on a per class basis.
	 */
	transient private ClassDescriptor				classDescriptor;
	
	/**
	 * Construct. Create a link to a root optimizations object.
	 */
	public ElementState()
	{
	}

	public TranslationContext createGraphContext() throws SIMPLTranslationException
	{
		TranslationContext graphContext = new TranslationContext();
		graphContext.resolveGraph(this);
		isRoot = true;
		return graphContext;
	}

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
	public PES parent()
	{
		// return (parent != null) ? parent :
		// (parents != null && !parents.empty()) ? parents.firstElement() :
		// null;
		return parent;
	}

	/**
	 * Set the parent of this, to create the tree structure.
	 * 
	 * @param parent
	 */
	public void setParent(PES parent)
	{
		this.parent = parent;
	}

	public ElementState getRoot()
	{
		ElementState parent = parent();
		return parent == null ? this : parent.getRoot();
	}

	/**
	 * Perform custom processing on the newly created child node, just before it is added to this.
	 * <p/>
	 * This is part of depth-first traversal during translateFromXML().
	 * <p/>
	 * This, the default implementation, does nothing. Sub-classes may wish to override.
	 * 
	 * @param foo
	 */
	protected void createChildHook(ElementState foo)
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
	public void setupInParent(ElementState newParent)
	{
		this.elementByIdMap = newParent.elementByIdMap;
		this.manageParents(newParent);
	}

	private void manageParents(ElementState parentES)
	{
		PES parentPES = (PES) parentES;
		if (this.parent == null)
		{
			this.parent = parentPES;
		}
		else
		{
			if (this.parents == null)
			{
				this.parents = new Stack<PES>();
				this.parents.push(this.parent);
				this.parents.push(parentPES);
			}
			else
			{
				this.parents.push(parentPES);
			}
		}
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

	@Override
	public void serializationPostHook(TranslationContext translationContext)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void serializationPreHook(TranslationContext translationContext)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void deserializationPreHook(TranslationContext translationContext)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void deserializationPostHook(TranslationContext translationContext)
	{
		// TODO Auto-generated method stub

	}
}
