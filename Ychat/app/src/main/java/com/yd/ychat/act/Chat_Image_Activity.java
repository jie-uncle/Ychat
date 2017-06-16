package com.yd.ychat.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yd.ychat.R;
import com.yd.ychat.houdler.Imageviewhoudler;

public class Chat_Image_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__image_);
        ImageView chat_image = (ImageView) findViewById(R.id.chat_image);
        String url = getIntent().getStringExtra(Imageviewhoudler.URL);
        Glide.with(this).load(url).into(chat_image);
        chat_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
    }
}
