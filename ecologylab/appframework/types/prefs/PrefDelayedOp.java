package ecologylab.appframework.types.prefs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import ecologylab.collections.Scope;
import ecologylab.services.logging.MixedInitiativeOp;
import ecologylab.xml.xml_inherit;

@xml_inherit
public class PrefDelayedOp extends PrefOp<MixedInitiativeOp> implements ActionListener
{

	/**
	 * delay in seconds
	 */
	@xml_attribute	int				delay;
	@xml_attribute 	boolean 	repeat 				= false;
	@xml_attribute	int 			initialDelay 	= 0;
	Timer timer;
	public PrefDelayedOp()
	{
		super();
	}
	
	public PrefDelayedOp(String name, int delay, boolean repeat, int initialDelay, MixedInitiativeOp op, ArrayList set)
	{
		super();
		this.name = name;
		this.delay = delay;
		this.repeat = repeat;
		this.initialDelay = initialDelay;
		this.op = op;
		this.set = set;
	}
	@Override
	public void postLoadHook(Scope scope)
	{
		if(op == null)
			op = getOp(); 
		
		PreferenceOp prefOp = (PreferenceOp) op;
		prefOp.setScope(scope);
		
		debug("delayed op: " + op.action() + " initialized with delay: " + delay + " seconds");
		timer = new Timer(delay * 1000, this);
		timer.setInitialDelay(delay * 1000);
		timer.start();
	}

	public void actionPerformed(ActionEvent arg0)
	{
		if(op == null)
			return;
		debug("Performing delayed op: " + op.action());
		op.performAction(false);
		if(!repeat)
			timer.stop();
	}
	@Override
	public String toString()
	{
		return "PrefDelayedOp: " + name;
	}
	
	/**
	 * See Pref.clone for why this is important.
	 * @see ecologylab.appframework.types.prefs.Pref#clone()
	 */
	@Override
	public Pref<MixedInitiativeOp> clone()
	{
		PrefDelayedOp prefDelayedOp = new PrefDelayedOp(name, delay, repeat, initialDelay, op, set);
		return prefDelayedOp;
	}
	
}
