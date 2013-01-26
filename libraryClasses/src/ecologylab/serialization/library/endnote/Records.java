/*
 * Created on Dec 12, 2006
 */
package ecologylab.serialization.library.endnote;

import java.util.ArrayList;

import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_nowrap;

import ecologylab.serialization.ElementState;

public @simpl_inherit class Records extends ElementState
{
	
	@simpl_collection("Record")
	@simpl_nowrap
	ArrayList<Record> records;
 
    public Records()
    {
        
    }
}
