/*

convert jop file to memory dump file. Used with DE2 Control Panel
for debugging purposes only.

*/

#include <windows.h>
#include <stdio.h>
#include <string.h>

// maximum of 1MB
#define MAX_MEM	(1048576/4)

static int prog_cnt = 0;
static char prog_char[] = {'|','/','-','\\','|','/','-','\\'};
static char *exitString = "JVM exit!";


int main(int argc, char *argv[]) {

	unsigned char c;
	int i, j;
	long l;

	long *ram;
	long len;

	unsigned char *byt_buf;

	HANDLE hCom;
	DCB dcb;
	COMMTIMEOUTS ctm;
	DWORD dwError;
	BOOL fSuccess;

	FILE *fp;
	char buf[10000];
	char *tok;

	ram = calloc(MAX_MEM, 4);
	if (ram==NULL) {
		printf("error with allocation\n");
		exit(-1);
	}
	byt_buf = malloc(MAX_MEM*4);
	if (byt_buf==NULL) {
		printf("error with allocation\n");
		exit(-1);
	}

	if (argc!=2) {
		printf("usage: jop2dat  jop_file\n");
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
			if (sscanf(tok, "%ld", &l)==1) {
				ram[len++] = l;
			}
			tok = strtok(NULL, " ,\t");
			if (len>=MAX_MEM) {
				printf("too many words (%d/%d)\n", len, MAX_MEM);
				exit(-1);
			}
		}
	}

	fclose(fp);

	for (i=0; i<len; ++i) {
		l = ram[i];
		
		//for (j=0; j<4; ++j) {
			byt_buf[i*4+0] = l>>(2*8);
			byt_buf[i*4+1] = l>>(3*8);
			byt_buf[i*4+2] = l>>(0*8);
			byt_buf[i*4+3] = l>>(1*8);
		//}
	}
//
//	write external RAM
//
	sprintf(buf, "%s.bin",argv[1]); 
	hCom = CreateFile(buf,
		GENERIC_READ | GENERIC_WRITE,
		0,    /* comm devices must be opened w/exclusive-access */
		NULL, /* no security attrs */
		CREATE_ALWAYS, 
		0,    /* not overlapped I/O */
		NULL  /* hTemplate must be NULL for comm devices */
		);

	printf("%d words of Java bytecode (%d KB)\n", ram[1]-1, (ram[1]-1)/256);

		WriteFile(hCom, byt_buf, len*4, &i, NULL);
		if (i!=len*4) {
			printf("download error %ld %ld\n", len*4, i);
			exit(-1);
		}

	printf("%d words external RAM (%d KB)\n", len, len/256);

	printf("convert complete\n");
	printf("\n\n");


	CloseHandle(hCom);

	return 0;
}

