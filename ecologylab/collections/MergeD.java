/*
 * Copyright 1996-2002 by Andruid Kerne. All rights reserved.
 * CONFIDENTIAL. Use is subject to license terms.
 */
package ecologylab.collections;


/**
 * Building block for merge sort implementation.
 */
public interface MergeD
{
/**
 * Descending merge operation for merge sort descending.
 * 
 * @param list1 input, assumed to already be in descending sorted order.
 * @param list2 input, assumed to already be in descending sorted order.
 * 
 * @return	The 2 {@link DLL DLL}s merged, in descending order.
 */
   public DLL mergeDescending(DLL list1, DLL list2);
}
