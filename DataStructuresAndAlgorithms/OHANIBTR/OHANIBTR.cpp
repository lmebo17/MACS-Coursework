
#include <iostream>
#include <bits/stdc++.h>
using namespace std;

struct node{
    int val;
    node* left;
    node* right;
};

void saveParents(int parent , vector<int> &result , node* curr){
    if(curr == NULL) return;
    saveParents(curr->val , result , curr->left);
    result.push_back(parent);
    saveParents(curr->val , result , curr->right);
}

void rec(node* curr , vector<int>& vc , int &index){
    if(curr == NULL) return;
    rec(curr->left , vc , index);
    curr->val = vc[index]; index++;
    rec(curr->right , vc , index);
    
}

int search(vector<int>& vc , int v){
   if(vc.size() == 0) return 0;
   int left = 0;
   int right = vc.size() - 1;
   while(left <= right){
       int mid = (left + right)/2;
       if(vc[mid] == v){
           return mid;
       } else if(vc[mid] < v){
           left = mid + 1;
       } else right = mid - 1;
   }
   return right + 1;
}

int main(){
    int t; cin >> t;
    for(int f = 0 ; f < t; f++){
        int n; cin >> n;
        vector<int> arr(n);
        for(int j = 0; j < n; j++){
            cin >> arr[j];
        }
        vector<int> vc; 
        for(int j = 0; j < n; j++){ 
            int v = arr[j];
            int index = search(vc , v);
            if(index == vc.size()){
                vc.push_back(v);
            } else if(v < vc[index]){
                vc[index] = v;
            }
        }
        cout << "Case " << f + 1 << ":"<< endl;
        cout << "Minimum Move: " << n - vc.size() << endl;
        sort(arr.begin() , arr.end());
        vector<node*>tree;
        for(int j = 0; j < n; j++){
            node* tmp = new node;
            tmp->val = 0;
            tmp->left = NULL;
            tmp->right = NULL;
            tree.push_back(tmp);
        }
        for(int j = 0; j < n; j++){
            node* curr = tree[j];
            if(2*j+1 < n){
                curr->left = tree[2*j + 1];
            }
            if(2*j + 2 < n){
                curr->right = tree[2*j + 2];
            }
        }
        int tmp = 0;
        rec(tree[0] , arr , tmp);
        
        vector<int> result;
        saveParents(-1 , result , tree[0]);
        for(int i = 0 ; i < (int)result.size() - 1; i++){
            cout << result[i] << " ";
        }
        cout << result[(int)result.size()-1] << endl;;
   
            
        
        
        
    }

    return 0;
}