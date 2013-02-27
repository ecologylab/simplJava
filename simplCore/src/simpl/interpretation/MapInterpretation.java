package simpl.interpretation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.ReflectionTools;
import simpl.types.MapType;

public class MapInterpretation implements SimplInterpretation{

	public MapInterpretation()
	{
		this.entryInterpretations = new ArrayList<MapEntryInterpretation>();
		this.mapType = new MapType();
	}

	List<MapEntryInterpretation> entryInterpretations;
	
	String fieldName;
	MapType mapType;
	
	public void addEntryInterpretation(SimplInterpretation key, SimplInterpretation value)
	{
		entryInterpretations.add(new MapEntryInterpretation(key,value));
	}
	
	@Override
	public void resolve(Object context, Set<String> refSet,
			UnderstandingContext understandingContext)
			throws SIMPLTranslationException {
		Object value = getValue(context, refSet, understandingContext);

		try 
		{
			ReflectionTools.setFieldValue(value, context.getClass().getField(this.fieldName), context);
		}
		catch (Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}

	@Override
	public Object getValue(Object context, Set<String> refSet,
			UnderstandingContext understandingContext)
			throws SIMPLTranslationException 
	{
		Map m = (Map)this.mapType.getInstance();
		
		for(MapEntryInterpretation entry : entryInterpretations)
		{
			MapEntryEvaluation result = (MapEntryEvaluation)entry.getValue(context, refSet, understandingContext);
			m.put(result.key, result.value);
		}
			
		return m;
	}

}
