package ecologylab.xml;

import java.awt.geom.Rectangle2D;

/**
 * Encapsulates a Rectangle2D.Double for use in translating to/from XML.
 * 
 * ***WARNING!!!***
 * 
 * Performing transformations (such as setFrame()) on the result of getRect()
 * will cause this object to become out of synch with its underlying
 * Rectangle2D. DO NOT DO THIS!
 * 
 * If other transformation methods are required, either notify me, or implement
 * them yourself. :D
 * 
 * Accessor methods (such as contains()) on the result of getRect() are fine.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class Rectangle2DDoubleState extends ElementState
{
    private Rectangle2D.Double rect      = new Rectangle2D.Double();

    /**
     * Used to determine when rect needs to be re-made.
     */
    private boolean            rectReady = false;

    /**
     * Location and dimensions of the rectangle.
     */
    public double              x         = 0;

    public double              y         = 0;

    public double              w         = 0;

    public double              h         = 0;

    public Rectangle2DDoubleState()
    {
        super();
    }

    public Rectangle2DDoubleState(double x, double y, double w, double h)
    {
        setFrame(x, y, w, h);
    }

    public void setFrame(double x, double y, double w, double h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        rectReady = false;
    }

    /**
     * Ensures that the measures of the rectangle and the actual rectangle are
     * in synch. This should be called whenever rect will be accessed.
     * 
     */
    private void fixRect()
    {
        if (!rectReady)
        {
            rect.setFrame(x, y, w, h);
            rectReady = true;
        }
    }

    public Rectangle2D.Double getRect()
    {
        fixRect();

        return rect;
    }

}
