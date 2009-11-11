# current working directory

XML_DIR = xml xml/types/scalar xml/types/element xml/library/yahoo xml/library/dc xml/library/feedburner xml/library/media xml/library/itunes xml/library/rss  xml/library/icdl xml/library/opml xml/library/geom xml/library/jnlp xml/library/jnlp/applet xml/library/jnlp/application xml/library/jnlp/information xml/library/jnlp/resource 

SERVICES = services/authentication services/authentication/listener services/authentication/logging services/authentication/messages services/authentication/nio services/authentication/registryobjects services/distributed/client services/distributed/common services/distributed/impl services/distributed/legacy services/distributed/server services/distributed/server/clientsessionmanager services/distributed/server/varieties services/exceptions services/logging services/logging/playback services/messages services/messages/cf services/distributed/exception

FUNDAMENTAL_DIRS = appframework appframework/types appframework/types/prefs appframework/types/prefs/gui collections generic generic/text io net $(XML_DIR) $(SERVICES)

JAR_DIRS = $(FUNDAMENTAL_DIRS:%=ecologylab/%)

MAKE_DIR = ../../makefiles
include $(MAKE_DIR)/java.make

TARGET		= ecologylabFundamental

jar:
	rm -f $(JAR_FILE)
	$(JAR) cvf $(JAR_FILE) $(JAR_CONTENTS)
	cd ../ecologylabMacOS; $(JAR) uvf $(JAR_FILE) ecologylab/appframework/macos/MacOSApp.class

