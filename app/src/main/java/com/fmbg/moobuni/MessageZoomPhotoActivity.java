package com.fmbg.moobuni;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

public class MessageZoomPhotoActivity extends AppCompatActivity {

    ImageView imageScreen;
    String imageurl;
    Toolbar actionbarPhotoScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_zoom_photo);

        init();
        PhotoFullScreen();
    }


    public void PhotoFullScreen() {
        imageScreen = findViewById(R.id.image_screen);
        Bundle extras = getIntent().getExtras();
        imageurl = extras.getString("url");
        Glide.with(MobUni.getContext()).load(imageurl).into(imageScreen);
    }

    public void init(){
        actionbarPhotoScreen = findViewById(R.id.actionbarphotoscreen);
        setSupportActionBar(actionbarPhotoScreen);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionbarPhotoScreen.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
