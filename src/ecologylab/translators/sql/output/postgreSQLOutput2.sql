--MetadataClassDescriptor[ecologylab.semantics.generated.library.Rss]
CREATE TABLE Rss (
channel Channel 	/*simpl_composite */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Search]
CREATE TABLE Search (
searchResults SearchResult[] 	/*simpl_collection simpl_nowrap */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.IcdlImage]
CREATE TABLE IcdlImage (
languages text 	/*simpl_scalar */
)INHERITS (Image); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Channel]
CREATE TABLE Channel (
item Item[] 	/*simpl_collection simpl_nowrap */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.YahooResultSet]
CREATE TABLE YahooResultSet (
results Result[] 	/*simpl_collection simpl_nowrap */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.GooglePatent]
CREATE TABLE GooglePatent (
claims text ,	/*simpl_scalar */
picLinks SearchResult[] ,	/*simpl_collection */
citations SearchResult[] ,	/*simpl_collection */
abstractField text ,	/*simpl_scalar */
referencedBys SearchResult[] ,	/*simpl_collection */
inventor text ,	/*simpl_scalar */
picLink varchar(64) 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrImage]
CREATE TABLE FlickrImage (
browsePurl varchar(64) ,	/*simpl_scalar */
flickrTags FlickrTag[] 	/*simpl_collection */
)INHERITS (Image); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Paragraph]
CREATE TABLE Paragraph (
anchors Anchor[] ,	/*simpl_collection */
paragraphText text 	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.DcDocument]
CREATE TABLE DcDocument (
subject text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Item]
CREATE TABLE Item (
guid varchar(64) ,	/*simpl_scalar simpl_hints */
title text ,	/*simpl_scalar simpl_hints */
link varchar(64) 	/*simpl_scalar simpl_hints */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.WeatherReport]
CREATE TABLE WeatherReport (
wind text ,	/*simpl_scalar */
humidity text ,	/*simpl_scalar */
picUrl varchar(64) ,	/*simpl_scalar */
weather text ,	/*simpl_scalar */
dewPoint text ,	/*simpl_scalar */
temperature text ,	/*simpl_scalar */
city text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.SearchResult]
CREATE TABLE SearchResult (
link varchar(64) ,	/*simpl_scalar */
snippet text ,	/*simpl_scalar */
heading text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.Genre]
CREATE TABLE Genre (
name text ,	/*simpl_scalar */
genreLink varchar(64) 	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrLink]
CREATE TABLE FlickrLink (
title text ,	/*simpl_scalar */
link varchar(64) 	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrAuthor]
CREATE TABLE FlickrAuthor (
flickrLinkSet FlickrLink[] 	/*simpl_collection */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.ScholarlyArticle]
CREATE TABLE ScholarlyArticle (
classifications Link[] ,	/*simpl_collection */
authors Author[] ,	/*simpl_collection */
"references" ScholarlyArticle[] ,	/*simpl_collection */
metadataPage varchar(64) ,	/*simpl_scalar */
citations ScholarlyArticle[] ,	/*simpl_collection */
source Source ,	/*simpl_composite */
abstractField text ,	/*xml_tag simpl_scalar */
keyTerms Link[] 	/*simpl_collection */
)INHERITS (Pdf); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Result]
CREATE TABLE Result (
summary text ,	/*xml_tag simpl_scalar simpl_hints */
title text ,	/*xml_tag simpl_scalar simpl_hints */
refererUrl varchar(64) ,	/*xml_tag simpl_scalar simpl_hints */
modificationDate int4 ,	/*xml_tag simpl_scalar simpl_hints */
mimeType text ,	/*xml_tag simpl_scalar simpl_hints */
url varchar(64) 	/*xml_tag simpl_scalar simpl_hints */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.builtins.Media]
CREATE TABLE Media (
context text 	/*xml_tag simpl_scalar simpl_hints */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Artwork]
CREATE TABLE Artwork (
artists Author[] ,	/*simpl_collection */
website varchar(64) ,	/*simpl_scalar */
abstractField text ,	/*xml_tag simpl_scalar */
year text ,	/*simpl_scalar */
medium text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.GoogleTrends]
CREATE TABLE GoogleTrends (
hotSearches HotSearch[] 	/*simpl_collection simpl_nowrap */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Thumbinner]
CREATE TABLE Thumbinner (
thumbImgCaption text ,	/*simpl_scalar */
thumbImgSrc varchar(64) 	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.MmArtwork]
CREATE TABLE MmArtwork (
artists Author[] ,	/*simpl_collection */
website varchar(64) ,	/*simpl_scalar */
abstractField text ,	/*xml_tag simpl_scalar */
year text ,	/*simpl_scalar */
extendedAbstract varchar(64) ,	/*simpl_scalar */
artTitle text ,	/*simpl_scalar */
medium text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrSearch]
CREATE TABLE FlickrSearch (
flickrResults FlickrImage[] 	/*simpl_collection */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrTags]
CREATE TABLE FlickrTags (
flickrLinkSet FlickrLink[] 	/*simpl_collection */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Nsdl]
CREATE TABLE Nsdl (
subject text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.CastMember]
CREATE TABLE CastMember (
actor Entity ,	/*simpl_composite */
"character" Entity 	/*simpl_composite */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.Source]
CREATE TABLE Source (
isbn text ,	/*simpl_scalar */
pages text ,	/*simpl_scalar */
archive varchar(64) ,	/*simpl_scalar */
yearOfPublication int4 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.ImdbTitle]
CREATE TABLE ImdbTitle (
posterImg varchar(64) ,	/*simpl_scalar */
releaseDate text ,	/*simpl_scalar */
"cast" CastMember[] ,	/*simpl_collection */
genres Genre[] ,	/*simpl_collection */
yearReleased text ,	/*simpl_scalar */
writers PersonDetails[] ,	/*simpl_collection */
titlePhotos Image[] ,	/*simpl_collection */
rating text ,	/*simpl_scalar */
directors PersonDetails[] ,	/*simpl_collection */
tagline text ,	/*simpl_scalar */
plot text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Anchor]
CREATE TABLE Anchor (
anchorText text ,	/*simpl_scalar */
link varchar(64) 	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.Metadata]
CREATE TABLE Metadata (
mixins Metadata[] ,	/*semantics_mixin simpl_collection */
loadedFromPreviousSession boolean ,
isTrueSeed boolean ,
seed Seed ,
metaMetadataName text ,	/*simpl_scalar xml_other_tags xml_tag */
isDnd boolean ,
termVector CompositeTermVector ,
MIXINS_FIELD_NAME text ,
repository MetaMetadataRepository ,
MIXIN_TRANSLATION_STRING text ,
mixinClasses Class[] ,
MIXIN_TRANSLATIONS TranslationScope ,
INITIAL_SIZE int4 ,
metaMetadata MetaMetadata 
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Pdf]
CREATE TABLE Pdf (
summary text ,	/*simpl_scalar */
author text ,	/*simpl_scalar */
trapped text ,	/*simpl_scalar */
keywords text ,	/*simpl_scalar */
contents text ,	/*simpl_scalar */
subject text ,	/*simpl_scalar */
creationdate text ,	/*simpl_scalar */
modified text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Dlms]
CREATE TABLE Dlms (
subject text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Bookmark]
CREATE TABLE Bookmark (
title text ,	/*simpl_scalar */
link varchar(64) ,	/*simpl_scalar */
pic varchar(64) 	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.builtins.Entity]
CREATE TABLE Entity (
linkedDocument Document ,
location varchar(64) ,	/*simpl_scalar simpl_hints */
gist text 	/*simpl_scalar simpl_hints */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrTagsInteresting]
CREATE TABLE FlickrTagsInteresting (
flickrLinkSet FlickrLink[] 	/*simpl_collection */
)INHERITS (Search); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.HotSearch]
CREATE TABLE HotSearch (
search text 	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Lolz]
CREATE TABLE Lolz (
picture varchar(64) ,	/*simpl_scalar */
mightLike varchar(64) ,	/*simpl_scalar */
caption text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Tumblr]
CREATE TABLE Tumblr (
post text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.builtins.DebugMetadata]
CREATE TABLE DebugMetadata (
newTermVector text 	/*simpl_scalar simpl_hints */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.Author]
CREATE TABLE Author (
name text ,	/*simpl_scalar */
affiliation text ,	/*simpl_scalar */
city text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.WikipediaPageType]
CREATE TABLE WikipediaPageType (
thumbinners Thumbinner[] ,	/*simpl_collection */
paragraphs Paragraph[] ,	/*simpl_collection */
categories Category[] 	/*simpl_collection */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.GooglePatentImage]
CREATE TABLE GooglePatentImage (
picUrls SearchResult[] ,	/*simpl_collection */
inventor text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.BirthDetail]
CREATE TABLE BirthDetail (
yearOfBirth text ,	/*simpl_scalar */
yearOfBirthLink varchar(64) ,	/*simpl_scalar */
placeOfBirth text ,	/*simpl_scalar */
dayOfBirthLink varchar(64) ,	/*simpl_scalar */
placeOfBirthLink varchar(64) ,	/*simpl_scalar */
dayOfBirth text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.ImdbChart]
CREATE TABLE ImdbChart (
results ImdbTitle[] 	/*simpl_collection */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.builtins.Image]
CREATE TABLE Image (
location varchar(64) ,	/*simpl_scalar */
localLocation text ,	/*simpl_scalar */
caption text 	/*simpl_scalar */
)INHERITS (Media); 

--MetadataClassDescriptor[ecologylab.semantics.library.uva.Topic]
CREATE TABLE Topic (
id int4 ,	/*simpl_scalar */
anchorKeywords text ,	/*simpl_scalar */
urlKeywords text ,	/*simpl_scalar */
contentKeywords text ,	/*simpl_scalar */
titleKeywords text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.WikipediaPage]
CREATE TABLE WikipediaPage (
thumbinners Thumbinner[] ,	/*simpl_collection */
paragraphs Paragraph[] ,	/*simpl_collection */
categories Category[] ,	/*simpl_collection */
mainImageSrc varchar(64) 	/*simpl_scalar */
)INHERITS (WikipediaPageType); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.ImdbGenre]
CREATE TABLE ImdbGenre (
results ImdbTitle[] 	/*simpl_collection */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.DeliciousHomepage]
CREATE TABLE DeliciousHomepage (
bookmarks Bookmark[] 	/*simpl_collection */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.builtins.Document]
CREATE TABLE Document (
pageStructure text ,	/*simpl_scalar simpl_hints */
title text ,	/*simpl_scalar */
location varchar(64) ,	/*simpl_scalar */
query text ,	/*simpl_scalar simpl_hints */
description text ,	/*simpl_scalar */
generation int4 	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.AcmProceeding]
CREATE TABLE AcmProceeding (
proceedings SearchResult[] ,	/*simpl_collection */
papers SearchResult[] 	/*simpl_collection */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Icdl]
CREATE TABLE Icdl (
languages text 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.UrbanSpoonGenre]
CREATE TABLE UrbanSpoonGenre (
topResults SearchResult[] 	/*simpl_collection */
)INHERITS (Search); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Text]
CREATE TABLE Text (
text text 	/*simpl_scalar */
)INHERITS (Media); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Category]
CREATE TABLE Category (
catLink varchar(64) ,	/*simpl_scalar */
name text 	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Restaurant]
CREATE TABLE Restaurant (
phone text ,	/*simpl_scalar */
priceRange text ,	/*simpl_scalar */
genres SearchResult[] ,	/*simpl_collection */
link varchar(64) ,	/*simpl_scalar */
map varchar(64) ,	/*simpl_scalar */
rating text ,	/*simpl_scalar */
pic varchar(64) 	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.Link]
CREATE TABLE Link (
link varchar(64) ,	/*simpl_scalar */
heading text 	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrImageDetail]
CREATE TABLE FlickrImageDetail (
flickr_image FlickrImage 	/*simpl_composite */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrTag]
CREATE TABLE FlickrTag (
tagName text ,	/*simpl_scalar */
tagLink varchar(64) 	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.PersonDetails]
CREATE TABLE PersonDetails (
personImg varchar(64) ,	/*simpl_scalar */
alternateNames text ,	/*simpl_scalar */
triviaLink varchar(64) ,	/*simpl_scalar */
titlesAsProducer ImdbTitle[] ,	/*simpl_collection */
titlesForSoundtrack ImdbTitle[] ,	/*simpl_collection */
titlesInDevelopment ImdbTitle[] ,	/*simpl_collection */
titlesAsDirector ImdbTitle[] ,	/*simpl_collection */
titlesAsActor ImdbTitle[] ,	/*simpl_collection */
titlesThankedIn ImdbTitle[] ,	/*simpl_collection */
awards text ,	/*simpl_scalar */
trivia text ,	/*simpl_scalar */
awardsLink varchar(64) ,	/*simpl_scalar */
birth_detail BirthDetail ,	/*simpl_composite */
miniBiography text ,	/*simpl_scalar */
biographyLink varchar(64) ,	/*simpl_scalar */
titlesAsSelf ImdbTitle[] 	/*simpl_collection */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Channel]
CREATE TYPE Channel AS (
item Item[]	/*simpl_collection simpl_nowrap */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.HotSearch]
CREATE TYPE HotSearch AS (
search text	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.Author]
CREATE TYPE Author AS (
name text,	/*simpl_scalar */
affiliation text,	/*simpl_scalar */
city text	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrImage]
CREATE TYPE FlickrImage AS (
browsePurl varchar(64),	/*simpl_scalar */
flickrTags FlickrTag[]	/*simpl_collection */
)INHERITS (Image); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.BirthDetail]
CREATE TYPE BirthDetail AS (
yearOfBirth text,	/*simpl_scalar */
yearOfBirthLink varchar(64),	/*simpl_scalar */
placeOfBirth text,	/*simpl_scalar */
dayOfBirthLink varchar(64),	/*simpl_scalar */
placeOfBirthLink varchar(64),	/*simpl_scalar */
dayOfBirth text	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Paragraph]
CREATE TYPE Paragraph AS (
anchors Anchor[],	/*simpl_collection */
paragraphText text	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Item]
CREATE TYPE Item AS (
guid varchar(64),	/*simpl_scalar simpl_hints */
title text,	/*simpl_scalar simpl_hints */
link varchar(64)	/*simpl_scalar simpl_hints */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.SearchResult]
CREATE TYPE SearchResult AS (
link varchar(64),	/*simpl_scalar */
snippet text,	/*simpl_scalar */
heading text	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.builtins.Image]
CREATE TYPE Image AS (
location varchar(64),	/*simpl_scalar */
localLocation text,	/*simpl_scalar */
caption text	/*simpl_scalar */
)INHERITS (Media); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.Genre]
CREATE TYPE Genre AS (
name text,	/*simpl_scalar */
genreLink varchar(64)	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrLink]
CREATE TYPE FlickrLink AS (
title text,	/*simpl_scalar */
link varchar(64)	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.ScholarlyArticle]
CREATE TYPE ScholarlyArticle AS (
classifications Link[],	/*simpl_collection */
authors Author[],	/*simpl_collection */
"references" ScholarlyArticle[],	/*simpl_collection */
metadataPage varchar(64),	/*simpl_scalar */
citations ScholarlyArticle[],	/*simpl_collection */
source Source,	/*simpl_composite */
abstractField text,	/*xml_tag simpl_scalar */
keyTerms Link[]	/*simpl_collection */
)INHERITS (Pdf); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Result]
CREATE TYPE Result AS (
summary text,	/*xml_tag simpl_scalar simpl_hints */
title text,	/*xml_tag simpl_scalar simpl_hints */
refererUrl varchar(64),	/*xml_tag simpl_scalar simpl_hints */
modificationDate int4,	/*xml_tag simpl_scalar simpl_hints */
mimeType text,	/*xml_tag simpl_scalar simpl_hints */
url varchar(64)	/*xml_tag simpl_scalar simpl_hints */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Thumbinner]
CREATE TYPE Thumbinner AS (
thumbImgCaption text,	/*simpl_scalar */
thumbImgSrc varchar(64)	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.CastMember]
CREATE TYPE CastMember AS (
actor Entity,	/*simpl_composite */
"character" Entity	/*simpl_composite */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.Source]
CREATE TYPE Source AS (
isbn text,	/*simpl_scalar */
pages text,	/*simpl_scalar */
archive varchar(64),	/*simpl_scalar */
yearOfPublication int4	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Category]
CREATE TYPE Category AS (
catLink varchar(64),	/*simpl_scalar */
name text	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.ImdbTitle]
CREATE TYPE ImdbTitle AS (
posterImg varchar(64),	/*simpl_scalar */
releaseDate text,	/*simpl_scalar */
"cast" CastMember[],	/*simpl_collection */
genres Genre[],	/*simpl_collection */
yearReleased text,	/*simpl_scalar */
writers PersonDetails[],	/*simpl_collection */
titlePhotos Image[],	/*simpl_collection */
rating text,	/*simpl_scalar */
directors PersonDetails[],	/*simpl_collection */
tagline text,	/*simpl_scalar */
plot text	/*simpl_scalar */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.Link]
CREATE TYPE Link AS (
link varchar(64),	/*simpl_scalar */
heading text	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Anchor]
CREATE TYPE Anchor AS (
anchorText text,	/*simpl_scalar */
link varchar(64)	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrTag]
CREATE TYPE FlickrTag AS (
tagName text,	/*simpl_scalar */
tagLink varchar(64)	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.Metadata]
CREATE TYPE Metadata AS (
mixins Metadata[],	/*semantics_mixin simpl_collection */
loadedFromPreviousSession boolean,
isTrueSeed boolean,
seed Seed,
metaMetadataName text,	/*simpl_scalar xml_other_tags xml_tag */
isDnd boolean,
termVector CompositeTermVector,
MIXINS_FIELD_NAME text,
repository MetaMetadataRepository,
MIXIN_TRANSLATION_STRING text,
mixinClasses Class[],
MIXIN_TRANSLATIONS TranslationScope,
INITIAL_SIZE int4,
metaMetadata MetaMetadata
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Bookmark]
CREATE TYPE Bookmark AS (
title text,	/*simpl_scalar */
link varchar(64),	/*simpl_scalar */
pic varchar(64)	/*simpl_scalar */
)INHERITS (Metadata); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.PersonDetails]
CREATE TYPE PersonDetails AS (
personImg varchar(64),	/*simpl_scalar */
alternateNames text,	/*simpl_scalar */
triviaLink varchar(64),	/*simpl_scalar */
titlesAsProducer ImdbTitle[],	/*simpl_collection */
titlesForSoundtrack ImdbTitle[],	/*simpl_collection */
titlesInDevelopment ImdbTitle[],	/*simpl_collection */
titlesAsDirector ImdbTitle[],	/*simpl_collection */
titlesAsActor ImdbTitle[],	/*simpl_collection */
titlesThankedIn ImdbTitle[],	/*simpl_collection */
awards text,	/*simpl_scalar */
trivia text,	/*simpl_scalar */
awardsLink varchar(64),	/*simpl_scalar */
birth_detail BirthDetail,	/*simpl_composite */
miniBiography text,	/*simpl_scalar */
biographyLink varchar(64),	/*simpl_scalar */
titlesAsSelf ImdbTitle[]	/*simpl_collection */
)INHERITS (Document); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.builtins.Entity]
CREATE TYPE Entity AS (
linkedDocument Document,
location varchar(64),	/*simpl_scalar simpl_hints */
gist text	/*simpl_scalar simpl_hints */
)INHERITS (Metadata); 

