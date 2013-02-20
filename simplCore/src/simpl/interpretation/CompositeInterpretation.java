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
		// TODO Auto-generated method stub
		if(this.refString == null)
		{
			ClassDescriptor cd = ClassDescriptors.getClassDescriptor(context.getClass());
			FieldDescriptor fd = cd.fields().by("name").get(this.fieldName);
			ClassDescriptor compositeDescriptor = fd.getFieldClassDescriptor();
			
			Object compositeObj = compositeDescriptor.getInstance();
			if(this.idString != null)
			{
				understandingContext.registerID(this.idString, compositeObj);
			}
			
			for(SimplInterpretation si : interpretations)
			{
				si.resolve(compositeObj, callbackMap, understandingContext);
			}
			
			updateObject(context, compositeObj);
		}
		else
		{
			if(understandingContext.isIDRegistered(this.refString))
			{
				// get the object and set it. :) 
				this.updateObject(context, understandingContext.getRegisteredObject(this.refString));
			}
			else
			{	
				final String refID = this.refString;
				final Object finalcontext = context;;
				callbackMap.insertCallback(new UpdateSimplRefCallback() {
					
					@Override
					public void resolveUpdate(Object referencedComposite) {
						updateObject(finalcontext, referencedComposite);
					}
					
					@Override
					public String getID() {
						// TODO Auto-generated method stub
						return refID;
					}
				});
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
