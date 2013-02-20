package simpl.interpretation;

import java.util.LinkedList;
import java.util.List;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.ReflectionTools;

public class CompositeInterpretation implements SimplInterpretation{

	public String refString; 
	public String idString;
	
	public String fieldName;
	
	public String getFieldName()
	{
		return this.fieldName;
	}
	
	public void setFieldName(String name)
	{
		this.fieldName = name;
	}
	
	List<SimplInterpretation> interpretations;
	
	public CompositeInterpretation()
	{
		this.interpretations = new LinkedList<SimplInterpretation>();
	}
	
	public void addInterpretation(SimplInterpretation si)
	{
		this.interpretations.add(si);
	}
	
	public String getIDString()
	{
		return this.idString;
	}
	
	public void setIDString(String ID)
	{
		this.idString = ID;
	}
	
	public String getRefString()
	{
		return this.refString;
	}
	
	public void setRefString(String ref)
	{
		this.refString = ref;
	}

	@Override
	public void resolve(Object context, SimplRefCallbackMap callbackMap, UnderstandingContext understandingContext) throws SIMPLTranslationException {
		Object ourObject = getValue(true, context, callbackMap, understandingContext);
	}
	
	@Override 
	public Object getValue(Object context, SimplRefCallbackMap callbackMap, UnderstandingContext understandingContext) throws SIMPLTranslationException
	{
		return getValue(false, context, callbackMap, understandingContext);
	}
	
	private Object getValue(boolean deferUpdateObject, Object context, SimplRefCallbackMap callbackMap, UnderstandingContext understandingContext) throws SIMPLTranslationException{
		// TODO Auto-generated method stub
		if(this.refString == null)
		{
			ClassDescriptor cd = ClassDescriptors.getClassDescriptor(context.getClass());
			FieldDescriptor fd = cd.fields().by("name").get(this.fieldName);
			ClassDescriptor compositeDescriptor = fd.getFieldClassDescriptor();

			Object compositeObj = compositeDescriptor.getInstance();
			if(this.idString != null)
			{
				if(understandingContext.isIDRegistered(this.idString))
				{
					understandingContext.updateID(this.idString, compositeObj);
				}else{
					understandingContext.registerID(this.idString, compositeObj);
				}
			}

			for(SimplInterpretation si : interpretations)
			{
				si.resolve(compositeObj, callbackMap, understandingContext);
			}

			if(deferUpdateObject)
			{
				updateObject(context, compositeObj);
			}
			
			return compositeObj;
		}
		else
		{
			if(understandingContext.isIDRegistered(this.refString))
			{
				
				Object o = understandingContext.getRegisteredObject(this.refString);
				
				// get the object and set it. :) 
				if(deferUpdateObject)
				{
					updateObject(context, o);
				}
				
				return o;
			}
			else
			{	
				Object value = new Object();
				understandingContext.registerID(this.refString, value);

				final String refID = this.refString;
				final Object finalobject = value;
				callbackMap.insertCallback(new UpdateSimplRefCallback() {

					@Override
					public void resolveUpdate(Object referencedComposite) {
						set(finalobject,referencedComposite);
					}

					private void set(Object finalobject,
							Object referencedComposite) {
						finalobject = referencedComposite;
					}

					@Override
					public String getID() {
						// TODO Auto-generated method stub
						return refID;
					}
				});

				if(deferUpdateObject)
				{
					final Object finalContext = context; 
					
					callbackMap.insertCallback(new UpdateSimplRefCallback() {
						
						@Override
						public void resolveUpdate(Object referencedComposite) {
							updateObject(finalContext, referencedComposite);
						}
						
						@Override
						public String getID() {
							// TODO Auto-generated method stub
							return refID;
						}
					});
				}
				
				return finalobject;
			}
		}	
	}
	
	private void updateObject(Object context, Object value)
	{
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(context.getClass());
		FieldDescriptor fd = cd.fields().by("name").get(this.fieldName);
		try {
			ReflectionTools.setFieldValue(value, fd.getField(), context);
		} catch (SIMPLTranslationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
