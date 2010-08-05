package ecologylab.standalone.researchnotebook;

import ecologylab.serialization.ElementState;

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
}
