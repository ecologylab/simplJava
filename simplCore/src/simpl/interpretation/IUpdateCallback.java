package simpl.interpretation;

public interface IUpdateCallback<KeyType, UpdatedObject> {
	KeyType getUpdateKey();
	void runUpdateCallback(UpdatedObject object);
}
