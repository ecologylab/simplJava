package simpl.interpretation;

import java.util.LinkedList;
import java.util.List;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;

public class SimplInterpreter {

	public List<ScalarInterpretation> interpretInstance(Object obj)
	{
		
		//ClassDescriptor<?> cd = ClassDescriptors.getClassDescriptor(obj.getClass());
		/*
		for(FieldDescriptor fd : cd.allFieldDescriptors())
		{
			cd.fieldDescriptors.Insert(fd);
		}
		
		System.out.println(cd.fieldDescriptors.Scalars.size());
		
		List<ScalarInterpretation> interps = new LinkedList<ScalarInterpretation>();

		for(FieldDescriptor fd : cd.fieldDescriptors.Scalars)
		{
			ScalarInterpretation si = new ScalarInterpretation(fd.getName(), fd.getValue(obj).toString());
			interps.add(si);
		}
		
		return interps;	*/
		return null;
		
	}
}
