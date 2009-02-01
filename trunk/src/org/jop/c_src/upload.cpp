/*
	uplaod Java bytecode to the DE2
	
*/

#include <windows.h>
#include <stdio.h>
#include <string.h>
#include "FTD2XX.H"


// maximum of 1MB
#define MAX_MEM	(1048576/4)

static int prog_cnt = 0;
static char prog_char[] = {'|','/','-','\\','|','/','-','\\'};
static char *exitString = "JVM exit!";


int main(int argc, char *argv[]) {

	unsigned char c;
	int i, j, count;
	long l1,l2,l3,l4;

	long *ram;
	long len;

	FILE *fp;
	char buf[10000];
	char *tok;

	ram = (long int*)calloc(MAX_MEM, 4);
	if (ram==NULL) {
		printf("error with allocation\n");
		exit(-1);
	}

	if (argc!=2) {
		printf("usage: upload  jop_file\n");
		exit(-1);
	}

//
//	read file
//
	if ((fp=fopen(argv[1],"r"))==NULL) {
		printf("Error opening %s\n", argv[1]);
		exit(-1);
	}

	len = 0;

	while (fgets(buf, sizeof(buf), fp)!=NULL) {
		for (i=0; i<strlen(buf); ++i) {
			if (buf[i]=='/') {
				buf[i]=0;
				break;
			}
		}
		tok = strtok(buf, " ,\t");
		while (tok!=NULL) {
			if (sscanf(tok, "%ld", &l1)==1) {
				ram[len++] = l1;
			}
			tok = strtok(NULL, " ,\t");
			if (len>=MAX_MEM) {
				printf("too many words (%d/%d)\n", len, MAX_MEM);
				exit(-1);
			}
		}
	}

	fclose(fp);
//////////////////////////////Uploading/////////////////////////
	int status ;
	DWORD queue_len, bytesread = 0, bytesend = 0;
	FT_HANDLE ft_handle;
	
	
	///Reset USB
	status = FT_Open(0, &ft_handle);
	if (status != FT_OK) {
		printf("FT_Open failed:%d",status);
		return 1;
	}
	FT_SetLatencyTimer(ft_handle, 2);
	buf[0] = 0x1F;
	FT_Write(ft_handle, buf, 1, &bytesend);
	FT_Close(ft_handle);


	status = FT_Open(0, &ft_handle);
	if (status != FT_OK) {
		printf("FT_Open failed:%d",status);
		return 1;
	}
	printf("Open Devicee OK. Uploading Java program...");
	
	
	FT_SetLatencyTimer(ft_handle, 2);
	buf[0] = 0x26;buf[1] = 0x27;buf[2] = 0x26; //Initialise Device
	FT_Write(ft_handle, buf, 3, &bytesend);
	
	ram[len+1]=ram[len+2]=ram[len+3] = 0;
	count = 15; //IN DWORDS, NO GREATER THAN 15
	DWORD t0 = GetTickCount();
	for (i=0; i<len; i += count) {
		j = (len-i)>=count ? count : (len-i);
		if (j == 0) break;
		buf[0] = 0x80 | (j * 4);
		for (j=0;j<count;j++) {
			l1 = ram[i+j];
			buf[j*4+1]  = l1>>(3*8);	buf[j*4+2]  = l1>>(2*8);	buf[j*4+3]  = l1>>(1*8);	buf[j*4+4] = l1>>(0*8);
		}
		//buf[5]  = l2>>(3*8);	buf[6]  = l2>>(2*8);	buf[7]  = l2>>(1*8);	buf[8] = l2>>(0*8);
		//buf[9]  = l3>>(3*8);	buf[10] = l3>>(2*8);	buf[11] = l3>>(1*8);	buf[12] = l3>>(0*8);
		//buf[13] = l4>>(3*8);	buf[14] = l4>>(2*8);	buf[15] = l4>>(1*8);	buf[16] = l4>>(0*8);
		FT_Write(ft_handle, buf, (buf[0] & 0x3F) + 1, &bytesend); //Send DWORD

		//buf[0] = 0x84;buf[1] = l>>(3*8);buf[2] = l>>(2*8);buf[3] = l>>(1*8);buf[4] = l>>(0*8);
		//FT_Write(ft_handle, buf, 4+1, &bytesend); //Send DWORD
		
		/*
		buf[0] = 0xC1; buf[1] = 0;
		FT_Write(ft_handle, buf, 2, &bytesend);   //Recv Acknowledge
		status = FT_Read(ft_handle, buf, 1, &bytesread);
		if (buf[0] != '1') {
			printf("Communication error at DWORD index %d(%d)\n", i, buf[0]);
			return 1;
		}*/
	}

	double speed = (double)len*4*1000/(GetTickCount()-t0)/1024.0;
	buf[0] = 0x1F;
	FT_Write(ft_handle, buf, 1, &bytesend); //Closing Device
	FT_Close(ft_handle);

	printf("\n%x words external RAM (%d KB)\n", len, len/256);
	printf("Speed:  %.1f KB/S\n", speed);
	printf("Uploading complete\n");


	return 0;
}

