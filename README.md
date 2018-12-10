# AndroidSomeTest

#### PreIntent model
PreIntent demo 路径：AndroidSomeTest/app/src/main/java/com/example/odds/java_main/`MainActivityJava.java`
[![](https://jitpack.io/v/oddshou/AndroidSomeTest.svg)](https://jitpack.io/#oddshou/AndroidSomeTest)

用法：给Activity public成员添加 @InitFile 注解 会在PreIntent下生成3个以该Activity名称为后缀的对应方法。包名：odds.annotation.processor

demo：
```java
public class TestPreIntentActivity extends AppCompatActivity {

    @InitFile
    public String mName;
    @InitFile
    public boolean isGood;
    @InitFile
    public int num;
    @InitFile
    public Bundle bundle = new Bundle();
    @InitFile
    public String age = "3333";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        PreIntent.onSave_MainActivityJava(outState, this);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreIntent.onCreate_MainActivityJava(savedInstanceState, this);
        setContentView(R.layout.activity_main_java);

        TextView view = findViewById(R.id.args);
        view.setText(String.format("mName: %s \n isGood: %s \n num: %d \n bundle: %s \n age: %s",
                mName, isGood, num, bundle, age));
    }


    public void toText(View view) {
        //进入下一个页面测试onSaveInstanceState 是否生效
        startActivity(new Intent(this, RxActivity.class));
    }
}
```
给 TestPreIntentActivity 传参：
```java
Bundle bundle = new Bundle();
bundle.putString("bundle", "bundle-bundle");
Intent intent = PreIntent.preIntent_MainActivityJava(this, "哈哈哈", true, 111, bundle, null);
startActivity(intent);
```
