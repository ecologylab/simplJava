/*
 * Copyright 1996-2002 by Andruid Kerne. All rights reserved.
 * CONFIDENTIAL. Use is subject to license terms.
 */
package cm.generic;

/**
 * DispatchTarget is for Client, a component that takes a delivery (is dispatched
 to) when some "task" (such as the downloading of an image) completes.
 */
public interface DispatchTarget
{
   // notification to the Client that event id is complete
   public void delivery(Object o);
}

