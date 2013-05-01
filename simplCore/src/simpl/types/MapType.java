package simpl.types;

import java.util.HashMap;

import simpl.annotations.dbal.simpl_scalar;
import simpl.interpretation.MapEntryEvaluation;

public class MapType {
	
	@simpl_scalar
	String mapClassName;
	
	Class<?> mapType;
	String mapTypeName;
	
	Class<?> declaredMapType;
	String declaredMapTypeName;
	
	Class<?> keyType;
	public Class<?> getKeyType() {
		return keyType;
	}

	public void setKeyType(Class<?> keyType) {
		this.keyType = keyType;
	}

	public Class<?> getValueType() {
		return valueType;
	}

	public void setValueType(Class<?> valueType) {
		this.valueType = valueType;
	}

	Class<?> valueType;
	
	public Class<?> getDeclaredMapType() {
		return declaredMapType;
	}

	public void setDeclaredMapType(Class<?> declaredMapType) {
		this.declaredMapType = declaredMapType;
	}

	public MapType(Class<?> mapClass)
	{
		// we just need the map class to make the map type >:3 
		this.declaredMapTypeName = mapClass.getName();	
		this.declaredMapType = mapClass;
	}
	
	//TODO: fix caching issues. 
	public Class<?> getMapType()
	{
		return this.mapType;
		
	}
	
	private Class<?> getMapClass()
	{
		if(this.mapType == null)
		{
			try
			{	
				return Class.forName(this.mapClassName);
			}
			catch(Exception e)
			{
				throw new RuntimeException(e);
				// TODO: This is a cardinal sin; fix this in the future with some standard rehydration code for class types??
			}
		}
		
		return this.mapType;
	}
	
	public Object createInstance()
	{
		try 
		{
			return this.getMapClass().newInstance();
		} 
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public void setMapType(Class<?> instancetype) {
		// TODO Auto-generated method stub
		this.mapType = instancetype;
	}
}
