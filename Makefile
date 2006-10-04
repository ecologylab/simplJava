# current working directory

ECOLOGY_LAB_PATH = .

include $(ECOLOGY_LAB_PATH)/ecologylabDirs.make

JAVA_CLASS = $(JAVA_SRC:%.java=%.class)

JAR_DIRS = $(ECOLOGY_LAB_DIRS:%=ecologylab/%)
DIRS	= $(JAR_DIRS)

KEYSTORE	=  -keystore c:/local/k/chain 

DOC_DIR = ../../cfdocs

DOC_DIRS = $(ECOLOGY_LAB_DIRS)

JAVA_ROOT = .

#JAVA_SRC =  Detect.java

DOC_PACKAGES = $(subst /,.,$(DOC_DIRS))

special:	jar

detect:	Detect.class

MAKE_DIR = ../../makefiles
include $(MAKE_DIR)/java.make

JAVA_CLASS = $(DIRS:%=%/*.class)

TARGET		= ecologylab
JAR_FILE	= $(TARGET).jar
SIGNER		= "Interface Ecology Lab"
RELEASE = 2.1Beta4

.PHONY: ecologylab.jar

