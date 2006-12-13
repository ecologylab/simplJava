/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.endnote;

import ecologylab.xml.ElementState;
import ecologylab.xml.StringState;

/**
 * @author Zach Toups (toupsz@gmail.com)
 */
public class Record extends ElementState
{
    private @xml_nested ContributorList contributors = new ContributorList();
    
//    private @xml_nested int ref-type = 0;

    private @xml_nested TitleList titles = new TitleList();
    
    private @xml_nested int volume;
    
    private @xml_nested int number;
    
    private @xml_nested StringState pages;
    
//    private @xml_nested String pub-location = "";
   
    private @xml_nested StringState publisher;
    
    private @xml_nested StringState isbn;
    
    private @xml_nested DateList dates = new DateList();
    
    private @xml_nested StringState label;
    
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
    public StringState getIsbn()
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
    public StringState getLabel()
    {
        return label;
    }

    /**
     * @return the number
     */
    public int getNumber()
    {
        return number;
    }

    /**
     * @return the pages
     */
    public StringState getPages()
    {
        return pages;
    }

    /**
     * @return the publisher
     */
    public StringState getPublisher()
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
    public int getVolume()
    {
        return volume;
    }

}
