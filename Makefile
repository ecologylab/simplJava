# current working directory

XML_DIR = xml xml/types/scalar xml/types/element xml/library/yahoo xml/library/dc xml/library/feedburner xml/library/media xml/library/itunes xml/library/rss 

SERVICES = services services/exceptions services/logging services/messages services/messages/cf

FUNDAMENTAL_DIRS = appframework appframework/types collections generic io net $(XML_DIR) $(SERVICES)

JAVA_CLASS = $(JAVA_SRC:%.java=%.class)

JAR_DIRS = $(FUNDAMENTAL_DIRS:%=ecologylab/%)
DIRS	= $(JAR_DIRS)

KEYSTORE	=  -keystore c:/local/k/chain 
KEYSTORE	=  -keystore h:/local/k/chain 

DOC_DIR = ../../cfdocs

DOC_DIRS = $(FUNDAMENTAL_DIRS)

DOC_PACKAGES = $(subst /,.,$(DOC_DIRS))

special:	jar

detect:	Detect.class

MAKE_DIR = ../../makefiles
include $(MAKE_DIR)/java.make

JAVA_CLASS = $(DIRS:%=%/*.class)

TARGET		= ecologylabFundamental
TARGET_DIR	= ../jars
SIGNER		= "Interface Ecology Lab"
STORE_PASS	= -storepass ecology

RELEASE = 2.2Beta2

.PHONY: ecologylab.jar

