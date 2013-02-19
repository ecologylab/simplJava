package simpl.descriptions;

import java.util.Collection;

public interface MetaInformation {
	String getAnnotationName();
	Collection<ParameterDescriptor> getParameters();
	Boolean hasParameter(String parameterName);
	Object getValueFor(String parameterName);
	void addParameter(ParameterDescriptor param);
}
