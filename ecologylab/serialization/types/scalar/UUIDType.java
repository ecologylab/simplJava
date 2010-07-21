package ecologylab.serialization.types.scalar;

import java.util.UUID;

import ecologylab.serialization.ScalarUnmarshallingContext;

public class UUIDType extends ScalarType {

	public UUIDType()
	{
		super(UUID.class);
	}
	
	public UUIDType(Class<? extends UUID> clazz)
	{
		super(clazz);
	}
	
	@Override
	public String getCSharptType() {
		return MappingConstants.DOTNET_UUID;
	}

	@Override
	public String getDbType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext) {
		return UUID.fromString(value);
	}

	@Override
	public String getObjectiveCType() {
		// TODO Auto-generated method stub
		return null;
	}

}
