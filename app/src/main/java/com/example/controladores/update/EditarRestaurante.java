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

import com.example.controladores.listar.ListarRestaurante;
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

public class EditarRestaurante extends AppCompatActivity {
    //fotos
    private String rutaAlmacenamiento="FotosDeRestaurantes/*";
    private String [] permisosDeAlmacenamiento;
    private Uri imagen_uri;
    private String perfilImagen="foto";
    //permisos solicitudes
    private static final int CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO=200;
    private static final int CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN=300;
    //intent
    private Intent intent;
    //vistas
    private EditText nombreRestaurante;
    private EditText calleRestaurante;
    private EditText descripcionRestaurante;
    private EditText tipoDeComida;
    private EditText latitudRestaurante;
    private EditText longitudRestaurante;
    private Button editarRestaurante;
    private ImageView fotoDeRestaurante;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("Restaurantes");
    private FirebaseStorage storage;
    private StorageReference referenciaAlmacenamiento;
    //datos restaurante y permisosUsuario
    private String nombre, descripcion , calle, fotoIntent,idRestaurante,tipoDeComidaIntent;
    private int permisosUser;
    private double lat, longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_restaurante);
        //datos restaurante y permisosUsuario
        permisosUser=getIntent().getIntExtra("permisos",permisosUser);
        nombre=getIntent().getStringExtra("nombre");
        descripcion=getIntent().getStringExtra("historia");
        calle=getIntent().getStringExtra("calle");
        fotoIntent=getIntent().getStringExtra("foto");
        idRestaurante=getIntent().getStringExtra("uid");
        tipoDeComidaIntent=getIntent().getStringExtra("comida");
        lat=getIntent().getDoubleExtra("latitud",lat);
        longi=getIntent().getDoubleExtra("longitud",longi);
        //vistas
        nombreRestaurante = findViewById(R.id.editarNombreDeRestaurante);
        nombreRestaurante.setText(nombre);
        calleRestaurante=findViewById(R.id.editarCalleRestaurante);
        calleRestaurante.setText(calle);
        descripcionRestaurante=findViewById(R.id.editarDescripcionRestaurante);
        descripcionRestaurante.setText(descripcion);
        tipoDeComida=findViewById(R.id.editarTipoDeComida);
        tipoDeComida.setText(tipoDeComidaIntent);
        latitudRestaurante=findViewById(R.id.editarEt_latitudRestaurante);
        latitudRestaurante.setText(Double.toString(lat));
        longitudRestaurante=findViewById(R.id.editarEt_LongitudRestaurante);
        longitudRestaurante.setText(Double.toString(longi));
        editarRestaurante=findViewById(R.id.btnEditarRestaurante);
        fotoDeRestaurante=findViewById(R.id.editarImagenRestaurante);
        //bd
        storage = FirebaseStorage.getInstance();
        referenciaAlmacenamiento = storage.getReference();
        //mostrar foto del restaurante
        consultaParaMostrarFoto();
        //al clickar en la foto del restaurante te lleva a la galeria y te permite elegir otra
        clickEnFotoRestauranteParaCambiarla();
        //click en boton de Editar para editar los datos
        clickEnBotonParaCambiarDatos();
    }

    private void clickEnBotonParaCambiarDatos() {
        editarRestaurante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(idRestaurante).child("nombre").setValue(nombreRestaurante.getText().toString());
                myRef.child(idRestaurante).child("calle").setValue(calleRestaurante.getText().toString());
                myRef.child(idRestaurante).child("tipoDeComida").setValue(tipoDeComida.getText().toString());
                myRef.child(idRestaurante).child("descripcion").setValue(descripcionRestaurante.getText().toString());
                myRef.child(idRestaurante).child("latitud").setValue(Double.parseDouble(latitudRestaurante.getText().toString()));
                myRef.child(idRestaurante).child("longitud").setValue(Double.parseDouble(longitudRestaurante.getText().toString()));
                intent=new Intent(EditarRestaurante.this, ListarRestaurante.class);
                Toast.makeText(EditarRestaurante.this, "Datos cambiados correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickEnFotoRestauranteParaCambiarla() {
        fotoDeRestaurante.setOnClickListener(new View.OnClickListener() {
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
        boolean resultado= ContextCompat.checkSelfPermission(EditarRestaurante.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
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
        String rutaDeArchivoYNombre=rutaAlmacenamiento + "" + perfilImagen +"_" +idRestaurante;
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
                            myRef.child(idRestaurante).updateChildren(resultado).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EditarRestaurante.this,"Foto actualizada correctamente", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditarRestaurante.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(EditarRestaurante.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarRestaurante.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
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
                        Picasso.get().load(imagen).into(fotoDeRestaurante);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.ic_person_selected).into(fotoDeRestaurante);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}