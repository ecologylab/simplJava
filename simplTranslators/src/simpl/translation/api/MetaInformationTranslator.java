package simpl.translation.api;

import ecologylab.serialization.MetaInformation;


/**
 * A component that translates "MetaInformation" (that is, classes representing annotations or attributes in a target lanugage) 
 * into source code for a language
 * @author twhite
 *
 */
public abstract class MetaInformationTranslator extends BaseTranslator{
	public abstract SourceAppender translateMetaInformation(MetaInformation metaInfo);
}
