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
import com.example.modelo.Calle;
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

public class AnadirCalle extends AppCompatActivity {
    private Intent intent;
    //vistas
    private EditText nombreCalle;
    private EditText historiaCalle;
    private EditText latitudCalle;
    private EditText longitudCalle;
    private Button anadirCalle;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child("Calles");
    private FirebaseStorage storage;
    private StorageReference storageReference;
    //imagen
    private Uri imageUri;
    private String imagenRuta="foto";
    private ImageView fotoDeCalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_calle);
        latitudCalle=findViewById(R.id.et_latitudCalle);
        longitudCalle=findViewById(R.id.et_LongitudCalle);
        nombreCalle = findViewById(R.id.nombreDeCalle);
        historiaCalle = findViewById(R.id.historiaCalle);
        anadirCalle = findViewById(R.id.agregarCalle);
        fotoDeCalle=findViewById(R.id.imagenCalle);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        fotoDeCalle.setImageResource(R.drawable.logo);
        //llamo al metodo para registrar
        registrarRuta();

        //metodo que te lleva a la galeria y haced que puedas cambiar de foto
        cambiarFoto();
    }

    private void cambiarFoto() {
        fotoDeCalle.setOnClickListener(new View.OnClickListener() {
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
            fotoDeCalle.setImageURI(imageUri);
        }
    }

    private void registrarRuta() {
        anadirCalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = nombreCalle.getText().toString();
                String historia = historiaCalle.getText().toString();
                String uId = UUID.randomUUID().toString();
                String latitud= latitudCalle.getText().toString();
                double latidudNum=Double.parseDouble(latitud);
                String longitud= longitudCalle.getText().toString();
                double longitudNum=Double.parseDouble(longitud);

                StorageReference ref= storageReference.child("FotosDeCalles/");
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
                                    Calle calle = new Calle(uId, nombre, historia, downloadUri.toString(),latidudNum,longitudNum);
                                    myRef.child(calle.getuId()).setValue(calle);
                                    Toast.makeText(AnadirCalle.this, "Calle subida correctamente", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(AnadirCalle.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AnadirCalle.this, "Subida de imagen fallida", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}