/*
 * Created on Dec 12, 2006
 */
package ecologylab.serialization.library.endnote;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.ElementState;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class Record extends ElementState
{
    private @simpl_composite ContributorList contributors = new ContributorList();
    
//    private @xml_nested int ref-type = 0;

    private @simpl_composite TitleList titles = new TitleList();
    
    private @simpl_scalar @simpl_hints(Hint.XML_LEAF) String volume;
    
    private @simpl_scalar @simpl_hints(Hint.XML_LEAF) String number;
    
    private @simpl_scalar @simpl_hints(Hint.XML_LEAF) String pages;
    
//    private @xml_nested String pub-location = "";
   
    private @simpl_scalar @simpl_hints(Hint.XML_LEAF) String publisher;
    
    private @simpl_scalar @simpl_hints(Hint.XML_LEAF) String isbn;
    
    private @simpl_composite DateList dates = new DateList();
    
    private @simpl_scalar @simpl_hints(Hint.XML_LEAF) String label;
    
    private @simpl_composite KeywordList keywords = new KeywordList();
    
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
