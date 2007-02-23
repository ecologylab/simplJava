# current working directory

XML_DIR = xml xml/types/scalar xml/types/element xml/library/yahoo xml/library/dc xml/library/feedburner xml/library/media xml/library/itunes xml/library/rss 

SERVICES = services services/exceptions services/logging services/messages services/messages/cf

FUNDAMENTAL_DIRS = appframework appframework/types appframework/types/prefs collections generic io net $(XML_DIR) $(SERVICES) appframework/macos

JAR_DIRS = $(FUNDAMENTAL_DIRS:%=ecologylab/%)

MAKE_DIR = ../../makefiles
include $(MAKE_DIR)/java.make

TARGET		= ecologylabFundamental

jar:
	rm -f $(JAR_FILE)
	$(JAR) cvf $(JAR_FILE) $(JAR_CONTENTS)
	cd ../ecologylabMacOS; $(JAR) uvf $(JAR_FILE) ecologylab/appframework/macos/MacOSApp.class

