package ecologylab.serialization;

import java.util.ArrayList;

import simpl.annotations.dbal.FieldUsage;
import simpl.annotations.dbal.simpl_collection;
import simpl.descriptions.FieldDescriptor;


public class enumIssueTestClass {

	@simpl_collection
	private ArrayList<FieldUsage> ourUsages;
	
	@simpl_collection
	private ArrayList notGeneric;
	
	@simpl_collection
	private ArrayList<FieldDescriptor> composite;
	
}
