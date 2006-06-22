package ecologylab.xml;

import java.awt.geom.Point2D;

/**
 * Encapsulates a Point2D.Double for use in translating to/from XML.
 * 
 * ***WARNING!!!***
 * 
 * Performing transformations (such as setLocation()) on the result of getPoint()
 * will cause this object to become out of synch with its underlying
 * Point2D. DO NOT DO THIS!
 * 
 * If other transformation methods are required, either notify me, or implement
 * them yourself. :D
 * 
 * Accessor methods (such as contains()) on the result of getPoint() are fine.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class Point2DDoubleState extends ElementState
{
    private Point2D.Double point      = new Point2D.Double();

    /**
     * Used to determine when point needs to be re-made.
     */
    private boolean            pointReady = false;

    /**
     * Location and dimensions of the point.
     */
    public double              x         = 0;

    public double              y         = 0;

    public Point2DDoubleState()
    {
        super();
    }

    public Point2DDoubleState(double x, double y)
    {
        setLocation(x, y);
    }

    public void setLocation(double x, double y)
    {
        this.x = x;
        this.y = y;

        pointReady = false;
    }

    /**
     * Ensures that the measures of the pointangle and the actual pointangle are
     * in synch. This should be called whenever point will be accessed.
     * 
     */
    private void fixPoint()
    {
        if (!pointReady)
        {
            point.setLocation(x, y);
            pointReady = true;
        }
    }

    public Point2D.Double getPoint()
    {
        fixPoint();

        return point;
    }

}
