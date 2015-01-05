package ecologylab.tutorials.game;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.*;
import java.util.ArrayList;

public class GameData<T extends Threat> extends ElementState
{
  @simpl_scalar
  protected long                                timestamp;

  /** Number of game cycles remaining. */
  @simpl_scalar
  protected int                                cycRem;

  /**
   * Indicates that, if the game is running, it should be paused; by default, the game starts this
   * way and a user needs to activate it.
   */
  @simpl_scalar
  protected boolean                              paused  = false;

  @simpl_scalar
  protected boolean                              loaded  = false;
  
  /**
   * Game state flag indicating that the game is currently executing play cycles.
   */
  @simpl_scalar
  protected boolean                              running                  = false;

  /** Game state flag indicating that the players have won the game. */
  @simpl_scalar
  protected boolean                              won                      = false;

  /** List of Threat objects. */
  
  @simpl_classes(
  { Threat.class, SingleSeekerThreat.class, OrbitingThreat.class, RepellableThreat.class,
      PatrollingThreat.class })
  @simpl_collection
  protected ArrayList<T>                    threats                  = new ArrayList<T>();


  @simpl_scalar
  protected double                              score                    = 0;

  /** No-argument constructor, required for ElementState. */
  public GameData()
  {
    super();
  }
}