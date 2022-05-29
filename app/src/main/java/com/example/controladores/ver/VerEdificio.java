package com.example.controladores.ver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controladores.listar.ListarMonumento;
import com.example.controladores.update.EditarEdificio;
import com.example.just_scan.R;
import com.example.maps.MapEdificio;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class VerEdificio extends AppCompatActivity {
    //AdMob
    private String tag ="VerEdificio";
    private AdView mAdView;
    private  AdRequest adRequest;
    //intent
    private Intent intent;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child("Edificios");
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    //datos edificios y permisos usuarios
    private String titulo, descripcion , calle, fotoIntent,idEdificio;
    private double lat, longi;
    private int permisosUser;
    //vistas
    private TextView nombreTv;
    private ImageView verFotoEdificio;
    private TextView descripcionTv;
    private TextView calleTv;
    private FloatingActionButton btnVerMapa, btnVerMapaGoogleMaps, btnNavegar, btnPanoramico, btnAnimacion,borrarElemento,editarElemento;
    //animacion
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isOpen=false;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_edificio);
        //AdMob
        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //datos edificio y permisos ususario
        lat=getIntent().getDoubleExtra("latitud",lat);
        longi=getIntent().getDoubleExtra("longitud",longi);
        titulo=getIntent().getStringExtra("nombre");
        descripcion=getIntent().getStringExtra("historia");
        calle=getIntent().getStringExtra("calle");
        fotoIntent=getIntent().getStringExtra("foto");
        idEdificio=getIntent().getStringExtra("uid");
        permisosUser=getIntent().getIntExtra("permisos",permisosUser);
        //vistas
        nombreTv=findViewById(R.id.nombreEdificioExtras);
        descripcionTv=findViewById(R.id.tvDescripcionEdificioExtras);
        calleTv=findViewById(R.id.tvCalleEdificioExtras);
        nombreTv.setText(titulo);
        descripcionTv.setText(descripcion);
        calleTv.setText("Localización: " + calle);
        verFotoEdificio= findViewById(R.id.verFotoEdificioExtras);
        btnVerMapa=findViewById(R.id.mostrarEnMapaEdificio);
        btnVerMapaGoogleMaps=findViewById(R.id.mostrarEnGoogleMapsEdificio);
        btnNavegar=findViewById(R.id.btnNavegarEdificio);
        btnPanoramico=findViewById(R.id.btnPanoramicoEdificio);
        btnAnimacion=findViewById(R.id.btnAnimationEdificio);
        borrarElemento=findViewById(R.id.borrarEdificio);
        editarElemento=findViewById(R.id.EditarEdificio);
        //animacion
        fabOpen= AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose=AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotateForward=AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward=AnimationUtils.loadAnimation(this,R.anim.rotate_backward);
        //oculta botones de editar y eliminar segun los permisos del usuario
        ocultarBotonesSegunPermisos();
        //intent a la ventana de editar
        intentAEditarElemento();
        //borrar elemento
        borrarEdificioAccion();
        //animacion que despliega o esconde los diferentes botones del maps
        animacionBotonPrincipalMaps();
        //boton de maps que despliega vista panoramica
        mostrarVistaPanoramica();
        //boton de maps que te ubica el edificio con un marker en el mapa
        mostrarEdificioConMarker();
        //boton de maps que te ubica el barrio del edificio
        mostrarBarrioEdificio();
        //boton del maps que te despliega un gps que navega hasta el edificio
        navegarHastaEdificio();
        //muestra la foto del edificio en la vista
        mostrarFotoEdificio();
        //anuncio
        anuncio();
    }

    private void ocultarBotonesSegunPermisos() {
        if(permisosUser==1){
            borrarElemento.setVisibility(View.VISIBLE);
            editarElemento.setVisibility(View.VISIBLE);
        }else{
            borrarElemento.setVisibility(View.GONE);
            editarElemento.setVisibility(View.GONE);
        }
    }

    private void anuncio() {
        //AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    private void mostrarFotoEdificio() {
        try {
            Picasso.get().load(fotoIntent).into(verFotoEdificio);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_person_selected).into(verFotoEdificio);
        }
    }

    private void navegarHastaEdificio() {
        btnNavegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri=Uri.parse("google.navigation:q="+lat+","+longi+"&mode=d");
                intent=new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.google.android.apps.maps");
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }
            }

        });
    }

    private void mostrarBarrioEdificio() {
        btnVerMapaGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri= Uri.parse("geo:"+lat+","+longi);
                intent=new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.google.android.apps.maps");
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }
            }
        });
    }

    private void mostrarEdificioConMarker() {
        btnVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(VerEdificio.this, MapEdificio.class);
                intent.putExtra("latitud", lat);
                intent.putExtra("longitud", longi);
                intent.putExtra("nombre", titulo);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }

            }
        });
    }

    private void mostrarVistaPanoramica() {
        btnPanoramico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri= Uri.parse("google.streetview:cbll="+lat+","+longi);
                intent=new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.google.android.apps.maps");
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }
            }
        });
    }

    private void animacionBotonPrincipalMaps() {
        btnAnimacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationOpen();
            }
        });
    }

    private void borrarEdificioAccion() {
        borrarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(idEdificio).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(VerEdificio.this,"Elemento Borrado correctamente", Toast.LENGTH_SHORT).show();
                        intent=new Intent(VerEdificio.this, ListarMonumento.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerEdificio.this,"Error al borrar elemento", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });
    }


    private void intentAEditarElemento() {
        editarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent (VerEdificio.this, EditarEdificio.class);
                intent.putExtra("nombre", titulo);
                intent.putExtra("historia", descripcion);
                intent.putExtra("foto",fotoIntent);
                intent.putExtra("calle",calle);
                intent.putExtra("uid", idEdificio);
                intent.putExtra("latitud", lat);
                intent.putExtra("longitud", longi);
                intent.putExtra("permisos",permisosUser);

                startActivity(intent);
            }
        });
    }

    private void animationOpen(){
        if(isOpen){
            if(permisosUser==1){
                editarElemento.setVisibility(View.VISIBLE);
                borrarElemento.setVisibility(View.VISIBLE);
            }
            btnAnimacion.startAnimation(rotateForward);
            btnVerMapa.startAnimation(fabClose);
            btnVerMapa.setClickable(false);
            btnPanoramico.startAnimation(fabClose);
            btnPanoramico.setClickable(false);
            btnNavegar.startAnimation(fabClose);
            btnNavegar.setClickable(false);
            btnVerMapaGoogleMaps.startAnimation(fabClose);
            btnVerMapaGoogleMaps.setClickable(false);
            isOpen=false;
        }else{
            if(permisosUser==1){
                editarElemento.setVisibility(View.GONE);
                borrarElemento.setVisibility(View.GONE);
            }
            btnAnimacion.startAnimation(rotateBackward);
            btnVerMapa.startAnimation(fabOpen);
            btnVerMapa.setClickable(true);
            btnPanoramico.startAnimation(fabOpen);
            btnPanoramico.setClickable(true);
            btnNavegar.startAnimation(fabOpen);
            btnNavegar.setClickable(true);
            btnVerMapaGoogleMaps.startAnimation(fabOpen);
            btnVerMapaGoogleMaps.setClickable(true);
            isOpen=true;
        }
    }
}