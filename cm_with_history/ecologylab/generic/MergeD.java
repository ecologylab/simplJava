/*
 * Copyright 1996-2002 by Andruid Kerne. All rights reserved.
 * CONFIDENTIAL. Use is subject to license terms.
 */
package cm.generic;

/**
 * Building block for merge sort implementation.
 */
public interface MergeD
{
/**
 * Descending merge operation for descending merge sort.
 * 
 * @param	both input lists assumed to already be in descending
 *		sorted order.
 * 
 * @return	The 2 {@link DLL DLL}s merged, in descending order.
 */
   public DLL mergeDescending(DLL a, DLL b);
}
