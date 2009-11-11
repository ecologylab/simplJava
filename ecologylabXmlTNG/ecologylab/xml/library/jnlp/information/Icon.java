/**
 * 
 */
package ecologylab.xml.library.jnlp.information;

import ecologylab.xml.ElementState;

/**
 * Contains an HTTP URL to an image file in either GIF or JPEG format. The icons are used to represents the application
 * 
 * <ul>
 * <li>during launch when Java Web Start presents the application to the user;</li>
 * <li>in the Java Application Cache Viewer;</li>
 * <li>in desktop shortcuts.</li>
 * </ul>
 * 
 * A 64x64 icon is shown during download; in the Java Application Cache Viewer and in desktop shortcuts a 32x32 icon is
 * used. Java Web Start automatically resizes an icon to the appropriate size.
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
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class Icon extends ElementState
{
    @xml_attribute private String href;

    @xml_attribute private int    width;

    @xml_attribute private int    height;

    @xml_attribute private String kind;

    /**
     * 
     */
    public Icon()
    {
        super();
    }

}
