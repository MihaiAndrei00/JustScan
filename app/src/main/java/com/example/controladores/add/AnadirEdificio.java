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

import com.example.just_scan.R;
import com.example.modelo.Edificio;
import com.example.modelo.Ruta;
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
    private Button anadirEdificio;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child("Edificios");
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Ruta ruta;
    private Uri imageUri;
    private String imagenRuta="foto";
    private ImageView fotoDeEdificio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_edificio);
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
                String foto = "https://firebasestorage.googleapis.com/v0/b/justscan-c5c3e.appspot.com/o/fotoRutas%2Flogo.png?alt=media&token=8fd56909-e3f5-463b-be59-cc106a09bb8e";
                String uId = UUID.randomUUID().toString();
                double lat=40.415511;
                double longi=-3.7074009;

                StorageReference ref= storageReference.child("FotosDeEdificios/");
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
                                    Edificio edificio = new Edificio(uId, nombre, calle, historia, downloadUri.toString(),lat,longi);
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

                        Toast.makeText(AnadirEdificio.this, "Subida de imagen fallida", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}