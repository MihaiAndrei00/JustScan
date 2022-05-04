package com.example.controladores;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.just_scan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CambiarPerfil extends AppCompatActivity {
    private ImageView imagenPerfil;
    private Button botonCambiar;
    FirebaseStorage storage;
    StorageReference reference;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_perfil);

        imagenPerfil = findViewById(R.id.fotoDePerfil);
        botonCambiar = findViewById(R.id.cambiar);

        botonCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        imagenPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });
    }

    private void uploadImage() {
         if(imageUri!=null){
             reference=storage.getReference().child("fotoPerfil" + UUID.randomUUID().toString());
             reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                     if(task.isSuccessful()){
                         Toast.makeText(CambiarPerfil.this, "Imagen cambiada", Toast.LENGTH_SHORT).show();
                     }else{
                         Toast.makeText(CambiarPerfil.this, "Imagen fallida", Toast.LENGTH_SHORT).show();
                     }
                 }
             });
         }
    }

    ActivityResultLauncher<String> mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if (result!=null){
                imagenPerfil.setImageURI(result);
                imageUri=result;
            }
        }
    });


}