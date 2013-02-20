package simpl.interpretation;

public interface UpdateSimplRefCallback {
	String getID();
	void resolveUpdate(Object referencedComposite);
}
