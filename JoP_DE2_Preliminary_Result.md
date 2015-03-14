I worked on JOP and here are my preliminary results.


The native way that JOP communicates with PC is via the Serial i.e. RS-232 port. Unfortunately my laptop does not have a serial port. The solution is to buy a USB-Serial adapter (http://www.serialgear.com/USB-to-Serial-Adapters-(RS232,-RS422-&-RS485)-USBG-232.html). The driver for this adapter will create a virtual COM port on PC on which the serial communication is carried out.

JOP's manual did mention that compiled Java programs can be downloaded via "USB". This tricked me for several hours before I found out that this is a completely different USB than the Blaster Interface we have on the DE2. Basically, In order for this "USB" downloading to work, I have to connect a expansion board (http://www.jopdesign.com/board.jsp#baseio) to the DE2 and use the USB port on that board. This isn't easy because the expansion board is not designed for DE2. Also, I suspect some tweaks on pin definitions have to be made to the corresponding VHDL files.

P.S. Maybe I was wrong... I'll check the source code for USBRunner tomorrow.


The ECAD lab Game of Life did manage to communicate with DE2 using a single Blaster cable. They managed to do it using JTAG\_UART which simulates a UART i.e. serial port on the FPGA. This will only work on a SoPC solution where JTAG\_UART is designed for. JOP has a quartus project for DE2 using SoPC, but for some strange reason a key component (jop\_avalon) was missing from the CVS so currently I’m unable to build it. Also, SoPC is twice as slow as the plain JOP on DE2. The PC side for JTAG\_UART can also be a problem due to the lack of official support and source codes from Altera.

To be continued…