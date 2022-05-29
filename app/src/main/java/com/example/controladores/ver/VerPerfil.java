package com.example.controladores.ver;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.controladores.Principal;
import com.example.just_scan.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class VerPerfil extends AppCompatActivity {
    //vistas
    private EditText txtEmail;
    private EditText txtNombreUsuario;
    private EditText txtTelefono;
    private Button btnActualizar;
    private ImageView fotoPerfil;
    //adMob
    private AdView mAdView;
    private String tag ="Principal";
    private AdRequest adRequest;
    //intent
    private Intent intent;
    //bd
    private FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private StorageReference referenciaAlmacenamiento;
    //foto
    private String rutaAlmacenamiento="FotosDePerfil/*";
    private String [] permisosDeAlmacenamiento;
    private Uri imagen_uri;
    private String perfilImagen="fotoPerfil";
    /*PERMISOS*/
    private static final int CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO=200;
    private static final int CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN=300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);
        mAdView = findViewById(R.id.adView);
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //vistas
        txtEmail=findViewById(R.id.txtEmailUpdate);
        txtNombreUsuario=findViewById(R.id.txtNombreUsuarioUpdate);
        txtTelefono=findViewById(R.id.txtTelefonoUpdate);
        btnActualizar=findViewById(R.id.btnActualizar);
        fotoPerfil=findViewById(R.id.fotoPerfil);
        //bd
        referenciaAlmacenamiento= FirebaseStorage.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Usuarios");
        //permisos
        permisosDeAlmacenamiento=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //metodo que al clickar en la foto la puedas cambiar
        cambiarFotoPerfil();
        //si el usuario esta loggeado consulta y muestra sus datos en las vistas
        usuarioLoggeado();
        //AdMob
        bannerAnuncio();
        //al clickar en el boton de actualizar, actualiza los datos y te redirige al main
        actualizarDatosEIntentPrincipal();
    }

    private void actualizarDatosEIntentPrincipal() {
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               reference.child(user.getUid()).child("nombreUsuario").setValue(txtNombreUsuario.getText().toString());
               reference.child(user.getUid()).child("email").setValue(txtEmail.getText().toString());
               reference.child(user.getUid()).child("telefono").setValue(txtTelefono.getText().toString());
               Toast.makeText(VerPerfil.this, "Datos acutalizados correctamente", Toast.LENGTH_SHORT).show();
               intent=new Intent(VerPerfil.this, Principal.class);
            }
        });
    }

    private void bannerAnuncio() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    private void cambiarFotoPerfil() {
        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarFotoPerfil();
            }
        });
    }

    private void actualizarFotoPerfil() {
        String [] opciones={"Galeria"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen de: ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(i==0){
                    //Selecciono la galeria
                    if(!comprobarPermisosAlmacenamiento()){
                        //Si no se habilita el permiso
                        solicitarPermisosAlmacenamiento();
                    }else{
                        //Si se habilita el permiso
                        elegirImagenGaleria();
                    }
                }
            }
        });
        builder.create().show();
    }
    //Comprobar si los permisos de almacenamiento estan habilitados
    private boolean comprobarPermisosAlmacenamiento() {
        boolean resultado= ContextCompat.checkSelfPermission(VerPerfil.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return resultado;
    }
    // Se llama cuando el usuario presiona permitir o denegar el cuadro de dialogo
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO:{
                //Seleccion de la galeria
                if(grantResults.length>0){
                    boolean escrituraDeAlmacenamientoAceptado= grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(escrituraDeAlmacenamientoAceptado){
                        //Permiso habilitado
                        elegirImagenGaleria();
                    }else{
                        Toast.makeText(this,"Habilite el permiso a la galeria", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //Se llama cuando ya eligió la imagen de la galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode== RESULT_OK){
            //De la imagen vamos a obtener la URI
            if(requestCode== CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN){
                imagen_uri= data.getData();
                SubirFoto(imagen_uri);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void SubirFoto(Uri imagen_uri) {
        String rutaDeArchivoYNombre=rutaAlmacenamiento + "" + perfilImagen +"_" +user.getUid();
        StorageReference storageReference= referenciaAlmacenamiento.child(rutaDeArchivoYNombre);
        storageReference.putFile(imagen_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri=uriTask.getResult();
                        if(uriTask.isSuccessful()){
                            HashMap<String, Object> resultado= new HashMap<>();
                            resultado.put(perfilImagen,downloadUri.toString());
                            reference.child(user.getUid()).updateChildren(resultado).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(VerPerfil.this,"Foto actualizada correctamente", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(VerPerfil.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(VerPerfil.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VerPerfil.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Abre la galeria
    private void elegirImagenGaleria() {
        intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN);
    }

    // permisos de almacenamiento en tiempo de ejcucion
    private void solicitarPermisosAlmacenamiento() {
        requestPermissions(permisosDeAlmacenamiento,CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO);
    }
    private void consultaParaMostrarDatos(){
        Query query= reference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String nombreUsuario= ""+ ds.child("nombreUsuario").getValue();
                    String imagen=""+ ds.child("fotoPerfil").getValue();
                    String email=""+ds.child("email").getValue();
                    String tlf=""+ds.child("telefono").getValue();
                    txtNombreUsuario.setText(nombreUsuario);
                    txtEmail.setText(email);
                    txtTelefono.setText(tlf);

                    try {
                        Picasso.get().load(imagen).into(fotoPerfil);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.ic_person_selected).into(fotoPerfil);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void usuarioLoggeado(){
        if(user!=null){
            consultaParaMostrarDatos();
            Toast.makeText(this,"Bienvenido a Just Scan", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(VerPerfil.this, Principal.class));
            finish();
        }
    }

}
