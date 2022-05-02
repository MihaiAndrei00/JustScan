package com.example.controladores;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.just_scan.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AnadirRuta extends AppCompatActivity {
    private ImageView verImagen;
    private ProgressDialog mProgressDialog;
    private Button botonAnadirRuta;
    private StorageReference mStorage;
    private Intent intent;
    private static final int GALERY_INTENT=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anadir_ruta);
        botonAnadirRuta=findViewById(R.id.AgregarFoto);
        mStorage= FirebaseStorage.getInstance().getReference();
        verImagen=findViewById(R.id.verImagen);
        mProgressDialog= new ProgressDialog(this);
        botonAnadirRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALERY_INTENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== GALERY_INTENT && resultCode==RESULT_OK){
           mProgressDialog.setTitle("Subiendo...");
           mProgressDialog.setCancelable(false);
           mProgressDialog.show();

            Uri uri= data.getData();
            StorageReference filePath= mStorage.child("fotosRutas").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   mProgressDialog.dismiss();
                   Uri descargarFoto=taskSnapshot.getStorage().getDownloadUrl().getResult();


                    Toast.makeText(AnadirRuta.this, "Foto subida", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
