/*
 * Copyright 1996-2002 by Andruid Kerne. All rights reserved.
 * CONFIDENTIAL. Use is subject to license terms.
 */
package cm.generic;

import java.awt.Color;

/**
 * Tools for manipulating color spaces.
 */
public class Palette
{
   static float	delta  = .085f;
   
   Palette(){}

   // generate complementary color
   public static int Complement(Color c)
     {
	float	hsb[]	= new float[3];
	Color.RGBtoHSB (c.getRed(), c.getGreen(), c.getBlue(), hsb);
	hsb[0] += .5f;
	if (hsb[0] > 1)
	    hsb[0] -= 1;
	return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
     }

   public static Color complement(Color c)
   {
      return new Color(Complement(c));
   }
   // return 2 colors delta away from the input
   public static void Split(Color input, int[] output)
     {
	float	hsb[]	= new float[3];
	float   newHue;
	Color.RGBtoHSB (input.getRed(), input.getGreen(), input.getBlue(),
			hsb);
	newHue	= (hsb[0] + delta) % 1.0f;
	output[0]  = Color.HSBtoRGB(newHue, hsb[1], hsb[2]);
	newHue	= (hsb[0] - delta) % 1.0f;
	output[1]  = Color.HSBtoRGB(newHue, hsb[1], hsb[2]);
     }

   public static Color hsvColor(float h, float s, float v)
   {
      int rgb[] = rgb(h,s,v);
      return new Color(rgb[0], rgb[1], rgb[2]);
   }

   // hsv to rgb
   public static int[]	rgb(float hf, float s, float v)
   {
      float	r=0,g=0,b=0;
      float	h	= hf * 6;
      if (s == 0)
      {
	 r = v;
	 g = v;
	 b = v;
      }
      else
      {
	 int i = (int) Math.floor(h);
	 float f = h - i;
	 float p = v * (1 - s);
	 float q = v * (1 - s*f);
	 float t = v * (1 - s*(1-f));
	 switch (i)
	 {
	 case 0:
	    r = v;
	    g = t;
	    b = p;
	    break;
	 case 1:
	    r = q;
	    g = v;
	    b = p;
	    break;
	 case 2:
	    r = p;
	    g = v;
	    b = t;
	    break;
	 case 3:
	    r = p;
	    g = q;
	    b = v;
	    break;
	 case 4:
	    r = t;
	    g = p;
	    b = v;
	    break;
	 case 5:
	    r = v;
	    g = p;
	    b = q;
	    break;
	 }
      }
      int rgb[] = new int[3];
      rgb[0] = (int) (r * 255);
      rgb[1] = (int) (g * 255);
      rgb[2] = (int) (b * 255);
//      System.out.println("rgb( " +hf+","+s+","+v+" => "+
//			 rgb[0]+","+rgb[1]+","+rgb[2]);
      return rgb;
   }

   // hsv to rgb
   public static void rgb(float hf, float s, float v, float[] result)
   {
      float	r=0,g=0,b=0;
      float	h	= hf * 6;
      if (s == 0)
      {
	 r = v;
	 g = v;
	 b = v;
      }
      else
      {
	 int i = (int) Math.floor(h);
	 float f = h - i;
	 float p = v * (1 - s);
	 float q = v * (1 - s*f);
	 float t = v * (1 - s*(1-f));
	 switch (i)
	 {
	 case 0:
	    r = v;
	    g = t;
	    b = p;
	    break;
	 case 1:
	    r = q;
	    g = v;
	    b = p;
	    break;
	 case 2:
	    r = p;
	    g = v;
	    b = t;
	    break;
	 case 3:
	    r = p;
	    g = q;
	    b = v;
	    break;
	 case 4:
	    r = t;
	    g = p;
	    b = v;
	    break;
	 case 5:
	    r = v;
	    g = p;
	    b = q;
	    break;
	 }
      }
      result[0]	= r;
      result[1]	= g;
      result[2]	= b;
   }

   // from rgb to hsv
   public static float[] hsv(Color c)
   {
      // note that when saturation=0, hue is undefined (we'll set to 0)
      float	hsv[]	= new float[3];
      Color.RGBtoHSB (c.getRed(), c.getGreen(), c.getBlue(), hsv);
      float f = Math.min(hsv[1],.999f);
      hsv[1]  = f;
      f	      = Math.min(hsv[2],.999f);
      hsv[2]  = f;
      
//      System.out.println("hsv("+c+")="+hsv[0]+","+hsv[1]+","+hsv[2]);
      return hsv;
   }

   public static float[] myHsv(Color c)
   {
      return myHsv(c.getRed(), c.getGreen(), c.getBlue());
   }
   
   public static float[] myHsv(int ri, int gi, int bi)
   {
      float	r	= ((float) ri) / 255,
      		g	= ((float) gi) / 255,
      		b	= ((float) bi) / 255;
	 
      float h, s;
      int	isMax;		   // 0 red, 1 green, 2 blue
      // nb:	ties don't effect value calculation
      // 	ties here don't effect hue calculation due to code below
      // 	ties aren't effecting sat calcuation -- seems weak,
      // but actually, photoshop seems that way, too
      float max;
      float min	= Math.min(Math.min(r,g), b);
      if (r >= b)
      {
	 if (r >= g)
	 {
	    max		= r;	   // perhaps r==b or r==g
	    isMax	= 0;
	 }
	 else			   // perhaps r==b; g is true max
	 {
	    max		= g;
	    isMax	= 1;
	 }
      }
      else
      {
	 if (g >= b)
	 {
	    max		= g;	   // perhaps g==b
	    isMax	= 1;
	 }
	 else
	 {
	    max		= b;	   // b is true max
	    isMax	= 2;
	 }
      }

      float delta = max - min;

      // calculate saturation
      if (max == 0)
	 s	= 0;		   
      else
	 s	= delta / max;	   // (s == 0) also if delta = 0

      // calculate hue
      if (s == 0)
	 h	= (float) Math.random(); // hue is undefined when sat == 0
      else
      {
	 // red	= 0		yellow	= 60 (1)	green	= 120 (2)
	 // cyan= 180 (3)	blue	= 240 (4)	magenta = 300 (5)
	 // scale of 0 to 6, intitally
	 switch (isMax)
	 {
	 case 0:		   // red is max
	    if (r == b)
	       h	= 5;		   // pure magenta
	    else if (r == g)
	       h	= 1;		   // pure yellow
	    else
	       h	= (g-b) / delta;   // between yellow & magenta
	    break;
	 case 1:		   // green is max
	    if (g == b)
	       h	= 3;		   // pure cyan
	    else
	       h	= 2 + (b-r) / delta; // between cyan & yellow
	    break;
	 default:		   // case 2: blue is max
	    h		= 4 + (r-g) / delta; // between magenta & cyan
	 }
	 h	= h * 60;
	 if (h < 0)
	    h  += 360;
	 h	= h / 360;
	 
      }
      float hsv[]	= {h, s, max};
      
//      System.out.println("myHsv("+r+","+g+","+b+")="+
//			 hsv[0]+","+hsv[1]+","+hsv[2]);
      return hsv;
   }
   public static float hue(Color c)     { return myHsv(c)[0]; }
   public static float sat(Color c)     { return myHsv(c)[1]; }
   public static float value(Color c)   { return myHsv(c)[2]; }
   
   public static Color value(Color c, float newValue)
   {
      float	hsv[] = myHsv(c);
      return Color.getHSBColor(hsv[0], hsv[1], newValue);
   }
   public static Color nearby(Color c, int HSorB, float factor)
   {
      float	hsb[] = new float[3], changer;
      // ??? could perhaps make it smaller w shifts and &&
      Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);
//      System.out.println("Palette.nearby() " + hsb);
      changer = (hsb[HSorB] + MoreMath.pM(MoreMath.randGain(.35f), factor)
		 + 1.0f)
	        % 1.0f;
      hsb[HSorB] = changer;
      return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
  }
   
   public static Color hsb(float hue, float saturation, float brightness)
     { return new Color(Color.HSBtoRGB(hue, saturation, brightness)); }
//   { return Color.getHSBColor(hue, saturation, brightness); }


}
