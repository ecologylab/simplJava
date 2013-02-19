package simpl.descriptions;

public interface UpdateClassDescriptorCallback {
	Class<?> getClassToUpdate();
	void updateWithCD(ClassDescriptor icd);
}
