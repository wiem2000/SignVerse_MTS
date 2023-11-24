package com.gl4.texttovideo;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

public class ConfigActivity extends AppCompatActivity {

    private Spinner spinnerSelectedSignLanguages;
    private List<String> signLanguages;
    private ArrayAdapter<String> adapter;

    private VideoView videoViewMan,videoViewWoman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        videoViewMan=(VideoView) findViewById(R.id.videoViewMan);
        videoViewWoman=(VideoView) findViewById(R.id.videoViewWoman);
        spinnerSelectedSignLanguages = findViewById(R.id.spinnerSelectedSignLanguages);

        default_video();

        // Remplir la liste des langues des signes disponibles (exemple statique)
        signLanguages = new ArrayList<>();
        signLanguages.add("American Sign Language (ASL)");
        signLanguages.add("British Sign Language (BSL)");
        signLanguages.add("French Sign Language (LSF)");
        signLanguages.add("Japanese Sign Language (JSL)");

/*
        // Créer un adaptateur pour le Spinner
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, signLanguages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Appliquer l'adaptateur au Spinner
        spinnerSelectedSignLanguages.setAdapter(adapter);

        spinnerSelectedSignLanguages.setSelection(0);

*/
        adapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, signLanguages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Appliquer l'adaptateur au Spinner
        spinnerSelectedSignLanguages.setAdapter(adapter);

    }

    public void default_video(){
        Uri manUri= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.man_avatar);
        Uri womanUri= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.woman_avatar);
        videoViewMan.setVideoURI(manUri);
        videoViewWoman.setVideoURI(womanUri);

        videoViewMan.start();
        videoViewWoman.start();


    }
}