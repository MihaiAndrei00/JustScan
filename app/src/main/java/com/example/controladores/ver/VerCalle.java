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

import com.example.controladores.Principal;
import com.example.controladores.interfaces.ComunicaMenu;
import com.example.controladores.listar.ListarCalle;
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

public class VerCalle extends AppCompatActivity implements ComunicaMenu {
    //AdMob
    private String tag ="VerCalle";
    private AdView mAdView;
    private Intent intent;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child("Calles");
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    private String nombre, historia , fotoIntent, idCalle;
    private TextView tituloTv;
    private ImageView verFotoCalle;
    private TextView descripcionTv;
    private int permisosUser;
    private FloatingActionButton btnVerMapa, btnVerMapaGoogleMaps, btnNavegar, btnPanoramico, btnAnimacion,borrarElemento,editarElemento;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isOpen=false;
    private Uri uri;
    private double lat, longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ver_calle);
            //AdMob
            MobileAds.initialize(this);

            lat=getIntent().getDoubleExtra("latitud",lat);
            longi=getIntent().getDoubleExtra("longitud",longi);
            permisosUser=getIntent().getIntExtra("permisos",permisosUser);
            nombre=getIntent().getStringExtra("nombre");
            historia=getIntent().getStringExtra("historia");
            fotoIntent=getIntent().getStringExtra("foto");
            idCalle=getIntent().getStringExtra("uid");
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
            fabOpen= AnimationUtils.loadAnimation(this,R.anim.fab_open);
            fabClose=AnimationUtils.loadAnimation(this,R.anim.fab_close);
            rotateForward=AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
            rotateBackward=AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        if(permisosUser==1){
            borrarElemento.setVisibility(View.VISIBLE);
            editarElemento.setVisibility(View.VISIBLE);
        }else{
            borrarElemento.setVisibility(View.GONE);
            editarElemento.setVisibility(View.GONE);
        }

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

        btnAnimacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationOpen();
            }
        });

        btnPanoramico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri=Uri.parse("google.streetview:cbll="+lat+","+longi);
                intent=new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.google.android.apps.maps");
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }
            }
        });


        btnVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(VerCalle.this, MapCalle.class);
                intent.putExtra("latitud", lat);
                intent.putExtra("longitud", longi);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }

            }
        });

        btnVerMapaGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri=Uri.parse("geo:"+lat+","+longi);
                intent=new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.google.android.apps.maps");
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }
            }
        });

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

            try {
                Picasso.get().load(fotoIntent).into(verFotoCalle);
            }catch (Exception e){
                Picasso.get().load(R.drawable.ic_person_selected).into(verFotoCalle);
        }

        //AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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

    @Override
    public void menu(int QuebotonSePulsa) {
        intent= new Intent(this, Principal.class);
        startActivity(intent);
    }





}