#include "kernel/types.h"
#include "kernel/stat.h"
#include "kernel/fcntl.h"
#include "user/user.h"



void child_work(int lp[]){
    int pr;
    int k = read(lp[0], &pr, sizeof(int));
    if(k <= 0) return;
    printf("prime %d\n", pr);
    if(pr == 31) return;
    int rp[2];
    pipe(rp);
    int curr = 0;
    while(1){
        int n = read(lp[0], &curr, sizeof(int));
        if(n <= 0) {
            break;
        }
        if(curr % pr){
           n = write(rp[1], &curr, sizeof(int));
        }
        if(n <= 0) break;
    }
    int pid = fork();
    if(pid == 0){
        close(rp[1]);
        close(lp[0]);
        child_work(rp);
        close(rp[0]);
    } else {
        close(rp[0]);
        close(rp[1]);
        close(lp[0]);
        wait(0); 
    }
}

int main(int argc, char* argv[]) {
    int p[2];
    int k = pipe(p);
    if(k == -1) return k;
    for(int i = 2; i <= 35; i++){
        int tmp = i;
        k = write(p[1], &tmp, sizeof(int));
        if(k == -1) return k;
    }
    close(p[1]);
    child_work(p);
    close(p[0]);
    return 0;
}
