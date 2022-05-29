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
    private AdRequest adRequest;
    //Intent
    private Intent intent;
    //vistas
    private SearchView buscador;
    private RecyclerView rv;
    private CallesAdapter adpt;
    private Button botonAnadirCalle;
    //bd
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference referenceCalles;
    //datos usuario
    private String userId;
    private String  fullName,email;
    private int permisos;
    private String telefono;
    //lista de calles
    private ArrayList<Calle> listaCalles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_calle);
        //AdMob
        MobileAds.initialize(this);
        adRequest = new AdRequest.Builder().build();
        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);
        //bd
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Usuarios");
        rv=(RecyclerView) findViewById(R.id.vistaCalle);
        userId=user.getUid();
        referenceCalles=FirebaseDatabase.getInstance().getReference("Calles");
        //vistas
        buscador=findViewById(R.id.buscadorCalle);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listaCalles=new ArrayList<>();
        adpt= new CallesAdapter(this,listaCalles,this);
        rv.setAdapter(adpt);
        botonAnadirCalle=findViewById(R.id.botonAnadirCalleAdmin);

        //intnet que lleva al formulario de añadir calle
        irAnadirCalle();
        //metodo que recorre  las calles de Firebase y las añade a mi lista de calles
        recorrerCallesYAnadirALista();
        // metodo que recoge los datos del usuario de firebase
        recogerDatosDelUsuarioDeFirebase();
        //aplico el setOnQueryListener al buscador
        aplicarListenerBuscar();
        //AdMob
        anuncio();
    }
    //Al clickar en una calle te lleva al activity para ver esa calle y le paso como extras los atributos de la calle
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
        intent.putExtra("permisos", permisos);
        startActivity(intent);
    }
    private void recorrerCallesYAnadirALista() {
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
    }

    private void irAnadirCalle() {
        botonAnadirCalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(ListarCalle.this, AnadirCalle.class);
                startActivity(intent);
            }
        });
    }

    private void anuncio() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    private void aplicarListenerBuscar() {
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
    }

    private void recogerDatosDelUsuarioDeFirebase() {
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


