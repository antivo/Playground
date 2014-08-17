#include <stdio.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <signal.h>

#define true 1
#define false 0
typedef int bool; // C ne podrzava BOOL tip podataka, istina ili laz, ovim ga stvaramo

#define SEMAPHORE_NUM 5
enum {READ, FULL, EMPTY, WRITE, CREATE}; // enum je posebna struktura koju nazivamo pobrojani tip
// ono sto ona napravi je deklarira konstante i pridjeli im broj pocevsi od nula. Ovdje je to READ = 0, FULL = 1 ...

typedef struct {
  int input, output;
  char M[5];
} container;

int sharedDataSID = -1; // prestavlja index prema kojemu raspoznajemo dijeljenu memoriju. postavljamo na -1 da znamo da nije inicijalizirana
int SemaphoreSet = -1; // prestavlja index prema memoriji u kojoj su spremljeni semafori. -1 znaci da ta memorija nije inicijalizirana
container* sharedContainer = (container *) -1; // pokazivac na dijeljenu memoriju. Postavljamo na -1 umjesto na NULL jer funkcija koja
// dodjeljuje vrijednost ovome pokazivacu u slucaju pogreske dodjeljuje -1, pa odma znamo da na nista ne pokazuje.

// funkcija u kojoj postavljamo inicijalne vrijednosti semafora te ULAZ i IZLAZ varijable na nula 
void initializeValues() {  
  sharedContainer->input = 0;
  sharedContainer->output = 0;
  semctl(SemaphoreSet, CREATE, SETVAL, 0);
  semctl(SemaphoreSet, EMPTY, SETVAL, 0);
  semctl(SemaphoreSet, WRITE, SETVAL, 1);
  semctl(SemaphoreSet, READ, SETVAL, 1);
  semctl(SemaphoreSet, FULL, SETVAL, 5);
}

// funkcija u kojoj inicijaliziramo semafore, inicijaliramo zajednicki dijeljeni prostor
bool initialize() {
  sharedDataSID = shmget(IPC_PRIVATE, sizeof(container), 0600); // allocate shared memory for data, vraca -1 u slucaju greske
  if(-1 != sharedDataSID) {
    sharedContainer = (container *) shmat(sharedDataSID, NULL, 0); // attach shared memory for data, vraca -1 u slucaju greske
    if(-1 != (int) sharedContainer) {
      SemaphoreSet = semget(IPC_PRIVATE, SEMAPHORE_NUM, 0600); //allocate semaphores, vraca -1 u slucaju greske
      if(-1 != SemaphoreSet) {
        initializeValues(); // poziva funkciju koja inicijalizira vrijednosti u alociranim memorijama
        return true; // ako je sve poslo kako treba ovdje izlazi iz funkcije
      }
    }
  }
  perror("Nastala je greska pri inicijalizaciji dijeljene memorije"); // ako se dogodila ikakva greska vrati false 
  return false;
}

void postaviBSEM(int semaphore) { // ovim postavljas binarni semafor sa indexom "semaphore" 
  semctl(SemaphoreSet, semaphore, SETVAL, 1);
}

void cekajBSEM(int semaphore) { // funckija za cekat binarni semafor sa indexom "semaphore"
  struct sembuf sbuf;
  sbuf.sem_num = semaphore;
  sbuf.sem_op  = -1;
  sbuf.sem_flg = 0;
  semop(SemaphoreSet, &sbuf, 1);
}
 
void postaviOSEM(int semaphore) { // za postavit operacijski semafor
  struct sembuf sbuf;
  sbuf.sem_num = semaphore;
  sbuf.sem_op  = 1;
  sbuf.sem_flg = 0;
  semop(SemaphoreSet, &sbuf, 1);
}
 
void cekajOSEM(int semaphore) { // za cekat operacijski semafor
  struct sembuf sbuf;
  sbuf.sem_num = semaphore;
  sbuf.sem_op  = -1;
  sbuf.sem_flg = 0;
  semop(SemaphoreSet, &sbuf, 1);  
}

void proizvodac(int proizvodacID) { 
  char in[255]; // varijabla u koju spremas poruku koju uneses sa tipkovnice
  int position = 0; // pozicija koju trenutno promatras u poruci unesenoj s tipkovnice
  cekajBSEM(READ); // cekas na semafor koji kaze smijes li citat sa ulaza tipkovnice
  printf("Unesi znakove za proizvođača %d :\n", proizvodacID + 1);
  scanf("%s", in);
  postaviBSEM(READ); 
  if (0 == proizvodacID) { // ako je ovo prvi prozivodac 
    cekajBSEM(CREATE);
  } else {
    postaviBSEM(CREATE);
  }
  
  do {
    cekajOSEM(FULL);
    cekajBSEM(WRITE);
    int input = sharedContainer->input;
    char currentChar = in[position];
    sharedContainer->M[input] = currentChar;
    printf("PROIZVOĐJAČ%d -> %c \n", proizvodacID, currentChar);
    sharedContainer->input = (input + 1) % 5;
    postaviBSEM(WRITE);
    postaviOSEM(EMPTY);          
    sleep(1);
  } while (in[position++]); // check if in[position] = '\0' and then increment position
}

void potrosac() {
  char out[255]; // poruka koju cemo ispisat na potrosacu
  int position = 0;
  int remaining = 2;
  while (remaining) {
    cekajOSEM(EMPTY);
    int output = sharedContainer->output;
    char currentChar = sharedContainer->M[output];
    out[position] = currentChar;
    printf("POTROŠAČ <- %c \n", currentChar);
    if (!currentChar) {
      --remaining;
    }
    sharedContainer->output = (output + 1) % 5;
    postaviOSEM(FULL);
    ++position;  
  }
  printf("\nPrimljeno je %s\n\n", out);
}

void cleanup() { // pri ciscenju obavljamo akcije obrnutim redosljedom
  if(-1 != (int) sharedContainer) { // ako je memorija assigned onda treba izbrisat taj pokazivac na koji je dodjeljena
    shmdt((void *) sharedContainer);
  }
  if(-1 != sharedDataSID) {
    shmctl(sharedDataSID, IPC_RMID, NULL); // ako je memorija alocirana, onda ju treba dealocirat
  }
  if(-1 != SemaphoreSet) {
    semctl(SemaphoreSet, 0, IPC_RMID, 0); // izbrisi/oslobodi semafore
  }
}

int main() {
  sigset(SIGINT, cleanup);
  if(initialize()) {
    int i = 0; 
    for (i=0;i<2;++i){
      switch(fork()){
        case -1:
          printf("Ne moze se stvoriti novi proces!\n");
          exit(0);
        case 0:
          proizvodac(i);
          exit(0);
        default:
          break;  
        }
    }
         
    switch(fork()){
      case -1:
        printf("Ne moze se stvoriti novi proces!\n");
        exit(0);
      case 0:
        potrosac();
        exit(0);
      default:
        break;
    }
   
    for (i=0;i<3;i++){
      wait(NULL);
    }
  }
  cleanup();
  return 0;
}
