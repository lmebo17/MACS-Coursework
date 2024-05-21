#include "kernel/types.h"
#include "kernel/stat.h"
#include "kernel/fcntl.h"
#include "user/user.h"
#include "kernel/param.h"




int main(int argc, char* argv[]){
    char* func_to_exec = argv[1];  
    while(1){
        char buf[100];
        int n = read(0, buf, sizeof(buf));
        int cnt = 1;
        for(int i = 0; i < n; i++){
            cnt += (buf[i] == ' ' || buf[i] == '\n');
        }
        
        printf("");

        char* new_buf[argc + cnt];
        for(int i = 0; i < argc - 1; i++){
            new_buf[i] = argv[i+1];
        }
        
        
        int ind = argc - 1;
        char* arg = malloc(1);
        char* start = arg;
        for(int i = 0; i <= n; i++){
            if(buf[i] == ' ' || buf[i] == '\n' || buf[i] == '\t'){
                *arg = '\0';
                new_buf[ind] = start;
                ind++;
                arg = malloc(1);
                start = arg;
                continue;
            }
             *arg = buf[i];
              arg++;
        }
            
        new_buf[argc - 1 + cnt] = 0;

        if(n <= 0) break;
        int pid = fork();
        if(pid == 0){
            exec(func_to_exec, new_buf);
        } else {
            wait(0);
        }
    
    }

    return 0;
}