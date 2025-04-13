C++标准模板库：STL，包含四个组件——**算法、容器**、仿函数、迭代器。

快速排序：`sort(arr.begin(),arr.end())`
## 向量 vector
`#include <vector>`
- 连续的顺序的存储结构（跟数组一样的类型），，但长度可变
### 构造：
`vector<类型> arr(长度，[初值])`
```c++
vector<int> arr; //构造int数组
vector<int> arr(100)

vector<vector<int>> arr(5,vector<int> (6,10))
// 二维数组，五行六列，初值为10

```

### 尾接与尾删
```c++
arr.push_back(1) //在后面加上一个数

arr.pop_back()  //弹出尾部一个数

cout << arr.back() <<endl; //输出最后一个数 
```
获取长度：`arr.size()`
快速清理：`arr.clear()`
判断是否清空：`arr.empty()`
改变长度：`arr.resize(5,3)` 改变数组的长度为5，并将多出来的元素赋值为3
### 提前指定长度
```c++
vector<int> a(1e8)
for(int i=9;i<a.size();i++) a[i]=i;
```

## 栈 stack
`#include <stack>`
构造
```c++
stack<double> stk;
stk.push(1.0)  //插入数
stk.push(1.4)

stk.top() //查看栈顶 1.4

stk.pop() //弹出栈顶的数
```
查看数组大小：`stk.size()`
判断是否清空：`stk.empty()`
无法直接清空全部的数
>栈无法访问内部元素，只能访问栈顶元素

## 队列 queue （先进先出）
`#include <<queue`
构造：
```c++
queue<int> que;
que.push(1);
que.push(2);
cout<< que.front() <<endl;  //1
cout<< que.back() <<endl; //2

que.pop(); //出队,先进先出
cout<< que.front() <<endl;  //2
cout<< que.back() <<endl; //2
```
队列大小：`que.size()`
判度是否为空：`que.empty`
>不能访问内部元素 

## 优先队列 priority_queue
`priority_queue<类型，容器，比较器> pque`
```c++
priority_queue<int> pque;
pque.push(1);
pque.push(2);
cout << pque.top() <<endl; //输出队列的最大值

pque.pop(); //弹出最大的元素
```
如果要变成小顶堆
```c++
priority_queue<int,vector<int>,greater<int>> pque;

cout << pque.top() <<endl; //输出队列的最小值

pque.pop(); //弹出最小的元素

```

## 集合 set
`#include set`
```c++
set<int> st;
st.insert(1);  //插入元素
st.insert(2);
st.insert(2);
st.insert(0);
//打印元素
for(auto &ele: st)
	cout << ele <<endl;

st.erase(2) //清除元素2

//查找元素
if(st.fine(1)!=st.end())
{
	cout<<"yes"<<endl;
}
st.count(1) //判度1元素出现的个数，在集合中只有0和1两种形式 
```
查看大小：`st.size()`
清空：`st.clear()`
判空：`st.empty()`
用迭代器进行遍历：
```c++
for (set<int>::iterator it = st.begin();it!=st.end;++it)
	cout << *it << endl;
```

- 集合可以做去重，且有顺序
- 判度元素是否出现过，当元素大小很大，但元素数量很小的时候使用。
>仅能用迭代器遍历，不能用下标访问

## 映射 map
`#include <map>`
```c++
map<string,int> a; //string到int的映射
a["awa"]=3;
a["sdf"]=2;
```
- 键值只能映射一个值
- 键是从小到大的

构造：`map<键类型,值类型,比较器> mp`
```c++
map<int,int> mp;
mp[2]=1;
//查询
if(mp.find(2)!=mp.end())
{
	cout << "yes" <<endl;
}

//删除元素
mp.erase(2);

//判度元素出现了几次
mp.cout(2); //map中只有0，1两种选择
```
查看大小：`mp.size()`
清空：`mp.clear()`
判空：`mp.empty()`
遍历：
```c++
for(map<int,int>::iterator it=mp.begin();it!=mp.end();++it)
{
	cout << it->fitst << ' ' <<< it->second <<endl;
}
```
第二种：
```c++
for(auto &pr :mp)
{
	cout << pr->fitst << ' ' <<< pr->second <<endl;
}
```

统计字符串出现的个数：
![[Pasted image 20240125152736.png]]

## 字符串 string
`#include <string>`
```c++
string s; //声明字符串
cin >> s; //输入字符串
cout << s；//输出字符串

s="awa" //赋值
s[1]='b' //修改元素

s1==s2  //字符串比较
s1+s2  //连接两个字符串

s1="123123123"
cout << s1.substr(3，4) << endl; 
/返回下标从3开始的子字符串，长度为4

// 查询
if(s1.find("321") != string::npos)
{
	cout <<"yes"<< endl;
}
```
- `s1.find("321") `的返回值是起始点下标，为2

- 尾接字符串使用 +=   s+="a"

## 二元组 pair
`#include <<utility>`
```c++
pair<int,int> p ={1,2};
cout << p.first << ' ' << p.second; //访问元素

pair<char,int> p2={'a',2};  //二元组
```


## 迭代器
### 遍历vector
```c++
for(int i=0;i<a.size();i++)
	cout<< a[i] << endl;
```
或：
```c++
for(vector<int>::iterator it=a.begin();it!=a.end;++it)
	cout << *it <<endl;
```
- `a.begin()`是头迭代器
- `a.end()`是尾迭代器

## 常用算法
- `swao(a,b)`  交换两个数
- `sort(arr.begin(),arr.end())`

