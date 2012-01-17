package ecologylab.translators.javascript.test;

import java.io.File;
import java.io.IOException;

import ecologylab.semantics.collecting.MetaMetadataRepositoryInit;
import ecologylab.semantics.metametadata.MetaMetadata;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.semantics.metametadata.MetaMetadataRepositoryLoader;
import ecologylab.semantics.metametadata.MetaMetadataTranslationScope;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.SimplTypesScope.GRAPH_SWITCH;
import ecologylab.serialization.formatenums.Format;
import ecologylab.serialization.formatenums.StringFormat;
import ecologylab.translators.javascript.JavascriptTranslator;

public class MMDToJS
{
	public static void main(String[] args) throws IOException, SIMPLTranslationException
	{
		//MMD In JSON, that needs to be deserialized by simpl.js.
		//{"meta_metadata":{"name":"amazon_product","package":"ecologylab.semantics.generated.library.products","schema_org_itemtype":"http://schema.org/Product","user_agent_name":"firefox_3_6_8","user_agent_string":"Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.2.8) Gecko/20100722 BTRS86393 Firefox/3.6.8 ( .NET CLR 3.5.30729; .NET4.0C)","parser":"xpath","extends":"product","redirect_handling":"REDIRECT_USUAL","visibility":"GLOBAL","kids":[{"composite":{"name":"department","xpath":"//div[@id='navbar']//table[@id='navCatSubnav']//a[@class='navCatA']","type":"document","kids":[{"scalar":{"name":"title","xpath":"."}},{"scalar":{"name":"location","xpath":"./@href","hide":"True"}}]}},{"scalar":{"name":"price","xpath":"//b[@class='priceLarge']","scalar_type":"ecologylab.semantics.metadata.scalar.types.MetadataStringScalarType"}},{"scalar":{"name":"title","xpath":"//span[@id='btAsinTitle']","style":"metadata_h1","layer":"10","navigates_to":"location","scalar_type":"ecologylab.semantics.metadata.scalar.types.MetadataStringScalarType"}},{"scalar":{"name":"description","xpath":"//a[@id='technical_details']/following-sibling::div[1]","layer":"9","scalar_type":"ecologylab.semantics.metadata.scalar.types.MetadataStringScalarType"}},{"scalar":{"name":"image_url","xpath":"//td[@id='prodImageCell']//img/@src","hide":"True","scalar_type":"ecologylab.semantics.metadata.scalar.types.MetadataParsedURLScalarType"}},{"scalar":{"name":"reviews_location","xpath":"//span[@ref='dp_top_cm_cr_acr_pop_']/a/@href","hide":"True","scalar_type":"ecologylab.semantics.metadata.scalar.types.MetadataParsedURLScalarType"}},{"scalar":{"name":"overall_rating","xpath":".//span[1]/@title","context_node":"reviews_span","navigates_to":"reviews_location","scalar_type":"ecologylab.semantics.metadata.scalar.types.MetadataStringScalarType"}},{"collection":{"name":"reviews","xpath":"//div[@class='content']/table/tbody/tr[2]/td[1]/div[position() < last()-1]","schema_org_itemtype":"http://schema.org/Review","child_type":"product_review","kids":[{"scalar":{"name":"rating","xpath":"./div[2]/span/span[1]/@title","layer":"20"}},{"scalar":{"name":"content","xpath":".","layer":"10"}}]}},{"composite":{"name":"bestseller_list","xpath":"//li[@class='zg_hrsr_item']","extends":"document","kids":[{"scalar":{"name":"title","xpath":"./span[@class='zg_hrsr_ladder']/b/a/text()"}},{"scalar":{"name":"location","xpath":"./span[@class='zg_hrsr_ladder']/b/a/@href","hide":"True"}},{"scalar":{"name":"rank","xpath":"./span[@class='zg_hrsr_rank']/text()","scalar_type":"ecologylab.semantics.metadata.scalar.types.MetadataStringScalarType"}}]}},{"scalar":{"name":"location","hide":"True","always_show":"True","layer":"8","scalar_type":"ecologylab.semantics.metadata.scalar.types.MetadataParsedURLScalarType"}},{"scalar":{"name":"model","scalar_type":"ecologylab.semantics.metadata.scalar.types.MetadataStringScalarType"}},{"scalar":{"name":"query","comment":"The search query","layer":"5","scalar_type":"ecologylab.semantics.metadata.scalar.types.MetadataStringScalarType"}},{"scalar":{"name":"page_structure","comment":"For debugging. Type of the structure recognized by information extraction.","hide":"True","layer":"6","scalar_type":"ecologylab.semantics.metadata.scalar.types.MetadataStringScalarType"}},{"collection":{"name":"additional_locations","hide":"True","child_tag":"location"}},{"scalar":{"name":"meta_metadata_name","comment":"Stores the name of the meta-metadata, and is used on restoring from XML.","tag":"mm_name","hide":"True","ignore_in_term_vector":"True","scalar_type":"ecologylab.semantics.metadata.scalar.types.MetadataStringScalarType"}},{"collection":{"name":"mixins","promote_children":"True","polymorphic_scope":"repository_metadata","child_type":"metadata"}}],"def_var":[{"name":"reviews_span","xpath":"//span[@ref='dp_top_cm_cr_acr_pop_']","type":"node"}],"selector":[{"url_regex":"http://www.amazon.com/[^/]*/dp/[^/]*","domain":"amazon.com"},{"url_regex":"http://www.amazon.com/gp/product/[^/]*","domain":"amazon.com"}],"semantic_actions":[{"if":{"checks":[{"not_null":{"value":"amazon_item"}}]}}]}}
		MetaMetadataRepositoryLoader loader = new MetaMetadataRepositoryLoader();
		
		MetaMetadataRepository repo = loader.loadFromDir(MetaMetadataRepositoryInit.findRepositoryLocation(), Format.XML);

		SimplTypesScope.graphSwitch = GRAPH_SWITCH.ON;
		
		System.out.println("Javascript Translator");
		SimplTypesScope ts = MetaMetadataTranslationScope.get();

		//SimplTypesScope.serialize(ts, System.out, Format.JSON);
		//ts.deserialize(System.out, Format.JSON);

		JavascriptTranslator jst = new JavascriptTranslator();
		jst.translateToJavascript(new File("jscode/mmd.js"), ts);
	}
}
