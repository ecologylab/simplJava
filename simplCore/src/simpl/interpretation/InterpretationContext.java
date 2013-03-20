package simpl.interpretation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;

public class InterpretationContext {
	
	Set<ObjectIdentity> objectRegistry;
	
	UpdateCompositeInterpretationCallbackMap updateCallbacks;
	
	Map<ObjectIdentity, String> simplIDMap;

	SimplInterpreter interpreter;
	
	public InterpretationContext(SimplInterpreter interpreter)
	{
		this.objectRegistry = new HashSet<ObjectIdentity>();
		this.interpreter = interpreter;
		this.updateCallbacks = new UpdateCompositeInterpretationCallbackMap();
		this.simplIDMap = new HashMap<ObjectIdentity, String>();
	}
	
	public SimplInterpretation interpretObject(Object obj) throws SIMPLTranslationException
	{
		return (SimplInterpretation) interpreter.interpretInstance(obj);
	}
	
	public SimplInterpretation interpretObject(Object obj, FieldDescriptor field) throws SIMPLTranslationException
	{
		return (SimplInterpretation) interpreter.interpretField(obj,field);
	
	}

	private Integer currentID = 0;
	
	public String fetchSimplID()
	{	
		currentID = currentID + 1;
		return currentID.toString();
	}
	
	public boolean objectHasBeenSeen(Object o)
	{
		ObjectIdentity id = new ObjectIdentity(o);
		
		if(this.objectRegistry.contains(id))
		{
			if(this.updateCallbacks.getPendingUpdateKeys().contains(id))
			{
				// We need to obtain a SIMPL ID for the object.
				this.updateCallbacks.resolveCallbacks(id, this); 
				// We pass "this" because we're going to use this context for the SIMPL ID piece.
			}
			
			return true;
		}else{
			this.objectRegistry.add(id);
						
			return false;
		}
	}
	
	public void registerUpdateCallback(Object theObject, final CompositeInterpretation theInterp)
	{
		final ObjectIdentity theID = new ObjectIdentity(theObject);

		this.updateCallbacks.insertCallback(new UpdateCompositeInterpretationCallback() {
			
			@Override
			public void runUpdateCallback(InterpretationContext theContext) {
				
				String simplID = theContext.fetchSimplID();
				
				theContext.registerSimplID(theID, simplID);
				
				theInterp.setIDString(simplID);		
			}
			
			@Override
			public ObjectIdentity getUpdateKey() {
				// TODO Auto-generated method stub
				return theID;
			}
		});
	}
	
	public String getRegisteredSIMPLID(Object o)
	{
		ObjectIdentity id = new ObjectIdentity(o);
		return this.simplIDMap.get(id);	
	}
	
	public void registerSimplID(ObjectIdentity identity, String simplID)
	{
		this.simplIDMap.put(identity, simplID);
	}
}

