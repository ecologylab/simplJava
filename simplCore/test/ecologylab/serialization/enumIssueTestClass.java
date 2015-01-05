package ecologylab.serialization;

import java.util.ArrayList;

import ecologylab.serialization.annotations.FieldUsage;
import ecologylab.serialization.annotations.simpl_collection;

public class enumIssueTestClass {

	@simpl_collection
	private ArrayList<FieldUsage> ourUsages;
	
	@simpl_collection
	private ArrayList notGeneric;
	
	@simpl_collection
	private ArrayList<FieldDescriptor> composite;
	
}
