# AndroidSomeTest

#### PreIntent model
##### 这个model 主要为了解决Intent多参数传递的问题

##### 引入库
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
        implementation 'com.github.oddshou:AndroidSomeTest:1.3'
}
```

PreIntent demo 路径：AndroidSomeTest/app/src/main/java/com/example/odds/java_main/`TestPreIntentActivity.java`
[![](https://jitpack.io/v/oddshou/AndroidSomeTest.svg)](https://jitpack.io/#oddshou/AndroidSomeTest)

用法：给Activity public成员添加 @InitFile 注解 会在PreIntent下生成3个以该Activity名称为后缀的对应方法。

demo：
```java
@InitFile
public String mName;
@InitFile
public boolean isGood;
@InitFile
public int num;
@InitFile
public Bundle bundle = new Bundle();
@InitFile(Serializable = true)
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
//有多少个参数都需要按顺序传递
Intent intent = PreIntent.preIntent_MainActivityJava(this, "哈哈哈", true, 111, bundle, null);
startActivity(intent);
```
