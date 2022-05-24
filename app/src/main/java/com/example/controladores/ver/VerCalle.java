package com.example.controladores.ver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controladores.Principal;
import com.example.controladores.interfaces.ComunicaMenu;
import com.example.just_scan.R;
import com.example.maps.MapCalle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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
    private FloatingActionButton btnVerMapa, btnVerMapaGoogleMaps, btnNavegar, btnPanoramico;
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

    @Override
    public void menu(int QuebotonSePulsa) {
        intent= new Intent(this, Principal.class);
        startActivity(intent);
    }





}