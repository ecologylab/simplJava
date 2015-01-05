/*
 * Copyright 1996-2002 by Andruid Kerne. All rights reserved.
 * CONFIDENTIAL. Use is subject to license terms.
 */
package ecologylab.generic;

/**
 * DispatchTarget is a mechanism that enables non-linear flows of control around
 * asynchronous tasks such as networked I/O.
 * <p/>
 * In such cases, when an object makes a call to initiate an asynchronous operation,
 * it passes itself to the service provider, as a <code>DispatchTarget</code>.
 * Usually the result will be an immediate return, followed by a call to the
 * <code>delivery</code> method at some later point in time. Usually, the service provider
 * must guarantee that it will make such calls, even in cases of error.
 */
public interface Continuation<T>
{
	/**
	 * Called, probably later asynchronously, when the requested service is complete.
	 * 
	 * @param o -- enables an argument of any type to be passed back.
	 */
   // notification to the Client that event id is complete
   public void callback(T o);
}

