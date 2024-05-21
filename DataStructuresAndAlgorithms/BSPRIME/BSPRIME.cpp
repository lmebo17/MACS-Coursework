#include <bits/stdc++.h>
using namespace std;
 
int num[101865032], pnum[101865032] , res[150000101];; 

int main(){
    int k = 0, n, j, i;
    n = 101865031;
    int index = 3;
    int count = 1;
    res[1] = 1;
    res[2] = 1;
    for(i = 3; i <= n; i += 2){
        if(num[i] == 0){  
            num[i] = i;
            int p = i;
            pnum[k++] = i;
            int leadingZeros = __builtin_clz(p);
            int withoutZeros = 32 - leadingZeros;
            p = p << leadingZeros;
            int bits = 1;
            while(bits <= withoutZeros){
                int cur = p & INT_MIN;
                if(cur){
                    count++;
                }
                res[index] = count;
                bits++;
                index++;
                p = p << 1;
            }
                   
        }
        for(j = 0; ; j++){
            if ( (j==k) || ((i*pnum[j])>n) || (pnum[j]>num[i]) ) break;
            num[i*pnum[j]] = pnum[j];

        }

    }

    int t; cin >> t;
    for(int i = 0; i < t; i++){
        int n; scanf("%d" , &n);
        int ans = res[n];
        printf("%d\n" ,ans);
    }



    return 0;

}