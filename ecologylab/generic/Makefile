# current working directory
DIR	= cm/generic

JAVA_SRC = Environment.java ApplicationEnvironment.java DispatchTarget.java \
	   MoreMath.java Palette.java DLL.java IntSlot.java Bounds.java \
	   FloatSetElement.java FloatWeightSet.java \
	   Debug.java Memory.java StringInputStream.java  Convenience.java\
	   StringInputStream.java Displacement1D.java Line2D.java

# IntSlot used by Parser, Map

# Noise.java  Ordered.java Debug.java \
#	    List.java MimeList.java TestElement.java \

JAVA_ROOT=../../

MAKE_DIR = ../../../../makefiles
include $(MAKE_DIR)/applets.make

cabsign:	all

nsjar:		all

mm:	MoreMath.class
	java -classpath $(CLASSPATH) blocks.MoreMath

fws:	FloatWeightSet.class
	java -classpath $(CLASSPATH) blocks.FloatWeightSet

