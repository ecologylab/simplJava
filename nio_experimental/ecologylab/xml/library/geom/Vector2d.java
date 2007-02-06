/*
 * Created on Nov 14, 2006
 */
package ecologylab.xml.library.geom;

import java.awt.geom.Point2D;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;

/**
 * @author Zach Toups (toupsz@gmail.com)
 */
public @xml_inherit class Vector2d extends ElementState implements Cloneable
{
    public static Vector2d add(Vector2d v1, Vector2d v2)
    {
        return new Vector2d(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    public static Vector2d scalarMultiply(Vector2d vector, double scalar)
    {
        return new Vector2d(vector.getX() * scalar, vector.getY() * scalar);
    }

    public static double dot(Vector2d v1, Vector2d v2)
    {
        return (v1.getX() * v2.getX()) + (v1.getY() * v2.getY());
    }

    protected @xml_attribute double x = 0;

    protected @xml_attribute double y = 0;

    /**
     * 
     */
    public Vector2d()
    {
        super();
    }

    public Vector2d(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d otherVect)
    {
        x = otherVect.getX();
        y = otherVect.getY();
    }

    public void add(Vector2d v)
    {
        // System.out.println("----------add: "+this.toString()+" +
        // "+v.toString());
        this.x += v.getX();
        this.y += v.getY();

        // System.out.println("-------result: "+this.toString());
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

    public double norm()
    {
        return Math.sqrt(x*x + y*y);
    }

    public Vector2d mult(double scalar)
    {
        this.x *= scalar;
        this.y *= scalar;
        
        return this;
    }

    /**
     * Rotates this vector around the origin by the specified angle in degrees.
     * 
     * @param angle -
     *            in radians
     */
    public void rotate(double angle)
    {
        // System.out.println("x: "+x+", y: "+y);

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        // double mag = this.norm();

        // this.x += ((cos / sin) * mag);
        // this.y += ((sin / cos) * mag);

        double x1 = this.x;
        double y1 = this.y;

        this.x = (x1 * cos) - (y1 * sin);
        this.y = (y1 * cos) + (x1 * sin);

        // System.out.println("x: "+x+", y: "+y);
    }

    /**
     * Rotates this vector so that it is aligned to the specified angle in
     * degrees.
     * 
     * @param angle -
     *            in radians
     */
    public void rotateTo(double angle)
    {
        // TODO gotta make this more efficient!
        this.rotate(angle - this.toRadians());
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(double x)
    {
        this.x = x;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(double y)
    {
        this.y = y;
    }

    public void sub(Vector2d v)
    {
        this.x -= v.getX();
        this.y -= v.getY();
    }

    public double toRadians()
    {
        // System.out.println("("+x+", "+y+") "+Math.atan2(y,
        // x)/Math.PI+"rads");
        return Math.atan2(y, x);
    }

    public Vector2d unitVector()
    {
        double mag = this.norm();

        return new Vector2d(this.x / mag, this.y / mag);
    }

    public void unitize()
    {
        double mag = this.norm();

        this.setX(this.x / mag);
        this.setY(this.y / mag);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override public Vector2d clone()
    {
        return new Vector2d(this);
    }

    public void set(Vector2d pos)
    {
        this.setX(pos.getX());
        this.setY(pos.getY());
    }

    public Point2D toPoint()
    {
        return new Point2D.Double(x, y);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ecologylab.generic.Debug#toString()
     */
    @Override public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Adjusts the magnitude of this vector to match mag.
     * 
     * @param mag
     */
    public void setNorm(double mag)
    {
        this.unitize();
        this.mult(mag);
    }

    public void zero()
    {
        x = 0;
        y = 0;
    }

    public static Vector2d sub(Vector2d v1, Vector2d v2)
    {
        return new Vector2d(v1.getX() - v2.getX(), v1.getY() - v2.getY());
    }
}
