package com.gl4.texttovideo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

public class StartActivity extends AppCompatActivity {

    private VideoView videoView;

    private Button btnStartChat ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        videoView=(VideoView) findViewById(R.id.videoView);
        btnStartChat=(Button) findViewById(R.id.btnStartChat);

        default_video();

        btnStartChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent pour démarrer MainActivity
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    public void default_video(){
        Uri defaultVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.avatar_welcome);
        videoView.setVideoURI(defaultVideoUri);
        videoView.start();


    }
}