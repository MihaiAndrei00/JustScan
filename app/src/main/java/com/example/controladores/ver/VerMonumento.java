package com.example.controladores.ver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

public class VerMonumento extends AppCompatActivity {
    //AdMob
    private String tag ="VerMonumento";
    private AdView mAdView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child("Momnumentos");
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    private String nombre, descripcion , calle, fotoIntent,idMonumento;
    private TextView nombreTv;
    private ImageView verFotoMonumento;
    private TextView descripcionTv;
    private TextView calleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_monumento);
        //AdMob
        MobileAds.initialize(this);
        nombre=getIntent().getStringExtra("nombre");
        descripcion=getIntent().getStringExtra("historia");
        calle=getIntent().getStringExtra("calle");
        fotoIntent=getIntent().getStringExtra("foto");
        idMonumento=getIntent().getStringExtra("uid");
        nombreTv=findViewById(R.id.nombreMonumentoExtras);
        descripcionTv=findViewById(R.id.tvDescripcionMonumentoExtras);
        calleTv=findViewById(R.id.tvCalleMonumentoExtras);
        verFotoMonumento= findViewById(R.id.verFotoMonumentoExtras);
        nombreTv.setText(nombre);
        descripcionTv.setText(descripcion);
        calleTv.setText("Localización: " + calle);
        try {
            Picasso.get().load(fotoIntent).into(verFotoMonumento);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_person_selected).into(verFotoMonumento);
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
}