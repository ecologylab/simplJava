package simpl.descriptions.beiber;

public interface UpdateClassDescriptorCallback {

	Class<?> getClassToUpdate();
	void updateWithCD(IClassDescriptor icd);
}
