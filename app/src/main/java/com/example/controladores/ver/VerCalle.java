package com.example.controladores.ver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.controladores.Principal;
import com.example.controladores.interfaces.ComunicaMenu;
import com.example.just_scan.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ver_calle);
            //AdMob
            MobileAds.initialize(this);
            nombre=getIntent().getStringExtra("nombre");
            historia=getIntent().getStringExtra("historia");
            fotoIntent=getIntent().getStringExtra("foto");
            idCalle=getIntent().getStringExtra("uid");
            tituloTv=findViewById(R.id.tituloCalleExtras);
            descripcionTv=findViewById(R.id.tvDescripcionCalleExtras);
            verFotoCalle=findViewById(R.id.verFotoCalleExtras);
            tituloTv.setText(nombre);
            descripcionTv.setText(historia);
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