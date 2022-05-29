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
import com.example.modelo.Monumento;
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

public class AnadirMonumento extends AppCompatActivity {
    private Intent intent;
    //vistas
    private EditText nombreMonumento;
    private EditText calleMonumento;
    private EditText historiaMonumento;
    private EditText latitudMonumento;
    private EditText longitudMonumento;
    private Button anadirMonumento;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("Monumentos");
    private FirebaseStorage storage;
    private StorageReference storageReference;
    //imagen
    private Uri imageUri;
    private String imagenRuta = "foto";
    private ImageView fotoDeMonumento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_monumento);
        latitudMonumento=findViewById(R.id.et_latitudMonumento);
        longitudMonumento=findViewById(R.id.et_LongitudMonumento);
        nombreMonumento = findViewById(R.id.nombreDeMonumento);
        calleMonumento = findViewById(R.id.calleMonumento);
        historiaMonumento = findViewById(R.id.historiaMonumento);
        fotoDeMonumento = findViewById(R.id.imagenMonumento);
        anadirMonumento = findViewById(R.id.agregarMonumento);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        fotoDeMonumento.setImageResource(R.drawable.logo);
        //llamo al metodo para registrar
        registrarRuta();

        //metodo que te lleva a la galeria y haced que puedas cambiar de foto
        cambiarFotoMonumento();
    }

    private void cambiarFotoMonumento() {
        fotoDeMonumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            fotoDeMonumento.setImageURI(imageUri);
        }
    }

    private void registrarRuta() {
        anadirMonumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = nombreMonumento.getText().toString();
                String calle = calleMonumento.getText().toString();
                String historia = historiaMonumento.getText().toString();
                String uId = UUID.randomUUID().toString();
                String latitud= latitudMonumento.getText().toString();
                double latidudNum=Double.parseDouble(latitud);
                String longitud= longitudMonumento.getText().toString();
                double longitudNum=Double.parseDouble(longitud);

                StorageReference ref = storageReference.child("FotosDeMonumentos/*");
                ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isSuccessful()) ;
                                Uri downloadUri = uriTask.getResult();
                                if (uriTask.isSuccessful()) {
                                    HashMap<String, Object> resultado = new HashMap<>();
                                    resultado.put(imagenRuta, downloadUri.toString());
                                    Monumento monumento = new Monumento(uId, nombre, calle, historia, downloadUri.toString(),latidudNum, longitudNum);
                                    myRef.child(monumento.getuId()).setValue(monumento);
                                    Toast.makeText(AnadirMonumento.this, "Monumento subida correctamente", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AnadirMonumento.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(AnadirMonumento.this, "Subida de imagen fallida", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}