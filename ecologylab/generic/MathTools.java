/*
 * Compiled by Andruid Kerne, Texas A&M University and Creating Media LLC.
 * Copyright 1996-2002 by Andruid Kerne. 
 * Use freely without profit, so long as this notice is included intact.
 */
package ecologylab.generic;

import java.lang.Math;
import java.util.Vector;

/**
 * Mathematical tools for shaping ranges of values;
 * wrappers beyond @link java.lang.Math. 
 * Includes various falvors of random.
 */
public class MathTools
{
   MathTools() {}
/**
 * @return	a random float (instead of a double)
 */
   public static float random() { return (float) Math.random(); }
/**
 * @return	a random integer <= max
 */
   public static int rand(int max)
   {
      return (int) (Math.random() * (max + 1));
   }
   // an int: [0, max - 1]
/**
 * @return	a random integer < max
 */
   public static int random(int max)
   {
      return (int) (Math.random() * max);
   }
/**
 * @return	a random float < max
 */
   public static float random(float max)
   {
      return (float) Math.random() * max;
   }
/**
 * heads i win.  tails you lose.
 */
   public static boolean tossCoin() { return rand(1) == 1; }
/**
 * @param	choices	:= array of pairs;
 *		each pair := [value, probability]
 */
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
 * push <code>a</code> to 1 extreme or the other, based on b
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
	{ return MathTools.gain(random(), factor); }
   public static float randBias(float factor)
	{ return MathTools.bias(random(), factor); }
/**
 * plus or minus
 * @param	op is 0 to 1
 */
   public static float pM(float op, float factor) 
     { return (op - .5f) * factor;}

/**
 * random plus or minus
 * @param factor the magnitude of the desired result.
 * 
 * @return	a random number in the interval [-factor, factor].
 */
   public static float randPM(float factor) 
     { return pM(random(), factor);}

   public static float toExtremes(float x)
   {
      float factor	= bias(random(), .8f);
      return gain(x, factor);
   }
}
