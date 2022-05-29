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

import com.example.controladores.listar.ListarCalle;
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

public class EditarCalle extends AppCompatActivity {
    //foto
    private String rutaAlmacenamiento="FotosDeCalles/*";
    private String [] permisosDeAlmacenamiento;
    private Uri imagen_uri;
    private String perfilImagen="foto";
    //permisos
    private static final int CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO=200;
    private static final int CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN=300;
    //intent
    private Intent intent;
    //vista
    private EditText nombreCalle;
    private EditText descripcionCalle;
    private EditText latitudCalle;
    private EditText longitudCalle;
    private Button editarCalle;
    private ImageView fotoDeCalle;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("Calles");
    private FirebaseStorage storage;
    private StorageReference referenciaAlmacenamiento;
    //datos calle
    private String nombre, descripcion , fotoIntent,idCalle;
    private int permisosUser;
    private double lat, longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_calle);
        //datos calle y permisos usuario
        permisosUser=getIntent().getIntExtra("permisos",permisosUser);
        nombre=getIntent().getStringExtra("nombre");
        descripcion=getIntent().getStringExtra("historia");
        fotoIntent=getIntent().getStringExtra("foto");
        idCalle=getIntent().getStringExtra("uid");
        lat=getIntent().getDoubleExtra("latitud",lat);
        longi=getIntent().getDoubleExtra("longitud",longi);
        //vistas
        nombreCalle = findViewById(R.id.editarNombreDeCalle);
        nombreCalle.setText(nombre);
        descripcionCalle=findViewById(R.id.editarHistoriaCalle);
        descripcionCalle.setText(descripcion);
        latitudCalle=findViewById(R.id.editarEt_latitudCalle);
        latitudCalle.setText(Double.toString(lat));
        longitudCalle=findViewById(R.id.editarEt_LongitudCalle);
        longitudCalle.setText(Double.toString(longi));
        editarCalle=findViewById(R.id.btnEditarCalle);
        fotoDeCalle=findViewById(R.id.editarImagenCalle);
        //bd
        storage = FirebaseStorage.getInstance();
        referenciaAlmacenamiento = storage.getReference();
        //muestra la foto actual
        consultaParaMostrarFoto();

        //click en la foto del edificio que te lleva en la galeria para cambiarla
        clickEnFotoParaCambiar();
        //editar datos de la calle
        clickEnBotonEditar();

    }

    private void clickEnBotonEditar() {
        editarCalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(idCalle).child("nombre").setValue(nombreCalle.getText().toString());
                myRef.child(idCalle).child("descripcion").setValue(descripcionCalle.getText().toString());
                myRef.child(idCalle).child("latitud").setValue(Double.parseDouble(latitudCalle.getText().toString()));
                myRef.child(idCalle).child("longitud").setValue(Double.parseDouble(longitudCalle.getText().toString()));
                intent=new Intent(EditarCalle.this, ListarCalle.class);
                Toast.makeText(EditarCalle.this, "Datos cambiados correctamente", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void clickEnFotoParaCambiar() {
        fotoDeCalle.setOnClickListener(new View.OnClickListener() {
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
        boolean resultado= ContextCompat.checkSelfPermission(EditarCalle.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
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
        String rutaDeArchivoYNombre=rutaAlmacenamiento + "" + perfilImagen +"_" +idCalle;
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
                            myRef.child(idCalle).updateChildren(resultado).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EditarCalle.this,"Foto actualizada correctamente", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditarCalle.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(EditarCalle.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarCalle.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
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
        Query query= myRef.orderByChild(idCalle);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String imagen=""+ ds.child("foto").getValue();
                    try {
                        Picasso.get().load(imagen).into(fotoDeCalle);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.ic_person_selected).into(fotoDeCalle);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}