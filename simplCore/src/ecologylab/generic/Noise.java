package ecologylab.generic;

import java.util.Random;

/**
 * Perlin Noise on tap.
 */
public class Noise
{
   static private final int B  = 0x100;
   static private final int BP = 8;
   static private final int BM = 0xff;

   static private final int N  = 0x1000;
   static private final int NP = 12;
   static private final int NM = 0xfff;

   static private int p[] = new int[B + B +2];
   static private double g2[][] = new double[B + B + 2][2];
   static private double g1[] = new double[B + B +2];

   public static double noise(double arg)
   {
      int bx0, bx1;
      double rx0, rx1, sx, t, u, v;

      t = arg + N;
      bx0 = ((int)t)&BM;
      bx1 = (bx0+1)&BM;
      rx0 = t - (int) t;
      rx1 = rx0 -1;

      sx = rx0*rx0*(3.0 - 2.0 * rx0);
      u = rx0 * g1[p[bx0]];
      v = rx1 * g1[p[bx1]];

      return (lerp(sx, u, v));
   }

   static private double noise2(float vec[])
   {
      int bx0, bx1, by0, by1, b00, b10, b01, b11;
      double rx0, rx1, ry0, ry1, q[], sx, sy, a, b, t, u, v;
      int i,j;

      t = vec[0] + N;
      bx0 = ((int)t)&BM;
      bx1 = (bx0+1)&BM;
      rx0 = t - (int) t;
      rx1 = rx0 -1;

      t = vec[1] + N;
      by0 = ((int)t)&BM;
      by1 = (by0+1)&BM;
      ry0 = t - (int) t;
      ry1 = ry0 -1;

      i = p[ bx0 ];
      j = p[ bx1 ];

      b00 = p[ i + by0 ];
      b10 = p[ j + by0 ];
      b01 = p[ i + by1 ];
      b11 = p[ j + by1 ];

      sx = rx0*rx0*(3.0 - 2.0 * rx0);
      sy = rx0*rx0*(3.0 - 2.0 * ry0);

      q = g2[ b00 ] ; u = (rx0 * q[0] + ry0 * q[1]);
      q = g2[ b10 ] ; v = (rx1 * q[0] + ry0 * q[1]);
      a = lerp(sx, u, v);

      q = g2[ b01 ] ; u = (rx0 * q[0] + ry1 * q[1]);
      q = g2[ b11 ] ; v = (rx1 * q[0] + ry1 * q[1]);
      b = lerp(sx, u, v);

      return lerp(sy,a,b);
   }


   public static double lerp(double t, double a, double b)
   {
      return a+t*(b-a);
   }

   static
   {
      int i, j, k;
      double t;
      Random r = new Random(1);
      for (i = 0; i < B ; i++)
      {
	 p[i] = i;
	 t = ((double)(r.nextInt()&BM))/B;
	 g1[i] = 2.0 * t - 1.0;

	 for ( j=0; j<2 ; j++)
//	    g2[i][j] = (float)((r.nextInt()&BM % (B + B)) -B) /B;
	    g2[i][j] = (float)(r.nextInt() &BM) / B;
	 normalize2(g2[i]);
      }
      while (--i > 0)
      {
	 k = p[i];
	 j = r.nextInt() & BM;
	 p[i] = p[j];
	 p[j] = k;
      }
      for (i = 0; i < B+2; i++)
      {
	 p[B+i]  = p[i];
	 g1[B+i] = g1[i];
      }
   }
   static private void normalize2(double v[]) 
   {
      double s;

      s = Math.sqrt(v[0] * v[0] + v[1] * v[1]);
      v[0] = v[0] / s;
      v[1] = v[1] / s;
   }
}
