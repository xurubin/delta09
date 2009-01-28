CC=gcc
CFLAGS=-pedantic -Wall

ifeq ($(WINDIR),)
	LDLIBS=-lftdi
else
	LDLIBS=FTD2XX.lib
endif

all: usb jop2bin upload console


console: console.o
	$(CC)  -o $@ $^ $(LDLIBS)
	cp $@.exe ..

upload: upload.o
	$(CC)  -o $@ $^ $(LDLIBS)
	cp $@.exe ..

jop2bin: jop2bin.o
	$(CC) $(LDFLAGS) -o $@ $^ $(LDLIBS)
	cp $@.exe ..

usb: usb.o
	$(CC)  -o $@ $^ $(LDLIBS)
#	cp $@.exe ..


jop2bin.o: jop2bin.c
usb.o: usb.cpp FTD2XX.H
upload.o: upload.cpp FTD2XX.H
console.o: console.cpp FTD2XX.H
clean::
	rm -f *.o *~
	rm -f *.exe *~
