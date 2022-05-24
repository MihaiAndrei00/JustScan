package com.example.controladores.listar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controladores.AdapterCalles.CallesAdapter;
import com.example.controladores.add.AnadirCalle;
import com.example.controladores.ver.VerCalle;
import com.example.just_scan.R;
import com.example.modelo.Calle;
import com.example.modelo.Usuario;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListarCalle extends AppCompatActivity  implements CallesAdapter.myViewHolder.onCalleListener{
    //AdMob
    private String tag ="Principal";
    private AdView mAdView;
    private Button botonAnadirCalle;
    private Intent intent;
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference referenceCalles;
    private String userId;
    private String  fullName,email;
    private SearchView buscador;
    private int permisos;
    private String telefono;
    private RecyclerView rv;
    private CallesAdapter adpt;
    private ArrayList<Calle> listaCalles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_calle);
        //AdMob
        MobileAds.initialize(this);
        rv=(RecyclerView) findViewById(R.id.vistaCalle);
        buscador=findViewById(R.id.buscadorCalle);
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Usuarios");
        userId=user.getUid();
        referenceCalles=FirebaseDatabase.getInstance().getReference("Calles");
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listaCalles=new ArrayList<>();
        adpt= new CallesAdapter(this,listaCalles,this);
        rv.setAdapter(adpt);
        botonAnadirCalle=findViewById(R.id.botonAnadirCalleAdmin);
        botonAnadirCalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(ListarCalle.this, AnadirCalle.class);
                startActivity(intent);
            }
        });
        referenceCalles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Calle calle= dataSnapshot.getValue(Calle.class);
                    listaCalles.add(calle);
                }
                adpt.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Read from the database
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario userProfile= snapshot.getValue(Usuario.class);
                if(userProfile!=null){
                    fullName= userProfile.getNombreUsuario();
                    email=userProfile.getEmail();
                    permisos=userProfile.getEsAdmin();
                    telefono=userProfile.getTelefono();
                    if(permisos==1){
                        botonAnadirCalle.setVisibility(View.VISIBLE);
                    }else{
                        botonAnadirCalle.setVisibility(View.INVISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscar(newText);
                return false;
            }
        });

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
    public void clickEnCalle(int position) {
        listaCalles.get(position);
        intent= new Intent(this, VerCalle.class);
        intent.putExtra("nombre", listaCalles.get(position).getNombre());
        intent.putExtra("historia", listaCalles.get(position).getHistoria());
        intent.putExtra("foto", listaCalles.get(position).getFoto());
        intent.putExtra("uid", listaCalles.get(position).getuId());
        intent.putExtra("latitud", listaCalles.get(position).getLatitud());
        intent.putExtra("longitud", listaCalles.get(position).getLongitud());
        startActivity(intent);
    }
    private void buscar(String s){
        ArrayList<Calle>calleBuscada=new ArrayList<>();

        for(Calle calle: listaCalles){
            if(calle.getNombre().toLowerCase().contains(s.toLowerCase())){
                calleBuscada.add(calle);
            }
        }
        adpt=new CallesAdapter(this,calleBuscada,this);
        rv.setAdapter(adpt);
    }

}


