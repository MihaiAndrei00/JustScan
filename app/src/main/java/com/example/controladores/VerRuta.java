package com.example.controladores;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.just_scan.R;

public class VerRuta extends AppCompatActivity {
    private String titulo, descripcion , duracion;
    private TextView tituloTv;
    private TextView descripcionTv;
    private TextView duracionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ruta);
        titulo=getIntent().getStringExtra("titulo");
        descripcion=getIntent().getStringExtra("descripcion");
        duracion=getIntent().getStringExtra("duracion");

        tituloTv=findViewById(R.id.tituloExtras);
        descripcionTv=findViewById(R.id.tvDescripcion);
        duracionTv=findViewById(R.id.tvDuracion);

        tituloTv.setText(titulo);
        descripcionTv.setText(descripcion);
        duracionTv.setText(duracion);


    }
}