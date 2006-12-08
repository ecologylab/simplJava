# current working directory

FUNDAMENTAL_DIRS = net generic io types xml xml/yahoo xml/dc xml/feedburner xml/media xml/itunes xml/rss services services/messages services/messages/studies services/messages/cf services/logging

JAVA_CLASS = $(JAVA_SRC:%.java=%.class)

JAR_DIRS = $(FUNDAMENTAL_DIRS:%=ecologylab/%)
DIRS	= $(JAR_DIRS)

KEYSTORE	=  -keystore c:/local/k/chain 
#KEYSTORE	=  -keystore h:/local/k/chain 

DOC_DIR = ../../cfdocs

DOC_DIRS = $(FUNDAMENTAL_DIRS)

JAVA_ROOT = .

#JAVA_SRC =  Detect.java

DOC_PACKAGES = $(subst /,.,$(DOC_DIRS))

special:	jar

detect:	Detect.class

MAKE_DIR = ../../makefiles
include $(MAKE_DIR)/java.make

JAVA_CLASS = $(DIRS:%=%/*.class)

TARGET		= ecologylabFundamental
TARGET_DIR	= ../cf
JAR_FILE	= $(TARGET_DIR)/$(TARGET).jar
SIGNER		= "Interface Ecology Lab"
STORE_PASS	= -storepass ecology

RELEASE = 2.1Beta4

.PHONY: ecologylab.jar

