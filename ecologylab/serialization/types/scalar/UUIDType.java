package ecologylab.serialization.types.scalar;

import java.util.UUID;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.types.MappingConstants;
import ecologylab.serialization.types.ScalarType;

public class UUIDType extends ScalarType<UUID>
implements MappingConstants
{

	public UUIDType()
	{
		this(UUID.class);
	}

	public UUIDType(Class<? extends UUID> clazz)
	{
		super(clazz, JAVA_UUID, DOTNET_UUID, null, null);
	}

	@Override
	public UUID getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		return UUID.fromString(value);
	}

}
