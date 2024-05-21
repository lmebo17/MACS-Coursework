using namespace std;
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <unistd.h>
#include "imdb.h"
#include <string.h>
#include <stdlib.h>

const char *const imdb::kActorFileName = "actordata";
const char *const imdb::kMovieFileName = "moviedata";

typedef struct{
    string name;
    const void* file;
} actor;

typedef struct{
    film movie;
    const void* file;
} filmStruct;

string to_String(char* name , int& size){
    string res = "";
    while(*name != '\0'){
        res += *name;
        size++;
        name++;
    }
    
    return res;
}

// comparing the names and years of movies
int movie_cmp(const void* a, const void* b){
    filmStruct* tmp = (filmStruct*)a;
    film first = tmp->movie;
    film second;
    char* start = (char*)(tmp->file) + *(int*)b;
    int size = 1;
    string name = to_String(start , size);
    second.title = name; 
    second.year = 1900 + *(char*)(start + size);
    if(first < second) return -1;
    if(first == second) return 0;
    return 1;
   
}

// comparing the names of actors
int myStrcmp(const void* first , const void* second){
    string a = ((actor*)first)->name;  
    return strcmp(a.c_str() , ((char*)((actor*)first)->file + *(int*)second));
}

imdb::imdb(const string& directory){
  const string actorFileName = directory + "/" + kActorFileName;
  const string movieFileName = directory + "/" + kMovieFileName;
  
  actorFile = acquireFileMap(actorFileName, actorInfo);
  movieFile = acquireFileMap(movieFileName, movieInfo);
  
}

bool imdb::good() const{
  return !( (actorInfo.fd == -1) || 
	    (movieInfo.fd == -1) ); 
}


// you should be implementing these two methods right here... 
bool imdb::getCredits(const string& player, vector<film>& films) const {
    
    int amount = *(int*)actorFile; // in the beginning the pointer is referring to the number of actors
    
    actor actor; // creating an actor struct in order to save a pointer to the files and then use it in comparator functions
    actor.name = player;
    actor.file = actorFile;
    
    int* curr = (int*)bsearch(&actor , (int*)actorFile + 1 , amount , sizeof(int) , myStrcmp);
    if(curr == NULL) return false;
    int playerBites = player.size() + 1 + (player.size() % 2 == 0 ? 1 : 0); 
    // movie_count points to the short containing the number of movies
    char* movie_count =(char*)actorFile + *curr + playerBites;
    short count = *(short*)(movie_count); // number of movies
    
    // calculating the beginning of the next usefull pointer
    playerBites += 2;
    int dif = playerBites;
    playerBites += playerBites % 4 == 0 ? 0 : 2; dif = playerBites - dif;

    // generating the movies in which the actor played
    int* start = (int*)(movie_count + 2 + dif);
    for(int i = 0; i < count; i++){
        char* name = (char*)movieFile + *(start + i);
        int size = 1;
        string movie = to_String(name , size);
        int year = 1900 + *(char*)(name + size);
        film curr;
        curr.title = movie;
        curr.year = year;
        films.push_back(curr);
    }
    return true;

}


bool imdb::getCast(const film& movie, vector<string>& players) const { 
    int movieCount = *(int*)movieFile; // in the beginning the pointer is referring to the number of movies
    
    filmStruct film; // creating a film struct in order to save a pointer to the files and then use it in comparator functions
    film.file = movieFile;
    film.movie = movie; 

    char* curr = (char*)bsearch(&film , (int*)movieFile + 1, movieCount, sizeof(int) , movie_cmp);
    if(curr == NULL) return false;
    // calculating the beginnings of pointers
    curr = (char*)movieFile + *(int*)curr;
    int size = movie.title.size() + 2;
    size += (size % 2 == 1);
    curr += size; 
    short num = *(short*)curr; // number of actors
    size += 2;
    curr += 2;
    if(size % 4 != 0) {
        size += 2;
        curr += 2;
    }

    // generating actors
    for(int i = 0; i < num; i++){
        int index = *(int*)(curr + i*sizeof(int));
        string player = (char*)actorFile + index;
        players.push_back(player);
    }
    
    return true; 
    
}

imdb::~imdb(){
  releaseFileMap(actorInfo);
  releaseFileMap(movieInfo);
}

// ignore everything below... it's all UNIXy stuff in place to make a file look like
// an array of bytes in RAM.. 
const void *imdb::acquireFileMap(const string& fileName, struct fileInfo& info){
  struct stat stats;
  stat(fileName.c_str(), &stats);
  info.fileSize = stats.st_size;
  info.fd = open(fileName.c_str(), O_RDONLY);
  return info.fileMap = mmap(0, info.fileSize, PROT_READ, MAP_SHARED, info.fd, 0);
}

void imdb::releaseFileMap(struct fileInfo& info){
  if (info.fileMap != NULL) munmap((char *) info.fileMap, info.fileSize);
  if (info.fd != -1) close(info.fd);
}
