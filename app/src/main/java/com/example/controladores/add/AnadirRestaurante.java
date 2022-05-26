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
import com.example.modelo.Restaurante;
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

public class AnadirRestaurante extends AppCompatActivity {
    private Intent intent;
    //vistas
    private EditText nombreRestaurante;
    private EditText calleRestaurante;
    private EditText descripcionRestaurante;
    private EditText tipoDeComida;
    private EditText latitudRestaurante;
    private EditText longitudRestaurante;
    private Button anadirRestaurante;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("Restaurantes");
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imageUri;
    private String imagenRuta = "foto";
    private ImageView fotoDeRestaurante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_restaurante);
        latitudRestaurante=findViewById(R.id.et_latitudRestaurante);
        longitudRestaurante=findViewById(R.id.et_latitudRestaurante);
        nombreRestaurante = findViewById(R.id.nombreDeRestaurante);
        calleRestaurante = findViewById(R.id.calleRestaurante);
        descripcionRestaurante = findViewById(R.id.descripcionRestaurante);
        tipoDeComida = findViewById(R.id.tipoDeComida);
        anadirRestaurante = findViewById(R.id.agregarRestaurante);
        fotoDeRestaurante=findViewById(R.id.imagenRestaurante);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        fotoDeRestaurante.setImageResource(R.drawable.logo);
        //llamo al metodo para registrar
        registrarRuta();


        fotoDeRestaurante.setOnClickListener(new View.OnClickListener() {
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
            fotoDeRestaurante.setImageURI(imageUri);
        }
    }

    private void registrarRuta() {
        anadirRestaurante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = nombreRestaurante.getText().toString();
                String calle = calleRestaurante.getText().toString();
                String descripcion = descripcionRestaurante.getText().toString();
                String tipoComida = tipoDeComida.getText().toString();
                String uId = UUID.randomUUID().toString();
                String latitud= latitudRestaurante.getText().toString();
                double latidudNum=Double.parseDouble(latitud);
                String longitud= longitudRestaurante.getText().toString();
                double longitudNum=Double.parseDouble(longitud);

                StorageReference ref = storageReference.child("FotosDeRestaurantes/");
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
                                    Restaurante restaurante = new Restaurante(uId, nombre, calle,tipoComida,descripcion, downloadUri.toString(),latidudNum,longitudNum );
                                    myRef.child(restaurante.getuId()).setValue(restaurante);
                                    Toast.makeText(AnadirRestaurante.this, "Restaurante subida correctamente", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AnadirRestaurante.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(AnadirRestaurante.this, "Subida de imagen fallida", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}