/*

Experimental program with USB_Blaster Interface

*/


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <windows.h>
#include "FTD2XX.H"

FT_HANDLE            ft_handle;

void initDevice(void) {
	DWORD status,bytesent = 0;
	status = FT_Open(0, &ft_handle);
	FT_SetLatencyTimer(ft_handle, 2);
	char buf[5];
	buf[0] = 0x1F; buf[1]=buf[2]=buf[3]=buf[4]=0;
	FT_Write(ft_handle, &buf, 1, &bytesent);

	FT_Close(ft_handle);
	//status = FT_Open(0, &ft_handle);
}

void sendStr(char cmd[], int len) {
	char buf[1024];
	DWORD status,bytesent = 0;

	buf[0] = 0x1F; buf[1]=buf[2]=buf[3]=buf[4]=0;
	FT_Write(ft_handle, buf, 1, &bytesent);

	FT_Close(ft_handle);


	status = FT_Open(0, &ft_handle);
	FT_SetLatencyTimer(ft_handle, 2);
	buf[0] = 0x26;buf[1] = 0x27;buf[2] = 0x26;buf[3] = 0x81;buf[4] = 0x00;
	FT_Write(ft_handle, buf, 5, &bytesent);
	buf[0] = 0x1F; buf[1]=buf[2]=buf[3]=buf[4]=0;
	FT_Write(ft_handle, &buf, 1, &bytesent);
	FT_Close(ft_handle);


	status = FT_Open(0, &ft_handle);
	if (status != FT_OK) {
		printf("FT_Open failed:%d",status);
		return;
	}

	FT_SetLatencyTimer(ft_handle, 2);
	buf[0] = 0x26;buf[1] = 0x27;buf[2] = 0x26;buf[3] = 0x81;buf[4] = 0x00;
	FT_Write(ft_handle, buf, 5, &bytesent);

	memcpy(buf+1, cmd, len);
	buf[0] = 0x88; buf[1] = '1';buf[2] = '7';buf[3] = '1';buf[4] = '7';buf[5] = '1';buf[6] = '7';buf[7] = '1';buf[8] = '7';
	FT_Write(ft_handle, buf, 8+1, &bytesent);
	
	//Sleep(1000);
	buf[0] = 0x1F; buf[1]=buf[2]=buf[3]=buf[4]=0;
	FT_Write(ft_handle, buf, 1, &bytesent);

	FT_Close(ft_handle);
	
	status = FT_Open(0, &ft_handle);
	FT_SetLatencyTimer(ft_handle, 2);
	buf[0] = 0x26;buf[1] = 0x27;buf[2] = 0x26;buf[3] = 0x81;buf[4] = 0x00;
	FT_Write(ft_handle, buf, 5, &bytesent);
	buf[0] = 0x1F; buf[1]=buf[2]=buf[3]=buf[4]=0;
	FT_Write(ft_handle, &buf, 1, &bytesent);
	FT_Close(ft_handle);
	
}

int recvStr(char* cmd) {
	char buf[1024];
	DWORD status, queue_len, bytesread = 0;
	int len = 0;
	

	printf("send byte\n");
	buf[0] = 0x84; buf[1] = 0x00;buf[2] = 0x00;buf[3] = 0x00;buf[4] = 3;
	FT_Write(ft_handle, buf, 4+1, &bytesread);
	buf[0] = 0xC1; buf[1] = 0;
	FT_Write(ft_handle, buf, 2, &bytesread);
	status = FT_Read(ft_handle, cmd, 1, &bytesread);
	printf("recv byte %d %c\n", cmd[0], cmd[0]);

	printf("send byte\n");
	buf[0] = 0x84; buf[1] = 0x12;buf[2] = 0x34;buf[3] = 0x56;buf[4] = 0x78;
	FT_Write(ft_handle, buf, 4+1, &bytesread);
	buf[0] = 0xC1; buf[1] = 0;
	FT_Write(ft_handle, buf, 2, &bytesread);
	status = FT_Read(ft_handle, cmd, 1, &bytesread);
	printf("recv byte %d %c\n", cmd[0], cmd[0]);

	printf("send byte\n");
	buf[0] = 0x84; buf[1] = 0x12;buf[2] = 0x34;buf[3] = 0x56;buf[4] = 0x78;
	FT_Write(ft_handle, buf, 4+1, &bytesread);
	buf[0] = 0xC1; buf[1] = 0;
	FT_Write(ft_handle, buf, 2, &bytesread);
	status = FT_Read(ft_handle, cmd, 1, &bytesread);
	printf("recv byte %d %c\n", cmd[0], cmd[0]);

	return 0;
	
	printf("send byte\n");
	buf[0] = 0x84; buf[1] = 0x00;buf[2] = 0x00;buf[3] = 0x00;buf[4] = 3;
	FT_Write(ft_handle, buf, 4+1, &bytesread);
	buf[0] = 0xC1; buf[1] = 0;
	FT_Write(ft_handle, buf, 2, &bytesread);
	status = FT_Read(ft_handle, cmd, 1, &bytesread);
	printf("recv byte %d %c\n", cmd[0], cmd[0]);

	printf("send byte\n");
	buf[0] = 0x84; buf[1] = 0x12;buf[2] = 0x34;buf[3] = 0x56;buf[4] = 0x78;
	FT_Write(ft_handle, buf, 4+1, &bytesread);
	buf[0] = 0xC1; buf[1] = 0;
	FT_Write(ft_handle, buf, 2, &bytesread);
	status = FT_Read(ft_handle, cmd, 1, &bytesread);
	printf("recv byte %d %c\n", cmd[0], cmd[0]);

	printf("send byte\n");
	buf[0] = 0x84; buf[1] = 0x12;buf[2] = 0x34;buf[3] = 0x56;buf[4] = 0x78;
	FT_Write(ft_handle, buf, 4+1, &bytesread);
	buf[0] = 0xC1; buf[1] = 0;
	FT_Write(ft_handle, buf, 2, &bytesread);
	status = FT_Read(ft_handle, cmd, 1, &bytesread);
	printf("recv byte %d %c\n", cmd[0], cmd[0]);

	return 0;
	//while(1) 
	{
		bytesread = 0;
		buf[0] = 0xC1; buf[1] = 0;
		FT_Write(ft_handle, buf, 2, &bytesread);

		len = 0;
		cmd[len] = '\0';
		status = FT_Read(ft_handle, cmd+len, 1, &bytesread);
		printf("recv byte %d\n", cmd[len]);
		//printf("%2x",cmd[len]);
		//if (cmd[len]=='\0') break;
		if (status != FT_OK || len ==1024-1) {
				//printf("FT_Read failed:%d",status);
				return len;
		}
		len++;
	}
	

	//buf[0] = 0x1F; buf[1]=buf[2]=buf[3]=buf[4]=0;
	//FT_Write(ft_handle, buf, 1, &bytesread);
	
	//FT_Close(ft_handle);
	if (len != 0)
	{
		//buf[0] = 0x88; buf[1] = '1';buf[2] = '2';buf[3] = '3';buf[4] = '4';buf[5] = '5';buf[6] = '6';buf[7] = '7';buf[8] = '8';
		//FT_Write(ft_handle, buf, 8+1, &bytesread);
	}
	return len;
}
void program(char * filename) {
	/*
	HANDLE hFile = CreateFile(buf,
		GENERIC_READ | GENERIC_WRITE,
		0,    /* comm devices must be opened w/exclusive-access 
		NULL, /* no security attrs /
		CREATE_ALWAYS, 
		0,    /* not overlapped I/O 
		NULL  // hTemplate must be NULL for comm devices 
		);
		*/
}
int main(int argc, char **argv)
{
	int status, len ;
	char buf[1024];
	DWORD queue_len, bytesread = 0;
	char cmd;
	int rawonly = 0;
	initDevice();
	if (argc >= 2)
		if (strcmp(argv[1] , "-raw") == 0)
			rawonly = 1;
		else if (strcmp(argv[1], "-p") == 0)
		{
			program(argv[2]);
			return 0;
		}
	
	
	printf("Open Devicee OK. Wait for command. Press q to quit\nUse -raw switch to view raw output\n\n");
	
	status = FT_Open(0, &ft_handle);
	if (status != FT_OK) {
		printf("FT_Open failed:%d",status);
		return 1;
	}
	
	
	FT_SetLatencyTimer(ft_handle, 2);
	buf[0] = 0x26;buf[1] = 0x27;buf[2] = 0x26;buf[3] = 0x81;buf[4] = 0x00;
	FT_Write(ft_handle, buf, 3, &bytesread);
//	while(1)
	{
		memset(buf,0,sizeof(buf));
		len = recvStr(buf);
		if (len >0) printf("%s\n", buf);
		//sendStr("7",1);
		//Sleep(5000);
		//len = recvStr(buf);
		//printf("%s", buf); 
		
	}
	return 0;
}
