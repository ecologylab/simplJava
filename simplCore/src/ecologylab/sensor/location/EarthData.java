/**
 * 
 */
package ecologylab.sensor.location;

/**
 * Constants about the earth size and shape, for geo location calculations.
 */
public interface EarthData
{
	/** Radius of earth, in miles. */
	public static final double	RADIUS_EARTH					= 3959.0;

	/** Radius of earth, in feet. */
	public static final double	RADIUS_EARTH_FEET			= RADIUS_EARTH * 5280.0;

	/** Radius of earth, in kilometers. */
	public static final double	RADIUS_EARTH_K_METERS	= 6371.0;

	/** Radius of earth, in meters. */
	public static final double	RADIUS_EARTH_METERS		= RADIUS_EARTH_K_METERS * 1000.0;
}
