#
# JFTD2XX Makefile
# NOTE: DLL provided gcc under cygwin does not work with JNI (freeze duing loading). I use MS's cl.exe for compilation

# cl /LD JFTD2XX.c FTD2XX.lib

JSRC = JFTD2XX.java
JOBJ = JFTD2XX.class

TARGETS = JFTD2XX.class jftd2xx_JFTD2XX.h JFTD2XX.dll

INC = -I.
DEF = -D_JNI_IMPLEMENTATION_
LIB = FTD2XX.lib

CC = cl
JAVAH = javah -jni
JAVAC = javac

JFLAGS = -O
CFLAGS = -O2 /Ld -g $(INC) $(DEF)
LDFLAGS = -shared -Wl,--kill-at

.PRECIOUS: %.class

all: $(TARGETS)

%.class: $(JSRC)
	$(JAVAC) $(JFLAGS) $^

%.h: %.class
	$(JAVAH) -classpath .. $*

%.dll: %.o
	$(CC) $(LDFLAGS)  $^ $(LIB)

clean:
	$(RM) $(TARGETS) JFTD2XX.exp JFTD2XX.lib  JFTD2XX.obj

distclean: clean
	$(RM) *.bak *.bak
