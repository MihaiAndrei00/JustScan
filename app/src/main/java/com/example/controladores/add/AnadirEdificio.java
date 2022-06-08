package com.example.controladores.add;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controladores.validar.Validar;
import com.example.just_scan.R;
import com.example.modelo.Edificio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class AnadirEdificio extends AppCompatActivity {
    private Intent intent;
    //vistas
    private EditText nombreEdificio;
    private EditText calleEdificio;
    private EditText historiaEdificio;
    private EditText latitudEdificio;
    private EditText longitudEdificio;
    private Button anadirEdificio;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child("Edificios");
    private FirebaseStorage storage;
    private StorageReference storageReference;
    //imagen
    private Uri imageUri;
    private String imagenRuta="foto";
    private ImageView fotoDeEdificio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_edificio);
        latitudEdificio=findViewById(R.id.et_latitudEdificio);
        longitudEdificio=findViewById(R.id.et_LongitudEdificio);
        nombreEdificio = findViewById(R.id.nombreDeEdificio);
        calleEdificio = findViewById(R.id.calleEdificio);
        historiaEdificio = findViewById(R.id.historiaEdificio);
        fotoDeEdificio = findViewById(R.id.imagenEdificio);
        anadirEdificio = findViewById(R.id.agregarEdificio);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        fotoDeEdificio.setImageResource(R.drawable.logo);
        //llamo al metodo para registrar
        registrarRuta();

        //metodo que te lleva a la galeria y haced que puedas cambiar de foto
        cambairFotoEdificio();
    }

    private void cambairFotoEdificio() {
        fotoDeEdificio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            fotoDeEdificio.setImageURI(imageUri);
        }
    }

    private void registrarRuta() {
        anadirEdificio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = nombreEdificio.getText().toString();
                String calle = calleEdificio.getText().toString();
                String historia = historiaEdificio.getText().toString();
                String uId = UUID.randomUUID().toString();
                String latitud= latitudEdificio.getText().toString();
                double latidudNum=Double.parseDouble(latitud);
                String longitud= longitudEdificio.getText().toString();
                double longitudNum=Double.parseDouble(longitud);
                StorageReference ref= storageReference.child("FotosDeEdificios/*");
                if(nombre.isEmpty()){
                    Toast.makeText(AnadirEdificio.this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                }else{
                    if(calle.isEmpty()){
                        Toast.makeText(AnadirEdificio.this, "La calle no puede estar vacía", Toast.LENGTH_SHORT).show();
                    }else{
                        if(historia.isEmpty()){
                            Toast.makeText(AnadirEdificio.this, "La historia no puede estar vacía", Toast.LENGTH_SHORT).show();

                        }else{
                            if(latitud.isEmpty()){
                                Toast.makeText(AnadirEdificio.this, "la latitud no puede estar vacía", Toast.LENGTH_SHORT).show();

                            }else{
                                if(longitud.isEmpty()){
                                    Toast.makeText(AnadirEdificio.this, "la longitud no puede estar vacía", Toast.LENGTH_SHORT).show();

                                }else{
                                    if(!Validar.validarLetrasNumYSpace(nombreEdificio)){
                                        Toast.makeText(AnadirEdificio.this, "Formato nombre incorrecto", Toast.LENGTH_SHORT).show();

                                    }else{
                                        if(!Validar.validarLetrasNumYSpace(calleEdificio)){
                                            Toast.makeText(AnadirEdificio.this, "Formato calle incorrecto", Toast.LENGTH_SHORT).show();

                                        }else{
                                            if(!Validar.validarLetrasNumYSpace(historiaEdificio)){
                                                Toast.makeText(AnadirEdificio.this, "Formato historia incorrecto", Toast.LENGTH_SHORT).show();

                                            }else{
                                                if (!Validar.validarNumDouble(latitudEdificio)){
                                                    Toast.makeText(AnadirEdificio.this, "Formato latitud incorrecto", Toast.LENGTH_SHORT).show();

                                                }else{
                                                    if (!Validar.validarNumDouble(longitudEdificio)){
                                                        Toast.makeText(AnadirEdificio.this, "Formato longitud incorrecto", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                                                                        while (!uriTask.isSuccessful());
                                                                        Uri downloadUri=uriTask.getResult();
                                                                        if(uriTask.isSuccessful()){
                                                                            HashMap<String, Object> resultado= new HashMap<>();
                                                                            resultado.put(imagenRuta,downloadUri.toString());
                                                                            Edificio edificio = new Edificio(uId, nombre, calle, historia, downloadUri.toString(),latidudNum,longitudNum);
                                                                            myRef.child(edificio.getuId()).setValue(edificio);
                                                                            Toast.makeText(AnadirEdificio.this, "Edificio subida correctamente", Toast.LENGTH_SHORT).show();
                                                                        }else{
                                                                            Toast.makeText(AnadirEdificio.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                                Toast.makeText(AnadirEdificio.this, "Subida de imagen fallida,volver a intentar", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}