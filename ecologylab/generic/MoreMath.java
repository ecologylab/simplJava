/*
 * Copyright 1996-2002 by Andruid Kerne. All rights reserved.
 * CONFIDENTIAL. Use is subject to license terms.
 */
package cm.generic;

import java.lang.Math;
import java.util.Vector;

/**
 * Mathematical conveniences beyond @link java.lang.Math
 */
public class MoreMath
{
   MoreMath() {}
   // random integer <= max
   // an int: [0, max]
   public static int rand(int max)
   {
      return (int) (Math.random() * (max + 1));
   }
   // an int: [0, max - 1]
   public static int random(int max)
   {
      return (int) (Math.random() * max);
   }
   public static float random(float max)
   {
      return (float) Math.random() * max;
   }
   // heads i win.  tails you lose.
   public static boolean tossCoin() { return rand(1) == 1; }
   // choices	:= array of pairs
   // each pair	:= value probability
   public static float pick(float[][] choices)
     {
	float	sum = 0, indicator, bracket = 0;
	for (int i=0; i != choices.length; i++)
	     sum       += choices[i][1];
	indicator	= (float) Math.random() * sum;
	for (int i=0; i != choices.length; i++)
	  {
	     bracket	+= choices[i][1];
	     if (indicator <= bracket)
		  return choices[i][0];
	  }
	throw new Error("MoreMath.pick() didnt pick any");
     }
   
   public static Vector randomize(Vector v)
     {
	// swap the random pick with the ith pick (starts w the last one)
	// then do it again, without the last one (its been randomized)
	for (int i=v.size(); i != 0; i--)
	  {
	     int pick	= (int) (Math.random() * i);
	     // swap em
	     Object o	= v.elementAt(i - 1);
	     v.setElementAt(v.elementAt(pick), i - 1);
	     v.setElementAt(o, pick);
	  }
	return v;
     }
   
/**
 * push a to 1 extreme or the other, based on b
 */
      public static float bias(float a, float b)
     {
	if (a < .001)
	     return 0;
	else if (a > .999)
	     return 1;
	else if (b < .001)
	     return 0;
	else if (b > .999)
	     return 1;
	else
	     return (float)(Math.pow(a, Math.log(b) / Math.log(0.5)));
     }
/**
 * push a towards middle (b < .5), or extrema (b > .5).
 * 
 * could also be called "contrast".
 */
   public static float gain(float a, float b)
   {
      float p;

      if (a < .001f)
	 return 0;
      else if (a > .999)
	 return 1;
      b = (b < .001f) ? .001f : (b > .999f) ? .999f : b;
      p = (float) (Math.log(1 - b) / Math.log(0.5f));
      if (a < 0.5f)
	 return (float) (Math.pow(2 * a, p) / 2);
      else
	 return (float) (1 - Math.pow(2 * (1 - a), p) / 2);
   }
   public static float randGain(float factor)
	{ return MoreMath.gain(random(), factor); }
   public static float randBias(float factor)
	{ return MoreMath.bias(random(), factor); }
   // plus or minus (op is 0 to 1)
   public static float pM(float op, float factor) 
     { return (op - .5f) * factor;}
   // plus or minus (op is 0 to 1)
   public static float randPM(float factor) 
     { return pM(random(), factor);}
   public static float toExtremes(float x)
   {
      float factor	= bias(random(), .8f);
      return gain(x, factor);
   }
   public static float random() { return (float) Math.random(); }
   static public void main(String[] s)
   {
      for (int i=0; i<10; i++)
      {
	 float ifl	= .1f * i;
	 System.out.print("ifl: " + ifl + "\t");
	 for (int j=0; j<10; j++)
	 {
	    float jf	= .1f * j;
	    
	    System.out.print(MoreMath.bias(ifl, jf) + "\t");
	 }
	 System.out.println("");
      }
   }
}
