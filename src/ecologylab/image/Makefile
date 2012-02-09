DIR	= cs/media

JAVA_SRC = PixelBased.java ImageState.java \
	AlphaGradientState.java BlurState.java \
	DesaturateState.java AreaAveragingScaleFilter.java \
	IIOPhoto.java Sound.java BufferedPhoto.java


#	QTTools.java QTMovie.java

# PhotoI.java Picture.java Icon.java FilmLoop.java Sound.java Text.java
#ToolBar.java ChoiceBar.java
# SoundPlay.java DesaturateFilter.java PhotoXform.java

JAVA_ROOT=../../

PACKAGE_CLASS = $(PACKAGE_SRC:%.java=%.class)

MAKE_DIR = ../../../../makefiles
include $(MAKE_DIR)/applets.make

cabsign: all

nsjar: all

qtmovie:	$(JAVA_CLASS)
	$(JAVA) cm.media.QTMovie

