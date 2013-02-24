package simpl.interpretation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;

public class ListInterpretation implements SimplInterpretation {

	String fieldName; 
	
	public String getFieldName()
	{
		return this.fieldName;
	}
	
	public void setFieldName(String s)
	{
		this.fieldName = s;
	}
	
	List<SimplInterpretation> interps; 
	
	public ListInterpretation()
	{
		this.interps = new LinkedList<SimplInterpretation>();
	}
	
	public void addItemInterpretation(SimplInterpretation si)
	{
		this.interps.add(si);
	}
	
	@Override
	public void resolve(Object context, SimplRefCallbackMap callbackMap,
			UnderstandingContext understandingContext)
			throws SIMPLTranslationException {
		
		List<Object> items = new ArrayList<Object>();
		for(int i = 0; i < this.interps.size(); i++)
		{
			items.add(i, interps.get(i).getValue(context, callbackMap, understandingContext));
		}
		
		for(Object item : items)
		{
		//	reflectivelyAdd(context, item);
		}
	}

	@Override
	public Object getValue(Object context, SimplRefCallbackMap callbackMap,
			UnderstandingContext understandingContext)
			throws SIMPLTranslationException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
