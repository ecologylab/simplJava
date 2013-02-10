package simpl.types.scalar;

import java.util.UUID;

import simpl.annotations.dbal.simpl_inherit;
import simpl.core.ScalarUnmarshallingContext;
import simpl.types.CrossLanguageTypeConstants;
import simpl.types.ScalarType;


@simpl_inherit
public class UUIDType extends ScalarType<UUID>
implements CrossLanguageTypeConstants
{

	public UUIDType()
	{
		this(UUID.class);
	}

	public UUIDType(Class<? extends UUID> clazz)
	{
		super(clazz, DOTNET_UUID, null, null);
	}

	@Override
	public UUID getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		return UUID.fromString(value);
	}

}
