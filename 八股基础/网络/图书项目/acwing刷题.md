## Dijkstra求最短路

[AcWing 849. Dijkstra求最短路 I](https://www.acwing.com/activity/content/problem/content/918/)

> 给定一个 n 个点 m 条边的有向图，图中可能存在重边和自环，所有边权均为正值。
>
> 请你求出 1 号点到 n 号点的最短距离，如果无法从 1 号点走到 n 号点，则输出 −1。
>
> 接下来 m 行每行包含三个整数 x,y,z，表示存在一条从点 x 到点 y 的有向边，边长为 z。

Dijkstra 算法的更新逻辑（围绕 “路径长度”）

当从优先队列取出顶点 `u`（当前离起点最近且未确定路径），并标记 `visited[u]=true` 后：

- 遍历 `u` 的所有邻接顶点 `v`（即存在边 `u→v`）；
- 计算 “从起点经 `u` 到 `v` 的新路径长度”：`new_dist = dist[u] + 边u→v的权`；
- 若 `new_dist < dist[v]`（即发现更短的路径），则更新 `dist[v] = new_dist`，并将 `(new_dist, v)` 加入优先队列；
- 核心：更新的是 “顶点 `v` 到起点的累计路径长度”，依赖于 `u` 的 `dist` 值（路径累计结果）。

```java
import java.util.*;

public class Main {
    static class Edge{
        int to,w;
        Edge(int to,int w){
            this.to=to;
            this.w=w;
        }
    }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        //这是一个数组，数组的每个元素是一个List<Edge>
        List<Edge>[] graph = new ArrayList[n+1];
        //为每个顶点（1 到 n）初始化一个空的ArrayList，用于后续存储它的邻接边
        for(int i=1;i<=n;i++) graph[i] = new ArrayList<>();
        //循环读取m条边的信息：起点a、终点b、权重c
        //每条边被封装成Edge对象（包含终点b和权重c），并添加到起点a的邻接表中
        for(int i=0;i<m;i++){
            int a =  sc.nextInt();
            int b = sc.nextInt();
            int c =  sc.nextInt();
            graph[a].add(new Edge(b,c));
        }

        int ans = dijkstra(n,graph);
        System.out.println(ans);
    }

    static int dijkstra(int n,List<Edge>[] graph){
        int[] dist = new int[n+1];
        //初始化距离数组
        Arrays.fill(dist,Integer.MAX_VALUE/2);
        dist[1] = 0;
        //按照数组的第二个元素（a[1]）的大小进行升序排序
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[]{1,0}); //{节点，距离}

        boolean[] visited = new boolean[n+1];

        while(!pq.isEmpty()){
            int[] cur = pq.poll();
            int u = cur[0], d=cur[1];
            if(visited[u]) continue;
            visited[u] = true;
            for(Edge e:graph[u]){
                int v = e.to;
                int w = e.w;
                if(dist[v]>dist[u]+w){
                    dist[v] = dist[u] + w;
                    pq.offer(new int[]{v,dist[v]});
                }
            }
        }
        return dist[n] >= Integer.MAX_VALUE/2?-1:dist[n];
    }
}


```





