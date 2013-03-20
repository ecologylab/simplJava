package simpl.interpretation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.FieldType;
import simpl.exceptions.SIMPLTranslationException;
import simpl.types.ScalarType;
import simpl.types.TypeRegistry;

public class SimplInterpreter {	
	
	private InterpretationContext thisInterpContext; 
	
	public SimplInterpreter()
	{
		this.thisInterpContext= new InterpretationContext(this);
		this.initializeInterpreters();
	}
	
	static CompositeInterpretation compositeInterpReference = new CompositeInterpretation("");
	
	public SimplInterpretation interpretInstance(Object obj) throws SIMPLTranslationException
	{
		return selectInterpretationForObject(obj).interpretObject(obj, this.thisInterpContext);
	}
	
	private Map<FieldType, SimplInterpretation> interpreterByFieldType = new HashMap<FieldType, SimplInterpretation>();
	private List<InterpreterInstanceSelector> interpreterBySelection = new ArrayList<InterpreterInstanceSelector>(4);
	
	private SimplInterpretation selectInterpretationForObject(Object obj) throws SIMPLTranslationException
	{
		int size = interpreterBySelection.size();
		for(int i = 0; i < size; i++)
		{
			if(interpreterBySelection.get(i).selectInstance(obj))
			{
				return interpreterBySelection.get(i).obtainInterpreter();
			}
		}
		throw new SIMPLTranslationException("No interpreter matches the given object type! " + obj.getClass().getName());
	}
	
	private void initializeInterpreters()
	{
		// Register fieldtype level mappings 
		interpreterByFieldType.put(FieldType.SCALAR, new ScalarInterpretation());
		interpreterByFieldType.put(FieldType.COMPOSITE_ELEMENT, new CompositeInterpretation(""));
		interpreterByFieldType.put(FieldType.COLLECTION_ELEMENT, new ListInterpretation());
		interpreterByFieldType.put(FieldType.COLLECTION_SCALAR, new ListInterpretation());
		interpreterByFieldType.put(FieldType.MAP_SCALAR, new MapInterpretation());
		interpreterByFieldType.put(FieldType.MAP_ELEMENT, new MapInterpretation());
		
		// Register type level mappings
		interpreterBySelection.add(new InterpreterInstanceSelector() {
			@Override public boolean selectInstance(Object obj) {
				return TypeRegistry.containsScalarTypeFor(obj.getClass()) || obj.getClass().isEnum();
			}
			
			@Override public SimplInterpretation obtainInterpreter() {
				return new ScalarInterpretation(); 
			}});
		
		interpreterBySelection.add(new InterpreterInstanceSelector() {
			@Override public boolean selectInstance(Object obj) {
				return Map.class.isAssignableFrom(obj.getClass());
			}
			
			@Override public SimplInterpretation obtainInterpreter() {
				return new MapInterpretation(); 
			}});
		
		interpreterBySelection.add(new InterpreterInstanceSelector() {
			@Override public boolean selectInstance(Object obj) {
				return !Map.class.isAssignableFrom(obj.getClass()) && Collection.class.isAssignableFrom(obj.getClass());
			}
			
			@Override public SimplInterpretation obtainInterpreter() {
				return new ListInterpretation(); 
			}});
		
		interpreterBySelection.add(new InterpreterInstanceSelector() {
			@Override public boolean selectInstance(Object obj) {
				return true;
			}
			
			@Override public SimplInterpretation obtainInterpreter() {
				return new CompositeInterpretation(""); 
			}});
	}
	
	private SimplInterpretation getInterpreterByFieldType(FieldType ft) throws SIMPLTranslationException
	{
		SimplInterpretation interp = interpreterByFieldType.get(ft);
		if(interp == null)
		{
			throw new SIMPLTranslationException("Type currently not supported! " + ft.name());
		}
		return interp;
	}
	
	public SimplInterpretation interpretField(Object obj, FieldDescriptor fd) throws SIMPLTranslationException 
	{
		return this.getInterpreterByFieldType(fd.getType()).interpret(obj, fd, this.thisInterpContext);
	}
	
	
	
}
