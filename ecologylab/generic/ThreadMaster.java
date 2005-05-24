package ecologylab.generic;

/**
 * Implement this class in an object that knows about many other threads.
 * Then, use the provided methods to have that object pause many
 * threads at once. This is done in order to let the CPU concentrate
 * on a specific intensive activity, like handling interactive drag and drop,
 * or pruning large collections.
 */
public interface
ThreadMaster
{
/**
 * Pause all the threads we know about.
 */
   public void pauseThreads();
/**
 * Unpause (continue) all the threads we know about.
 */
   public void unpauseThreads();
}
