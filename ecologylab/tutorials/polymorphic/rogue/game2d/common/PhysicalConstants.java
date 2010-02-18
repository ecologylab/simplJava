/*
 * Created on Nov 16, 2006
 */
package ecologylab.tutorials.polymorphic.rogue.game2d.common;


public interface PhysicalConstants
{
    /**
     * Indicates the fraction of a second that will be simulated whenever a
     * simulate call is run.
     */
    public static final double       TIME_STEP         = 1.0;

    public static final double       EPSILON           = .01;

//    public static SpinnerNumberModel VISCOSITY         = new SpinnerNumberModel(
  //                                                             -0.09, -1.0,
    //                                                           0.0, -.01);

    public static final String       VIRTUAL_VISCOSITY = "VIRTUAL_VISCOSITY";

    public static final String       KC                = "KC";

    public static final String       KV                = "KV";

    public static final String       KF                = "KF";

    public static final String       GOAL_ATTRACTION   = "GOAL_ATTRACTION";

    public static final String       GOAL_REPULSION    = "GOAL_REPULSION";

    public static final String       BASE_REPULSION    = "BASE_REPULSION";
}
