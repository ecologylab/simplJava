package ecologylab.standalone.researchnotebook;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.ElementState.xml_tag;

public class CompositionState extends ElementState{
	@simpl_scalar float version;
	@simpl_scalar String save_agent_state;
	@simpl_scalar int cool_space_size; 
	
	@simpl_composite Preferences preferences; 
	@simpl_composite SeedSet seed_set; 
	@simpl_composite SiteSet site_set; 
	@simpl_composite TraversableSet traversable_set; 
	@simpl_composite UntraversableSet untraversable_set; 
	@simpl_composite RejectDomainSet reject_domain_set; 
	@simpl_composite ContainerSet container_set; 
	@simpl_composite SurrogateSet surrogate_set; 
	@simpl_composite InterestModel interest_model; 
	
	public void setState(String isTrue){
		save_agent_state = isTrue; 
	}
	
	public void setSize(int size){
		cool_space_size = size; 
	}
}
