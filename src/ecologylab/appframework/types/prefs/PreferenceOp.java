package ecologylab.appframework.types.prefs;

import ecologylab.collections.Scope;

public interface PreferenceOp
{
	public void setScope(Scope scope);
	public void performAction(boolean invert);
	public String action();
}