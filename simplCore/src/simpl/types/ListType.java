package simpl.types;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import simpl.descriptions.EnumerationDescriptor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.ReflectionTools;

public class ListType {

	String listTypeName;
	Class<?> listType;
	
	String declaredListTypeName;
	
	public String getDeclaredListTypeName() {
		return declaredListTypeName;
	}

	public void setDeclaredListTypeName(String declaredListTypeName) {
		this.declaredListTypeName = declaredListTypeName;
	}

	public Class<?> getDeclaredListType() {
		return declaredListType;
	}

	public void setDeclaredListType(Class<?> declaredListType) {
		this.declaredListType = declaredListType;
	}

	Class<?> declaredListType;
	
	
	
	public Class<?> getListType() {
		return listType;
	}

	String listItemTypeName;
	Class<?> listItemType;
	
	public Class<?> getListItemType() {
		return listItemType;
	}

	public void setListItemType(Class<?> listItemType) {
		this.listItemType = listItemType;
	}

	public ListType()
	{
		this(ArrayList.class);
	}
	
	public ListType(Class<?> listType)
	{
		this.setDeclaredListType(listType);
	}
	
	public Collection<?> createInstance()
	{
		try
		{
			return (Collection<?>)this.listType.newInstance();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void setListType(Class<?> listType)
	{
		this.listType = listType;
		this.listTypeName = listType.getName();
	}
	
	public void addTo(Object context, Field f, Object value) throws SIMPLTranslationException
	{
		Collection l = (Collection<?>)ReflectionTools.getFieldValue(f, context);
		if(l == null)
		{
			l = this.createInstance();// Reasonable default if not initialized. 
			ReflectionTools.setFieldValue(l, f, context);
		}
		
		l.add(value);
	}
	
}
