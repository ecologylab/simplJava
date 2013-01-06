package ecologylab.tests;

import java.io.File;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.Format;

public class TestReferenceAndPrimitiveScalars extends ElementState
{

	@simpl_scalar
	Float		nullRFloat			= null;

	@simpl_scalar
	Float		nonNullRFloat		= 3.5f;

	@simpl_scalar
	float		nonNullFloat		= -746.934f;

	@simpl_scalar
	Integer	nullRInteger		= null;

	@simpl_scalar
	Integer	nonNullRInteger	= 13245;

	@simpl_scalar
	int			nonNullInteger	= -9876;

	@simpl_scalar
	Boolean	nullRBoolean		= null;

	@simpl_scalar
	Boolean	nonNullRBoolean	= false;

	@simpl_scalar
	boolean	nonNullBoolean	= true;

	@simpl_scalar
	Double	nullRDouble			= null;

	@simpl_scalar
	Double	nonNullRDouble	= 87234.258;

	@simpl_scalar
	double	nonNullDouble		= 234235.25;

	@simpl_scalar
	Long		nullRLong				= null;

	@simpl_scalar
	Long		nonNullRLong		= 28357902L;

	@simpl_scalar
	long		nonNullLong			= 298349028390L;

	public TestReferenceAndPrimitiveScalars()
	{

	}

	public void append(StringBuilder sb, String prefix, Object o)
	{
		sb.append(prefix + ": " + o + "\n");
	}

	@Override
	public String toString()
	{
		StringBuilder res = new StringBuilder();

		append(res, "null reference Boolean", nullRBoolean);
		append(res, "nonnull reference Boolean", nonNullRBoolean);
		append(res, "nonnull primitive Boolean", nonNullBoolean);

		append(res, "null reference Integer", nullRInteger);
		append(res, "nonnull reference Integer", nonNullRInteger);
		append(res, "nonnull primitive Integer", nonNullInteger);

		append(res, "null reference Long", nullRLong);
		append(res, "nonnull reference Long", nonNullRLong);
		append(res, "nonnull primitive Long", nonNullLong);

		append(res, "null reference Float", nullRFloat);
		append(res, "nonnull reference Float", nonNullRFloat);
		append(res, "nonnull primitive Float", nonNullFloat);

		append(res, "null reference Double", nullRDouble);
		append(res, "nonnull reference Double", nonNullRDouble);
		append(res, "nonnull primitive Double", nonNullDouble);

		return res.toString();
	}

	public static void main(String[] args)
	{
		TestReferenceAndPrimitiveScalars t = new TestReferenceAndPrimitiveScalars();
		System.out.println("before:\n" + t);

		try
		{
			SimplTypesScope.serialize(t, new File("tes.xml"), Format.XML);

			TestReferenceAndPrimitiveScalars tt = (TestReferenceAndPrimitiveScalars) SimplTypesScope
					.get("test", TestReferenceAndPrimitiveScalars.class).deserialize(new File("tes.xml"),
							Format.XML);
			System.out.println("---------------------\nafter:\n" + tt);
		}
		catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
		}
	}
}
