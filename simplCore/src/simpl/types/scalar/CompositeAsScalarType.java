package simpl.types.scalar;

import java.util.Collection;
import java.util.LinkedList;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.indexers.FieldDescriptorIndexer;
import simpl.exceptions.SIMPLTranslationException;
import simpl.types.ScalarType;

public class CompositeAsScalarType extends ScalarType {

	public String composite_tag_name; 
	
	private final Object defaultValue;
	
	private final ClassDescriptor compositeClass; 
	private final Collection<Class<?>> supportedType; 
	
	private final FieldDescriptor scalarField;
	private final ScalarType scalarType;
	
	public CompositeAsScalarType(ClassDescriptor compositeClass)
	{
		this.compositeClass = compositeClass;
		defaultValue = compositeClass.getInstance();
		
		if(this.compositeClass.fields().CompositesAsScalars.size() == 1)
		{
			this.scalarField = compositeClass.fields().CompositesAsScalars.iterator().next();
			// tood; add all items. :) 
			
			if(this.scalarField.getScalarType() == null)
			{
				throw new RuntimeException("The type of field ["+this.scalarField.getName()+"] in ["+ compositeClass.getName()+ "] must be a scalar type!");
			}else{
				this.scalarType = this.scalarField.getScalarType();
				
				Collection<Class<?>> support = new LinkedList<Class<?>>();
				support.add(compositeClass.getJavaClass());
				this.supportedType = support;
				Register();
			}
		}
		else
		{
			throw new RuntimeException("Invalid scalar as composite class... has too many @simpl_composite_as_scalar fields! " + compositeClass.getName());
		}
		assert(this.supportedType != null);
		assert(this.supportedType.size() == 1);
	}
	
	@Override
	public String marshal(Object object) throws SIMPLTranslationException {
		return scalarType.getFieldString(this.scalarField.getField(), object);
	}

	@Override
	public Object unmarshal(String string) throws SIMPLTranslationException {
		// TODO Auto-generated method stub
		Object obj = compositeClass.getInstance();
		
		scalarType.setFieldValue(string, scalarField.getField(), obj);
		
		return obj;
	}

	@Override
	public Object getDefaultValue() {
		// TODO Auto-generated method stub
		return defaultValue;
	}
	
	@Override 
	public String getSimpleName()
	{
		return this.compositeClass.getSimpleName();
	}
	
	@Override
	public String getTagName()
	{
		return getSimpleName();
	}
	
	@Override
	public Collection<Class<?>> getSupportedTypes()
	{
		return this.supportedType;
	}
}
