package com.example.controladores.ver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.just_scan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class VerEdificio extends AppCompatActivity {
    private Intent intent;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child("Edificios");
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    private String titulo, descripcion , calle, fotoIntent,idEdificio;
    private TextView nombreTv;
    private ImageView verFotoEdificio;
    private TextView descripcionTv;
    private TextView calleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_edificio);
        titulo=getIntent().getStringExtra("nombre");
        descripcion=getIntent().getStringExtra("historia");
        calle=getIntent().getStringExtra("calle");
        fotoIntent=getIntent().getStringExtra("foto");
        idEdificio=getIntent().getStringExtra("uid");
        nombreTv=findViewById(R.id.nombreEdificioExtras);
        descripcionTv=findViewById(R.id.tvDescripcionEdificioExtras);
        calleTv=findViewById(R.id.tvCalleEdificioExtras);
        nombreTv.setText(titulo);
        descripcionTv.setText(descripcion);
        calleTv.setText("Localización: " + calle);
        verFotoEdificio= findViewById(R.id.verFotoEdificioExtras);

        try {
            Picasso.get().load(fotoIntent).into(verFotoEdificio);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_person_selected).into(verFotoEdificio);
        }
    }
}