/**
 * 
 */
package ecologylab.appframework.types.prefs;

/**
 * An object that monitors a Pref or set of Prefs to respond to changes in
 * it/them.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public interface PrefChangedListener
{
    /**
     * Responds to a change in a Pref object. This method will be called
     * whenever ANY Pref changes, so the Object implementing this interface must
     * decide what to do with the Pref, most likely based upon its getName()
     * method.
     * 
     * @param pref
     */
    public void prefChanged(Pref pref);
}
