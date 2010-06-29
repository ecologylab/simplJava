--ClassDescriptor[ecologylab.standalone.sqltranslator.input.Channel]
CREATE TABLE Channel (
title text UNIQUE,	/*ecologylab.xml.ElementState$xml_leaf*/
items Item[],	/*ecologylab.xml.ElementState$xml_nowrap*/
description text,	/*ecologylab.xml.ElementState$xml_leaf*/
link varchar(30),	/*ecologylab.xml.ElementState$xml_leaf*/
CONSTRAINT Channel_pkey PRIMARY KEY(title));

--ClassDescriptor[ecologylab.standalone.sqltranslator.input.Item]
CREATE TABLE Item (
guid varchar(30) UNIQUE,	/*ecologylab.xml.ElementState$xml_leaf*/
categorySet text[],	/*ecologylab.xml.ElementState$xml_nowrap*/
author text,	/*ecologylab.xml.ElementState$xml_leaf*/
title text,	/*ecologylab.xml.ElementState$xml_leaf*/
description text,	/*ecologylab.xml.ElementState$xml_leaf*/
link varchar(30),	/*ecologylab.xml.ElementState$xml_leaf*/
CONSTRAINT Item_pkey PRIMARY KEY(guid));

--ClassDescriptor[ecologylab.standalone.sqltranslator.input.RssState]
CREATE TABLE RssState (
channel Channel UNIQUE,	/*ecologylab.xml.ElementState$xml_nested*/
version float,	/*ecologylab.xml.ElementState$xml_attribute*/
CONSTRAINT RssState_pkey PRIMARY KEY(channel));

--ClassDescriptor[ecologylab.standalone.sqltranslator.input.Channel]
CREATE TYPE Channel AS (
title text UNIQUE,	/*ecologylab.xml.ElementState$xml_leaf*/
items Item[],	/*ecologylab.xml.ElementState$xml_nowrap*/
description text,	/*ecologylab.xml.ElementState$xml_leaf*/
link varchar(30),	/*ecologylab.xml.ElementState$xml_leaf*/
);

--ClassDescriptor[ecologylab.standalone.sqltranslator.input.Item]
CREATE TYPE Item AS (
guid varchar(30) UNIQUE,	/*ecologylab.xml.ElementState$xml_leaf*/
categorySet text[],	/*ecologylab.xml.ElementState$xml_nowrap*/
author text,	/*ecologylab.xml.ElementState$xml_leaf*/
title text,	/*ecologylab.xml.ElementState$xml_leaf*/
description text,	/*ecologylab.xml.ElementState$xml_leaf*/
link varchar(30),	/*ecologylab.xml.ElementState$xml_leaf*/
);

