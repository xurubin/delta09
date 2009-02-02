/*

USB_JTAG Console viewer

*/


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <windows.h>
#include "FTD2XX.H"

FT_HANDLE            ft_handle;


int recvStr(char* cmd) {
	char buf[1024];
	DWORD status, queue_len, bytesread = 0;
	int len = 0;
	
	while(1) 
	{
		bytesread = 0;
		buf[0] = 0xC1; buf[1] = 0;
		FT_Write(ft_handle, buf, 2, &bytesread);

		len = 0;
		cmd[len] = '\0';
		status = FT_Read(ft_handle, cmd+len, 1, &bytesread);
		
		buf[0] = 0x84;buf[1] = 0xAA;buf[2] = 0x55;buf[3] = 0xAA;buf[4] = 0x55;
		FT_Write(ft_handle, buf, 4+1, &bytesread); //Send DWORD
		if (cmd[len]=='\0') break;
		printf("%c",cmd[len]);
		if (status != FT_OK) {
			printf("FT_Read failed:%d",status);
			return 100;
		}
		if (len == 1024 - 1 ) {
			printf("Buffer full.\n");
			return len;
		}
		len++;
	}
	
	return len;
}

int main(int argc, char **argv)
{
	int status, len ;
	char buf[1024];
	DWORD queue_len, bytesread = 0;
	
	status = FT_Open(0, &ft_handle);
	FT_SetLatencyTimer(ft_handle, 2);
	buf[0] = 0x1F; 
	FT_Write(ft_handle, buf, 1, &bytesread);
	FT_Close(ft_handle);
	
	status = FT_Open(0, &ft_handle);
	if (status != FT_OK) {
		printf("FT_Open failed:%d",status);
		return 1;
	}
	printf("Open Devicee OK. Wait for Output.\n\n");
	FT_SetLatencyTimer(ft_handle, 2);
	buf[0] = 0x26;buf[1] = 0x27;buf[2] = 0x26;
	FT_Write(ft_handle, buf, 3, &bytesread);
	
	if (argc > 1) {
			buf[0] = 0x1F;
		FT_Write(ft_handle, buf, 1, &bytesread); //Closing Device
		FT_Close(ft_handle);
		return 0;
	}
	while(1)
	{
		memset(buf,0,sizeof(buf));
		len = recvStr(buf);
		Sleep(100);
	}
	
	return 0;
}
