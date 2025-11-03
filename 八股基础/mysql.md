## 执行一条select
mysql的架构分为两层：server层和存储引擎层
- service层负责建立连接、分析和执行sql
- 存储引擎负责数据存储和提取：innoDB

第一步：连接
```bash
mysql -h$ip -u$user -p
```
指定mysql服务ip地址，用户名，密码
- `show processlist`可以查看mysql服务被多少个客户端连接

第二步：查询缓存
mysql收到sql语句后解析，如果是查询语句就回去查询缓存，看看之前有没有执行过这一命令
- 查询缓存时以key-value的形式保存在内存中的，key为sql查询语句，value是sql语句查询的结果

mysql8.0之后就取消了查询缓存

第三步：解析sql
解析器进行词法分析和语法分析，构建语法树

第四步：执行SQL
三个阶段：
- prepare：预处理阶段 —— 检查sql查询语句中的表和字段是否存在
- optimize：优化阶段 —— 将sql查询语句的执行方案确定下来（选择索引）
- execute：执行阶段 —— 正式执行语句 执行器会跟存储引擎交互

## SQL基础
- NOSQL是非关系型数据库，只要MongoDB Redis。存储方式可以是JSON文档，哈希表或者其他。数据之间无关系，非常容易扩展
- sql是关系型数据库，存储结构化数据。

- 1NF：要求数据库表的每一列都是不可分割的原子数据项
- 2NF：消除非主属性对主码的部分函数依赖
- 3NF：消除传递依赖

- 内连接：返回两个表中有匹配关系的行。（INNER JOIN 表名 ON A.id=B.id）
- 左外连接：返回左表所有行，未匹配的右表为null（LEFT JOIN  ON）
- 右外连接：返回右表所有行 （RIGHT JOIN）
- 全外连接：返回两个表中所有行（UNION）

- UNIQUE可以避免插入重复数据
- IP地址可以用`VARCHAR(15)`存储
- 外键约束是确保数据完整性和一致性
- IN关键字用于检查左边表达式是否存在右边列表中,存在返回true
```sql
SELECT column_name(s)
FROM table_name
WHERE column_name IN (value1, value2, ...);
```
- EXISTS用于判断子查询是否能至少返回一行数据，不关心返回什么数据，只在乎是否有结果
```sql
SELECT column_name(s)
FROM table_name
WHERE EXISTS (SELECT column_name FROM another_table WHERE condition);
```

基本函数:
- CONCAR(str1,str2…)：连接多个字符串和成一个字符串
- LENGTH(str)
- ABS(num)  POWER(num,exponent)
- NOW() CURDATE()
- COUNT(colunm)  SUM(colunm)  AVG(column) MAX  MIN

## 可重入锁
建表：
```sql
CREATE TABLE `app_lock` (
  `lock_key` VARCHAR(200) NOT NULL,
  `owner` VARCHAR(200) NOT NULL,
  `count` INT NOT NULL DEFAULT 1,
  `last_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`lock_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
- lock_key：锁的标识（例如 book_copy:123 或 book:456，你可以根据需要选择粒度）。
- owner：持有者 ID（调用方生成的唯一标识，如 userId:threadId:uuid）。
- count：重入计数。
- last_update：最后更新时间，方便外部脚本或管理员清理超时锁。

调用：
```java
String ownerId = userId + ":" + UUID.randomUUID().toString();
String lockKey = "book_copy:" + copyId; // or lock by bookId if you want coarse-grained lock
boolean ok = dbReentrantLockService.tryLock(lockKey, ownerId, 5000);
if (!ok) throw new BizException("获取锁失败，请稍后重试");
try {
    // 在同一个事务 / Service 方法里（重要），执行选副本、更新状态、写借阅记录等
} finally {
    dbReentrantLockService.release(lockKey, ownerId);
}
```
1. 开始尝试：生成owenrid，决定lockkey，记录开始时间
2. 开启数据库事务：@Transactional
3. 在事务内执行SELECT FOR UPDATE
```sql
SELECT lock_key, owner, count
FROM app_lock
WHERE lock_key = #{lockKey}
FOR UPDATE;
```
- 若行不存在 就直接加锁 执行`INSERT INTO app_lock (lock_key, owner, count) VALUES (#{lockKey}, #{ownerId}, 1);
- 若查询结果存在 且owner==owerid（同一持有者—）表示可重入 层架重入次数
- 若查询结果存在，被别人持有，就不能取得锁

  4. 提交事务

> 我们的可重入锁基于 app_lock 表实现：加锁时在事务内 SELECT ... FOR UPDATE 该 lock_key，
> 如果行不存在就 INSERT（获得锁），如果存在且 owner 相同就 UPDATE count++（重入），否则表示被别人持有，按重试策略等待或返回失败；
> 解锁时在事务内再 SELECT ... FOR UPDATE，若 owner 相同则 count-- 或 DELETE（count==1）。


## 存储引擎
InnoDB：事务支持（ACID），并发性能（行级锁定），崩溃恢复（redolog）
是mysql默认引擎
用B+树：所有叶子节点在同一层，非叶子节点存储键值（索引），不包含数据记录。叶子节点存储数据记录，自平衡。叶子节点有链表连接


## 索引
创建表的时候会根据存储引擎会根据不同场景选择不同的列作为索引
- 有主键 默认使用主键作为聚簇索引的索引键
- 没有主键 就选择第一个不包含null值的唯一列作为聚簇索引的key

- 聚簇索引（主键索引）：B+ tree的叶子节点存放实际数据
- 二级索引：B+ tree叶子节点存放的是主键值，不是实际数据
> 如果查询时用到了二级索引，查询的数据能在二级索引里查到，这个过程时覆盖索引。
> 如果数据不在二级索引，就会先检索二级索引，找到叶子节点，获取主键值，再检索主键值，要回表

- 主键索引：主键字段
- 唯一索引：UNIQUE字段
- 普通索引：普通字段
- 前缀索引：对字符串类型字段的前几个字符建立的索引
- 联合索引：最左匹配
- 覆盖索引：一个索引包含了查询所需的所有列，不需要回表查询

- 聚簇索引：索引的叶子节点包含了实际的数据行，可以直接从索引中获得数据行，通常基于主键构建
- 非聚簇索引：叶子节点不包含完整数据，而是包含指向数据行的指针或主键值

如果一个列即使单列索引，又是联合索引，单独查它的话先走哪个？
- mysql 优化器会分析每个索引的查询成本，然后选择成本最低的方案来执行 sql。

## 事务
- 原子性：undo log（回滚日志）
- 一致性
- 隔离性：MVCC
- 持久性：redo log（重做日志）

## 并发
- 脏读
- 不可重复读
- 幻读

## 隔离级别
- 读未提交：还没提交就能被看到
- 读提交：提交后才能被看到
- 可重复读：一个事务执行过程中看到的数据 一直到这个事务启动时看到的数据是一致的
> 也就是说A事务提交的的数据，B事务是看不见的，只有B事务结束后才能看见。
> 但是如果在查询的时候 其他用户增加了一行 会看到多出的一行，出现幻读。
- 串行化：会对记录加上读写锁（行级锁）

可重复读如何防止不发生幻读？
- 开启事务之后，马上执行select for update 加锁

## MVCC
MVCC允许多个事务同时读取同一行数据，而不会彼此阻塞，每个事务看到的数据版本是该事务开始时的数据版本。
也就是其他事务即使修改了，正在运行的事务仍然看到的是开始的数据。
- 是用Read view实现的——类似数据快照
> MVCC 是多版本并发控制，用于在高并发场景下提高性能。它通过在每行数据后**保存事务ID和undo日志指针**，生成数据的多个版本，
让读操作无需加锁也能读到一致的数据快照。在 InnoDB 中，MVCC 主要在 READ COMMITTED 和 REPEATABLE READ 隔离级别下工作。
它的本质是：通过版本链和 Read View 实现事务可见性控制，做到“读写不阻塞”。

## 日志
- redo log ：物理日志，记录某个数据页做了什么修改，是innodb存储引擎生成的日志，用于掉电等故障恢复
> 事务提交之后会把redo log写入磁盘，实现持久化
- undo log：用于撤销回退的日志，用于事务回滚和MVCC
- bin log：记录所有数据库表结构变更和表修改的日志

