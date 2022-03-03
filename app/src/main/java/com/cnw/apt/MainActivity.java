package com.cnw.apt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cnw.apt.annotation.BindView;
import com.cnw.apt.annotation.Click;
import com.cnw.apt.annotation.ContentView;
import com.cnw.apt.annotation.common.OnClick;
import com.cnw.apt.annotation.common.OnLongClick;


@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_name)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InjectTool.inject(this);

        textView.setText("inject after..");

//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        textView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });
//
//        textView.setOnDragListener(new View.OnDragListener(){
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//                return false;
//            }
//        });

    }

    @Click(R.id.tv_name)
    void click() {
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.tv_onclick)
    void OnClick() {
        Toast.makeText(this, "OnClick", Toast.LENGTH_SHORT).show();
    }

    @OnLongClick(R.id.tv_onLongClick)
    boolean OnLongClick() {
        Toast.makeText(this, "OnLongClick", Toast.LENGTH_SHORT).show();
        return false;
    }
}