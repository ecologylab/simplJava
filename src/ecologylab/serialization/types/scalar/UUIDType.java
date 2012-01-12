package ecologylab.serialization.types.scalar;

import java.util.UUID;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.types.CrossLanguageTypeConstants;
import ecologylab.serialization.types.ScalarType;

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
