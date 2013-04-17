/**
 * 
 */
package ecologylab.serialization.library.jnlp.application;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;
import simpl.core.ElementState;
import simpl.core.SimplTypesScope;
import simpl.core.TranslationContext;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.StringFormat;

import ecologylab.appframework.types.prefs.PrefSet;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class ApplicationDesc extends ElementState
{
	@simpl_scalar
	@simpl_tag("main-class")
	String						mainClass;

	@simpl_nowrap
	@simpl_collection("argument")
	ArrayList<String>	arguments							= new ArrayList<String>();

	/**
	 * The PrefSet that is to be passed as an argument through the JNLP file. This object is
	 * serialized separately and added to the list of arguments at serialization time. After the JNLP
	 * file has been serialized, it is removed from the arguments. It may be safely modified between
	 * serializations to provide different sets of prefs.
	 */
	private PrefSet		prefSet;

	private int				prefSetArgumentIndex	= -1;

	/**
	 * 
	 */
	public ApplicationDesc()
	{
		super();
	}

	/**
	 * Add the argument to the JNLP application description.
	 * 
	 * @param argument
	 */
	public void add(String argument)
	{
		this.arguments.add(argument);
	}

	public ArrayList<String> getArguments()
	{
		return arguments;
	}

	/**
	 * @return the prefSet
	 */
	public PrefSet getPrefSet()
	{
		return prefSet;
	}

	/**
	 * @param prefSet
	 *          the prefSet to set
	 */
	public void setPrefSet(PrefSet prefSet)
	{
		this.prefSet = prefSet;
	}

	/**
	 * @see simpl.core.ElementState#serializationPreHook()
	 */
	@Override
	public void serializationPreHook(TranslationContext translationContext)
	{
		if (prefSet != null)
		{
			try
			{

				this.add(URLEncoder.encode(SimplTypesScope.serialize(prefSet, StringFormat.XML).toString(),
						"UTF-8"));

				this.prefSetArgumentIndex = this.arguments.size() - 1;
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			catch (SIMPLTranslationException e)
			{
				e.printStackTrace();
			}
		}

		super.serializationPreHook(translationContext);
	}

	/**
	 * @see simpl.core.ElementState#serializationPostHook()
	 */
	@Override
	public void serializationPostHook(TranslationContext translationContext)
	{
		if (this.prefSetArgumentIndex > -1)
		{ // we need to remove it from the arguments list
			this.arguments.remove(this.prefSetArgumentIndex);

			this.prefSetArgumentIndex = -1;
		}

		super.serializationPostHook(translationContext);
	}
}