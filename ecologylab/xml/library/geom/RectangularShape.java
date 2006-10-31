/*
 * Created on Aug 29, 2006
 */
package ecologylab.xml.geom;

import java.awt.Shape;

import ecologylab.xml.ElementState;

public abstract class RectangularShape extends ElementState implements Shape
{
    /**
     * Location and dimensions of the ellipse.
     */
    public double              x         = 0;

    public double              y         = 0;

    public double              w         = 0;

    public double              h         = 0;   
    
    public RectangularShape()
    {
        super();
    }
    
    public RectangularShape(double x, double y, double width, double height)
    {
        super();
        
        setFrame(x, y, width, height);
    }
    
    public void setFrame(double x, double y, double w, double h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * @return the h
     */
    public double getHeight()
    {
        return h;
    }

    /**
     * @return the w
     */
    public double getWidth()
    {
        return w;
    }

    /**
     * @return the x
     */
    public double getX()
    {
        return x;
    }

    /**
     * @return the y
     */
    public double getY()
    {
        return y;
    }
}
