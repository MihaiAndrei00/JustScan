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

import com.example.controladores.listar.ListarRestaurante;
import com.example.controladores.update.EditarRestaurante;
import com.example.just_scan.R;
import com.example.maps.MapRestaurante;
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

public class VerRestaurante extends AppCompatActivity {
    //AdMob
    private String tag ="VerRestaurante";
    private AdView mAdView;
    private AdRequest adRequest;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child("Restaurantes");
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    //permisos user y datos restaurante
    private String nombre, descripcion , calle, fotoIntent,idRestaurante,tipoDeComida;
    private int permisosUser;
    private double lat, longi;
    //intent
    private Intent intent;
    //vistas
    private TextView nombreTv;
    private TextView tipoDeComidaTv;
    private ImageView verFotoRestaurante;
    private TextView descripcionTv;
    private TextView calleTv;
    private FloatingActionButton btnVerMapa, btnVerMapaGoogleMaps, btnNavegar, btnPanoramico, btnAnimacion, borrarElemento,editarElemento;
    //animacion
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isOpen=false;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_restaurante);
        //AdMob
        MobileAds.initialize(this); mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //permisos usuario y datos restaurante
        permisosUser=getIntent().getIntExtra("permisos",permisosUser);
        nombre=getIntent().getStringExtra("nombre");
        descripcion=getIntent().getStringExtra("historia");
        calle=getIntent().getStringExtra("calle");
        fotoIntent=getIntent().getStringExtra("foto");
        idRestaurante=getIntent().getStringExtra("uid");
        tipoDeComida=getIntent().getStringExtra("comida");
        lat=getIntent().getDoubleExtra("latitud",lat);
        longi=getIntent().getDoubleExtra("longitud",longi);
        //vistas
        nombreTv=findViewById(R.id.nombreRestauranteExtras);
        descripcionTv=findViewById(R.id.tvDescripcionRestauranteExtras);
        calleTv=findViewById(R.id.tvCalleRestauranteExtras);
        verFotoRestaurante= findViewById(R.id.verFotoRestauranteExtras);
        tipoDeComidaTv=findViewById(R.id.tipoDeComidaExtras);
        nombreTv.setText(nombre);
        descripcionTv.setText(descripcion);
        calleTv.setText("Localización: " + calle);
        tipoDeComidaTv.setText(tipoDeComida);
        btnVerMapa=findViewById(R.id.mostrarEnMapa);
        btnVerMapaGoogleMaps=findViewById(R.id.mostrarEnGoogleMaps);
        btnNavegar=findViewById(R.id.btnNavegar);
        btnPanoramico=findViewById(R.id.btnPanoramico);
        btnAnimacion=findViewById(R.id.btnAnimationRestaurante);
        borrarElemento=findViewById(R.id.borrarRestaurante);
        editarElemento=findViewById(R.id.EditarRestaurante);
        //animacion
        fabOpen= AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose=AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotateForward=AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward=AnimationUtils.loadAnimation(this,R.anim.rotate_backward);
        //oculta o muestra botones de borrar y editar segun los permisos
        ocultarBotonesSegunPermisos();
        //redirige a la pantalla de editar el elemento
        intentAEditarElemento();
        //borra el elemento
        accionBorrarElemento();
        //muestra u oculta botones del maps al clickar
        animacionBotonPrincipalMaps();
        //vista panoramica del restaurante
        vistaPanoramicaRestauranteMaps();
        //muestra con un marker la ubicacion del restaurante
        mostrarEnMapaConMarker();
        //muestra en el mapa el barrio del restaurante
        mostrarBarrioEnGoogleMaps();
        //navega con el gps hasta el restaurante
        navegarHastaRestauranteGPS();
        //muestra la foto del restaurante
        mostrarFotoRestaurante();
        //banner del anuncio
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

    private void mostrarFotoRestaurante() {
        try {
            Picasso.get().load(fotoIntent).into(verFotoRestaurante);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_person_selected).into(verFotoRestaurante);
        }
    }

    private void navegarHastaRestauranteGPS() {
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

    private void mostrarBarrioEnGoogleMaps() {
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

    private void mostrarEnMapaConMarker() {
        btnVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(VerRestaurante.this, MapRestaurante.class);
                intent.putExtra("latitud", lat);
                intent.putExtra("longitud", longi);
                intent.putExtra("nombre", nombre);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }
            }
        });
    }

    private void vistaPanoramicaRestauranteMaps() {
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

    private void accionBorrarElemento() {
        borrarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(idRestaurante).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(VerRestaurante.this,"Elemento Borrado correctamente", Toast.LENGTH_SHORT).show();
                        intent=new Intent(VerRestaurante.this, ListarRestaurante.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerRestaurante.this,"Error al borrar elemento", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });
    }

    private void intentAEditarElemento() {
        editarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent (VerRestaurante.this, EditarRestaurante.class);
                intent.putExtra("nombre", nombre);
                intent.putExtra("calle", calle);
                intent.putExtra("historia", descripcion);
                intent.putExtra("comida", tipoDeComida);
                intent.putExtra("foto",fotoIntent);
                intent.putExtra("uid", idRestaurante);
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
            btnVerMapa.setClickable(true);
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
            btnVerMapa.setClickable(false);
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