package simpl.interpretation;

/**
 * An interface for an "update" callback which updates some final reference to an object with some updatedValue. 
 *  The "Key" identifies which objects rely on some updated value. 
 *
 * @param <KeyType> The type of the object which should trigger updating operations
 * @param <UpdatedObject> The updated value to set or use. 
 */
public interface IUpdateCallback<KeyType, UpdatedObject> {
	KeyType getUpdateKey();
	void runUpdateCallback(UpdatedObject object);
}
