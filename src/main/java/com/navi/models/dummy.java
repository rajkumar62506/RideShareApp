#include<bits/stdc++.h>
using namespace std;
int getMaxPower(vector<int>& arr, vector<int> &power) {
    int n = arr.size();
    int mod = 1e9 + 7;
    sort(power.begin(), power.end());
    int i = 0, j = power.size() - 1;
    vector<long long> pref(n + 1, 0);
    for(int i = 1; i <= n; i++){
        pref[i] = pref[i - 1] + arr[i - 1];
    }
    long long ans = 0;
    while(i < j) {
        ans += (pref[power[j] + 1] - pref[power[i]]);
        ans %= mod;
        i++;
        j--;
    }
    return ans;
}
struct cmp {
bool operator() (const int &a, const int &b) {
    return a < b;
}
};
int main()
{
    priority_queue<int, vector<int>, cmp> pq;
    pq.push(1);
    pq.push(2);
    cout<<pq.top()<<endl;
    
    return 0;
}
