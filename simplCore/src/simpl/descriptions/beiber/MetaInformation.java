package simpl.descriptions.beiber;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class MetaInformation implements IMetaInformation {
	String Name;
	Map<String, IParameterDescriptor> parameters;
	
	public MetaInformation(String name)
	{
		this.Name = name;
		this.parameters = new HashMap<String, IParameterDescriptor>();
	}
	
	@Override
	public String getAnnotationName() {
		// TODO Auto-generated method stub
		return this.Name;
	}

	@Override
	public Collection<IParameterDescriptor> getParameters() {
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
		return this.parameters.get(parameterName);
	}

	@Override
	public void addParameter(IParameterDescriptor param) {
		if(param != null)
		{
			this.parameters.put(param.getName(), param);
		}else{
			throw new RuntimeException("Attempted to add null parameterDescirptor to the MetaInformation!");
		}
	}

}
