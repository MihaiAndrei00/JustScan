package com.example.controladores.update;

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

import com.example.controladores.listar.ListarEdificio;
import com.example.controladores.validar.Validar;
import com.example.just_scan.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class EditarEdificio extends AppCompatActivity {
    private String rutaAlmacenamiento="FotosDeEdificios/*";
    private String [] permisosDeAlmacenamiento;
    private Uri imagen_uri;
    private String perfilImagen="foto";
    //permisos
    private static final int CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO=200;
    private static final int CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN=300;
    //intent
    private Intent intent;
    //vistas
    private ImageView fotoDeEdificio;
    private EditText editarNombreEdificio;
    private EditText editarCalleEdificio;
    private EditText editarHistoriaEdificio;
    private EditText editarLatitudEdificio;
    private EditText editarLongitudEdificio;
    private Button btneditarEdificio;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("Edificios");
    private FirebaseStorage storage;
    private StorageReference referenciaAlmacenamiento;
    //datos edificio y permisos
    private String nombre, histoira , fotoIntent,idEdificio,calleEdificio;
    private int permisosUser;
    private double lat, longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_edificio);
        //datos edificio y permisos
        permisosUser=getIntent().getIntExtra("permisos",permisosUser);
        nombre=getIntent().getStringExtra("nombre");
        histoira=getIntent().getStringExtra("historia");
        fotoIntent=getIntent().getStringExtra("foto");
        idEdificio=getIntent().getStringExtra("uid");
        lat=getIntent().getDoubleExtra("latitud",lat);
        longi=getIntent().getDoubleExtra("longitud",longi);
        calleEdificio=getIntent().getStringExtra("calle");
        //vistas
        editarNombreEdificio = findViewById(R.id.editarNombreDeEdificio);
        editarNombreEdificio.setText(nombre);
        editarCalleEdificio=findViewById(R.id.editarCalleEdificio);
        editarCalleEdificio.setText(calleEdificio);
        editarHistoriaEdificio=findViewById(R.id.editarHistoriaEdificio);
        editarHistoriaEdificio.setText(histoira);
        editarLatitudEdificio=findViewById(R.id.editarEt_latitudEdificio);
        editarLatitudEdificio.setText(Double.toString(lat));
        editarLongitudEdificio=findViewById(R.id.editarEt_LongitudEdificio);
        editarLongitudEdificio.setText(Double.toString(longi));
        btneditarEdificio=findViewById(R.id.btnEditarEdificio);
        fotoDeEdificio=findViewById(R.id.editarImagenEdificio);
        //bd
        storage = FirebaseStorage.getInstance();
        referenciaAlmacenamiento = storage.getReference();
        //muestra la foto del edificio
        consultaParaMostrarFoto();

        //al clickar en la foto del edificio te lleva a la galeria y te permite elegir otra
        clickEnFotoEdificioParaCambiarla();
        //
        clickEnBotonParaCambiarDatos();

    }

    private void clickEnBotonParaCambiarDatos() {
        btneditarEdificio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editarNombreEdificio.getText().toString().isEmpty()){
                    Toast.makeText(EditarEdificio.this, "El nombre no puede estar vacio", Toast.LENGTH_SHORT).show();

                }else{
                    if(editarHistoriaEdificio.getText().toString().isEmpty()){
                        Toast.makeText(EditarEdificio.this, "La historia no puede estar vacia", Toast.LENGTH_SHORT).show();

                    }else{
                        if(editarCalleEdificio.getText().toString().isEmpty()){
                            Toast.makeText(EditarEdificio.this, "la calle no puede estar vacia", Toast.LENGTH_SHORT).show();

                        }else{
                            if(editarLatitudEdificio.getText().toString().isEmpty()){
                                Toast.makeText(EditarEdificio.this, "La latitud no puede estar vacia", Toast.LENGTH_SHORT).show();

                            }else{
                                if(editarLongitudEdificio.getText().toString().isEmpty()){
                                    Toast.makeText(EditarEdificio.this, "La longitud no puede estar vacia", Toast.LENGTH_SHORT).show();

                                }else{
                                    if(!Validar.validarLetrasNumYSpace(editarNombreEdificio)){
                                        Toast.makeText(EditarEdificio.this, "Formato nombre incorrecto", Toast.LENGTH_SHORT).show();

                                    }else{
                                        if(!Validar.validarLetrasNumYSpace(editarHistoriaEdificio)){
                                            Toast.makeText(EditarEdificio.this, "Formato hisroria incorrecto", Toast.LENGTH_SHORT).show();

                                        }else{
                                            if(!Validar.validarLetrasNumYSpace(editarCalleEdificio)){
                                                Toast.makeText(EditarEdificio.this, "Formato calle incorrecto", Toast.LENGTH_SHORT).show();

                                            } else{
                                                if(!Validar.validarNumDouble(editarLatitudEdificio)){
                                                    Toast.makeText(EditarEdificio.this, "Formato latitud incorrecto", Toast.LENGTH_SHORT).show();

                                                }else{
                                                    if(!Validar.validarNumDouble(editarLongitudEdificio)){
                                                        Toast.makeText(EditarEdificio.this, "Formato longitud incorrecto", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        myRef.child(idEdificio).child("nombre").setValue(editarNombreEdificio.getText().toString());
                                                        myRef.child(idEdificio).child("historia").setValue(editarHistoriaEdificio.getText().toString());
                                                        myRef.child(idEdificio).child("latitud").setValue(Double.parseDouble(editarLatitudEdificio.getText().toString()));
                                                        myRef.child(idEdificio).child("longitud").setValue(Double.parseDouble(editarLongitudEdificio.getText().toString()));
                                                        myRef.child(idEdificio).child("calle").setValue(editarCalleEdificio.getText().toString());
                                                        intent=new Intent(EditarEdificio.this, ListarEdificio.class);
                                                        Toast.makeText(EditarEdificio.this, "Datos cambiados correctamente", Toast.LENGTH_SHORT).show();
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

    private void clickEnFotoEdificioParaCambiarla() {
        fotoDeEdificio.setOnClickListener(new View.OnClickListener() {
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
        boolean resultado= ContextCompat.checkSelfPermission(EditarEdificio.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
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
        String rutaDeArchivoYNombre=rutaAlmacenamiento + "" + perfilImagen +"_" +idEdificio;
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
                            myRef.child(idEdificio).updateChildren(resultado).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EditarEdificio.this,"Foto actualizada correctamente", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditarEdificio.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(EditarEdificio.this,"Ha ocurrido un error,volver a intentar", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarEdificio.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
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
    private void consultaParaMostrarFoto(){
        Query query= myRef.orderByChild("uid");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String imagen=""+ ds.child("foto").getValue();
                    try {
                        Picasso.get().load(imagen).into(fotoDeEdificio);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.ic_person_selected).into(fotoDeEdificio);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}