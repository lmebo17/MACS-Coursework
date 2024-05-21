#include <bits/stdc++.h>
using namespace std;
vector<int> acceptance_set;
int n, a, t;

bool haveIntersection(vector<int>& accept_set, vector<int>& current_set){
    for(int i = 0; i < accept_set.size(); i++){
        if(find(current_set.begin(), current_set.end(), accept_set[i]) != current_set.end()) return true;
    }
    return false;
}

string processInput(string input ,  vector<map<char, vector<int>>>& adj){
   
   vector<bool> using_states(n);
   for(int i = 0; i < n; i++){
       using_states[i] = false;
   }
   vector<int> current_states;
   current_states.push_back(0);
   string result = "";
   bool gotInTrap = false;
   for(int i = 0; i < input.length(); i++){
       if(gotInTrap){
           result += "N";
           continue;
       }
       
       bool flag = false;
       char ch = input[i];
       vector<int> transition_states;
       for(auto state : current_states){
           for(auto v : adj[state][ch]){
                    transition_states.push_back(v);
            }
        }
       gotInTrap = !transition_states.size();
       if(gotInTrap){
           result += "N";
           continue;
       }
       if(haveIntersection(acceptance_set, transition_states)){
           result += "Y";
       } else result += "N";
       current_states = transition_states;
       
   }
   return result;
}

string solve() {
    acceptance_set.clear();
    string input; cin >> input;
    n,a,t; cin >> n >> a >> t;
    vector<map<char, vector<int>>> adj(n);
    for(int i = 0; i < a; i++){
        int p; cin >> p;
        acceptance_set.push_back(p);
    }
    for(int i = 0; i < n; i++){
        int k; cin >> k;
        for(int j = 0; j < k; j++){
            char ch; int p;
            cin >> ch >> p;
            adj[i][ch].push_back(p);             
        }
    }
    return processInput(input, adj);
    
}   

int main(){
    cout << solve() << endl;
    return 0;
}
