/*
 * Created on Apr 13, 2007
 */
package ecologylab.services.logging.playback;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import ecologylab.services.logging.MixedInitiativeOp;

public abstract class View<T extends MixedInitiativeOp> extends JPanel
{
    protected T currentOp;

    protected boolean     loaded = false;

    protected int width;
    protected int height;
    
    public View(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override protected void paintComponent(Graphics arg0)
    {
        if (loaded)
            this.render(arg0);
        else
            this.renderLoading(arg0);
    }

    protected void load(T firstOp)
    {
        this.loaded = true;
        
        this.currentOp = firstOp;
    }
    
    protected abstract void render(Graphics g);

    protected void renderLoading(Graphics g)
    {
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        g.setColor(Color.BLACK);

        g.drawString("No log file loaded.", 10, 10);
    }

    public void setLoaded(boolean loaded)
    {
        this.loaded = loaded;
    }

    public void changeOp(T newOp)
    {
        this.currentOp = newOp;
        this.repaint();
    }

    /**
     * Indicates whether or not this View has a KeyListener object.
     * 
     * @return
     */
    public boolean hasKeyListenerSubObject()
    {
        return false;
    }

    /**
     * If this View contains a KeyListener object (for example, to enable some
     * sort of keyboard interaction with the log frame); this method returns it.
     * 
     * If hasKeyListener() returns true, then this method must return a
     * KeyListener object; otherwise, it should return null.
     * 
     * @return
     */
    public KeyListener getKeyListenerSubObject()
    {
        return null;
    }
    
    /**
     * Indicates whether or not this View has an ActionListener object.
     * 
     * @return
     */
    public boolean hasActionListenerSubObject()
    {
        return false;
    }

    /**
     * If this View contains a ActionListener object (for example, to enable some
     * sort of interaction with the log frame); this method returns it.
     * 
     * If hasActionListener() returns true, then this method must return a
     * KeyListener object; otherwise, it should return null.
     * 
     * @return
     */
    public ActionListener getActionListenerSubObject()
    {
        return null;
    }
}
