package ecologylab.services.logging;

import ecologylab.xml.simpl_inherit;

/**
 * A user operation, which can be serialized, logged, Undo/Redo'ed, played in history, and so on.
 * 
 * @author andruid
 */
abstract public @simpl_inherit class MixedInitiativeOp extends BasicOp
{	
	@simpl_scalar protected short	intensity;
	
	/**
	 * invert value for the dual operation. 
	 * This value indicates which operation should be performed in the object of dual operations. 
	 */
	@simpl_scalar protected boolean	invert;
	
	@simpl_scalar protected String		action = null;
	@simpl_scalar protected long		recordTime;
	
	static protected final short UNKNOWN_OP		= -1024;
	static protected final short UNDEFINED_OP		= -1;
	static protected final short AGENT_OP			= 0;
	static protected final short HUMAN_ELEMENT_OP	= 1;
	static protected final short HUMAN_EDIT_OP	= 2;
	static protected final short CONTROL_OP		= 3;
	static protected final short PROGRAM_OP		= 4;

	/** No-argument constructor for XML translation. */
	public MixedInitiativeOp()
	{
		super();
	}

	/**
	 * Perform the op. Perhaps invert it, as for undo.
	 * 
	 * @param invert
	 */
	abstract public void performAction(boolean invert);

	/**
	 * In a mixed initiative system, some ops are by the human, while others are by the agent.
	 * 
	 * The presence of this here at the moment may be a hack. It may be good design :-) Human and dyadic undo should
	 * probably be split into 2 separte UndoRedo classes.
	 * 
	 * @return
	 */
	public boolean isHuman(){
		return true;
	}
	
	public long recordTime()
    {
		return recordTime;
    }

	public void setRecordTime(long recordTime) {
		this.recordTime = recordTime;
	}
	
	/** Free resources associated with this. */
	public void recycle(boolean invert){}
	
	public String action()
	{ 
		return action;
	}
	
}
