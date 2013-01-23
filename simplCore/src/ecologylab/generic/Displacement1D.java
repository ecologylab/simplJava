package ecologylab.generic;

/**
 * defines a displacement in one dimension, of the form:
 *	a*x + b*width + c,
 * where a=1, always, in our cases so far.
 */
public class Displacement1D
{
   int b, c;
   
   public Displacement1D(int[] v)
   {
      b	= v[0];
      c	= v[1];
   }
   public Displacement1D(int b, int c)
   {
      this.b	= b;
      this.c	= c;
   }
   public int xform(int q, int size)
   {
      return q + b*size + c;
   }
}
