/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.library.endnote;

import java.util.ArrayList;

import ecologylab.xml.ElementState;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class XmlState extends ElementState
{
//    private @xml_nested Records records = new Records();
    private @xml_collection("Record")
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
