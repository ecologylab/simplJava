package ecologylab.generic;

public abstract class CappedResourcePool<T> extends ResourcePool<T>
{
	private int maximumSize;
	
	protected CappedResourcePool(boolean instantiateResourcesInPool, int initialPoolSize,
			int minimumPoolSize, int maximumSize, boolean checkMultiRelease)
	{
		super(instantiateResourcesInPool, initialPoolSize, minimumPoolSize, checkMultiRelease);
		this.maximumSize = maximumSize;
	}

	@Override
	protected synchronized void onRelease(T resourceToRelease)
	{
		this.notify();
	}
	
	@Override
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
