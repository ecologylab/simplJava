/*
 * Created on Dec 12, 2006
 */
package ecologylab.serialization.library.endnote;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_nowrap;

public @simpl_inherit class Records extends ElementState
{
	
	@simpl_collection("Record")
	@simpl_nowrap
	ArrayList<Record> records;
 
    public Records()
    {
        
    }
}
