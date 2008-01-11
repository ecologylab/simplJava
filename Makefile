# current working directory

XML_DIR = xml xml/types/scalar xml/types/element xml/library/yahoo xml/library/dc xml/library/feedburner xml/library/media xml/library/itunes xml/library/rss  xml/library/icdl

SERVICES = services/exceptions services/logging services/logging/playback services/messages services/messages/cf services/authentication services/authentication/logging services/authentication/messages services/authentication/nio services/authentication/registryobjects services/distributed/client services/distributed/common services/distributed/impl services/distributed/legacy services/distributed/server services/distributed/server/contextmanager  

FUNDAMENTAL_DIRS = appframework appframework/types appframework/types/prefs appframework/types/prefs/gui collections generic io net $(XML_DIR) $(SERVICES) appframework/macos

JAR_DIRS = $(FUNDAMENTAL_DIRS:%=ecologylab/%)

MAKE_DIR = ../../makefiles
include $(MAKE_DIR)/java.make

TARGET		= ecologylabFundamental

jar:
	rm -f $(JAR_FILE)
	$(JAR) cvf $(JAR_FILE) $(JAR_CONTENTS)
	cd ../ecologylabMacOS; $(JAR) uvf $(JAR_FILE) ecologylab/appframework/macos/MacOSApp.class

