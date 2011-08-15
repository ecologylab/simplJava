/*
 * Created on Dec 12, 2006
 */
package ecologylab.serialization.library.endnote;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class XmlState extends ElementState
{
//    private @xml_nested Records records = new Records();
    private @simpl_collection("Record")
    ArrayList<Record> records;
    /**
     * 
     */
    public XmlState()
    {
    }

    /**
     * @return the records
     */
    public ArrayList<Record> getRecords()
    {
        return records;
    }

    /**
     * @param records the records to set
     */
    public void setRecords(ArrayList<Record> records)
    {
        this.records = records;
    }

}
