### 介绍
手写运行时注入框架,实现布局、控件以及自定义事件注入

### 使用到的技术
注解+反射

注解： 声明用到的注解信息


反射： 结合注解信息利用反射技术实现Activity布局、控件以及自定义事件的注入


### 使用说明
```java

// 注入布局
@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

	  // 注解控件
    @BindView(R.id.tv_name)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 核心方法： 解析注解信息，进行动态注入
        InjectTool.inject(this);

        textView.setText("inject after..");
    }

    // 注解事件
    @Click(R.id.tv_name)
    void click() {
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
    }
	// 注解事件
    @OnClick(R.id.tv_onclick)
    void OnClick() {
        Toast.makeText(this, "OnClick", Toast.LENGTH_SHORT).show();
    }
	// 注解事件
    @OnLongClick(R.id.tv_onLongClick)
    boolean OnLongClick() {
        Toast.makeText(this, "OnLongClick", Toast.LENGTH_SHORT).show();
        return false;
    }
}
```
