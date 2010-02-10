/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.library.endnote;

import ecologylab.xml.ElementState;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class XmlState extends ElementState
{
    private @xml_nested Records records = new Records();
    
    /**
     * 
     */
    public XmlState()
    {
    }

    /**
     * @return the records
     */
    public Records getRecords()
    {
        return records;
    }

    /**
     * @param records the records to set
     */
    public void setRecords(Records records)
    {
        this.records = records;
    }

}
