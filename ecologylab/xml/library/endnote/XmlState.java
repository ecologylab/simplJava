/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.endnote;

import ecologylab.xml.ElementState;

/**
 * @author Zach Toups (toupsz@gmail.com)
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
