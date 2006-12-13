package ecologylab.xml.endnote;

import ecologylab.xml.TranslationSpace;

/**
 * Contains all of the information necessary to translate XML objects used in
 * the network communications of the Rogue game. Use MasterNameSpace.get() to
 * acquire a NameSpace object fit for use in the game.
 * 
 * @author Zach Toups
 * 
 */
public class EndnoteNameSpace
{

    public static final String    NAME           = "endnote";

    protected static final String PACKAGE_NAME   = "ecologylab.xml.endnote";

    protected static final Class  TRANSLATIONS[] =
                                                 {

            ecologylab.xml.StringState.class, ecologylab.xml.IntState.class,
            ecologylab.xml.ArrayListState.class,

            ecologylab.xml.endnote.Author.class,
            ecologylab.xml.endnote.ContributorList.class,
            ecologylab.xml.endnote.DateList.class,
            ecologylab.xml.endnote.Keyword.class,
            ecologylab.xml.endnote.KeywordList.class,
            ecologylab.xml.endnote.Record.class,
            ecologylab.xml.endnote.Records.class,
            ecologylab.xml.endnote.TitleList.class,
            ecologylab.xml.endnote.XmlState.class,

            ecologylab.xml.geom.Point2DDoubleState.class,
            ecologylab.xml.geom.Rectangle2DDoubleState.class,
            ecologylab.xml.geom.Ellipse2DDoubleState.class,
            ecologylab.xml.geom.RectangularShape.class };

    public static TranslationSpace get()
    {
        return TranslationSpace.get(NAME, PACKAGE_NAME, TRANSLATIONS);
    }

    /**
     * @return the tRANSLATIONS
     */
    public static Class[] getTRANSLATIONS()
    {
        return TRANSLATIONS;
    }
}
