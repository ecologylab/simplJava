/*
 * Created on Aug 29, 2006
 */
package ecologylab.xml.geom;

import java.awt.Shape;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;

public @xml_inherit abstract class RectangularShape extends ElementState implements Shape
{
    /**
     * Location and dimensions of the ellipse.
     */
    protected @xml_attribute double              x         = 0;

    protected @xml_attribute double              y         = 0;

    protected @xml_attribute double              w         = 0;

    protected @xml_attribute double              h         = 0;   
    
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
