#include <bits/stdc++.h>
using namespace std;
set<char> language;

typedef struct{
    int edges;
    int count;
    vector<int> accept_states;
    vector<vector<pair<char, int>>> transition_states;
}NFA;

int cmp(char ch){
    switch(ch){
        case '*': return 3;
        case '+': return 2;
        default: return 1;
    }
}

bool isChar(char ch){
    return (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || ch == 'E';
}

bool isOperator(char ch){
    return ch == '*' || ch == '+' || ch == '|';
}

string postfix(string regex){
    stack<char> stk;
    string result = "";
    for(int i = 0; i < regex.length(); i++){
        if(isChar(regex[i])){
            result += regex[i];
        }
        if(regex[i] == '('){
            stk.push('(');
        } 
        if(!stk.empty() && isOperator(stk.top()) && isOperator(regex[i])){
            while(!stk.empty() && isOperator(stk.top()) && (cmp(stk.top()) >= cmp(regex[i]))){
                result += stk.top();
                stk.pop();
            }
        }
        if(isOperator(regex[i])){
            if(!stk.empty() && (cmp(stk.top()) < cmp(regex[i]) || stk.top() == '(')){
                stk.push(regex[i]);
            } else if(stk.empty()) stk.push(regex[i]);
        } 
        if(regex[i] == ')'){
            while(stk.top() != '('){
                result += stk.top();
                stk.pop();
            }
            stk.pop();
        } 
    }
    while(!stk.empty()){
        result += stk.top();
        stk.pop(); 
    }
    return result;
}



string fix_regex() {
    string regex; cin >> regex;
    string new_regex = "";
    int n = regex.length();
    for(int i = 0; i < n; i++){
        language.insert(regex[i]);
    }
    string tmp = "";
    bool flag = false;
    for(int i = 0; i < regex.length(); i++){
        flag = false;
        if(i < regex.length() - 1){
            if(regex[i] == '(' && regex[i+1] == ')'){
                tmp += "E";
                i++;
                flag = true;
            }
        }
        if(!flag) tmp += regex[i];
    }
    regex = tmp;
    
    n = regex.length();
    for(int i = 0; i < n; i++){
        new_regex += regex[i];
        if(i == n - 1) break;
        
        if(isChar(regex[i]) && (isChar(regex[i+1]) || regex[i+1] == '(')){
            new_regex += "+";
        } else if(regex[i] == ')' && (regex[i+1] == '(' || isChar(regex[i+1]))){
            new_regex += "+";
        } else if(regex[i] == '*' && (isChar(regex[i+1]) || regex[i+1] == '(')){
            new_regex += "+";
        }
    }
    return new_regex;
    
}   

NFA star(NFA nfa){
    bool flag = true;
    for(int i = 0; i < nfa.accept_states.size(); i++){
        flag &= !(nfa.accept_states[i] == 0);
        if(!nfa.accept_states[i]) continue;
        nfa.edges += nfa.transition_states[0].size();
        for(int j = 0; j < nfa.transition_states[0].size(); j++){
            nfa.transition_states[nfa.accept_states[i]].push_back({nfa.transition_states[0][j].first, nfa.transition_states[0][j].second});
        }
    }
    if(flag) nfa.accept_states.push_back(0);
    return nfa;
}

NFA orOperation(NFA nfa1, NFA nfa2){
    NFA result = nfa1;
    nfa1.count--;
    result.count =  nfa1.count + nfa2.count;
    for(int i = 0; i < nfa2.accept_states.size(); i++){
        int state = nfa2.accept_states[i];
        if(state){
            result.accept_states.push_back(state + nfa1.count);
        } else {
            bool flag = (find(nfa1.accept_states.begin(), nfa1.accept_states.end(), 0) == nfa1.accept_states.end());
            if(flag) result.accept_states.push_back(0);
        }
        
    }
    
    result.edges += nfa2.transition_states[0].size();
    
    for(int i = 0; i < nfa2.transition_states[0].size(); i++){
        if(!nfa2.transition_states[0][i].second){
            result.transition_states[0].push_back({nfa2.transition_states[0][i].first, 0});
        } else {
            result.transition_states[0].push_back({nfa2.transition_states[0][i].first, nfa2.transition_states[0][i].second + nfa1.count});
        }
    }
    
    for(int i = 1; i < nfa2.transition_states.size(); i++){
        vector<pair<char,int>> vc;
        result.edges += nfa2.transition_states[i].size();
        for(int j = 0; j < nfa2.transition_states[i].size(); j++){
            if(nfa2.transition_states[i][j].second == 0) vc.push_back({nfa2.transition_states[i][j].first, 0});
			else vc.push_back({nfa2.transition_states[i][j].first, nfa2.transition_states[i][j].second + nfa1.count});
        }
        result.transition_states.push_back(vc);
    }
    return result;
}

NFA concat(NFA nfa1, NFA nfa2){
    nfa1.count--;
    NFA result = nfa1;
	result.count = nfa1.count + nfa2.count;
	
	for(int i=0; i<nfa1.accept_states.size(); i++){
		result.edges += nfa2.transition_states[0].size();
		for(int j=0; j<nfa2.transition_states[0].size(); j++){
			if(!nfa2.transition_states[0][j].second){ 
			    result.transition_states[nfa1.accept_states[i]].push_back({nfa2.transition_states[0][j].first, nfa1.accept_states[i]});
			} else {
                result.transition_states[nfa1.accept_states[i]].push_back({nfa2.transition_states[0][j].first, nfa2.transition_states[0][j].second+nfa1.count});
			}
		}
	}

	bool flag = false;
	flag = (find(nfa2.accept_states.begin(), nfa2.accept_states.end(), 0) != nfa2.accept_states.end());
    if(!flag) result.accept_states.clear();
	
	for(int i=1; i<nfa2.transition_states.size(); i++){
		vector<pair<char, int> > vc;
		for(int j = 0; j < nfa2.transition_states[i].size(); j++){
			if(nfa2.transition_states[i][j].second == 0){
			    result.edges += nfa1.accept_states.size();
				for(int k = 0; k < nfa1.accept_states.size(); k++){
					vc.push_back({nfa2.transition_states[i][j].first, nfa1.accept_states[k]});
				}
			} else {
				vc.push_back({nfa2.transition_states[i][j].first, nfa2.transition_states[i][j].second+nfa1.count});
				result.edges++;
			}
		}
		result.transition_states.push_back(vc);
	}
	
	for(int i = 0; i < nfa2.accept_states.size(); i++){
		if(nfa2.accept_states[i]){
			result.accept_states.push_back(nfa1.count + nfa2.accept_states[i]);
		}
	}

	return result;
}

NFA constructNfa(string regex){
    stack<NFA> expression;
    for(int i = 0; i < regex.length(); i++){
        if(isChar(regex[i])){
           if(regex[i] == 'E'){
               NFA toAdd;
               toAdd.count = 1;
               toAdd.edges = 0;
               toAdd.transition_states = vector<vector<pair<char,int>>>(1, vector<pair<char,int>>(0));
               toAdd.accept_states.push_back(0);
               expression.push(toAdd);
           } else {
               NFA toAdd;
               toAdd.count = 2;
               toAdd.edges = 1;
               toAdd.transition_states = vector<vector<pair<char,int>>>(2, vector<pair<char,int>>(0));
               toAdd.transition_states[0].push_back({regex[i],1});
               toAdd.accept_states.push_back(1);
               expression.push(toAdd);
           }
        } else if(regex[i] == '*'){
            NFA nfa = expression.top();
            expression.pop();
            expression.push(star(nfa));
        } else if(regex[i] == '|'){
            NFA nfa1 = expression.top();
            expression.pop();
            NFA nfa2 = expression.top();
            expression.pop();
            expression.push(orOperation(nfa1,nfa2));
        } else if(regex[i] == '+'){
            NFA nfa1 = expression.top();
            expression.pop();
            NFA nfa2 = expression.top();
            expression.pop();
            expression.push(concat(nfa2,nfa1));
        }
    }
    return expression.top();
    
}

int main(){
    
    string regex = fix_regex();
    
    
    regex = postfix(regex);
    
    NFA result = constructNfa(regex);
    
    cout << result.count << " " << result.accept_states.size() << " " << result.edges << endl;
    
    for(int i = 0; i < result.accept_states.size(); i++){
        cout << result.accept_states[i] << " ";
    } cout << endl;
    
    for(int i = 0; i < result.count; i++){
        cout << result.transition_states[i].size() << " ";
        for(int j=0; j < result.transition_states[i].size(); j++){
            cout << result.transition_states[i][j].first << " " << result.transition_states[i][j].second << " ";  
        } cout << endl;
    }
    
    return 0;
}

