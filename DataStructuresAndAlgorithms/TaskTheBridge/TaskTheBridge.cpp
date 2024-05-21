#include <bits/stdc++.h>
using namespace std;
 
int dp[100000];
int main(){
    int n; cin >> n;
    int arr[n];
    for(int i = 0; i < n; i++){
        scanf("%d" , &arr[i]);;
    }
    sort(arr , arr + n);
    dp[0] = arr[0];
    dp[1] = arr[1];
    dp[2] = dp[0] + dp[1] + arr[2];
    for(int i = 3; i < n; i++){
        dp[i] = min(dp[i-2] + dp[0] + 2*dp[1] + arr[i] , dp[i-1] + dp[0] + arr[i]);
    }
    
    cout << dp[n-1] << endl;
    
    return 0;       
}