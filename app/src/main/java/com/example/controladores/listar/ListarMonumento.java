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

import com.example.controladores.AdapterMonumentos.MonumentosAdapter;
import com.example.controladores.add.AnadirMonumento;
import com.example.controladores.ver.VerMonumento;
import com.example.just_scan.R;
import com.example.modelo.Monumento;
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

public class ListarMonumento extends AppCompatActivity  implements MonumentosAdapter.myViewHolder.onMonumentoListener{
    //AdMob
    private String tag ="Principal";
    private AdView mAdView;
    private AdRequest adRequest;
    //intent
    private Intent intent;
    //vistas
    private Button botonAnadirMonumento;
    private RecyclerView rv;
    private MonumentosAdapter adpt;
    private ArrayList<Monumento> listaMonumentos;
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
        setContentView(R.layout.activity_listar_monumento);
        //AdMob
        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //bd
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Usuarios");
        userId=user.getUid();
        referenceEdificios=FirebaseDatabase.getInstance().getReference("Monumentos");
        //vistas
        rv=(RecyclerView) findViewById(R.id.vistaMonumentos);
        buscador=findViewById(R.id.buscadorMonumentosItem);
        botonAnadirMonumento=findViewById(R.id.botonAnadirMonumentoAdmin);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listaMonumentos=new ArrayList<>();
        adpt= new MonumentosAdapter(this,listaMonumentos,this);
        rv.setAdapter(adpt);


        //redireccion a añadir monumento
        intentAnadirMonumento();
        //añadir monumentos de firebase a mi lista
        recorrerMonumentosEnfirebaseYAnadirALista();
        // leer datos del usuario de firebase
        recogerDatosUsuarioDeFirebase();
        //buscar monumento en el adapter
        buscarEnAdapterMonumentos();
        //AdMob
        anuncio();
    }
    //al clickar en un monumento me lleva a la vista de este y le paso como extras sus datos
    @Override
    public void clickEnMonumento(int position) {
        listaMonumentos.get(position);
        intent= new Intent(this, VerMonumento.class);
        intent.putExtra("nombre", listaMonumentos.get(position).getNombre());
        intent.putExtra("calle", listaMonumentos.get(position).getCalle());
        intent.putExtra("historia", listaMonumentos.get(position).getHistoria());
        intent.putExtra("foto",listaMonumentos.get(position).getFoto());
        intent.putExtra("uid", listaMonumentos.get(position).getuId());
        intent.putExtra("latitud", listaMonumentos.get(position).getLatitud());
        intent.putExtra("longitud", listaMonumentos.get(position).getLongitud());
        intent.putExtra("permisos",permisos);
        startActivity(intent);
    }

    private void intentAnadirMonumento() {
        botonAnadirMonumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(ListarMonumento.this, AnadirMonumento.class);
                startActivity(intent);
            }
        });
    }

    private void recorrerMonumentosEnfirebaseYAnadirALista() {
        referenceEdificios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Monumento monumento= dataSnapshot.getValue(Monumento.class);
                    listaMonumentos.add(monumento);
                }
                adpt.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recogerDatosUsuarioDeFirebase() {
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
                        botonAnadirMonumento.setVisibility(View.VISIBLE);
                    }else{
                        botonAnadirMonumento.setVisibility(View.INVISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void buscarEnAdapterMonumentos() {
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
        ArrayList<Monumento>monumentoBuscado=new ArrayList<>();

        for(Monumento monumento: listaMonumentos){
            if(monumento.getNombre().toLowerCase().contains(s.toLowerCase())){
                monumentoBuscado.add(monumento);
            }
        }
        adpt=new MonumentosAdapter(this,monumentoBuscado,this);
        rv.setAdapter(adpt);
    }
}

