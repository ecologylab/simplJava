--ClassDescriptor[translators.sql.testing.ecologylabXmlTest.ItemTest]
CREATE TABLE ItemTest (
guid varchar(30) ,	/*simpl_scalar simpl_hints */
categorySet text[] ,	/*simpl_nowrap simpl_collection */
author text ,	/*simpl_scalar simpl_hints */
title text ,	/*simpl_scalar simpl_hints */
description text ,	/*simpl_scalar simpl_hints */
link varchar(30) 	/*simpl_scalar simpl_hints */
); 

--ClassDescriptor[translators.sql.testing.ecologylabXmlTest.ChannelTest]
CREATE TABLE ChannelTest (
title text PRIMARY KEY UNIQUE ,	/*simpl_scalar simpl_hints */
items ItemTest[] NOT NULL ,	/*simpl_nowrap simpl_collection */
description text NOT NULL UNIQUE ,	/*simpl_scalar simpl_hints */
link varchar(30) UNIQUE 	/*simpl_scalar simpl_hints */
); 

--ClassDescriptor[translators.sql.testing.ecologylabXmlTest.RssStateTest]
CREATE TABLE RssStateTest (
CNN_TOP_FEED varchar(30) ,
outputFile bytea ,
FEEDBURNER_EXAMPlE text ,
ITEM_EXAMPLE text ,
ABC_SPORTS_FEED varchar(30) ,
DELICIOUS_FEED varchar(30) ,
version float NOT NULL ,	/*simpl_scalar */
NS_EXAMPLE text ,
BBC_FRONT_FEED varchar(30) ,
FLICKR_EXAMPLE text ,
NABEEL_TEST text ,
FLICKR_FEED varchar(30) ,
ABC_EXAMPLE text ,
channel ChannelTest PRIMARY KEY ,	/*simpl_composite */
NYT_TECH_FEED varchar(30) 
); 

--ClassDescriptor[translators.sql.testing.ecologylabXmlTest.ItemTest]
CREATE TYPE ItemTest AS (
guid varchar(30),	/*simpl_scalar simpl_hints */
categorySet text[],	/*simpl_nowrap simpl_collection */
author text,	/*simpl_scalar simpl_hints */
title text,	/*simpl_scalar simpl_hints */
description text,	/*simpl_scalar simpl_hints */
link varchar(30)	/*simpl_scalar simpl_hints */
); 

--ClassDescriptor[translators.sql.testing.ecologylabXmlTest.ChannelTest]
CREATE TYPE ChannelTest AS (
title text,	/*simpl_scalar simpl_hints */
items ItemTest[],	/*simpl_nowrap simpl_collection */
description text,	/*simpl_scalar simpl_hints */
link varchar(30)	/*simpl_scalar simpl_hints */
); 

