package com.yd.ychat.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yd.ychat.MainActivity;

/**
 * Created by 荀高杰 on 2017/4/21.
 */

public class BaseActivity extends AppCompatActivity {
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
}
