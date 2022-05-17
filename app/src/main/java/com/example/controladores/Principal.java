package com.example.controladores;

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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.controladores.listar.ListarCalle;
import com.example.controladores.listar.ListarEdificio;
import com.example.controladores.listar.ListarMonumento;
import com.example.controladores.listar.ListarRestaurante;
import com.example.controladores.listar.ListarRuta;
import com.example.controladores.logs.LogUsuario;
import com.example.controladores.ver.VerEdificio;
import com.example.just_scan.R;
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

public class Principal extends AppCompatActivity implements View.OnClickListener{
    private FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private StorageReference referenciaAlmacenamiento;
    private String rutaAlmacenamiento="FotosDePerfil/*";

    /*PERMISOS*/
    private static final int CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO=200;
    private static final int CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN=300;
    /*MATRICES*/
    private String [] permisosDeAlmacenamiento;
    private Uri imagen_uri;
    private String perfilImagen="fotoPerfil";
    //Vista
    private ImageView fotoUsuario;
    private TextView nombreUsuarioTv;
    private Intent intent;
    private CardView cardEdificios, cardRutas, cardRestauratnes,cardQR,cardMonumentos,cardCalles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        referenciaAlmacenamiento= FirebaseStorage.getInstance().getReference();
        permisosDeAlmacenamiento=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("Usuarios");
        //vistas
        fotoUsuario=findViewById(R.id.fotoUsuario);
        nombreUsuarioTv=findViewById(R.id.nombreUsuarioTv);
        cardEdificios=findViewById(R.id.cardEdificios);
        cardEdificios.setOnClickListener(this);
        cardRutas=findViewById(R.id.cardRutas);
        cardRutas.setOnClickListener(this);
        cardRestauratnes=findViewById(R.id.cardRestaurantes);
        cardRestauratnes.setOnClickListener(this);
        cardQR=findViewById(R.id.cardQR);
        cardQR.setOnClickListener(this);
        cardMonumentos=findViewById(R.id.cardMonumentos);
        cardMonumentos.setOnClickListener(this);
        cardCalles=findViewById(R.id.cardCalles);
        cardCalles.setOnClickListener(this);
        fotoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarFotoPerfil();
            }
        });
        usuarioLoggeado();

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cardEdificios:
                intent=new Intent(Principal.this, ListarEdificio.class);
                startActivity(intent);
                break;
            case R.id.cardCalles:
                intent=new Intent(Principal.this, ListarCalle.class);
                startActivity(intent);
                break;
            case R.id.cardRutas:
                intent=new Intent(Principal.this, ListarRuta.class);
                startActivity(intent);
                break;
            case R.id.cardMonumentos:
                intent=new Intent(Principal.this, ListarMonumento.class);
                startActivity(intent);
                break;
            case R.id.cardQR:
                intent=new Intent(Principal.this, ListarEdificio.class);
                startActivity(intent);
                break;
            case R.id.cardRestaurantes:
                intent=new Intent(Principal.this, ListarRestaurante.class);
                startActivity(intent);
                break;
        }
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
        boolean resultado= ContextCompat.checkSelfPermission(Principal.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
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
                                        Toast.makeText(Principal.this,"Foto actualizada correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Principal.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(Principal.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                            }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Principal.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
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
                    nombreUsuarioTv.setText(nombreUsuario);

                    try {
                        Picasso.get().load(imagen).into(fotoUsuario);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.ic_person_selected).into(fotoUsuario);
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
            startActivity(new Intent(Principal.this, LogUsuario.class));
            finish();
        }
    }


}
