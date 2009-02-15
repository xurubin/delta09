#
#	Makefile
#
#	Should build JOP and all tools from scratch.
#
#
#	You probably want to change the folloing parts in the Makefile:
#
#		QPROJ ... your Quartus FPGA project
#		all:, japp: ... USB download
#		TARGET_APP_PATH, MAIN_CLASS ... your target application
#
#
#


#
#
BLASTER_TYPE=USB-Blaster
QPROJ=altde2sram

#	Add your application source pathes and class that contains the
#	main method here. We are using those simple P1/2/3 variables for
#		P1=directory, P2=package name, and P3=main class
#	for sources 'inside' the JOP source tree
#
#	TARGET_APP_PATH is the path to your application source
#
#	MAIN_CLASS is the class that contains the Main method with package names
# here an example how to define an application outside
# from the jop directory tree
#TARGET_APP_PATH=/usr2/muvium/jopaptalone/src
#MAIN_CLASS=com/muvium/eclipse/PeriodicTimer/JOPBootstrapLauncher

P1=test
P2=test
P3=HelloWorld
# To do a benchmark, using the following project
#P1=bench
#P2=jbe
#P3=DoApp

TARGET=java/target
TARGET_APP_PATH=$(TARGET)/src/$(P1)
MAIN_CLASS=$(P2)/$(P3)


ifeq ($(WINDIR),)
	S=:
else
	S=\;
endif


#
#	some variables
#
TOOLS=java/tools
EXT_CP=-classpath java/lib/bcel-5.1.jar$(S)java/lib/jakarta-regexp-1.3.jar$(S)java/lib/RXTXcomm.jar$(S)java/lib/lpsolve55j.jar$(S)java/lib/log4j-1.2.15.jar$(S)java/lib/jgrapht-jdk1.5.jar$(S)java/lib/velocity-1.5.jar$(S)java/lib/velocity-dep-1.5.jar

TOOLS_JFLAGS=-g -d $(TOOLS)/dist/classes $(EXT_CP) -sourcepath $(TOOLS)/src$(S)$(TARGET)/src/common -encoding Latin1

PCTOOLS=java/pc
PCTOOLS_JFLAGS=-g -d $(PCTOOLS)/dist/classes -sourcepath $(PCTOOLS)/src -encoding Latin1



# changed to add another class to the tool chain
TOOLS_CP=$(EXT_CP)$(S)$(TOOLS)/dist/lib/jop-tools.jar$(S)$(TOOLS)/dist/lib/JopDebugger.jar

TARGET_SOURCE=$(TARGET)/src/common$(S)$(TARGET)/src/jdk_base$(S)$(TARGET)/src/jdk11$(S)$(TARGET)/src/rtapi$(S)$(TARGET_APP_SOURCE_PATH)
TARGET_JFLAGS=-d $(TARGET)/dist/classes -sourcepath $(TARGET_SOURCE) -bootclasspath "" -extdirs "" -classpath "" -source 1.5
GCC_PARAMS=""

# uncomment this if you want floating point operations in hardware
# ATTN: be sure to choose 'cycfpu' as QPROJ else no FPU will be available
#GCC_PARAMS="-DFPU_ATTACHED"



#	add more directoies here when needed
#		(and use \; to escape the ';' when using a list!)
TARGET_APP_SOURCE_PATH=$(TARGET_APP_PATH)$(S)$(TARGET)/src/bench$(S)$(TARGET)/src/app
TARGET_APP=$(TARGET_APP_PATH)/$(MAIN_CLASS).java


# setting for rup DSVM on JopCMP
#TARGET_APP_PATH=/usrx/DSVMFP/src

# just any name that the .jop file gets.
JOPBIN=$(P3).jop




#
#	application optimization with ProGuard:
#	proguard.sourceforge.net/
#	uncomment following line to use it
#OPTIMIZE=mv java/target/dist/lib/classes.zip java/target/dist/lib/in.zip; java -jar java/lib/proguard.jar @optimize.pro

#
#	application optimization with JOPtimizer
#	uncomment the following lines to use it
#
#OPTIMIZE=java $(EXT_CP)$(S)$(TOOLS)/dist/lib/joptimizer.jar joptimizer.JOPtimizerRunner \
#	 -config jar:file:$(TOOLS)/dist/lib/joptimizer.jar!/jop.conf $(MAIN_CLASS) && \
#	cd $(TARGET)/dist/classes && jar cf ../lib/classes.zip *



# build everything from scratch
all:
	make tools
	make jopser
	make japp
# build the Java application and download it
japp:
	make java_app
	make prog_jop
	make download


install:
	@echo nothing to install

clean:
############TODO:##########################
	-rm -rf $(TOOLS)/dist

#
#	build all the (Java) tools
#
tools:
	-rm -rf $(TOOLS)/dist
	mkdir $(TOOLS)/dist
	mkdir $(TOOLS)/dist/lib
	mkdir $(TOOLS)/dist/classes
	javac $(TOOLS_JFLAGS) $(TOOLS)/src/*.java
	javac $(TOOLS_JFLAGS) $(TOOLS)/src/com/jopdesign/build/*.java
	javac $(TOOLS_JFLAGS) $(TOOLS)/src/com/jopdesign/tools/*.java
	javac $(TOOLS_JFLAGS) $(TOOLS)/src/com/jopdesign/wcet/*.java
	javac $(TOOLS_JFLAGS) $(TOOLS)/src/com/jopdesign/dfa/*.java
#	javac $(TOOLS_JFLAGS) $(TOOLS)/src/com/jopdesign/wcet08/*.java
# Build libgraph and joptimizer
	#make joptimizer -e TOOLS_JFLAGS="$(TOOLS_JFLAGS)" TOOLS="$(TOOLS)"
# quick hack to get the tools with the debugger ok
# the build.xml from the debugger contains the correct info
# but also some more (old?) stuff
# does not work as some Sun classes for JDWP are missing
#	javac $(TOOLS_JFLAGS) $(TOOLS)/src/com/jopdesign/debug/jdwp/*.java
	cd $(TOOLS)/dist/classes && jar cf ../lib/jop-tools.jar *

#
#	Build joptimizer and libgraph
#	JAR must contain the filename of the jar tool of the JDK.
#
ifneq ($(JAVA_HOME),)
    JAR="$(JAVA_HOME)/bin/jar"
else
    JAR=jar
endif

jop_usb:
	-rm -rf $(TARGET)/dist/usb
	mkdir $(TARGET)/dist/usb
	javac -g -classpath $(TARGET)/src/common $(TARGET)/src/common/com/jopdesign/io/HostDatagramLayer.java -d $(TARGET)/dist/usb
	javac -g -classpath $(TARGET)/src/common $(TARGET)/src/common/jftd2xx/JFTD2XX.java -d $(TARGET)/dist/usb
	cd $(TARGET)/dist/usb && jar cf jop_usb.jar *
	cp $(TARGET)/dist/usb/jop_usb.jar ../lib
	-rm -rf $(TARGET)/dist/usb

joptimizer:
	make compile_java -e JAVAC_FLAGS="$(TOOLS_JFLAGS)" JAVA_DIR=$(TOOLS)/src/com/jopdesign/libgraph
	make compile_java -e JAVAC_FLAGS="$(TOOLS_JFLAGS)" JAVA_DIR=$(TOOLS)/src/joptimizer
	#cd $(TOOLS)/dist/classes && jar cfm ../lib/joptimizer.jar ../../src/joptimizer/MANIFEST.MF \
	cd $(TOOLS)/dist/classes && $(JAR) cf ../lib/joptimizer.jar \
		joptimizer com/jopdesign/libgraph \
		-C ../../src/joptimizer log4j.properties \
		-C ../../src/joptimizer jop.conf
	cd $(TOOLS)/dist/classes && $(JAR) uf ../lib/joptimizer.jar \
		-C ../../src joptimizer/config/jop-arch.properties \
		-C ../../src joptimizer/config/jvm-arch.properties

#
#	A helper target to compile all java files in a directory and all subdirs
#	Dont know how to 'find' on windows, so going the long way..
#
ifneq ($(JAVA_DIR),)
  jdirs := $(subst :,,$(shell ls -R $(JAVA_DIR) | grep ":"))
  jfiles := $(foreach dir,$(jdirs),$(wildcard $(dir)/*.java))
endif
compile_java:
	@echo "Compiling files in $(JAVA_DIR) .."
	@javac $(JAVAC_FLAGS) $(jfiles)


# we moved the pc stuff to it's own target to be
# NOT built on make all.
# It depends on javax.comm which is NOT installed
# by default - Blame SUN on this!
#
#	TODO: change it to RXTXcomm if it's working ok
#
#pc:
#	-rm -rf $(PCTOOLS)/dist
#	mkdir $(PCTOOLS)/dist
#	mkdir $(PCTOOLS)/dist/lib
#	mkdir $(PCTOOLS)/dist/classes
#	make compile_java -e JAVA_DIR=$(PCTOOLS)/src
#	javac $(PCTOOLS_JFLAGS) $(PCTOOLS)/src/udp/*.java
#	cd $(PCTOOLS)/dist/classes && jar cf ../lib/jop-pc.jar *

#
#	compile and JOPize the application
#
java_app:
	-rm -rf $(TARGET)/dist
	-mkdir $(TARGET)/dist
	-mkdir $(TARGET)/dist/classes
	-mkdir $(TARGET)/dist/lib
	-mkdir $(TARGET)/dist/bin
	javac $(TARGET_JFLAGS) $(TARGET)/src/common/com/jopdesign/sys/*.java
	javac $(TARGET_JFLAGS) $(TARGET)/src/jdk_base/java/lang/annotation/*.java	# oh new Java 1.5 world!
	javac $(TARGET_JFLAGS) $(TARGET_APP)
	cd $(TARGET)/dist/classes && jar cf ../lib/classes.zip *
	$(OPTIMIZE)
# use SymbolManager for Paulo's version of JOPizer instead
	java $(DEBUG_JOPIZER) $(TOOLS_CP) -Dmgci=false com.jopdesign.build.JOPizer \
		-cp $(TARGET)/dist/lib/classes.zip -o $(TARGET)/dist/bin/$(JOPBIN) $(MAIN_CLASS)
#	java $(DEBUG_JOPIZER) $(TOOLS_CP) -Dmgci=false com.jopdesign.debug.jdwp.jop.JopSymbolManager \
#		-cp $(TARGET)/dist/lib/classes.zip -o $(TARGET)/dist/bin/$(JOPBIN) $(MAIN_CLASS)
	java $(TOOLS_CP) com.jopdesign.tools.jop2dat $(TARGET)/dist/bin/$(JOPBIN)
#	cp *.dat modelsim
	rm -f *.dat
	cd $(TARGET)
#	jop2bin.exe $(TARGET)/dist/bin/$(JOPBIN)
	cp $(TARGET)/dist/bin/$(JOPBIN) .

#
#	project.sof fiels are used to boot from the serial line
#
jopser:
	cd asm && export GCC_PARAMS=$(GCC_PARAMS) && ./jopser.bat
	@echo $(QPROJ)
	for target in $(QPROJ); do \
		make qsyn -e QBT=$$target; \
		cd quartus/$$target; \
		cd ../..; \
	done



#
#	Quartus build process
#		called by jopser, jopusb,...
#
qsyn:
	echo $(QBT)
	echo "building $(QBT)"
	-rm -rf quartus/$(QBT)/db
	-rm -f quartus/$(QBT)/jop.sof
	-rm -f jbc/$(QBT).jbc
	-rm -f rbf/$(QBT).rbf
	quartus_map quartus/$(QBT)/jop
	quartus_fit quartus/$(QBT)/jop
	quartus_asm quartus/$(QBT)/jop
	quartus_tan quartus/$(QBT)/jop

#
#	Modelsim target
#		without the tools
#
sim: java_app
#	cd asm && ./jopsim.bat
#	cd modelsim && ./sim.bat
	# for simulation of CMP 
	# cd modelsim && ./sim_cmp.bat

#
#	JopSim target
#		without the tools
#
jsim: java_app
	java $(DEBUG_JOPSIM) -cp java/tools/dist/lib/jop-tools.jar -Dlog="false" \
	com.jopdesign.tools.JopSim java/target/dist/bin/$(JOPBIN)


download:
#	java -cp java/tools/dist/lib/jop-tools.jar$(S)java/lib/RXTXcomm.jar com.jopdesign.tools.JavaDown \
#		$(COM_FLAG) java/target/dist/bin/$(JOPBIN) $(COM_PORT)
#
#	this is the download version with upload.exe
	upload java/target/dist/bin/$(JOPBIN) 
	console


prog_de2:
	quartus_pgm -c $(BLASTER_TYPE) -m JTAG -o p\;DE2_control_panel/DE2_USB_API.sof

prog_jop:
	quartus_pgm -c $(BLASTER_TYPE) -m JTAG -o p\;quartus/$(QPROJ)/jop.sof


jop_blink_test:
	cd asm && ./build.bat blink
	@echo $(QPROJ)
	for target in $(QPROJ); do \
		echo "building $$target"; \
		rm -rf quartus/$$target/db; \
		qp="quartus/$$target/jop"; \
		echo $$qp; \
		quartus_map $$qp; \
		quartus_fit $$qp; \
		quartus_asm $$qp; \
		quartus_tan $$qp; \
		cd quartus/$$target && quartus_cpf -c jop.cdf ../../jbc/$$target.jbc; \
	done
	cd quartus/$(DLPROJ) && quartus_pgm -c $(BLASTER_TYPE) -m JTAG jop.cdf
	console


jop_testmon:
	cd asm && ./build.bat testmon
	@echo $(QPROJ)
	for target in $(QPROJ); do \
		echo "building $$target"; \
		rm -rf quartus/$$target/db; \
		qp="quartus/$$target/jop"; \
		echo $$qp; \
		quartus_map $$qp; \
		quartus_fit $$qp; \
		quartus_asm $$qp; \
		quartus_tan $$qp; \
		cd quartus/$$target && quartus_cpf -c jop.cdf ../../jbc/$$target.jbc; \
	done
	cd quartus/$(DLPROJ) && quartus_pgm -c $(BLASTER_TYPE) -m JTAG jop.cdf







