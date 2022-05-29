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

import com.example.controladores.listar.ListarMonumento;
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

public class EditarMonumento extends AppCompatActivity {
    //foto
    private String rutaAlmacenamiento="FotosDeMonumentos/*";
    private String [] permisosDeAlmacenamiento;
    private Uri imagen_uri;
    private String perfilImagen="foto";
    //permisos
    private static final int CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO=200;
    private static final int CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN=300;
    //intent
    private Intent intent;
    //vistas
    private EditText editarNombreMonumento;
    private EditText editarCalleMonumento;
    private EditText editarHistoriaMonumento;
    private EditText editarLatitudMonumento;
    private EditText editarLongitudMonumento;
    private Button btnEditarMonumento;
    private ImageView fotoDeMonumento;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("Monumentos");
    private FirebaseStorage storage;
    private StorageReference referenciaAlmacenamiento;
    //datos usuario y permisos
    private String nombre, histoira , fotoIntent,idMonumento,calleEdificio;
    private int permisosUser;
    private double lat, longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_monumentos);
        //datos usuario y permisos
        permisosUser=getIntent().getIntExtra("permisos",permisosUser);
        nombre=getIntent().getStringExtra("nombre");
        histoira=getIntent().getStringExtra("historia");
        fotoIntent=getIntent().getStringExtra("foto");
        idMonumento=getIntent().getStringExtra("uid");
        lat=getIntent().getDoubleExtra("latitud",lat);
        longi=getIntent().getDoubleExtra("longitud",longi);
        calleEdificio=getIntent().getStringExtra("calle");
        //vistas
        editarNombreMonumento = findViewById(R.id.editarNombreDeMonumento);
        editarNombreMonumento.setText(nombre);
        editarCalleMonumento=findViewById(R.id.editarCalleMonumento);
        editarCalleMonumento.setText(calleEdificio);
        editarHistoriaMonumento=findViewById(R.id.editarHistoriaMonumento);
        editarHistoriaMonumento.setText(histoira);
        editarLatitudMonumento=findViewById(R.id.editarEt_latitudMonumento);
        editarLatitudMonumento.setText(Double.toString(lat));
        editarLongitudMonumento=findViewById(R.id.editarEt_LongitudMonumento);
        editarLongitudMonumento.setText(Double.toString(longi));
        btnEditarMonumento=findViewById(R.id.btnEditarMonumento);
        fotoDeMonumento=findViewById(R.id.editarImagenMonumento);
        //bd
        storage = FirebaseStorage.getInstance();
        referenciaAlmacenamiento = storage.getReference();
        //mostrar foto del monumento
        consultaParaMostrarFoto();
        //al clickar en la foto del monumento te lleva a la galeria y te permite elegir otra
        clickEnFotoMonumentoParaCambiarla();
        //click en boton de Editar para editar los datos
        clickEnBotonParaCambiarDatos();
    }

    private void clickEnBotonParaCambiarDatos() {
        btnEditarMonumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(idMonumento).child("nombre").setValue(editarNombreMonumento.getText().toString());
                myRef.child(idMonumento).child("historia").setValue(editarHistoriaMonumento.getText().toString());
                myRef.child(idMonumento).child("latitud").setValue(Double.parseDouble(editarLatitudMonumento.getText().toString()));
                myRef.child(idMonumento).child("longitud").setValue(Double.parseDouble(editarLongitudMonumento.getText().toString()));
                myRef.child(idMonumento).child("calle").setValue(editarCalleMonumento.getText().toString());
                intent=new Intent(EditarMonumento.this, ListarMonumento.class);
                Toast.makeText(EditarMonumento.this, "Datos cambiados correctamente", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void clickEnFotoMonumentoParaCambiarla() {
        fotoDeMonumento.setOnClickListener(new View.OnClickListener() {
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
        boolean resultado= ContextCompat.checkSelfPermission(EditarMonumento.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
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
        String rutaDeArchivoYNombre=rutaAlmacenamiento + "" + perfilImagen +"_" +idMonumento;
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
                            myRef.child(idMonumento).updateChildren(resultado).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EditarMonumento.this,"Foto actualizada correctamente", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditarMonumento.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(EditarMonumento.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarMonumento.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
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
        Query query= myRef.orderByChild(idMonumento);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String imagen=""+ ds.child("foto").getValue();
                    try {
                        Picasso.get().load(imagen).into(fotoDeMonumento);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.ic_person_selected).into(fotoDeMonumento);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}