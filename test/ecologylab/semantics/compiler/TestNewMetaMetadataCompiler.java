package ecologylab.semantics.compiler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import ecologylab.io.Files;
import ecologylab.semantics.generated.library.dlese.GetRecordAdditionalMetadata;
import ecologylab.semantics.metadata.scalar.MetadataString;
import ecologylab.semantics.metametadata.MetaMetadata;
import ecologylab.semantics.metametadata.MetaMetadataCollectionField;
import ecologylab.semantics.metametadata.MetaMetadataCompositeField;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.semantics.metametadata.MetaMetadataRepositoryLoader;
import ecologylab.semantics.metametadata.MetaMetadataScalarField;
import ecologylab.serialization.Format;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.translators.CodeTranslationException;

public class TestNewMetaMetadataCompiler
{
	
	protected CompilerConfig getCompilerConfig(final File testingRepository)
	{
		CompilerConfig config = new CompilerConfig(CompilerConfig.JAVA, new File(".." + Files.sep + "testMetaMetadataCompiler" + Files.sep + "src"))
		{
			@Override
			public MetaMetadataRepository loadRepository()
			{
				return this.getRepositoryLoader().loadFromFiles(Arrays.asList(testingRepository), Format.XML);
			}
		};
		return config;
	}

	protected CompilerConfig getCompilerConfigForDir(final File testingRepositoryDir)
	{
		CompilerConfig config = new CompilerConfig(CompilerConfig.JAVA, new File(".." + Files.sep + "testMetaMetadataCompiler" + Files.sep + "src"))
		{
			MetaMetadataRepository repo = null;

			@Override
			public MetaMetadataRepository loadRepository()
			{
				if (repo == null)
					repo =  getRepositoryLoader().loadFromDir(testingRepositoryDir, Format.XML);
				return repo;
			}
		};
		return config;
	}

	protected void doTest(String testName, final File testingRepository) throws IOException, SIMPLTranslationException, CodeTranslationException
	{
		System.err.println("\n\n\n\nTest: " + testName + "\n\n\n\n\n");
		CompilerConfig config = getCompilerConfig(testingRepository);
		NewMetaMetadataCompiler compiler = new NewMetaMetadataCompiler();
		compiler.compile(config);
	}

	protected MetaMetadataRepository doTestForDir(String testName, final File testingRepositoryDir) throws IOException, SIMPLTranslationException, CodeTranslationException
	{
		System.err.println("\n\n\n\nTest: " + testName + "\n\n\n\n\n");
		CompilerConfig config = getCompilerConfigForDir(testingRepositoryDir);
		NewMetaMetadataCompiler compiler = new NewMetaMetadataCompiler();
		compiler.compile(config);
		return config.loadRepository();
	}

	public void testGeneratingBasicTScope() throws IOException, SIMPLTranslationException, CodeTranslationException
	{
		doTest("basic-tscope", new File("data/testRepository/testGeneratingBasicTScope.xml"));
	}

	public void testTypeGraphs() throws IOException, SIMPLTranslationException, CodeTranslationException
	{
		doTest("type-graphs", new File("data/testRepository/testTypeGraphs.xml"));
	}

	public void testInlineMmd() throws IOException, SIMPLTranslationException, CodeTranslationException
	{
		doTest("inline-mmd", new File("data/testRepository/testInlineMmd.xml"));
	}

	public void testArticles() throws IOException, SIMPLTranslationException, CodeTranslationException
	{
		doTest("articles", new File("data/testRepository/testArticles.xml"));
	}

	public void testScalarCollections() throws IOException, SIMPLTranslationException, CodeTranslationException
	{
		doTest("scalar-collections", new File("data/testRepository/testScalarCollections.xml"));
	}

	public void testPolymorphicFields() throws IOException, SIMPLTranslationException, CodeTranslationException
	{
		doTest("poly-fields", new File("data/testRepository/testPolymorphicFields.xml"));
	}

	public void testOtherTags() throws IOException, SIMPLTranslationException, CodeTranslationException
	{
		doTest("other-tags", new File("data/testRepository/testOtherTags.xml"));
	}

	public void testPolymorphicScope() throws IOException, SIMPLTranslationException, CodeTranslationException
	{
		doTest("poly-scope", new File("data/testRepository/testPolymorphicScope.xml"));
	}

	public void testYahooGeoCode() throws IOException, SIMPLTranslationException, CodeTranslationException
	{
		doTest("yahoo-geo-code", new File("data/testRepository/testYahooGeoCode.xml"));
	}

	public void testLocalMmdScopes() throws IOException, SIMPLTranslationException, CodeTranslationException
	{
		MetaMetadataRepository repo = doTestForDir("local-mmd-scopes", new File("data/testRepository/testLocalMmdScopes"));
		System.out.println("\n\nScopes by Package:");
		for (String packageName : repo.getPackageMmdScopes().keySet())
		{
			System.out.println("\n" + packageName + ":");
			System.out.println(repo.getPackageMmdScopes().get(packageName));
		}
		System.out.println("\n\nMMD Local Scopes:");
		for (MetaMetadata mmd : repo.values())
		{
			System.out.println("\nLocal MMD Scope for " + mmd.getName() + ":");
			System.out.println(mmd.getMmdScope());
		}
	}

	/**
	 * use testArticles.xml as the input repository to validate inheritance relationships (any field:
	 * declaredMmd, inheritedField, nested field + mmds: inheritedMmd, mmds: inlineMmds).
	 */
	@Test
	public void testArticlesInheritanceRelationships()
	{
		MetaMetadataRepositoryLoader loader = new MetaMetadataRepositoryLoader();
		MetaMetadataRepository repository = loader.loadFromFiles(Arrays.asList(new File("data/testRepository/testArticles.xml")), Format.XML);
		TranslationScope tscope = repository.traverseAndGenerateTranslationScope("test-articles-inheritance");

		MetaMetadata metadata = repository.getMMByName("metadata");
		Assert.assertNull(metadata.getInheritedMmd());
		Assert.assertTrue(metadata.getMmdScope() == null || metadata.getMmdScope().isEmpty());
		// meta_metadata_name
		MetaMetadataScalarField metadata__meta_metadata_name = (MetaMetadataScalarField) metadata.getChildMetaMetadata().get("meta_metadata_name");
		Assert.assertNull(metadata__meta_metadata_name.getInheritedField());
		Assert.assertSame(metadata, metadata__meta_metadata_name.getDeclaringMmd());
		Assert.assertEquals(MetadataString.class.getName(), metadata__meta_metadata_name.getScalarType().getJavaTypeName());
		// mixins
		MetaMetadataCollectionField metadata__mixins = (MetaMetadataCollectionField) metadata.getChildMetaMetadata().get("mixins");
		Assert.assertNull(metadata__mixins.getInheritedField());
		Assert.assertSame(metadata, metadata__mixins.getDeclaringMmd());
		Assert.assertSame(metadata, metadata__mixins.getInheritedMmd());

		MetaMetadata document = repository.getMMByName("document");
		Assert.assertSame(metadata, document.getInheritedMmd());
		Assert.assertTrue(document.getMmdScope() == null || document.getMmdScope().isEmpty());
		Assert.assertEquals(metadata__meta_metadata_name, document.getChildMetaMetadata().get("meta_metadata_name"));
		Assert.assertEquals(metadata__mixins, document.getChildMetaMetadata().get("mixins"));
		// location
		MetaMetadataScalarField document__location = (MetaMetadataScalarField) document.getChildMetaMetadata().get("location");
		Assert.assertNull(document__location.getInheritedField());
		Assert.assertSame(document, document__location.getDeclaringMmd());
		// additional_locations
		MetaMetadataCollectionField document__additional_locations = (MetaMetadataCollectionField) document.getChildMetaMetadata().get("additional_locations");
		Assert.assertNull(document__additional_locations.getInheritedField());
		Assert.assertSame(document, document__additional_locations.getDeclaringMmd());
		Assert.assertNull(document__additional_locations.getInheritedMmd());

		MetaMetadata article = repository.getMMByName("article");
		MetaMetadata author = article.getMmdScope().get("author");
		Assert.assertSame(metadata, author.getInheritedMmd());
		Assert.assertEquals(metadata__meta_metadata_name, author.getChildMetaMetadata().get("meta_metadata_name"));
		Assert.assertEquals(metadata__mixins, author.getChildMetaMetadata().get("mixins"));
		// name
		MetaMetadataScalarField author__name = (MetaMetadataScalarField) author.getChildMetaMetadata().get("name");
		Assert.assertNull(author__name.getInheritedField());
		Assert.assertSame(author, author__name.getDeclaringMmd());
		// affiliation
		MetaMetadataScalarField author__affiliation = (MetaMetadataScalarField) author.getChildMetaMetadata().get("affiliation");
		Assert.assertNull(author__affiliation.getInheritedField());
		Assert.assertSame(author, author__affiliation.getDeclaringMmd());

		MetaMetadata source = article.getMmdScope().get("source");
		Assert.assertSame(document, source.getInheritedMmd());
		Assert.assertEquals(metadata__meta_metadata_name, source.getChildMetaMetadata().get("meta_metadata_name"));
		Assert.assertEquals(metadata__mixins, source.getChildMetaMetadata().get("mixins"));
		Assert.assertEquals(document__additional_locations, source.getChildMetaMetadata().get("additional_locations"));
		// archive_name
		MetaMetadataScalarField source__archive_name = (MetaMetadataScalarField) source.getChildMetaMetadata().get("archive_name");
		Assert.assertNull(source__archive_name.getInheritedField());
		Assert.assertSame(source, source__archive_name.getDeclaringMmd());
		// location
		MetaMetadataScalarField source__location = (MetaMetadataScalarField) source.getChildMetaMetadata().get("location");
		Assert.assertSame(document__location, source__location.getInheritedField());
		Assert.assertTrue(!document__location.isHide());
		Assert.assertTrue(source__location.isHide());
		// year_of_publication
		MetaMetadataScalarField source__year_of_publication = (MetaMetadataScalarField) source.getChildMetaMetadata().get("year_of_publication");
		Assert.assertNull(source__year_of_publication.getInheritedField());
		Assert.assertSame(source, source__year_of_publication.getDeclaringMmd());
		// isbn
		MetaMetadataScalarField source__isbn = (MetaMetadataScalarField) source.getChildMetaMetadata().get("isbn");
		Assert.assertNull(source__isbn.getInheritedField());
		Assert.assertSame(source, source__isbn.getDeclaringMmd());

		Assert.assertSame(document, article.getInheritedMmd());
		Assert.assertTrue(article.getMmdScope().size() == 2);
		Assert.assertEquals(metadata__meta_metadata_name, article.getChildMetaMetadata().get("meta_metadata_name"));
		Assert.assertEquals(metadata__mixins, article.getChildMetaMetadata().get("mixins"));
		Assert.assertEquals(document__location, article.getChildMetaMetadata().get("location"));
		Assert.assertEquals(document__additional_locations, article.getChildMetaMetadata().get("additional_locations"));
		// title
		MetaMetadataScalarField article__title = (MetaMetadataScalarField) article.getChildMetaMetadata().get("title");
		Assert.assertNull(article__title.getInheritedField());
		Assert.assertSame(article, article__title.getDeclaringMmd());
		// authors
		MetaMetadataCollectionField article__authors = (MetaMetadataCollectionField) article.getChildMetaMetadata().get("authors");
		Assert.assertNull(article__authors.getInheritedField());
		Assert.assertSame(article, article__authors.getDeclaringMmd());
		Assert.assertSame(author, article__authors.getInheritedMmd());
		// source
		MetaMetadataCompositeField article__source = (MetaMetadataCompositeField) article.getChildMetaMetadata().get("source");
		Assert.assertNull(article__source.getInheritedField());
		Assert.assertSame(article, article__source.getDeclaringMmd());
		Assert.assertSame(source, article__source.getInheritedMmd());
		// pages
		MetaMetadataScalarField article__pages = (MetaMetadataScalarField) article.getChildMetaMetadata().get("pages");
		Assert.assertNull(article__pages.getInheritedField());
		Assert.assertSame(article, article__pages.getDeclaringMmd());

		MetaMetadata paper = repository.getMMByName("paper");
		MetaMetadata tag = paper.getMmdScope().get("tag");
		Assert.assertSame(metadata, tag.getInheritedMmd());
		Assert.assertEquals(metadata__meta_metadata_name, tag.getChildMetaMetadata().get("meta_metadata_name"));
		Assert.assertEquals(metadata__mixins, tag.getChildMetaMetadata().get("mixins"));
		// tag_name
		MetaMetadataScalarField tag__tag_name = (MetaMetadataScalarField) tag.getChildMetaMetadata().get("tag_name");
		Assert.assertNull(tag__tag_name.getInheritedField());
		Assert.assertSame(tag, tag__tag_name.getDeclaringMmd());
		// link
		MetaMetadataScalarField tag__link = (MetaMetadataScalarField) tag.getChildMetaMetadata().get("link");
		Assert.assertNull(tag__link.getInheritedField());
		Assert.assertSame(tag, tag__link.getDeclaringMmd());

		Assert.assertSame(article, paper.getInheritedMmd());
		Assert.assertTrue(paper.getMmdScope().size() == 1);
		Assert.assertEquals(metadata__meta_metadata_name, paper.getChildMetaMetadata().get("meta_metadata_name"));
		Assert.assertEquals(metadata__mixins, paper.getChildMetaMetadata().get("mixins"));
		Assert.assertEquals(document__location, paper.getChildMetaMetadata().get("location"));
		Assert.assertEquals(document__additional_locations, paper.getChildMetaMetadata().get("additional_locations"));
		Assert.assertEquals(article__title, paper.getChildMetaMetadata().get("title"));
		// Assert.assertSame(article__authors, paper.getChildMetaMetadata().get("authors"));
		Assert.assertEquals(article__source, paper.getChildMetaMetadata().get("source"));
		Assert.assertEquals(article__pages, paper.getChildMetaMetadata().get("pages"));
		// authors: TODO
		// abstract_field
		MetaMetadataScalarField paper__abstract_field = (MetaMetadataScalarField) paper.getChildMetaMetadata().get("abstract_field");
		Assert.assertNull(paper__abstract_field.getInheritedField());
		Assert.assertSame(paper, paper__abstract_field.getDeclaringMmd());
		// references
		MetaMetadataCollectionField paper__references = (MetaMetadataCollectionField) paper.getChildMetaMetadata().get("references");
		Assert.assertNull(paper__references.getInheritedField());
		Assert.assertSame(paper, paper__references.getDeclaringMmd());
		Assert.assertSame(paper, paper__references.getInheritedMmd());
		// citations
		MetaMetadataCollectionField paper__citations = (MetaMetadataCollectionField) paper.getChildMetaMetadata().get("citations");
		Assert.assertNull(paper__citations.getInheritedField());
		Assert.assertSame(paper, paper__citations.getDeclaringMmd());
		Assert.assertSame(paper, paper__citations.getInheritedMmd());
		// keywords
		MetaMetadataCollectionField paper__keywords = (MetaMetadataCollectionField) paper.getChildMetaMetadata().get("keywords");
		Assert.assertNull(paper__keywords.getInheritedField());
		Assert.assertSame(paper, paper__keywords.getDeclaringMmd());
		Assert.assertEquals(MetadataString.class.getName(), paper__keywords.getChildScalarType().getJavaTypeName());

		MetaMetadata acm_paper = repository.getMMByName("acm_paper");
		Assert.assertSame(paper, acm_paper.getInheritedMmd());
		Assert.assertTrue(acm_paper.getMmdScope() == null || acm_paper.getMmdScope().isEmpty());
		Assert.assertEquals(metadata__meta_metadata_name, acm_paper.getChildMetaMetadata().get("meta_metadata_name"));
		Assert.assertEquals(metadata__mixins, acm_paper.getChildMetaMetadata().get("mixins"));
		Assert.assertEquals(document__location, acm_paper.getChildMetaMetadata().get("location"));
		Assert.assertEquals(document__additional_locations, acm_paper.getChildMetaMetadata().get("additional_locations"));
		Assert.assertEquals(article__source, acm_paper.getChildMetaMetadata().get("source"));
		Assert.assertEquals(article__pages, acm_paper.getChildMetaMetadata().get("pages"));
		Assert.assertEquals(paper__abstract_field, acm_paper.getChildMetaMetadata().get("abstract_field"));
		Assert.assertEquals(paper__references, acm_paper.getChildMetaMetadata().get("references"));
		Assert.assertEquals(paper__citations, acm_paper.getChildMetaMetadata().get("citations"));
		Assert.assertEquals(paper__keywords, acm_paper.getChildMetaMetadata().get("keywords"));
		// title
		MetaMetadataScalarField acm_paper__title = (MetaMetadataScalarField) acm_paper.getChildMetaMetadata().get("title");
		Assert.assertEquals(article__title, acm_paper__title.getInheritedField());
		Assert.assertSame(article, acm_paper__title.getDeclaringMmd());
		// authors
		MetaMetadataCollectionField acm_paper__authors = (MetaMetadataCollectionField) acm_paper.getChildMetaMetadata().get("authors");
		// Assert.assertSame(article__authors, acm_paper__authors.getInheritedField()); // should
		// inherit from paper__authors
		Assert.assertSame(article, acm_paper__authors.getDeclaringMmd());
		Assert.assertSame(author, acm_paper__authors.getInheritedMmd());
		// authors.name
		MetaMetadataScalarField acm_paper__authors__name = (MetaMetadataScalarField) acm_paper__authors.getChildMetaMetadata().get("name");
		Assert.assertEquals(author__name, acm_paper__authors__name.getInheritedField());
		Assert.assertSame(author, acm_paper__authors__name.getDeclaringMmd());
		Assert.assertEquals("location", acm_paper__authors__name.getNavigatesTo());
		// authors.affiliation
		MetaMetadataScalarField acm_paper__authors__affiliation = (MetaMetadataScalarField) acm_paper__authors.getChildMetaMetadata().get("affiliation");
		Assert.assertEquals("./affiliation", acm_paper__authors__affiliation.getXpath());
	}

	public static void main(String[] args) throws IOException, SIMPLTranslationException, CodeTranslationException
	{
		TestNewMetaMetadataCompiler test = new TestNewMetaMetadataCompiler();
		test.testGeneratingBasicTScope();
		test.testTypeGraphs();
		test.testInlineMmd();
		test.testArticles();
		test.testScalarCollections();
		test.testPolymorphicFields();
		test.testOtherTags();
		test.testPolymorphicScope();
		test.testLocalMmdScopes();
		test.testYahooGeoCode();
	}

}
