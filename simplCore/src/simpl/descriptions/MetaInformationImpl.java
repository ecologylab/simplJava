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

	@Override
	/**
	 * Gest the value of the Value() for a given annotation;
	 * this is fairly java specific; If there is a single value for an annotation, it'll be value();
	 * Accross platforms, this should just return the single value in an annotation, or an error if there are multiple values. 
	 */
	public Object getValue() {
		// TODO Auto-generated method stub
		return this.getValueFor("value");
	}

}
