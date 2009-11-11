package ecologylab.generic;

import java.awt.*;

/**
 * A 2D line, as defined by parametric coefficients.
 * The line, ultimately will be defined by 2 end points, that is, by 4
 * integers, when it is drawn.
 * The parametric representation is of the form:
 * x0 =	a*x + b*width + c, 
 * y0 =	a*y + b*height + c.
 * 
 * Displacement1D holds these parametric forms.
 * In our world a=1, always, so we assume that, and dont bother with
 * the extra multiplication. (This could always be changed inside the
 * encapsulated Displacement1D, without effecting the call sites.)
 */
public class Line2D
{
   Displacement1D	x0, y0, x1, y1;
   
   public Line2D(int[] dx0, int[] dy0, int[] dx1, int[] dy1)
   {
      x0	= new Displacement1D(dx0);
   }
   public Line2D(int[] dx0)
   {
      x0	= new Displacement1D(dx0);
   }
   public Line2D(int dxb0, int dxc0, int dyb0, int dyc0,
		 int dxb1, int dxc1, int dyb1, int dyc1)
   {
      x0	= new Displacement1D(dxb0, dxc0);
      y0	= new Displacement1D(dyb0, dyc0);
      x1	= new Displacement1D(dxb1, dxc1);
      y1	= new Displacement1D(dyb1, dyc1);
   }

   public void draw(Graphics g, int x, int y, int width, int height)
   {
      g.drawLine(x0.xform(x, width),
		 y0.xform(y, height),
		 x1.xform(x, width),
		 y1.xform(y, height));
   }
}
