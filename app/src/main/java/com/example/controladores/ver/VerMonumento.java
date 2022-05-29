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
import com.example.controladores.update.EditarMonumento;
import com.example.just_scan.R;
import com.example.maps.MapMonumento;
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

public class VerMonumento extends AppCompatActivity {
    //AdMob
    private String tag ="VerMonumento";
    private AdView mAdView;
    private AdRequest adRequest;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child("Momnumentos");
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    //datos monumento y permisos usuario
    private String nombre, descripcion , calle, fotoIntent,idMonumento;
    private int permisosUser;
    private double lat, longi;
    //vistas
    private TextView nombreTv;
    private ImageView verFotoMonumento;
    private TextView descripcionTv;
    private TextView calleTv;
    private FloatingActionButton btnVerMapa, btnVerMapaGoogleMaps, btnNavegar, btnPanoramico, btnAnimacion, borrarElemento,editarElemento;
    //animacion
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isOpen=false;
    private Uri uri;
    //intent
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_monumento);
        //AdMob
        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //datos monumento y permisos del usuario
        lat=getIntent().getDoubleExtra("latitud",lat);
        longi=getIntent().getDoubleExtra("longitud",longi);
        nombre=getIntent().getStringExtra("nombre");
        descripcion=getIntent().getStringExtra("historia");
        calle=getIntent().getStringExtra("calle");
        fotoIntent=getIntent().getStringExtra("foto");
        idMonumento=getIntent().getStringExtra("uid");
        permisosUser=getIntent().getIntExtra("permisos",permisosUser);
        //vistas
        nombreTv=findViewById(R.id.nombreMonumentoExtras);
        descripcionTv=findViewById(R.id.tvDescripcionMonumentoExtras);
        calleTv=findViewById(R.id.tvCalleMonumentoExtras);
        verFotoMonumento= findViewById(R.id.verFotoMonumentoExtras);
        nombreTv.setText(nombre);
        descripcionTv.setText(descripcion);
        calleTv.setText("Localización: " + calle);
        btnVerMapa=findViewById(R.id.mostrarEnMapaMonumento);
        btnVerMapaGoogleMaps=findViewById(R.id.mostrarEnGoogleMapsMonumento);
        btnNavegar=findViewById(R.id.btnNavegarMonumento);
        btnPanoramico=findViewById(R.id.btnPanoramicoMonumento);
        btnAnimacion=findViewById(R.id.btnAnimationMonumento);
        borrarElemento=findViewById(R.id.borrarMonumento);
        editarElemento=findViewById(R.id.EditarMonumento);
        //animacion
        fabOpen= AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose=AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotateForward=AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward=AnimationUtils.loadAnimation(this,R.anim.rotate_backward);
        //oculta botones de editar y eliminar segun los permisos del usaurios
        ocultarBotonesSegunPermisos();
        //intent que te lleva a Editar Elemento
        intentAEditarElemento();
        //borra el elemento
        accionBorrarElemento();
        //muestra u oculta los botones del maps
        animacionDelBotonPrincipalMaps();
        //muestra la vista panoramica del monumento
        mostrarVistaPanoramicaMonumento();
        //muestra el monumento en el maps con un marker
        verMonumentoMapsMarker();
        //muestra el barrio en el maps del monumento
        verBarrioMonumentoGoogle();
        //navega hasta la ubicacion del monumento con un gps
        navegarHaciaMonumentoGPS();
        //muestra la foto del monumento
        mostrarFotoMonumento();
        //banner anuncio
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

    private void mostrarFotoMonumento() {
        try {
            Picasso.get().load(fotoIntent).into(verFotoMonumento);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_person_selected).into(verFotoMonumento);
        }
    }

    private void navegarHaciaMonumentoGPS() {
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

    private void verBarrioMonumentoGoogle() {
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

    private void verMonumentoMapsMarker() {
        btnVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(VerMonumento.this, MapMonumento.class);
                intent.putExtra("latitud", lat);
                intent.putExtra("longitud", longi);
                intent.putExtra("nombre", nombre);

                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }

            }
        });
    }

    private void mostrarVistaPanoramicaMonumento() {
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

    private void animacionDelBotonPrincipalMaps() {
        btnAnimacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationOpen();
            }
        });
    }

    private void accionBorrarElemento() {
        borrarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(idMonumento).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(VerMonumento.this,"Elemento Borrado correctamente", Toast.LENGTH_SHORT).show();
                        intent=new Intent(VerMonumento.this, ListarMonumento.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerMonumento.this,"Error al borrar elemento", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });
    }

    private void intentAEditarElemento() {
        editarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent (VerMonumento.this, EditarMonumento.class);
                intent.putExtra("nombre", nombre);
                intent.putExtra("historia", descripcion);
                intent.putExtra("foto",fotoIntent);
                intent.putExtra("calle",calle);
                intent.putExtra("uid", idMonumento);
                intent.putExtra("latitud", lat);
                intent.putExtra("longitud", longi);
                intent.putExtra("permisos",permisosUser);

                startActivity(intent);
            }
        });
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