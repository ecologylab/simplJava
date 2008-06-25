package ecologylab.appframework;

import ecologylab.generic.Debug;
import ecologylab.appframework.types.prefs.Pref;
import ecologylab.appframework.types.prefs.PrefInt;
import ecologylab.collections.SyncLinkedList;
import ecologylab.services.logging.MixedInitiativeOp;

/**
 * Undo and Redo Mixed Initiative Operations. 
 * @author andruid, alexgrau
 */
public class UndoRedo<O extends MixedInitiativeOp> extends Debug{
	
	/**
	 * Synced Linked List of Undo Operations.
	 */
	protected final SyncLinkedList undoStack = new SyncLinkedList();
	
	/**
	 * Syned Linked List of Redo Operations
	 */
	protected final SyncLinkedList redoStack = new SyncLinkedList();
	
	/**
	 * Semaphor Object Used For Undo and Redo
	 */
	protected final Object undoRedoSemaphore = new Object();
	
	/**
	 * Sets the Int Prefs for the number of Undo/Redo Levels
	 */
	protected final PrefInt undoLevels = Pref.usePrefInt(UNDO_LEVELS, DEFAULT_UNDO_LEVELS);
	
	/**
	 * Number of possible Undo/Redo levels
	 */
	protected static final int DEFAULT_UNDO_LEVELS = 32;
	
	public static final String	UNDO_LEVELS 	= "undo_levels";
	
	protected int humanUndoCount;
	protected int humanRedoCount;
	
	/**
	 * Construct an Undo/Redo Object
	 */
	public UndoRedo(){
		super();
	}
	
	/**
	 * Undo an Operation. Returns the Operations.
	 * @return
	 */
	public MixedInitiativeOp undo(){
		synchronized (undoRedoSemaphore){
			MixedInitiativeOp op = popUndo();
			if(op == null)
				return null;
			op.performAction(true);
			pushRedo(op);
			return op;
		}
	}
	
	/**
	 * Redo an Operation. Returns the Operation.
	 * @return
	 */
	public MixedInitiativeOp redo(){
		synchronized (undoRedoSemaphore){
			MixedInitiativeOp op = popRedo();
			if (op == null)
				return null;
			op.performAction(false);
			pushUndo(op);
			return op;
	   }
	}
	
	/**
	 * Pushes an Operation onto the undo stack.
	 * Clears the redo stack because a new operation has taken place.
	 * @param op
	 */
	public MixedInitiativeOp pushOpToUndo(MixedInitiativeOp op){
		pushUndo(op);
		clearRedo();
		return op;
	}
	
	/**
	 * Pushes an Operation onto the Undo stack.
	 * If too many operations, removes oldest and adds op.
	 * @param op
	 */
	public void pushUndo(MixedInitiativeOp op){
		synchronized (undoStack){
			while (undoStack.size() >= undoLevels.value()){
				MixedInitiativeOp oldOp = (MixedInitiativeOp) undoStack.removeFirst();
				if (oldOp.isHuman())
					humanUndoCount--;
				oldOp.recycle(false);
			}
			undoStack.addLast(op);
			humanUndoCount++;
		}
	}
	
	/**
	 * Pops an Operation from the Undo stack.
	 * If empty, returns null.
	 * @return
	 */
	public MixedInitiativeOp popUndo(){
		synchronized (undoStack){
			return undoStack.isEmpty() ? null : (MixedInitiativeOp) undoStack.removeLast();
		}
	}

	/**
	 * Peak an Operation from the Undo stack.
	 * If empty, returns null.
	 * @return
	 */
	public MixedInitiativeOp peekUndo(){
		synchronized (undoStack){
			return undoStack.isEmpty() ? null : (MixedInitiativeOp) undoStack.getLast();
		}
	}
	
	/**
	 * Pushes an Operation onto the Redo stack.
	 * @param op
	 */
	private void pushRedo(MixedInitiativeOp op){
		synchronized (redoStack){
			redoStack.addLast(op);	
		}
	}
	
	/**
	 * Pops an Operation from the Redo stack.
	 * If empty, then returns null.
	 * @return
	 */
	private MixedInitiativeOp popRedo(){
		synchronized (redoStack){
			return redoStack.isEmpty() ? null : (MixedInitiativeOp) redoStack.removeLast();
		}
	}
	
	/**
	 * Clears both the undo and redo stacks.
	 */
	public synchronized void clear(){
		synchronized (undoStack){
			while (!undoStack.isEmpty()){
				MixedInitiativeOp op = popUndo();
				op.recycle(true);
			}
		}
		synchronized (redoStack){
			while (!redoStack.isEmpty()){
				MixedInitiativeOp op = popRedo();
				op.recycle(true);
			}
		}
	}
	
	/**
	 * Clears and recycles the redo stack after a new operation has been done.
	 */
	protected void clearRedo(){
		synchronized (redoStack){
			while (!redoStack.isEmpty()){
				MixedInitiativeOp op = popRedo();
				op.recycle(true);
			}
		}
	}
}
