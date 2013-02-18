package simpl.descriptions.beiber;

import java.util.Collection;
import java.util.List;

public interface IClassDescriptor {
	Class<?> getJavaClass();
	String getName();
	List<IFieldDescriptor> getFields();
	IClassDescriptor getSuperClassDescriptor();
	Collection<String> getOtherTags();
}
