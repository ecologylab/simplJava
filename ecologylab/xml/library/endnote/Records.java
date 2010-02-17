/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.library.endnote;

import java.util.ArrayList;
import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;

public @xml_inherit class Records extends ElementState
{
	
	@xml_collection("Record")
	@xml_nowrap
	ArrayList<Record> records;
 
    public Records()
    {
        
    }
}
