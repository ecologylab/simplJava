/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.library.endnote;

import ecologylab.xml.ElementState;
import ecologylab.xml.types.element.StringState;

/**
 * @author Zach Toups (toupsz@gmail.com)
 */
public class Record extends ElementState
{
    private @xml_nested ContributorList contributors = new ContributorList();
    
//    private @xml_nested int ref-type = 0;

    private @xml_nested TitleList titles = new TitleList();
    
    private @xml_leaf String volume;
    
    private @xml_leaf String number;
    
    private @xml_leaf String pages;
    
//    private @xml_nested String pub-location = "";
   
    private @xml_leaf String publisher;
    
    private @xml_leaf String isbn;
    
    private @xml_nested DateList dates = new DateList();
    
    private @xml_leaf String label;
    
    private @xml_nested KeywordList keywords = new KeywordList();
    
//    private @xml_nested URLList urls = new URLList();
    
//    private @xml_nested Abstract abstract = new Abstract();
    
    /**
     * 
     */
    public Record()
    {
    }

    /**
     * @return the contributors
     */
    public ContributorList getContributors()
    {
        return contributors;
    }

    /**
     * @return the dates
     */
    public DateList getDates()
    {
        return dates;
    }

    /**
     * @return the isbn
     */
    public String getIsbn()
    {
        return isbn;
    }

    /**
     * @return the keywords
     */
    public KeywordList getKeywords()
    {
        return keywords;
    }

    /**
     * @return the label
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * @return the number
     */
    public String getNumber()
    {
        return number;
    }

    /**
     * @return the pages
     */
    public String getPages()
    {
        return pages;
    }

    /**
     * @return the publisher
     */
    public String getPublisher()
    {
        return publisher;
    }

    /**
     * @return the titles
     */
    public TitleList getTitles()
    {
        return titles;
    }

    /**
     * @return the volume
     */
    public String getVolume()
    {
        return volume;
    }

}
