package com.example.controladores.ver;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.just_scan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class VerRuta extends AppCompatActivity {
    private Intent intent;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child("Rutas");
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    private String titulo, descripcion , duracion, fotoIntent,idRuta;
    private TextView tituloTv;
    private ImageView verFotoRuta;

    private TextView descripcionTv;
    private TextView duracionTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ruta);
        titulo=getIntent().getStringExtra("titulo");
        descripcion=getIntent().getStringExtra("descripcion");
        duracion=getIntent().getStringExtra("duracion");
        fotoIntent=getIntent().getStringExtra("foto");
        idRuta=getIntent().getStringExtra("uid");
        tituloTv=findViewById(R.id.tituloExtras);
        descripcionTv=findViewById(R.id.tvDescripcion);
        duracionTv=findViewById(R.id.tvDuracion);
        tituloTv.setText(titulo);
        descripcionTv.setText(descripcion);
        duracionTv.setText("Duracion: " + duracion);
        verFotoRuta= findViewById(R.id.verFotoRuta);

        try {
            Picasso.get().load(fotoIntent).into(verFotoRuta);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_person_selected).into(verFotoRuta);
        }
    }

}