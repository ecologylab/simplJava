package ecologylab.collections;

public interface RunnablePool
{

	public void start();
	public void stop();
	public void pause();
	public void unpause();
	public void toggleCollectingAgent();
}
