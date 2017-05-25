package com.example.user.ubike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LoadingActivity extends AppCompatActivity {


    String A="0",X="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
//        LayoutInflater inflater =  getLayoutInflater();
//        final View view1 = inflater.inflate(R.layout.googlemap, null);//找出第一個視窗
//        final View view2 = inflater.inflate(R.layout.activity_loading, null);//找出第二個視窗
//        try {
//            Thread.sleep(2000);
//            finish();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        for(; !X.equals("urldone");){
//            if (A.equals("urldone")) {
//                finish();
//
//            } else {
//                X = "1";
//            }
//        }

//        setContentView(view1);
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode ==0){
            if(requestCode==101){
                Intent i =new Intent();
                setResult(102,i);
                X="urldone";
                A=X;
            }
        }
    }
}
