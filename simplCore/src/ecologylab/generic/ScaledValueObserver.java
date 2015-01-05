/*
 * Created on Sep 28, 2005
 * 
 */
package ecologylab.generic;

import ecologylab.serialization.ElementObserver;

/**
 * @author Andrew Webb
 *
 */
public interface ScaledValueObserver extends ElementObserver 
{
	public short getScaledValue();
}
