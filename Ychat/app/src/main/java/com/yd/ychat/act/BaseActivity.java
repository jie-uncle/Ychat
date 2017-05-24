package com.yd.ychat.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by 荀高杰 on 2017/4/21.
 */

public class BaseActivity extends AppCompatActivity {
    public static final String SP_NAME="draft";
      public void showToast(String text){
          Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
      }
    public void intent2main(){
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void intent2login(){
        Intent i=new Intent(this, LoginActivity.class);
        startActivity(i);
    }
    public void intent2register(){
        Intent i=new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}
