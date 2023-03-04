#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// global variable declaration
int maxLengthOfSaying = 256;
int maxNumberOfSayings = 50;
int fileNameSize = 30;
char * fileContent;
FILE * fp;

int getFileSize(char fileName[]) {
  fp = fopen (fileName, "r");
  if (fp == NULL) {
    perror("error in opening file");
    printf("the file you pointed is invalid. program exits.\n");
    exit(0);
  }
  int fileSize = 0;
  fseek(fp, SEEK_SET, SEEK_END);
  fileSize = ftell(fp);
  printf("The given file has a size of %i.\n", fileSize);
  return fileSize;
}

void readFileContent(char fileName[], int fileSize) {
  fp = fopen (fileName, "r");
  fileContent = (char*) malloc (fileSize * sizeof(char));
  int charNum = 0;
  int tempChar;
  if (fp == NULL) {
    perror("error in opening file");
    printf("the file you tried to read is invalid. program exits.\n");
    exit(0);
  }
  while (1) {
    tempChar = fgetc(fp);
    if (feof(fp)) { break; }
    fileContent[charNum] = tempChar;
    charNum++;
  }
  printf("the designated file has been read successully.\n");
}

int main() {
  // read the starting text file
  char fileName[fileNameSize];
  printf("enter your file name: ");
  scanf("%s", fileName);
  printf("you entered <%s>.\n", fileName);
  const int fileSize = getFileSize(fileName);
  readFileContent(fileName, fileSize);
  // assign the file content to the database
  char database[maxNumberOfSayings][maxLengthOfSaying];
  int lineNumber = 0;
  int lineCursor = 0;
  char selection[maxNumberOfSayings][maxLengthOfSaying];
  int selectionLine = 0;
  for (int i = 0; i < fileSize; i++) {
    if (fileContent[i] == '\n') {
      lineNumber++;
      lineCursor = 0;
      continue;
    }
    database[lineNumber][lineCursor] = fileContent[i];
    lineCursor++;
  }
  //printf("%s", database[0]);
  printf("you now have options to do with your saying database as follows:\n");
  printf("1 - display all sayings currently in the database\n");
  printf("2 - enter a new saying into the database\n");
  printf("3 - list sayings that contain a given string entered by the user\n");
  printf("4 - save all sayings in a new text file\n");
  printf("5 - quit the program\n");
  char userSelection[1];
  char newSayingEntered[64];
  char * textEnteredToSearch;
  char * overlapped;
  //
  while (1) {
    printf("enter your selection: ");
    scanf("%s", userSelection);
    printf("you entered %s\n", userSelection);
    switch(userSelection[0]) {
    case '1':
      printf("okay, here is all sayings in the database.\n");
      for (int i = 0; i < lineNumber; i++) {
        printf("%s\n", database[i]);
      }
      printf("\n");
      break;
    case '2':
      printf("okay, enter a new saying: ");
      fflush(stdin);
      fgets(newSayingEntered, sizeof(newSayingEntered), stdin);
      newSayingEntered[strcspn(newSayingEntered, "\n")] = 0;
      fflush(stdin);
      for (int i = 0; i < maxLengthOfSaying; i++) {
        database[lineNumber][i] = newSayingEntered[i];
      }
      lineNumber++;
      printf("your new saying \"%s\" is recorded into database.\n", newSayingEntered);
      break; 
    case '3':
      printf("okay, enter any text to search: ");
      textEnteredToSearch = malloc(sizeof(char) * maxLengthOfSaying);
      fflush(stdin);
      scanf("%19[^\n]", textEnteredToSearch);
      fflush(stdin);
      printf("the piece you entered is: %s\n", textEnteredToSearch);
      for (int i = 0; i < maxNumberOfSayings; i++) {
        overlapped = strstr(database[i], textEnteredToSearch);
        if (overlapped) {
          printf("Yo!");
          for (int j = 0; j < maxNumberOfSayings; j++) {
             selection[selectionLine][j] = database[i][j];
           }
          selectionLine++;
        } 
      }
      printf("here are the selected in search:\n"); 
      for (int i = 0; i < maxNumberOfSayings; i++) {
        if (!selection[i][0]) { break; }
        printf("%s\n", selection[i]);
      }      
      break;
    case '4':
      fp = fopen("result.txt", "w");
      for (int i = 0; i < lineNumber; i++) {
        fprintf(fp, "%s\n", database[i]);
      }
      fclose(fp);
      printf("Your result file is saved.");
      break;
    case '5':
      printf("okay, we are quitting the program.\n");
      exit(0); break;
    default:
      printf("you entered the number that isn't our option. try again.\n");
    }
  }
  return 0;
}
