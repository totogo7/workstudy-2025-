## 78 子集

[78. 子集](https://leetcode.cn/problems/subsets/)

给你一个整数数组 `nums` ，数组中的元素 **互不相同** 。返回该数组所有可能的子集（幂集）。解集 **不能** 包含重复的子集。你可以按 **任意顺序** 返回解集。

用dfs直接输出每个值
```java
class Solution {
    List<Integer> path = new ArrayList<>();
    List<List<Integer>> res  = new ArrayList<>();
    public List<List<Integer>> subsets(int[] nums) {
        if(nums==null || nums.length==0) return res;
        dfs(nums,0);
        return res;
    }

    void dfs(int[] nums,int x){
        res.add(new ArrayList<>(path));

        for(int i=x;i<nums.length;i++){
            path.add(nums[i]);
            dfs(nums,i+1);
            path.remove(path.size()-1);
        }
    }
}
```
