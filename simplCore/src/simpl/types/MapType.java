package simpl.types;

import java.util.HashMap;

import simpl.annotations.dbal.simpl_scalar;
import simpl.interpretation.MapEntryEvaluation;

public class MapType {
	
	@simpl_scalar
	String mapClassName;
	
	Class<?> mapClass;
	
	public MapType(Class<?> mapClass)
	{
		// we just need the map class to make the map type >:3 
		this.mapClassName = mapClass.getName();	
	}
	
	private Class<?> getMapClass()
	{
		if(this.mapClass == null)
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
		
		return this.mapClass;
	}
	
	public Object getInstance()
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
}
