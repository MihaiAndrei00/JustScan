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

import com.example.controladores.listar.ListarCalle;
import com.example.controladores.update.EditarCalle;
import com.example.just_scan.R;
import com.example.maps.MapCalle;
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

public class VerCalle extends AppCompatActivity  {
    //AdMob
    private String tag ="VerCalle";
    private AdView mAdView;
    private AdRequest adRequest;
    //vistas
    private FloatingActionButton btnVerMapa, btnVerMapaGoogleMaps, btnNavegar, btnPanoramico, btnAnimacion,borrarElemento,editarElemento;
    private TextView tituloTv;
    private ImageView verFotoCalle;
    private TextView descripcionTv;
    //intent
    private Intent intent;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child("Calles");
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    //datos calles y permisos usuario
    private String nombre, historia , fotoIntent, idCalle;
    private int permisosUser;
    private double lat, longi;
    //animacion
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isOpen=false;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ver_calle);
            //AdMob
            MobileAds.initialize(this);
            mAdView = findViewById(R.id.adView);
            adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            //datos y permisos usuario
            lat=getIntent().getDoubleExtra("latitud",lat);
            longi=getIntent().getDoubleExtra("longitud",longi);
            permisosUser=getIntent().getIntExtra("permisos",permisosUser);
            nombre=getIntent().getStringExtra("nombre");
            historia=getIntent().getStringExtra("historia");
            fotoIntent=getIntent().getStringExtra("foto");
            idCalle=getIntent().getStringExtra("uid");
            //vistas
            tituloTv=findViewById(R.id.tituloCalleExtras);
            descripcionTv=findViewById(R.id.tvDescripcionCalleExtras);
            verFotoCalle=findViewById(R.id.verFotoCalleExtras);
            tituloTv.setText(nombre);
            descripcionTv.setText(historia);
            btnPanoramico=findViewById(R.id.btnPanoramicoCalle);
            btnNavegar=findViewById(R.id.btnNavegarCalle);
            btnVerMapaGoogleMaps=findViewById(R.id.mostrarEnGoogleMapsCalle);
            btnVerMapa=findViewById(R.id.mostrarEnMapaCalle);
            btnAnimacion=findViewById(R.id.btnAnimationCalle);
            borrarElemento=findViewById(R.id.borrarCalle);
            editarElemento=findViewById(R.id.EditarCalle);
            //anim
            fabOpen= AnimationUtils.loadAnimation(this,R.anim.fab_open);
            fabClose=AnimationUtils.loadAnimation(this,R.anim.fab_close);
            rotateForward=AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
            rotateBackward=AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

            //oculta los botones de editar y eliminar segun los permisos del usuario
            ocultarBotonSegunPermisos();
            //boton de maps que te ofrece vista panoramica de las calles
            mostrarCallePanoramica();
            //muestra una vision del barrio de la calle
            mostrarBarrioGoogleMaps();
            //te ubica esa calle en el mapa con un marker
            mostrarUbicacionEnMapa();
            //despliega el gps de google maps y navega hasta esa calle
            accionNavegarCalle();
            //boton que te despliega u oculta los botones del maps
            animacionBotonPrincipalMapas();
            //intent que dirige a editar esa calle
            intentAEditarElemento();
            //borra esa calle
            borrarElementoAccion();
            //muestra la foto de la calle
            recogerIntentYPonerFoto();
            //muestra el anuncio
            anuncio();

    }

    private void anuncio() {
        //AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    private void recogerIntentYPonerFoto() {
        try {
                Picasso.get().load(fotoIntent).into(verFotoCalle);
            }catch (Exception e){
                Picasso.get().load(R.drawable.ic_person_selected).into(verFotoCalle);
            }
    }

    private void mostrarCallePanoramica() {
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

    private void mostrarBarrioGoogleMaps() {
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

    private void mostrarUbicacionEnMapa() {
        btnVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(VerCalle.this, MapCalle.class);
                intent.putExtra("latitud", lat);
                intent.putExtra("longitud", longi);
                intent.putExtra("nombre", nombre);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }

            }
        });
    }

    private void accionNavegarCalle() {
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

    private void animacionBotonPrincipalMapas() {
        btnAnimacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationOpen();
            }
        });
    }

    private void borrarElementoAccion() {
        borrarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(idCalle).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(VerCalle.this,"Elemento Borrado correctamente", Toast.LENGTH_SHORT).show();
                        intent=new Intent(VerCalle.this, ListarCalle.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerCalle.this,"Error al borrar elemento", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });
    }

    private void intentAEditarElemento() {
        editarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent (VerCalle.this, EditarCalle.class);
                intent.putExtra("nombre", nombre);
                intent.putExtra("historia", historia);
                intent.putExtra("foto",fotoIntent);
                intent.putExtra("uid", idCalle);
                intent.putExtra("latitud", lat);
                intent.putExtra("longitud", longi);
                intent.putExtra("permisos",permisosUser);

                startActivity(intent);
            }
        });
    }

    private void ocultarBotonSegunPermisos() {
        if(permisosUser==1){
            borrarElemento.setVisibility(View.VISIBLE);
            editarElemento.setVisibility(View.VISIBLE);
        }else{
            borrarElemento.setVisibility(View.GONE);
            editarElemento.setVisibility(View.GONE);
        }
    }

    private void animationOpen() {
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