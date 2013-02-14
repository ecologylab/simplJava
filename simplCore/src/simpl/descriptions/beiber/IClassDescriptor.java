package simpl.descriptions.beiber;

import java.util.List;

public interface IClassDescriptor {
	Class<?> getJavaClass();
	String getName();
	List<IFieldDescriptor> getFields();
}
