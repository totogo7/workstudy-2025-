## JVM作用
JVM可以帮助java实现跨平台。java代码首先被编译成字节码文件，JVM将字节码文件翻译成机器语言，从而达到运行java程序的目的
因此运行java程序必须要JVM的支持，只要在不同平台上安装JVM，就可以运行字节码文件。
- JVM是java虚拟机，事java程序运行的环境
- JDK是java开发工具包，是java程序所需的工具集合
- JRE是java运行所需要的最小环境

## 八种基本数据类型
byte
short
int ：32位
long：64位
float ：F
double ： L
char
boolean

## 装箱 拆箱
基本数据类型和对应的包装类的转换

## Integer的作用
是int类型的包装类，对象封装的好处是可以用一些特殊的方法来处理数据
而且ArrayList集合类就只能以类作为存储对象，所以只能接受Integer
int无法直接转换成String，必须通过integer然后转换成字符串类型
java集合中只能存储对象

## 面向对象
封装、继承、多态
设计原则：
- 单一责任
- 开放封闭：对扩展开放，对修改封闭
- 里氏替换：子类可替换父类
- 接口隔离
- 依赖倒置：抽象不依赖细节，细节依赖抽象


## 多态的体现
- 方法重载
- 方法重写
- 接口与实现
- 向下转型 向上转型
> 多态可以实现子类替换弗雷，可以调用子类的方法实现。提高代码的扩展性和复用性

## 抽象类和接口
抽象类用于描述类的共同特性和行为，有成员变量、构造方法、具体方法
接口用于定义行为规范，只有常量和抽象方法
一个类可以实现多个接口，但只能继承一个抽象类
接口只有定义 不能有方法的实现；抽象类可以有定义和实现
接口成员变量默认public static final，所有成员方法是public、abstract
接口的成员变量默认default，可以在子类中被重新定义赋值
- 抽象类不能加final修饰符，抽象类需要被继承，final禁止类被继承
- 抽象类不能被实例化，无法new
- 接口不可以有构造方法

## 静态变量 静态方法
- 使用static修饰
- 共享性：所有类的实例共享一个静态变量
- 初始化：静态变量再类被加载的时候初始化，只分配一次内存
- 访问方式：类名或者实例访问

- 静态方法不能直接访问非静态的成员变量和方法
- 静态方法不支持重写

## 浅拷贝 深拷贝
浅拷贝只复制对象本身和其内部的值，不会复制对象引用类型字段。
- 两个对象指向的是同一个地址

深拷贝是复制对象的同时，将所有引用类型字段也复制一份
- 两个对象指向不同的地址
- 实现cloneable接口 重写clone()进行深拷贝
- 将对象序列化为字节流，从字节流反序列化为对象实现深拷贝

## 泛型
允许类、接口和方法在定义时使用一个或多个类型参数，这些类型参数在使用的时候被指定为具体的类型。- 
- 适合多种数据类型执行相同的代码
```java
private static int add(int a, int b) {
 System.out.println(a + "+" + b + "=" + (a + b));
 return a + b;

private static <T extends Number> double add(T a, T b) {
  System.out.println(a + "+" + b + "=" +(a.doubleValue() + b.doubleValue()));
  return a.doubleValue() + b.doubleValue();
}
```

## 创建对象方法
- new
- 反射创建：class的newInstance()方法
- 使用clone()方法：基于一个现有对象创建一个副本
- 使用反序列化
- 使用工厂模式

## 获取私有对象
- 通过getter方法
- 反射机制

## 反射
在运行状态中，对于任意一个类，都能知道这个类中的所有属性和方法。
对于任意一个对象，都能调用它的方法和属性
- 运行时类信息访问：运行时获取类的名字，包，接口，构造方法
- 动态对象创建
- 动态方法调用
- 访问和修改字段值：私有的也可以

## 注解
注解本质是一个继承了Annotation的特殊接口，其具体实现类是Java运行时生成的动态代理类。
通过反射获取注解，返回java运行时生成的动态代理对象。

## String StringBuffer StringBuilder
- String可变，剩下不可变
- String线程安全
- StringBuilder 不是线程安全的 适用于单线程
- StringBuffer 是线程安全的，其方法通过synchronized 关键字实现同步，适用于多线程环境。

## 序列化
将对象序列化为字节流，将其发送到另一个JVM，然后使用反序列化字节流恢复对象
- 这可以通过 Java 的 ObjectOutputStream 和ObjectInputStream 来实现。


## java集合
- ArrayList：动态数组
- LinkedList：双向链表
- HashMap：键值对
- HashSet：基于hashmap的set集合
- TreeMap：基于红黑树的有序map集合

线程安全：
- vector：动态数组
- Hashtable：线程安全的哈希表，给每个方法加上ObjectInputStream 来实现。

## list操作
.remove(index)：删除指定下标的元素
.add()：添加元素

## Map
遍历：使用entrySet
```java
 for (Map.Entry<String, Integer> entry :map.entrySet()) {
    System.out.println("Key: " + entry.getKey()+ ", Value: " + entry.getValue());
}
```
解决哈希冲突：
- 链接法
- 开放寻址 线性探测

HashMap不是线程安全的：采用数组+链表，多线程会出现数据丢失，链死循环
put操作过程：
- 根据要添加的键的哈希码计算位置
- 检查该位置是否为空，为空就创建一个entry对象来存储键值对
- 不为空就检查该位置后第一个键值对是否与其相同
- 如果相同就表明有相同的键，更新最新的值就行
- 如果不相同，就遍历整个链表查看是否有相同的键
- 如果找到了就替代
- 没找到就添加一个新的键值对到链表头部
- 检查链表长度是否到阈值
- 检查负载因子是否超阈值（超出阈值就会进行扩容）
- 扩容
- 完成添加操作

- HashTable，底层原理是数组+链表。线程安全，效率低一点，其内部方法基本都经过synchronized修饰
## ConcurrentHashMap
ConcurrentHashMap 是 Java 提供的一个线程安全的哈希表，
用来在多线程环境下高效地进行键值对存取。
它是 HashMap 的并发安全版本，相比 Collections.synchronizedMap() 整体加锁的做法，
ConcurrentHashMap 通过分段锁（JDK7）或CAS+局部同步（JDK8）实现了更高的并发性能。
底层原理——JDK7
- 整个 Map 被分成若干个 Segment（分段）；
- 每个 Segment 类似一个小的 HashMap，有独立的锁；
- 线程访问不同 Segment 时可以并行进行；
- 锁粒度比全表锁更细，大大提高并发性能。
JDK8：
- 去掉了 Segment，结构更接近 HashMap；
- 使用 CAS（Compare-And-Swap）无锁操作 来保证并发安全；
- 当 CAS 失败时，再用 synchronized 锁定单个桶（bin）；
- 链表长度超过阈值（8）时，自动转换为 红黑树 提高查询性能；
- 扩容时通过 协作式扩容（多线程一起帮忙迁移数据），提升效率。

CAS + synchronized 如何保证安全？
以 put 为例：
- 首先通过 key 计算 hash 定位桶；
- 如果桶为空，用 CAS（乐观锁） 插入；
- 如果 CAS 失败，说明有并发冲突：
- 退化为 synchronized（悲观锁），对该桶加锁；
- 在锁内遍历链表或红黑树更新；
- 释放锁，结束操作。


