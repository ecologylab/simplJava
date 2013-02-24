package simpl.interpretation;

import java.util.HashMap;
import java.util.Map;

import simpl.core.ISimplTypesScope;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.EnumerationDescriptor;
import simpl.exceptions.SIMPLTranslationException;

public class UnderstandingContext {
	
	ISimplTypesScope contextScope; 
	
	public UnderstandingContext(ISimplTypesScope scope)
	{
		this.graphObjects = new HashMap<String, Object>();
		this.contextScope = scope;
	}
	
	Map<String, Object> graphObjects;
	
	public void registerID(String ID, Object obj)
	{
		this.graphObjects.put(ID, obj);
	}
	
	public void updateID(String ID, Object obj)
	{
		this.graphObjects.put(ID, obj);
	}
	
	public boolean isIDRegistered(String ID)
	{
		return this.graphObjects.containsKey(ID);
	}
	
	public Object getRegisteredObject(String ID)
	{
		return this.graphObjects.get(ID);
	}
	
	public ClassDescriptor getClassDescriptor(String tagName) throws SIMPLTranslationException
	{
		ClassDescriptor desc = this.contextScope.getClassDescriptorByTag(tagName);
		if(desc == null)
		{
			throw new SIMPLTranslationException("Tag name {"+tagName+"} is not in the context scope! Did you initialize your type scope correctly?");
		}
		return desc;
	}
	
	public EnumerationDescriptor getEnumerationDescriptor(String tagName) throws SIMPLTranslationException
	{
		EnumerationDescriptor ed = this.contextScope.getEnumerationDescriptorByTag(tagName);
		if(ed == null)
		{
			throw new SIMPLTranslationException("Tag name {"+tagName+"} is not in the context scope! Did you initialize your type scope correctly?");	
		}
		return ed;
	}
	
}
