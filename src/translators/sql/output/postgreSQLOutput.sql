--MetadataClassDescriptor[ecologylab.semantics.generated.library.Rss]
CREATE TABLE Rss (
channel Channel 	/*simpl_composite */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Search]
CREATE TABLE Search (
searchResults SearchResult[] 	/*simpl_collection simpl_nowrap */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.IcdlImage]
CREATE TABLE IcdlImage (
languages text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Channel]
CREATE TABLE Channel (
item Item[] 	/*simpl_collection simpl_nowrap */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.YahooResultSet]
CREATE TABLE YahooResultSet (
results Result[] 	/*simpl_collection simpl_nowrap */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.GooglePatent]
CREATE TABLE GooglePatent (
claims text ,	/*simpl_scalar simpl_hints */
picLinks SearchResult[] ,	/*simpl_collection */
citations SearchResult[] ,	/*simpl_collection */
abstractField text ,	/*simpl_scalar simpl_hints */
referencedBys SearchResult[] ,	/*simpl_collection */
inventor text ,	/*simpl_scalar simpl_hints */
picLink varchar(30) 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrImage]
CREATE TABLE FlickrImage (
browsePurl varchar(30) ,	/*simpl_scalar simpl_hints */
flickrTags FlickrTag[] 	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Paragraph]
CREATE TABLE Paragraph (
anchors Anchor[] ,	/*simpl_collection */
paragraphText text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.DcDocument]
CREATE TABLE DcDocument (
subject text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Item]
CREATE TABLE Item (
guid varchar(30) ,	/*simpl_scalar simpl_hints */
title text ,	/*simpl_scalar simpl_hints */
link varchar(30) 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.WeatherReport]
CREATE TABLE WeatherReport (
wind text ,	/*simpl_scalar simpl_hints */
humidity text ,	/*simpl_scalar simpl_hints */
picUrl varchar(30) ,	/*simpl_scalar simpl_hints */
weather text ,	/*simpl_scalar simpl_hints */
dewPoint text ,	/*simpl_scalar simpl_hints */
temperature text ,	/*simpl_scalar simpl_hints */
city text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.SearchResult]
CREATE TABLE SearchResult (
link varchar(30) ,	/*simpl_scalar simpl_hints */
snippet text ,	/*simpl_scalar simpl_hints */
heading text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.Genre]
CREATE TABLE Genre (
name text ,	/*simpl_scalar simpl_hints */
genreLink varchar(30) 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrLink]
CREATE TABLE FlickrLink (
title text ,	/*simpl_scalar simpl_hints */
link varchar(30) 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrAuthor]
CREATE TABLE FlickrAuthor (
flickrLinkSet FlickrLink[] 	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.ScholarlyArticle]
CREATE TABLE ScholarlyArticle (
classifications Link[] ,	/*simpl_collection */
authors Author[] ,	/*simpl_collection */
references ScholarlyArticle[] ,	/*simpl_collection */
metadataPage varchar(30) ,	/*simpl_scalar simpl_hints */
citations ScholarlyArticle[] ,	/*simpl_collection */
source Source ,	/*simpl_composite */
abstractField text ,	/*xml_tag simpl_scalar simpl_hints */
keyTerms Link[] 	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Result]
CREATE TABLE Result (
summary text ,	/*xml_tag simpl_scalar simpl_hints */
title text ,	/*xml_tag simpl_scalar simpl_hints */
refererUrl varchar(30) ,	/*xml_tag simpl_scalar simpl_hints */
modificationDate int4 ,	/*xml_tag simpl_scalar simpl_hints */
mimeType text ,	/*xml_tag simpl_scalar simpl_hints */
url varchar(30) 	/*xml_tag simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.builtins.Media]
CREATE TABLE Media (
context text 	/*xml_tag simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Artwork]
CREATE TABLE Artwork (
artists Author[] ,	/*simpl_collection */
website varchar(30) ,	/*simpl_scalar simpl_hints */
abstractField text ,	/*xml_tag simpl_scalar simpl_hints */
year text ,	/*simpl_scalar simpl_hints */
medium text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.GoogleTrends]
CREATE TABLE GoogleTrends (
hotSearches HotSearch[] 	/*simpl_collection simpl_nowrap */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Thumbinner]
CREATE TABLE Thumbinner (
thumbImgCaption text ,	/*simpl_scalar simpl_hints */
thumbImgSrc varchar(30) 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.MmArtwork]
CREATE TABLE MmArtwork (
artists Author[] ,	/*simpl_collection */
website varchar(30) ,	/*simpl_scalar simpl_hints */
abstractField text ,	/*xml_tag simpl_scalar simpl_hints */
year text ,	/*simpl_scalar simpl_hints */
extendedAbstract varchar(30) ,	/*simpl_scalar simpl_hints */
artTitle text ,	/*simpl_scalar simpl_hints */
medium text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrSearch]
CREATE TABLE FlickrSearch (
flickrResults FlickrImage[] 	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrTags]
CREATE TABLE FlickrTags (
flickrLinkSet FlickrLink[] 	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Nsdl]
CREATE TABLE Nsdl (
subject text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.CastMember]
CREATE TABLE CastMember (
actor Entity ,	/*simpl_composite */
character Entity 	/*simpl_composite */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.Source]
CREATE TABLE Source (
isbn text ,	/*simpl_scalar simpl_hints */
pages text ,	/*simpl_scalar simpl_hints */
archive varchar(30) ,	/*simpl_scalar simpl_hints */
yearOfPublication int4 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.ImdbTitle]
CREATE TABLE ImdbTitle (
posterImg varchar(30) ,	/*simpl_scalar simpl_hints */
releaseDate text ,	/*simpl_scalar simpl_hints */
cast CastMember[] ,	/*simpl_collection */
genres Genre[] ,	/*simpl_collection */
yearReleased text ,	/*simpl_scalar simpl_hints */
writers PersonDetails[] ,	/*simpl_collection */
titlePhotos Image[] ,	/*simpl_collection */
rating text ,	/*simpl_scalar simpl_hints */
directors PersonDetails[] ,	/*simpl_collection */
tagline text ,	/*simpl_scalar simpl_hints */
plot text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Anchor]
CREATE TABLE Anchor (
anchorText text ,	/*simpl_scalar simpl_hints */
link varchar(30) 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.Metadata]
CREATE TABLE Metadata (
mixins Metadata[] ,	/*semantics_mixin simpl_collection */
loadedFromPreviousSession boolean ,
isTrueSeed boolean ,
seed Seed ,
metaMetadataName text ,	/*simpl_scalar simpl_hints */
termVector CompositeTermVector ,
isDnd boolean ,
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
summary text ,	/*simpl_scalar simpl_hints */
author text ,	/*simpl_scalar simpl_hints */
trapped text ,	/*simpl_scalar simpl_hints */
keywords text ,	/*simpl_scalar simpl_hints */
contents text ,	/*simpl_scalar simpl_hints */
subject text ,	/*simpl_scalar simpl_hints */
creationdate text ,	/*simpl_scalar simpl_hints */
modified text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Dlms]
CREATE TABLE Dlms (
subject text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Bookmark]
CREATE TABLE Bookmark (
title text ,	/*simpl_scalar simpl_hints */
link varchar(30) ,	/*simpl_scalar simpl_hints */
pic varchar(30) 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.builtins.Entity]
CREATE TABLE Entity (
linkedDocument Document ,
location varchar(30) ,	/*simpl_scalar simpl_hints */
gist text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrTagsInteresting]
CREATE TABLE FlickrTagsInteresting (
flickrLinkSet FlickrLink[] 	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.HotSearch]
CREATE TABLE HotSearch (
search text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Lolz]
CREATE TABLE Lolz (
picture varchar(30) ,	/*simpl_scalar simpl_hints */
mightLike varchar(30) ,	/*simpl_scalar simpl_hints */
caption text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Tumblr]
CREATE TABLE Tumblr (
post text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.builtins.DebugMetadata]
CREATE TABLE DebugMetadata (
newTermVector MetadataStringBuilder 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.Author]
CREATE TABLE Author (
name text ,	/*simpl_scalar simpl_hints */
affiliation text ,	/*simpl_scalar simpl_hints */
city text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.WikipediaPageType]
CREATE TABLE WikipediaPageType (
thumbinners Thumbinner[] ,	/*simpl_collection */
paragraphs Paragraph[] ,	/*simpl_collection */
categories Category[] 	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.GooglePatentImage]
CREATE TABLE GooglePatentImage (
picUrls SearchResult[] ,	/*simpl_collection */
inventor text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.BirthDetail]
CREATE TABLE BirthDetail (
yearOfBirth text ,	/*simpl_scalar simpl_hints */
yearOfBirthLink varchar(30) ,	/*simpl_scalar simpl_hints */
placeOfBirth text ,	/*simpl_scalar simpl_hints */
dayOfBirthLink varchar(30) ,	/*simpl_scalar simpl_hints */
placeOfBirthLink varchar(30) ,	/*simpl_scalar simpl_hints */
dayOfBirth text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.ImdbChart]
CREATE TABLE ImdbChart (
results ImdbTitle[] 	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.builtins.Image]
CREATE TABLE Image (
location varchar(30) ,	/*simpl_scalar simpl_hints */
localLocation text ,	/*simpl_scalar simpl_hints */
caption text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.uva.Topic]
CREATE TABLE Topic (
id int4 ,	/*simpl_scalar simpl_hints */
anchorKeywords MetadataStringBuilder ,	/*simpl_scalar simpl_hints */
urlKeywords MetadataStringBuilder ,	/*simpl_scalar simpl_hints */
contentKeywords MetadataStringBuilder ,	/*simpl_scalar simpl_hints */
titleKeywords MetadataStringBuilder 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.WikipediaPage]
CREATE TABLE WikipediaPage (
thumbinners Thumbinner[] ,	/*simpl_collection */
paragraphs Paragraph[] ,	/*simpl_collection */
categories Category[] ,	/*simpl_collection */
mainImageSrc varchar(30) 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.ImdbGenre]
CREATE TABLE ImdbGenre (
results ImdbTitle[] 	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.DeliciousHomepage]
CREATE TABLE DeliciousHomepage (
bookmarks Bookmark[] 	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.builtins.Document]
CREATE TABLE Document (
pageStructure text ,	/*simpl_scalar simpl_hints */
title text ,	/*simpl_scalar simpl_hints */
location varchar(30) ,	/*simpl_scalar simpl_hints */
query text ,	/*simpl_scalar simpl_hints */
description text ,	/*simpl_scalar simpl_hints */
generation int4 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.AcmProceeding]
CREATE TABLE AcmProceeding (
proceedings SearchResult[] ,	/*simpl_collection */
papers SearchResult[] 	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Icdl]
CREATE TABLE Icdl (
languages text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.UrbanSpoonGenre]
CREATE TABLE UrbanSpoonGenre (
topResults SearchResult[] 	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Text]
CREATE TABLE Text (
text text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Category]
CREATE TABLE Category (
catLink varchar(30) ,	/*simpl_scalar simpl_hints */
name text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Restaurant]
CREATE TABLE Restaurant (
phone text ,	/*simpl_scalar simpl_hints */
priceRange text ,	/*simpl_scalar simpl_hints */
genres SearchResult[] ,	/*simpl_collection */
link varchar(30) ,	/*simpl_scalar simpl_hints */
map varchar(30) ,	/*simpl_scalar simpl_hints */
rating text ,	/*simpl_scalar simpl_hints */
pic varchar(30) 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.Link]
CREATE TABLE Link (
link varchar(30) ,	/*simpl_scalar simpl_hints */
heading text 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrImageDetail]
CREATE TABLE FlickrImageDetail (
flickr_image FlickrImage 	/*simpl_composite */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrTag]
CREATE TABLE FlickrTag (
tagName text ,	/*simpl_scalar simpl_hints */
tagLink varchar(30) 	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.PersonDetails]
CREATE TABLE PersonDetails (
personImg varchar(30) ,	/*simpl_scalar simpl_hints */
alternateNames text ,	/*simpl_scalar simpl_hints */
triviaLink varchar(30) ,	/*simpl_scalar simpl_hints */
titlesAsProducer ImdbTitle[] ,	/*simpl_collection */
titlesForSoundtrack ImdbTitle[] ,	/*simpl_collection */
titlesInDevelopment ImdbTitle[] ,	/*simpl_collection */
titlesAsDirector ImdbTitle[] ,	/*simpl_collection */
titlesAsActor ImdbTitle[] ,	/*simpl_collection */
titlesThankedIn ImdbTitle[] ,	/*simpl_collection */
awards text ,	/*simpl_scalar simpl_hints */
trivia text ,	/*simpl_scalar simpl_hints */
awardsLink varchar(30) ,	/*simpl_scalar simpl_hints */
birth_detail BirthDetail ,	/*simpl_composite */
miniBiography text ,	/*simpl_scalar simpl_hints */
biographyLink varchar(30) ,	/*simpl_scalar simpl_hints */
titlesAsSelf ImdbTitle[] 	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.HotSearch]
CREATE TYPE HotSearch AS (
search text	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Channel]
CREATE TYPE Channel AS (
item Item[]	/*simpl_collection simpl_nowrap */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.Author]
CREATE TYPE Author AS (
name text,	/*simpl_scalar simpl_hints */
affiliation text,	/*simpl_scalar simpl_hints */
city text	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrImage]
CREATE TYPE FlickrImage AS (
browsePurl varchar(30),	/*simpl_scalar simpl_hints */
flickrTags FlickrTag[]	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.BirthDetail]
CREATE TYPE BirthDetail AS (
yearOfBirth text,	/*simpl_scalar simpl_hints */
yearOfBirthLink varchar(30),	/*simpl_scalar simpl_hints */
placeOfBirth text,	/*simpl_scalar simpl_hints */
dayOfBirthLink varchar(30),	/*simpl_scalar simpl_hints */
placeOfBirthLink varchar(30),	/*simpl_scalar simpl_hints */
dayOfBirth text	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Paragraph]
CREATE TYPE Paragraph AS (
anchors Anchor[],	/*simpl_collection */
paragraphText text	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Item]
CREATE TYPE Item AS (
guid varchar(30),	/*simpl_scalar simpl_hints */
title text,	/*simpl_scalar simpl_hints */
link varchar(30)	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.SearchResult]
CREATE TYPE SearchResult AS (
link varchar(30),	/*simpl_scalar simpl_hints */
snippet text,	/*simpl_scalar simpl_hints */
heading text	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.builtins.Image]
CREATE TYPE Image AS (
location varchar(30),	/*simpl_scalar simpl_hints */
localLocation text,	/*simpl_scalar simpl_hints */
caption text	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.Genre]
CREATE TYPE Genre AS (
name text,	/*simpl_scalar simpl_hints */
genreLink varchar(30)	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrLink]
CREATE TYPE FlickrLink AS (
title text,	/*simpl_scalar simpl_hints */
link varchar(30)	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.ScholarlyArticle]
CREATE TYPE ScholarlyArticle AS (
classifications Link[],	/*simpl_collection */
authors Author[],	/*simpl_collection */
references ScholarlyArticle[],	/*simpl_collection */
metadataPage varchar(30),	/*simpl_scalar simpl_hints */
citations ScholarlyArticle[],	/*simpl_collection */
source Source,	/*simpl_composite */
abstractField text,	/*xml_tag simpl_scalar simpl_hints */
keyTerms Link[]	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Result]
CREATE TYPE Result AS (
summary text,	/*xml_tag simpl_scalar simpl_hints */
title text,	/*xml_tag simpl_scalar simpl_hints */
refererUrl varchar(30),	/*xml_tag simpl_scalar simpl_hints */
modificationDate int4,	/*xml_tag simpl_scalar simpl_hints */
mimeType text,	/*xml_tag simpl_scalar simpl_hints */
url varchar(30)	/*xml_tag simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Thumbinner]
CREATE TYPE Thumbinner AS (
thumbImgCaption text,	/*simpl_scalar simpl_hints */
thumbImgSrc varchar(30)	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.CastMember]
CREATE TYPE CastMember AS (
actor Entity,	/*simpl_composite */
character Entity	/*simpl_composite */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.Source]
CREATE TYPE Source AS (
isbn text,	/*simpl_scalar simpl_hints */
pages text,	/*simpl_scalar simpl_hints */
archive varchar(30),	/*simpl_scalar simpl_hints */
yearOfPublication int4	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.ImdbTitle]
CREATE TYPE ImdbTitle AS (
posterImg varchar(30),	/*simpl_scalar simpl_hints */
releaseDate text,	/*simpl_scalar simpl_hints */
cast CastMember[],	/*simpl_collection */
genres Genre[],	/*simpl_collection */
yearReleased text,	/*simpl_scalar simpl_hints */
writers PersonDetails[],	/*simpl_collection */
titlePhotos Image[],	/*simpl_collection */
rating text,	/*simpl_scalar simpl_hints */
directors PersonDetails[],	/*simpl_collection */
tagline text,	/*simpl_scalar simpl_hints */
plot text	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Category]
CREATE TYPE Category AS (
catLink varchar(30),	/*simpl_scalar simpl_hints */
name text	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.scholarlyPublication.Link]
CREATE TYPE Link AS (
link varchar(30),	/*simpl_scalar simpl_hints */
heading text	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.generated.library.Anchor]
CREATE TYPE Anchor AS (
anchorText text,	/*simpl_scalar simpl_hints */
link varchar(30)	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.flickr.FlickrTag]
CREATE TYPE FlickrTag AS (
tagName text,	/*simpl_scalar simpl_hints */
tagLink varchar(30)	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.Metadata]
CREATE TYPE Metadata AS (
mixins Metadata[],	/*semantics_mixin simpl_collection */
loadedFromPreviousSession boolean,
isTrueSeed boolean,
seed Seed,
metaMetadataName text,	/*simpl_scalar simpl_hints */
termVector CompositeTermVector,
isDnd boolean,
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
title text,	/*simpl_scalar simpl_hints */
link varchar(30),	/*simpl_scalar simpl_hints */
pic varchar(30)	/*simpl_scalar simpl_hints */
); 

--MetadataClassDescriptor[ecologylab.semantics.library.imdb.PersonDetails]
CREATE TYPE PersonDetails AS (
personImg varchar(30),	/*simpl_scalar simpl_hints */
alternateNames text,	/*simpl_scalar simpl_hints */
triviaLink varchar(30),	/*simpl_scalar simpl_hints */
titlesAsProducer ImdbTitle[],	/*simpl_collection */
titlesForSoundtrack ImdbTitle[],	/*simpl_collection */
titlesInDevelopment ImdbTitle[],	/*simpl_collection */
titlesAsDirector ImdbTitle[],	/*simpl_collection */
titlesAsActor ImdbTitle[],	/*simpl_collection */
titlesThankedIn ImdbTitle[],	/*simpl_collection */
awards text,	/*simpl_scalar simpl_hints */
trivia text,	/*simpl_scalar simpl_hints */
awardsLink varchar(30),	/*simpl_scalar simpl_hints */
birth_detail BirthDetail,	/*simpl_composite */
miniBiography text,	/*simpl_scalar simpl_hints */
biographyLink varchar(30),	/*simpl_scalar simpl_hints */
titlesAsSelf ImdbTitle[]	/*simpl_collection */
); 

--MetadataClassDescriptor[ecologylab.semantics.metadata.builtins.Entity]
CREATE TYPE Entity AS (
linkedDocument Document,
location varchar(30),	/*simpl_scalar simpl_hints */
gist text	/*simpl_scalar simpl_hints */
); 

