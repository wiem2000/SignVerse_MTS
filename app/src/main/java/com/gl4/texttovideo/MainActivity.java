package com.gl4.texttovideo;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private VideoView videoView;

    private EditText editText ;

    private ImageView imageView ;

    private LinearLayout linearLayout;

    private TextView textView ;

    private Map<String, Integer> videoMap;

    private List<String> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //editText= (EditText) findViewById(R.id.editText);
        videoView=(VideoView) findViewById(R.id.videoView);
        textView=(TextView) findViewById(R.id.textView);
        imageView=(ImageView) findViewById(R.id.imageView) ;
        linearLayout=(LinearLayout) findViewById(R.id.linearLayout) ;

        // Initialize the video map
        videoMap = new HashMap<>();
        videoMap.put("fine", R.raw.avatar_fine);
        videoMap.put("thanks", R.raw.avatar_thanks);
        videoMap.put("hello", R.raw.woman_hello);
        videoMap.put("welcome", R.raw.avatar_welcome);

        default_video();

        //onEditText();



    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.suspend();
    }

    public void default_video(){
        Uri defaultVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.woman_avatar);
        videoView.setVideoURI(defaultVideoUri);
        videoView.start();
    }

/*
    public void onEditText(){
        Uri defaultVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.avatar);
        videoView.setVideoURI(defaultVideoUri);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String userInput = editable.toString().toLowerCase();


                if (videoMap.containsKey(userInput)) {
                    int videoResource = videoMap.get(userInput);
                    Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResource);
                    videoView.setVideoURI(videoUri);
                } else {

                    videoView.setVideoURI(defaultVideoUri);
                }

                videoView.start();
            }

        });

    }*/

    public void onSpeak(View v) {

        Button speakButton = (Button) findViewById(R.id.button);

        speakButton.setBackgroundColor(Color.GRAY);
        linearLayout.setBackgroundColor(Color.GRAY);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://192.168.1.53:5000/start_speak").build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Error: " + e.getMessage());


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        speakButton.setBackgroundColor(Color.RED);
                        linearLayout.setBackgroundColor(Color.RED);
                       // ou toute autre couleur de votre choix
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println(response.body().string());


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        speakButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
                        linearLayout.setBackgroundColor(getResources().getColor(R.color.purple_500));
                    }
                });

                getResponse();
            }
        });
    }

    public void getResponse() {
        OkHttpClient okHttpClient2 = new OkHttpClient();
        Request request2 = new Request.Builder().url("http://192.168.1.53:5000/get_result").build();

        okHttpClient2.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Error 2: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                final String resultText = response.body().string();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(resultText);
                        // Traitez la phrase
                        words = Arrays.asList(resultText.split("\\s+"));
                        // Lancer la première vidéo
                        launchNextVideo();
                    }
                });
            }
        });
    }


    private void launchNextVideo() {
        if (!words.isEmpty()) {
            String currentWord = words.get(0);
            words = words.subList(1, words.size()); // Supprimer le premier élément de la liste

            launchVideoForWord(currentWord);
        }
    }

    private void launchVideoForWord(String word) {
        if (videoMap.containsKey(word)) {
            int videoResource = videoMap.get(word);
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResource);

            videoView.setVideoURI(videoUri);
            updateTextView(word);
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // La vidéo en cours est terminée, passer à la vidéo suivante
                    launchNextVideo();

                }
            });

            videoView.start();
        } else {
            System.out.println("Aucune correspondance vidéo pour le mot : " + word);
            // Passer à la vidéo suivante même si la vidéo actuelle n'est pas trouvée
            launchNextVideo();
        }
    }

    private void updateTextView(String word) {

        String originalText = textView.getText().toString();


        int startIndex = originalText.indexOf(word);

        if (startIndex != -1) {

            SpannableString spannableString = new SpannableString(originalText);


            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.RED);
            spannableString.setSpan(colorSpan, startIndex, startIndex + word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            textView.setText(spannableString);
        }
    }

    public void onImageViewClick(View view) {
        // Création de l'Intent pour démarrer l'activité de configuration
        Intent intent = new Intent(this, ConfigActivity.class);
        startActivity(intent);
    }
}