# current working directory
DIR	= cf/generic/AssetsCache

JAVA_SRC = Assets.java

# IntSlot used by Parser, Map

# Noise.java  Ordered.java Debug.java \
#	    List.java MimeList.java TestElement.java \

JAVA_ROOT=../../

MAKE_DIR = ../../../../makefiles
include $(MAKE_DIR)/applets.make

cabsign:	all

nsjar:		all

sunjar:		all

f:	Files.class
	java -classpath "$(CLASSPATH)" cm.generic.Files http://csdl.tamu.edu

g:	Generic.class
	java -classpath "$(CLASSPATH)" cm.generic.Generic

st:	StringTools.class
	java -classpath "$(CLASSPATH)" cm.generic.StringTools thisIsTheTime foo fooBar andAway FFFF

#	java -classpath "$(CLASSPATH)" cm.generic.StringTools http://www.nytimes.com http://csdl.tamu.edu/~andruid/courses http://csdl.tamu.edu/~andruid/courses/informedia/content.html http://csdl.tamu.edu/~andruid/courses/informedia/content.html?foo=bar http://csdl.tamu.edu/~andruid/courses/informedia/content.html#too

