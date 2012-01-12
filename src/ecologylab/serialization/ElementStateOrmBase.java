package ecologylab.serialization;

/**
 * a base class for GUI states, providing the default ORM id field.
 * 
 * @author quyin
 *
 */
public class ElementStateOrmBase<PES extends ElementState> extends ElementState<PES>
{

	private long	ormId;

	public long getOrmId()
	{
		return ormId;
	}

	public void setOrmId(long ormId)
	{
		this.ormId = ormId;
	}

}
