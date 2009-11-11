package ecologylab.generic;

public abstract class CappedResourcePool<T> extends ResourcePool<T>
{
	private Object releaseCondVar = new Object();
	
	private int maximumSize;
	
	protected CappedResourcePool(boolean instantiateResourcesInPool, int initialPoolSize,
			int minimumPoolSize, int maximumSize, boolean checkMultiRelease)
	{
		super(instantiateResourcesInPool, initialPoolSize, minimumPoolSize, checkMultiRelease);
		this.maximumSize = maximumSize;
	}

	protected synchronized void onRelease(T resourceToRelease)
	{
		this.notify();
	}
	
	protected synchronized void onAcquire()
	{
		while(this.getPoolSize() == 0 && this.getCapacity() * 2 > maximumSize)
		{
			Debug.println("Waiting for pool to free up!");
			try
			{
				this.wait();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
	}
}
