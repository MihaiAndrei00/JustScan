package com.example.controladores;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.controladores.logs.LogRegistro;
import com.example.controladores.logs.LogUsuario;
import com.example.just_scan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    //video y mediaplayer
    private VideoView video;
    private MediaPlayer mediaPlayer;
    private int mCurrentVideoPosition;
    private ConstraintLayout constraintLayout;
    private AnimationDrawable animationDrawable;
    private Uri uri;
    //vistas
    private Button btnRegistro;
    private Button btnInicioSesion;
    //intent
    private Intent intent;
    //bd
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //variables para la animacion del main
        video = findViewById(R.id.video);
        uri = Uri.parse("android.resource://"
                + getPackageName()
                + "/"
                + R.raw.video2);
        video.setVideoURI(uri);
        video.start();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer = mediaPlayer;
                mediaPlayer.setLooping(true);
                if(mCurrentVideoPosition != 0){
                    mediaPlayer.seekTo(mCurrentVideoPosition);
                    mediaPlayer.start();
                }
            }
        });

        //asocio los botones con sus vistas
        btnRegistro=findViewById(R.id.btnIniciarSesion);
        btnInicioSesion=findViewById(R.id.btnInicioSesion);
        // bd
        database = FirebaseDatabase.getInstance();

        //intents de los botones
        intentARegistro();
        intentAInicioSesion();
    }

    private void intentAInicioSesion() {
        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(MainActivity.this, LogUsuario.class);
                startActivity(intent);
            }
        });
    }

    private void intentARegistro() {
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(MainActivity.this, LogRegistro.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

//        mCurrentVideoPosition = mediaPlayer.getCurrentPosition();
        video.pause();
    }
    @Override
    protected void onResume() {
        super.onResume();

        video.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mediaPlayer.release();
        mediaPlayer = null;
    }
}