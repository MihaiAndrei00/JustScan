package com.example.controladores.ver;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.controladores.Principal;
import com.example.just_scan.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

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