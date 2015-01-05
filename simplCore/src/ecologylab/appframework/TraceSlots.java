/**
 * 
 */
package ecologylab.appframework;

import ecologylab.generic.Debug;

/**
 * Peephole slots used for tracing concurrent real time deadlocks.
 * 
 * @author andruid
 *
 */
public class TraceSlots extends Debug
{
	public boolean		fatPixelGridRendering;
	public int			rolloverRenderStatus;
	public int			dragDropEndStatus;
	public int			setFocusStatus;
	public int			mouseExitStatus;
	static public int	rolloverRaiseStatus;
	public int			handleMouseEventStatus;
	public int			internalFocusStatus;
	public static int	renderStatus;
	public int			handleMouseEventId;
	static public int	pieMenuRaiseStatus;
	static public int	pieMenuRenderStatus;
	
}
