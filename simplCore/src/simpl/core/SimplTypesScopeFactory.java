package simpl.core;

import java.util.LinkedList;
import java.util.List;

import simpl.core.SimplTypesScopeFactory.STSTranslationsCompleted;
import simpl.descriptions.ClassDescriptor;
import simpl.types.TypeRegistry;

public class SimplTypesScopeFactory {
	
	static
	{
		TypeRegistry.init();
	}
	/**
	 * Represent the core data behind the factory process... pass it along
	 * between the different methods so that the data can be used at create();
	 */
	final class STSFactoryData
	{
		public String stsName;
		public List<Class<?>> translations;
		public List<ISimplTypesScope> scopesInherited;
		
		public STSFactoryData()
		{
			this.translations = new LinkedList<Class<?>>();
			this.scopesInherited = new LinkedList<ISimplTypesScope>();
		}
	}

	private static SimplTypesScopeFactory factory = new SimplTypesScopeFactory();
	
	public static STSNameCompleted name(String name)
	{
		STSFactoryData ourData = factory.new STSFactoryData();
		
		return factory.new STSNameCompleted(ourData, name);
	}
	
	public final class STSNameCompleted
	{
		private STSFactoryData ourData;
		
		private STSNameCompleted(STSFactoryData sfd, String name)
		{
			this.ourData = sfd;
			this.ourData.stsName = name;
		}
		
		public STSInheritsCompleted inherits(ISimplTypesScope... ists)
		{
			return factory.new STSInheritsCompleted(this.ourData, ists);
		}
		
		public STSInheritsCompleted inherits(String... simplTypesScopeNames)
		{
			List<ISimplTypesScope> scopes = new LinkedList<ISimplTypesScope>();
			
			for(String stsName : simplTypesScopeNames)
			{
				ISimplTypesScope val = SimplTypesScope.get(stsName);
				if(val != null)
				{
					scopes.add(val);
				}
			}
			
			return factory.new STSInheritsCompleted(this.ourData, scopes);
		}
		
		public STSTranslationsCompleted translations(Class<?> ... translationClasses)
		{
			return factory.new STSTranslationsCompleted(this.ourData, translationClasses);
		}
	}
	
	public final class STSInheritsCompleted
	{
		private STSFactoryData ourData;
	
		public STSInheritsCompleted(STSFactoryData data, List<ISimplTypesScope> scopesInherited)
		{
			this.ourData = data;
			
			this.ourData.scopesInherited = scopesInherited;

		}
		
		public STSInheritsCompleted(STSFactoryData data, ISimplTypesScope... scopesInherited)
		{
			this.ourData = data;

			List<ISimplTypesScope> stses = new LinkedList<ISimplTypesScope>();
			
			if(scopesInherited.length < 1)
			{
				throw new RuntimeException("Must have at least one inherited scope!");
			}
			
			for(ISimplTypesScope sts : scopesInherited)
			{
				stses.add(sts);
			}
			
			this.ourData.scopesInherited = stses;

		}
		
		
		public STSTranslationsCompleted translations(Class<?>... translationClasses)
		{
			return factory.new STSTranslationsCompleted(this.ourData, translationClasses);
		}

		public STSTranslationsCompleted translations(Class[] translations,
				Class... additionalTranslations) {
			// TODO Auto-generated method stub
			List<Class<?>> c = new LinkedList<Class<?>>();
			for(Class<?> k : translations)
			{
				c.add(k);
			}
			for(Class<?> k : additionalTranslations)
			{
				c.add(k);
			}

			Class<?>[] array = new Class<?>[c.size()];
			c.toArray(array);
			
			return new STSTranslationsCompleted(this.ourData, array);
		}
		
	}
	
	public final class STSTranslationsCompleted
	{
		STSFactoryData ourData;
		public STSTranslationsCompleted(STSFactoryData ourData,
				Class<?>[] translationClasses) {
			this.ourData = ourData;
			
			// Maybe we should have an invariant for empty STS.
			// TODO: THink about it. 
			
			if(translationClasses != null)
			{
				for(Class<?> c : translationClasses)
				{
					ourData.translations.add(c);
				}
			}
		}

		public ISimplTypesScope create()
		{
			ISimplTypesScope sts = new SimplTypesScope();
			sts.setName(this.ourData.stsName);
			
			for(ISimplTypesScope parentSTS: this.ourData.scopesInherited)
			{
				sts.inheritFrom(parentSTS);
			}
			
			for(Class<?> lass : ourData.translations)
			{
				sts.addTranslation(lass);
			}
			
			SimplTypesScope.registerSimplTypesScope(sts.getName(), sts);
			return sts;
		}
	}
}
