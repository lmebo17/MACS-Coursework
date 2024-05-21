#include "kernel/types.h"
#include "kernel/stat.h"
#include "kernel/fcntl.h"
#include "user/user.h"


int main(int argc, char* argv[]){
    int fd[2]; 
    char buf[1] = {'x'};
    pipe(fd);
    int pid = fork();
    if(pid == 0){
        read(fd[0], buf, 1);
        close(fd[0]);
        printf("%d: received ping\n", getpid());
        write(fd[1], buf , 1);
        close(fd[1]);
    } else {
        write(fd[1], buf, 1);
        close(fd[1]);
        read(fd[0], buf, 1);
        printf("%d: received pong\n", getpid());
        close(fd[0]);
        
    }
    exit(0);
}
