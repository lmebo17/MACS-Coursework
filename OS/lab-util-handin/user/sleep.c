#include "kernel/types.h"
#include "kernel/stat.h"
#include "kernel/fcntl.h"
#include "user/user.h"

int string_to_int(char* num);

int main(int argc, char* argv[]){
    if(argc == 1){
        fprintf(2, "error\n");
        return 0;
    }
    int ticks = string_to_int(argv[1]);
    
    sleep(ticks);
    return 0;
}

int string_to_int(char* num){
    int res = 0;
    while(*num != '\0'){
        res = 10*res + (*num - '0');
        num++;
    }
    return res;
}
