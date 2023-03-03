#include <stdio.h>

int main(void) {
  FILE * fp;
  fp = fopen("file.txt", "r");
  char fileContent[300];
  int z = 0;
  int tempChar;
  if (fp == NULL) {
    perror("error in opening file");
    return(-1);
  } do {
    tempChar = fgetc(fp);
    if (feof(fp)) {
      break;
    }
    fileContent[z] = tempChar;
    z++;
  } while(1);
  printf("%s", fileContent);
  // close file pointer
  fclose(fp);
  printf("Hello World\n");
  return 0;
}
