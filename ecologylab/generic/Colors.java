/*
 * Copyright 1996-2002 by Andruid Kerne. All rights reserved.
 * CONFIDENTIAL. Use is subject to license terms.
 */
package cm.generic;

/**
 * Constants for RGB/Alpha and HSV color space operations.
 */
public interface Colors
{
   public static final int	HUE	= 0;
   public static final int	SAT	= 1;
   public static final int	VAL	= 2;
   
   public static final int	ALPHA		= 0xff000000;
   public static final int	RGB		= 0xffffff;
   public static final int	R		= 0xff0000;
   public static final int	G		= 0xff00;
   public static final int	B		= 0xff;

   public static final float	YELLOW	= 60.0f/360.0f;
   public static final float	RED	= 0/360.0f;
   public static final float	GREEN	= 120.0f/360.0f;
   public static final float	ORANGE	= 30.0f/360.0f;
   public static final float	MAGENTA	= 312.0f/360.0f;
//   public static final float	MAGENTA	= 300.0f/360.0f;
   public static final float	PURPLE	= 280.0f/360.0f;
   public static final float	BLUE	= 248.0f/360.0f;
   public static final float	YELLOW_GREEN	= 90.0f/360.0f;
   public static final float	RED_ORANGE	= 15.0f/360.0f;
   public static final float	BLUE_MAGENTA	= 270.0f/360.0f;
   public static final float	CYAN	= 200.0f/360.0f;
   public static final float	YELLOW_ORANGE	= 45.0f/360.0f;
   public static final float	RED_MAGENTA	= 150.0f/360.0f;
   
}
