#include <stdio.h>

int getFileSize(char fileName[]) {
  FILE * fp;
  fp = fopen (fileName, "r");
  int fileSize = 0;
  fseek(fp, SEEK_SET, SEEK_END);
  fileSize = ftell(fp);
  printf("The given file has a size of %i.\n", fileSize);
  return fileSize;
}

int main() {

  // declare alphabet vars
  const int sizeOfAlphabets = 26;
  char firstLetter = 'a'; // equivalent to 97
  char alphabets[sizeOfAlphabets];

  // get array of alphabets
  int i;
  for (i = 0; i < sizeOfAlphabets; i++) {
    alphabets[i] = firstLetter + i;
  }
  printf("An array of alphabets are generated: %s\n", alphabets);
 
  // declare file pointer
  FILE * fp;
  fp = fopen("file.txt", "r");

  // get file size
  int fileSize = getFileSize("file.txt");
  
  // 
  char fileContent[fileSize];
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

  // close file pointer
  fclose(fp);

  // show the file content to the user
  printf("The content of the given file: %s\n", fileContent);
  
  int numOfblanks = 0;
  for (i = 0; i < fileSize; i++) {
    if (fileContent[i] ==  32) {
      numOfblanks++;
    }
  }
  printf("Number of words: %i.\n", numOfblanks);

  // frequencies per each alphabet
  int frequencyPerAlphabet[sizeOfAlphabets];
  int j;

  for (j = 0; j < sizeOfAlphabets; j++) {
    frequencyPerAlphabet[j] = 0;
  }
    
  for (i = 0; i < fileSize; i++) {
    for (j = 0; j < sizeOfAlphabets; j++) {
      if (fileContent[i] == alphabets[j]) {
        frequencyPerAlphabet[j]++;
      }
    }
  }

  for (j = 0; j < sizeOfAlphabets; j++) {
    printf("%c -> %i\n", alphabets[j], frequencyPerAlphabet[j]);
  }
  
  return 0;
}
