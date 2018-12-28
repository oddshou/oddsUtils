# AndroidSomeTest

#### PreIntent model
> 这个model 主要为了解决Intent多参数传递的问题
> 如果通过Intent传参给Activity 参数较多，同时一般我们还要OnsaveInstance做数据恢复，这个操作机械毫无意义。
> 通过注解编译时生成对应方法，直接调用即可解决这个问题

使用时注意以下几点：
- 成员 public
- 成员排列顺序代表参数顺序,order 参数强制排序
- 针对 List<T> 类型可能需要用到 @InitFile Serializable 和 Parcelable 参数，参见下方示例代码
- 默认值如果需要记得填写

##### 引入库
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
    implementation 'com.github.oddshou.AndroidSomeTest:annotations:1.3.1'
    kapt 'com.github.oddshou.AndroidSomeTest:compiler:1.3.1'
    //annotationProcessor 'com.github.oddshou.AndroidSomeTest:compiler:1.3.1'
}
```
##### Demo

PreIntent demo 路径：AndroidSomeTest/app/src/main/java/com/example/odds/java_main/`TestPreIntentActivity.java`
[![](https://jitpack.io/v/oddshou/AndroidSomeTest.svg)](https://jitpack.io/#oddshou/AndroidSomeTest)

用法：给Activity public成员添加 @InitFile 注解 会在编译时生成 `PreIntent.java`并在其内生成3个以该Activity名称为后缀的对应方法。

```java
//尽量用order 来规定参数顺序，避免因调整成员定义顺序导致调用异常
//order 非必填
@InitFile(order = 0)
public String mName;
@InitFile(order = 1)
public boolean isGood;
@InitFile(order = 2)
public int num;
@InitFile(order = 3)
public Bundle bundle = new Bundle();
@InitFile(Serializable = true, order = 4)
public List<String> ages;
@InitFile
public ParcelableClass parcelableClass;
@InitFile
public SerializeClass serializeClass;

@InitFile
public Parcelable[] parcelableClasss;//此处类型不能使用ParcelableClass，只能使用基类引用，避免强转失败。
@InitFile
public SerializeClass[] serializeClasss;

@InitFile(Parcelable = true)
public ArrayList<ParcelableClass> ParcelableList;//此处需要类型匹配 ArrayList<? extends Parcelable> ArrayList 不能改用List
@InitFile(Serializable = true)
public List<SerializeClass> serializeList;
```
Activity 相应方法处理
```java
@Override
protected void onSaveInstanceState(Bundle outState) {
    PreIntent.onSave_TestPreIntentActivity(outState, this);
    super.onSaveInstanceState(outState);
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    PreIntent.onCreate_TestPreIntentActivity(savedInstanceState, this);
    setContentView(R.layout.activity_main_java);
}
```
给 TestPreIntentActivity 传参：
```java
Bundle bundle = new Bundle();
bundle.putString("bundle", "bundle-bundle");
//有多少个参数都需要按顺序传递，按order的顺序定义参数顺序
Intent intent = PreIntent.preIntent_MainActivityJava(this, "哈哈哈", true, 111, bundle, null);
startActivity(intent);
```
##### Kotlin
主要区别在于成员注解,需要使用额外使用JvmField 取消属性 get，set方法，其他和 java 一直
```java
@InitFile
@JvmField
var detailId: Int = -1
@InitFile
lateinit var type: CommentType
```
