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

import com.example.controladores.AdapterEdificios.EdificiosAdapter;
import com.example.controladores.add.AnadirEdificio;
import com.example.controladores.ver.VerEdificio;
import com.example.just_scan.R;
import com.example.modelo.Edificio;
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

public class ListarEdificio extends AppCompatActivity implements EdificiosAdapter.myViewHolder.onEdificioListener{
    //AdMob
    private String tag ="Principal";
    private AdView mAdView;
    private AdRequest adRequest;
    //intent
    private Intent intent;
    //vistas
    private Button botonAnadirEdificio;
    private RecyclerView rv;
    private EdificiosAdapter adpt;
    private ArrayList<Edificio> listaEdificios;
    //bd
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference referenceEdificios;
    //datos usuario
    private String userId;
    private String  fullName,email;
    private SearchView buscador;
    private int permisos;
    private String telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_edificio);
        //AdMob
        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //bd
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Usuarios");
        userId=user.getUid();
        referenceEdificios=FirebaseDatabase.getInstance().getReference("Edificios");
        //vistas
        rv=(RecyclerView) findViewById(R.id.vistaEdificios);
        buscador=findViewById(R.id.buscadorEdificios);
        botonAnadirEdificio=findViewById(R.id.botonAnadirEdificioAdmin);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listaEdificios=new ArrayList<>();
        adpt= new EdificiosAdapter(this,listaEdificios,this);
        rv.setAdapter(adpt);
        // intent hacia la pantalla de añadir edificios
        intentAnadirEdificios();
        //recorre los edificios de Firebase y los añade a mi lista
        anadirEdificiosDeFirebaseALista();
        // Read from the database
        leerDatosUsuarioFirebase();
        //metodod del buscador que busca por el nombre un edificio
        buscarElementoEnLista();
        //AdMob
        anuncio();
    }
    //metodo que al clickar en un edificio te lleva a la clase VerEdificio
    @Override
    public void clickEnEdificio(int position) {
        listaEdificios.get(position);
        intent= new Intent(this, VerEdificio.class);
        intent.putExtra("nombre", listaEdificios.get(position).getNombre());
        intent.putExtra("calle", listaEdificios.get(position).getCalle());
        intent.putExtra("historia", listaEdificios.get(position).getHistoria());
        intent.putExtra("foto",listaEdificios.get(position).getFoto());
        intent.putExtra("uid", listaEdificios.get(position).getuId());
        intent.putExtra("latitud", listaEdificios.get(position).getLatitud());
        intent.putExtra("longitud", listaEdificios.get(position).getLongitud());
        intent.putExtra("permisos",permisos);
        startActivity(intent);
    }
    private void  intentAnadirEdificios() {
        botonAnadirEdificio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(ListarEdificio.this, AnadirEdificio.class);
                startActivity(intent);
            }
        });
    }

    private void anadirEdificiosDeFirebaseALista() {
        referenceEdificios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Edificio edificio= dataSnapshot.getValue(Edificio.class);
                    listaEdificios.add(edificio);
                }
                adpt.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void leerDatosUsuarioFirebase() {
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
                        botonAnadirEdificio.setVisibility(View.VISIBLE);
                    }else{
                        botonAnadirEdificio.setVisibility(View.INVISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void buscarElementoEnLista() {
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



    private void anuncio() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }
    private void buscar(String s){
        ArrayList<Edificio>edificioBuscado=new ArrayList<>();

        for(Edificio edificio: listaEdificios){
            if(edificio.getNombre().toLowerCase().contains(s.toLowerCase())){
                edificioBuscado.add(edificio);
            }
        }
        adpt=new EdificiosAdapter(this,edificioBuscado,this);
        rv.setAdapter(adpt);
    }

}

