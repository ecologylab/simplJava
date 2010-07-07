/**
 * 
 */
package ecologylab.serialization.library.jnlp.information;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.ElementState.xml_tag;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public @simpl_inherit @xml_tag("information") class InformationElement extends ElementState
{
    @simpl_scalar protected String                                               os;

    /** The name of the application. This element is required. */
    @simpl_scalar @simpl_hints(Hint.XML_LEAF) protected String                                                  title;

    /** The name of the vendor of the application. This element is required. */
    @simpl_scalar @simpl_hints(Hint.XML_LEAF) protected String                                                  vendor;

    /**
     * Contains a single attribute, href, which is a URL locating the home page for the Application. It is used by the
     * Java Application Cache Viewer to point the user to a Web page where more information about the application can be
     * found.
     */
    @simpl_composite protected HomepageElement                                         homepage;

    /**
     * A short statement about the application. Description elements are optional. The kind attribute defines how the
     * description should be used. It can have one of the following values:
     * 
     * <ul>
     * <li>one-line: If a reference to the application is going to appear on one row in a list or a table, this
     * description will be used.</li>
     * <li>short: If a reference to the application is going to be displayed in a situation where there is room for a
     * paragraph, this description is used.</li>
     * <li>tooltip: If a reference to the application is going to appear in a tooltip, this description is used.</li>
     * </ul>
     * 
     * Only one description element of each kind can be specified. A description element without a kind is used as a
     * default value. Thus, if Java Web Start needs a description of kind short, and it is not specified in the JNLP
     * file, then the text from the description without an attribute is used.
     * 
     * All descriptions contain plain text. No formatting, such as with HTML tags, is supported.
     */
  	@simpl_nowrap 
    @simpl_collection("description") protected ArrayList<Description>               descriptions   = new ArrayList<Description>();

    /**
     * Contains an HTTP URL to an image file in either GIF or JPEG format. The icons are used to represents the
     * application
     * 
     * <ul>
     * <li>during launch when Java Web Start presents the application to the user;</li>
     * <li>in the Java Application Cache Viewer;</li>
     * <li>in desktop shortcuts.</li>
     * </ul>
     * 
     * A 64x64 icon is shown during download; in the Java Application Cache Viewer and in desktop shortcuts a 32x32 icon
     * is used. Java Web Start automatically resizes an icon to the appropriate size.
     * 
     * The icon element requires an href attribute, specifiying the directory and name of the icon file.
     * 
     * Optional width and height attributes can be used to indicate the size of the images.
     * 
     * The optional kind="splash" attribute may be used in an icon element to indicate that the image is to be used as a
     * "splash" screen during the launch of an application. If the JNLP file does not contain an icon element with
     * kind="splash" attribute, Java Web Start will construct a splash screen using other items from the information
     * Element.
     * 
     * If the JNLP file does not contain any icon images, the splash image will consist of the application's title and
     * vendor, as taken from the JNLP file.
     * 
     * The first time an application is launched following the addition or modification of the icon element in the JNLP
     * file, the old splash image will still be displayed. The new splash image will appear on the second and subsequent
     * launches of the application.
     */
  	@simpl_nowrap 
    @simpl_collection("icon") protected ArrayList<Icon>                             icons          = new ArrayList<Icon>();

    /**
     * offline-allowed element: The optional offline-allowed element indicates if the application can be launched
     * offline.
     * 
     * If offline-allowed is specified, then the application can be launched offline by the Java Application Cache
     * Viewer, and shortcuts can be created which launch the application offline.
     * 
     * If an application is launched offline, it will not check for updates and the API call BasicService.isOffline()
     * will return true.
     * 
     * The offline-allowed element also controls how Java Web Start checks for an update to an application. If the
     * element is not specified—i.e., the application is required to be online to run—Java Web Start will always check
     * for an updated version before launching the application. And if an update is found, the new application will be
     * downloaded and launched. Thus, it is guaranteed that the user always runs the latest version of the application.
     * The application, however, must be run online.
     * 
     * If offline-allowed is specified, Java Web Start will also check to see if an update is available. However, if the
     * application is already downloaded the check will timeout after a few seconds, in which case the cached
     * application will be launched instead. Given a reasonably fast server connection, the latest version of the
     * application will usually be run, but it is not guaranteed. The application, however, can be run offline.
     */
  	@simpl_nowrap 
    @simpl_collection("offline-allowed") protected ArrayList<OfflineAllowedElement> offlineAllowed = new ArrayList<OfflineAllowedElement>();

    /**
     * The optional association element is a hint to the JNLP client that it wishes to be registered with the operating
     * system as the primary handler of certain extensions and a certain mime-type. The association element must have
     * the extensions and mime-type attributes.
     */
    @simpl_composite protected AssociationElement                                      association;

    /**
     * shortcut element: The optional shortcut element can be used to indicate an application's preferences for desktop
     * integration. The shortcut element and it's sub-elements provide hints that the JNLP Client may or may not use.
     * The shortcut element can contain the optional online attribute, and the two optional sub-elements, desktop and
     * menu.
     */
    @simpl_composite protected ShortcutElement                                         shortcut;

    /**
     * related-content element: The optional related-content element describes an additional piece of related content,
     * such as a readme file, help pages, or links to registration pages, as a hint to a JNLP Client. The application is
     * asking that this content be included in its desktop integration. The related-content element has a mandatory href
     * and title attribute. It can contain any of the following two sub-elements:
     * 
     * description element: A short description of the related content. icon element: The icon can be used by the JNLP
     * Client to identify the related content to the user.
     */
    @simpl_composite @xml_tag("related-content") RelatedContentElement               relatedContent;
    
    /** No-argument constructor for XML translation. */
    public InformationElement()
    {
        super();
    }

    /**
     * @return the descriptions
     */
    public ArrayList<Description> getDescriptions()
    {
        return descriptions;
    }

    /**
     * @return the icons
     */
    public ArrayList<Icon> getIcons()
    {
        return icons;
    }

	public String getTitle()
	{
		return title;
	}

	public String getVendor()
	{
		return vendor;
	}
}
