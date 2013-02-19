package simpl.descriptions;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class MetaInformationImpl implements MetaInformation {
	String Name;
	Map<String, ParameterDescriptor> parameters;
	
	public MetaInformationImpl(String name)
	{
		this.Name = name;
		this.parameters = new HashMap<String, ParameterDescriptor>();
	}
	
	@Override
	public String getAnnotationName() {
		// TODO Auto-generated method stub
		return this.Name;
	}
	
	@Override
	public Collection<ParameterDescriptor> getParameters() {
		// TODO Auto-generated method stub
		return this.parameters.values();
	}

	@Override
	public Boolean hasParameter(String parameterName) {
		// TODO Auto-generated method stub
		return this.parameters.containsKey(parameterName);
	}

	@Override
	public Object getValueFor(String parameterName) {
		// TODO Auto-generated method stub
		return this.parameters.get(parameterName).getValue();
	}

	@Override
	public void addParameter(ParameterDescriptor param) {
		if(param != null)
		{
			this.parameters.put(param.getName(), param);
		}else{
			throw new RuntimeException("Attempted to add null parameterDescirptor to the MetaInformation!");
		}
	}

}
