package simpl.core;

import java.util.LinkedList;
import java.util.List;

import simpl.descriptions.ClassDescriptor;

public class SimplTypesScopeFactory {
	
	/**
	 * Represent the core data behind the factory process... pass it along
	 * between the different methods so that the data can be used at create();
	 */
	final class STSFactoryData
	{
		public String stsName;
		public List<Class<?>> translations;
		public List<SimplTypesScope> scopesInherited;
		
		public STSFactoryData()
		{
			this.translations = new LinkedList<Class<?>>();
			this.scopesInherited = new LinkedList<SimplTypesScope>();
		}
	}
	private static SimplTypesScopeFactory factory = new SimplTypesScopeFactory();
	
	public static STSNameCompleted name(String name)
	{
		STSFactoryData ourData = factory.new STSFactoryData();
		
		return factory.new STSNameCompleted(ourData, name);
	}
	
	final class STSNameCompleted
	{
		private STSFactoryData ourData;
		
		private STSNameCompleted(STSFactoryData sfd, String name)
		{
			this.ourData = sfd;
			this.ourData.stsName = name;
		}
		
		public STSInheritsCompleted inherits(SimplTypesScope... scopesInherited)
		{
			return factory.new STSInheritsCompleted(this.ourData, scopesInherited);
		}
		
		public STSTranslationsCompleted translations(Class<?> ... translationClasses)
		{
			return factory.new STSTranslationsCompleted(this.ourData, translationClasses);
		}
	}
	
	final class STSInheritsCompleted
	{
		private STSFactoryData ourData;
		public STSInheritsCompleted(STSFactoryData data, SimplTypesScope... scopesInherited)
		{
			this.ourData = data;
			
			List<SimplTypesScope> stses = new LinkedList<SimplTypesScope>();
			
			if(scopesInherited.length < 1)
			{
				throw new RuntimeException("Must have at least one inherited scope!");
			}
			
			for(SimplTypesScope sts : scopesInherited)
			{
				stses.add(sts);
			}
			
			this.ourData.scopesInherited = stses;
		}
		
		public STSTranslationsCompleted translations(Class<?>... translationClasses)
		{
			return factory.new STSTranslationsCompleted(this.ourData, translationClasses);
		}
		
	}
	
	final class STSTranslationsCompleted
	{

		STSFactoryData ourData;
		public STSTranslationsCompleted(STSFactoryData ourData,
				Class<?>[] translationClasses) {
			this.ourData = ourData;

			if(translationClasses.length < 1)
			{
				throw new RuntimeException("Must have at least one class to translate!");
			}
			
			for(Class<?> c : translationClasses)
			{
				ourData.translations.add(c);
			}
		}

		public SimplTypesScope create()
		{
			SimplTypesScope sts = new SimplTypesScope();
			sts.setName(this.ourData.stsName);
			
			for(SimplTypesScope parentSTS: this.ourData.scopesInherited)
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
