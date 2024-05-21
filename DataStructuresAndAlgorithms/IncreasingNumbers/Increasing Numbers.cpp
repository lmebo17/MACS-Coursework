
#include <iostream>
#include <bits/stdc++.h>
using namespace std;

int fenw[1000001];

void update(int indx , int value){
	while(indx < 1000001){
		if(fenw[indx] >= value) return;
		fenw[indx] = value;
		indx += (indx & -indx);
	}
}

int calc(int indx){
	int res = 0;
	while(indx >= 0){
		res = max(res , fenw[indx]);
		if(indx == 0) break;
		indx -= (indx & -indx);
	}
	return res;
}


int main(){
    ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);
    int t; cin >> t;
    while(t--){
    	int n; cin >> n;
    	int arr[n];
    	for(int i = 0 ; i < n; i++){
    		cin >> arr[i];
    	}
    	int l; cin >> l;
    	if(l <= 0 || l > n){
    		cout << -1 << endl;
    		continue;
    	}
    	
    	for(int i = 0; i < n; i++){
    		update(arr[i] , 1 + calc(arr[i] - 1));
    	}
    	bool flag = false;
    	for(int i = 0; i < 1000001; i++){
    		if(fenw[i] >= l){
    			cout << i << endl;
    			flag = true;
    			break;
    		}
    	}
    	if(!flag) cout << -1 << endl;
    	for(int i = 0; i < 1000001; i++){
    	    fenw[i] = 0;
    	}
    }

    return 0;
}
