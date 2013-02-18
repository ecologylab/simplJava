package simpl.descriptions.beiber;

import java.util.Collection;

public interface IMetaInformation {
	String getAnnotationName();
	Collection<IParameterDescriptor> getParameters();
	Boolean hasParameter(String parameterName);
	Object getValueFor(String parameterName);
	void addParameter(IParameterDescriptor param);
}
