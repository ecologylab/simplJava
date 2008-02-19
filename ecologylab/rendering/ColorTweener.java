/**
 * 
 */
package ecologylab.rendering;

import java.awt.Color;
import java.util.HashMap;

/**
 * A Tweener object is used to determine intermediate animation states between
 * two paths, specified by same-length lists of points. It does not create any
 * steps, but lazily instantates them and stores them in a HashMap for quick
 * retrevial if they are ever requested again.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class ColorTweener
{
    Color                   c1;

    Color                   c2;

    HashMap<Integer, Color> intermediateStates = new HashMap<Integer, Color>();

    final int               maxStates;

    public ColorTweener(Color c1, Color c2, int maxStates)
    {
        this.maxStates = maxStates;

        this.c1 = c1;
        this.c2 = c2;

        intermediateStates.put(0, c1);
        intermediateStates.put(maxStates, c2);
    }

    /**
     * Retrieves an intermediate animation state.
     * 
     * @param position
     * @return
     */
    public Color getState(int position)
    {
        if (position > this.maxStates)
            position = maxStates;
        if (position < 0)
            position = 0;

        Color state = intermediateStates.get(position);

        if (state == null)
        {
            int baseR, baseG, baseB, baseA, endR, endG, endB, endA;

            baseR = c1.getRed();
            baseG = c1.getGreen();
            baseB = c1.getBlue();
            baseA = c1.getAlpha();

            endR = c2.getRed();
            endG = c2.getGreen();
            endB = c2.getBlue();
            endA = c2.getAlpha();

            int adjR, adjG, adjB, adjA;

            adjR = (position * (endR - baseR)) / maxStates;
            adjG = (position * (endG - baseG)) / maxStates;
            adjB = (position * (endB - baseB)) / maxStates;
            adjA = (position * (endA - baseA)) / maxStates;

            state = new Color(baseR + adjR, baseG + adjG, baseB + adjB, baseA
                    + adjA);

            intermediateStates.put(position, state);
        }

        return state;
    }

    /**
     * Can be used to replace certain states with other Areas. The main reason
     * to do this is if certain animation states are better represented using
     * other shapes, such as ellipses, that cannot be represented as a series of
     * points.
     * 
     * The originally-specified shapes are stored separately, so calling this
     * method will not affect them, nor will it affect how the intermediate
     * states are represented.
     * 
     * @param position
     * @param state
     */
    public void setState(int position, Color state)
    {
        intermediateStates.put(position, state);
    }

    /**
     * @return the maxState
     */
    public int getMaxStates()
    {
        return maxStates;
    }
}
