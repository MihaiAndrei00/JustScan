package com.example.controladores.add;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controladores.Principal;
import com.example.just_scan.R;
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

public class AnadirRuta extends AppCompatActivity {
    private Intent intent;
    //vistas
    private EditText tituloRuta;
    private EditText descripcionRuta;
    private EditText duracionRuta;
    private Button anadirRuta;
    private ProgressBar pb;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child("Rutas");
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Ruta ruta;
    private Uri imageUri;
    private String imagenRuta="foto";


    private ImageView fotoDeRuta;

    //declaramos la valoracion a null ya que mas adelan
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anadir_ruta);
        tituloRuta = findViewById(R.id.tituloRutaVista);
        descripcionRuta = findViewById(R.id.descripcionRutaVista);
        duracionRuta = findViewById(R.id.duracionRutaVista);
        fotoDeRuta = findViewById(R.id.imagenRuta);
        anadirRuta = findViewById(R.id.agregarRuta);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        fotoDeRuta.setImageResource(R.drawable.logo);
        //llamo al metodo para registrar
        registrarRuta();


        fotoDeRuta.setOnClickListener(new View.OnClickListener() {
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
            fotoDeRuta.setImageURI(imageUri);
        }
    }

    private void registrarRuta() {
        anadirRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titulo = tituloRuta.getText().toString();
                String descripcion = descripcionRuta.getText().toString();
                String duracion = duracionRuta.getText().toString();
                String foto = "https://firebasestorage.googleapis.com/v0/b/justscan-c5c3e.appspot.com/o/fotoRutas%2Flogo.png?alt=media&token=8fd56909-e3f5-463b-be59-cc106a09bb8e";
                String uId = UUID.randomUUID().toString();

                StorageReference ref= storageReference.child("FotosDeRutas/");
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
                                    Ruta ruta = new Ruta(uId, titulo, descripcion, duracion, downloadUri.toString());
                                    myRef.child(ruta.getuId()).setValue(ruta);
                                    Toast.makeText(AnadirRuta.this, "Ruta subida correctamente", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(AnadirRuta.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(AnadirRuta.this, "Subida de imagen fallida", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
