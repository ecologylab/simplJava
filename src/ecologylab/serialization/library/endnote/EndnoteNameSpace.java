package ecologylab.serialization.library.endnote;

import ecologylab.serialization.TranslationScope;

/**
 * Contains all of the information necessary to translate XML objects used in
 * the network communications of the Rogue game. Use MasterNameSpace.get() to
 * acquire a NameSpace object fit for use in the game.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class EndnoteNameSpace
{

    public static final String    NAME           = "endnote";

    protected static final String PACKAGE_NAME   = "ecologylab.serialization.library.endnote";

    protected static final Class  TRANSLATIONS[] =
                                                 {

            ecologylab.serialization.types.element.StringState.class, ecologylab.serialization.types.element.IntState.class,
            ecologylab.serialization.library.endnote.Author.class,
            ecologylab.serialization.library.endnote.ContributorList.class,
            ecologylab.serialization.library.endnote.DateList.class,
            ecologylab.serialization.library.endnote.Keyword.class,
            ecologylab.serialization.library.endnote.KeywordList.class,
            ecologylab.serialization.library.endnote.Record.class,
            ecologylab.serialization.library.endnote.Records.class,
            ecologylab.serialization.library.endnote.TitleList.class,
            ecologylab.serialization.library.endnote.XmlState.class,

            ecologylab.serialization.library.geom.Point2DDoubleState.class,
            ecologylab.serialization.library.geom.Rectangle2DDoubleState.class,
            ecologylab.serialization.library.geom.Ellipse2DDoubleState.class,
            ecologylab.serialization.library.geom.RectangularShape.class };

    public static TranslationScope get()
    {
        return TranslationScope.get(NAME, TRANSLATIONS);
    }

    /**
     * @return the tRANSLATIONS
     */
    public static Class[] getTRANSLATIONS()
    {
        return TRANSLATIONS;
    }
}
