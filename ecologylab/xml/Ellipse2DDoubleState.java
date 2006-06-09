package ecologylab.xml;

import java.awt.geom.Ellipse2D;

/**
 * Encapsulates a Ellipse2D.Double for use in translating to/from XML.
 * 
 * ***WARNING!!!***
 * 
 * Performing transformations (such as setFrame()) on the result of getEllipse()
 * will cause this object to become out of synch with its underlying
 * Ellipse2D. DO NOT DO THIS!
 * 
 * If other transformation methods are required, either notify me, or implement
 * them yourself. :D
 * 
 * Accessor methods (such as contains()) on the result of getEllipse() are fine.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class Ellipse2DDoubleState extends ElementState
{
    private Ellipse2D.Double ellipse      = new Ellipse2D.Double();

    /**
     * Used to determine when ellipse needs to be re-made.
     */
    private boolean            ellipseReady = false;

    /**
     * Location and dimensions of the ellipse.
     */
    public double              x         = 0;

    public double              y         = 0;

    public double              w         = 0;

    public double              h         = 0;

    public Ellipse2DDoubleState()
    {
        super();
    }

    public Ellipse2DDoubleState(double x, double y, double w, double h)
    {
        setFrame(x, y, w, h);
    }

    public void setFrame(double x, double y, double w, double h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        ellipseReady = false;
    }

    /**
     * Ensures that the measures of the ellipseangle and the actual ellipseangle are
     * in synch. This should be called whenever ellipse will be accessed.
     * 
     */
    private void fixEllipse()
    {
        if (!ellipseReady)
        {
            ellipse.setFrame(x, y, w, h);
            ellipseReady = true;
        }
    }

    public Ellipse2D.Double getEllipse()
    {
        fixEllipse();

        return ellipse;
    }

}
