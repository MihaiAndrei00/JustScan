package com.example.controladores.ver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.just_scan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class VerRestaurante extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child("Restaurantes");
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    private String nombre, descripcion , calle, fotoIntent,idRestaurante,tipoDeComida;
    private TextView nombreTv;
    private TextView tipoDeComidaTv;
    private ImageView verFotoRestaurante;
    private TextView descripcionTv;
    private TextView calleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_restaurante);
        nombre=getIntent().getStringExtra("nombre");
        descripcion=getIntent().getStringExtra("historia");
        calle=getIntent().getStringExtra("calle");
        fotoIntent=getIntent().getStringExtra("foto");
        idRestaurante=getIntent().getStringExtra("uid");
        tipoDeComida=getIntent().getStringExtra("comida");
        nombreTv=findViewById(R.id.nombreRestauranteExtras);
        descripcionTv=findViewById(R.id.tvDescripcionRestauranteExtras);
        calleTv=findViewById(R.id.tvCalleRestauranteExtras);
        verFotoRestaurante= findViewById(R.id.verFotoRestauranteExtras);
        tipoDeComidaTv=findViewById(R.id.tipoDeComidaExtras);
        nombreTv.setText(nombre);
        descripcionTv.setText(descripcion);
        calleTv.setText("Localización: " + calle);
        tipoDeComidaTv.setText(tipoDeComida);
        try {
            Picasso.get().load(fotoIntent).into(verFotoRestaurante);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_person_selected).into(verFotoRestaurante);
        }
    }
}