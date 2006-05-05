/*
 * Created on May 3, 2006
 */
package ecologylab.services;

/**
 * Interface that indicates the class has a start() and stop().
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public interface StartAndStoppable
{

    /**
     * Starts an asynchronous process.
     * 
     * @return true if the process was successfully started; false if it was not.
     */
    public boolean start();
    
    /**
     * Stops the asynchronous process.
     * 
     * @return true if the process was stopped, false otherwise.
     */
    public boolean stop();
    
}
