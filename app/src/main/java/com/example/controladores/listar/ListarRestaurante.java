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

import com.example.controladores.AdapterRestaurantes.RestaurantesAdapter;
import com.example.controladores.add.AnadirRestaurante;
import com.example.controladores.ver.VerRestaurante;
import com.example.just_scan.R;
import com.example.modelo.Restaurante;
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

public class ListarRestaurante extends AppCompatActivity implements RestaurantesAdapter.myViewHolder.onRestauranteListener{
    //AdMob
    private String tag ="Principal";
    private AdView mAdView;
    private AdRequest adRequest;
    //intent
    private Intent intent;
    //vistas
    private Button botonAnadirRestaurante;
    private SearchView buscador;
    private RecyclerView rv;
    private RestaurantesAdapter adpt;
    //bd
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference referenceRestaurantes;
    //datos usuarios
    private String userId;
    private String  fullName,email;
    private int permisos;
    private String telefono;
    //Lista restaurantes
    private ArrayList<Restaurante> listaRestaurantes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_restaurante);
        //AdMob
        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //bd
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Usuarios");
        userId=user.getUid();
        referenceRestaurantes=FirebaseDatabase.getInstance().getReference("Restaurantes");
        //vistas
        rv=(RecyclerView) findViewById(R.id.vistaRestaurantes);
        buscador=findViewById(R.id.buscadorRestaurantes);
        botonAnadirRestaurante=findViewById(R.id.botonAnadirRestauranteAdmin);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listaRestaurantes=new ArrayList<>();
        adpt= new RestaurantesAdapter(this,listaRestaurantes,this);
        rv.setAdapter(adpt);
        //intent a la activity añadirRestaurante
        intentAnadirRestaurante();
        //Recorre firebase y añade restaurantes a mi lista
        anadirRestauranteDeFirebaseALista();
        // recupera los datos del usuario de firebase
        recuperarDatosDeUsuario();
        //busca en el recycler el restaurante por su nombre
        buscarEnRecyclerRestaurantes();
        //AdMob
        anuncio();
    }
    //al clickar en un monumento me lleva a la vista de este y le paso como extras sus datos
    @Override
    public void clickEnRestaurante(int position) {
        listaRestaurantes.get(position);
        intent= new Intent(this, VerRestaurante.class);
        intent.putExtra("nombre", listaRestaurantes.get(position).getNombre());
        intent.putExtra("calle", listaRestaurantes.get(position).getCalle());
        intent.putExtra("historia", listaRestaurantes.get(position).getDescripcion());
        intent.putExtra("comida", listaRestaurantes.get(position).getTipoDeComida());
        intent.putExtra("foto",listaRestaurantes.get(position).getFoto());
        intent.putExtra("uid", listaRestaurantes.get(position).getuId());
        intent.putExtra("latitud", listaRestaurantes.get(position).getLatitud());
        intent.putExtra("longitud", listaRestaurantes.get(position).getLongitud());
        intent.putExtra("permisos",permisos);
        startActivity(intent);
    }

    private void anuncio() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    private void buscarEnRecyclerRestaurantes() {
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

    private void recuperarDatosDeUsuario() {
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
                        botonAnadirRestaurante.setVisibility(View.VISIBLE);
                    }else{
                        botonAnadirRestaurante.setVisibility(View.INVISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void anadirRestauranteDeFirebaseALista() {
        referenceRestaurantes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Restaurante restaurante= dataSnapshot.getValue(Restaurante.class);
                    listaRestaurantes.add(restaurante);
                }
                adpt.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void intentAnadirRestaurante() {
        botonAnadirRestaurante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(ListarRestaurante.this, AnadirRestaurante.class);
                startActivity(intent);
            }
        });
    }

    private void buscar(String s){
        ArrayList<Restaurante>restauranteBuscado=new ArrayList<>();
        for(Restaurante restaurante: listaRestaurantes){
            if(restaurante.getNombre().toLowerCase().contains(s.toLowerCase())){
                restauranteBuscado.add(restaurante);
            }
        }
        adpt=new RestaurantesAdapter(this,restauranteBuscado,this);
        rv.setAdapter(adpt);
    }
}
