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
    private Intent intent;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child("Edificios");
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    private String titulo, descripcion , calle, fotoIntent,idEdificio;
    private TextView nombreTv;
    private ImageView verFotoEdificio;
    private TextView descripcionTv;
    private TextView calleTv;
    private int permisosUser;
    private FloatingActionButton btnVerMapa, btnVerMapaGoogleMaps, btnNavegar, btnPanoramico, btnAnimacion,borrarElemento,editarElemento;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isOpen=false;
    private Uri uri;
    private double lat, longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_edificio);
        //AdMob
        MobileAds.initialize(this);
        lat=getIntent().getDoubleExtra("latitud",lat);
        longi=getIntent().getDoubleExtra("longitud",longi);
        titulo=getIntent().getStringExtra("nombre");
        descripcion=getIntent().getStringExtra("historia");
        calle=getIntent().getStringExtra("calle");
        fotoIntent=getIntent().getStringExtra("foto");
        idEdificio=getIntent().getStringExtra("uid");
        permisosUser=getIntent().getIntExtra("permisos",permisosUser);
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
        btnAnimacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationOpen();
            }
        });
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
        btnVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(VerEdificio.this, MapEdificio.class);
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
            Picasso.get().load(fotoIntent).into(verFotoEdificio);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_person_selected).into(verFotoEdificio);
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