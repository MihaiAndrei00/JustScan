package com.example.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controladores.AdapterRutas.MainAdapter;
import com.example.just_scan.R;
import com.example.modelo.Ruta;
import com.example.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class pruebaVisibilidad extends AppCompatActivity implements MainAdapter.myViewHolder.onRutaListener {
    private Button botonAnadir;
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference referenceRutas;
    private String userId;
    private String  fullName,email;
    private SearchView buscador;
    private int permisos;
    private String telefono;
    private Intent intent;
    private RecyclerView rv;
    private MainAdapter adpt;
    private ArrayList<Ruta>listaRutas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba_visibilidad);
        botonAnadir=findViewById(R.id.anadirRuta);
        rv=findViewById(R.id.vistaRutas);
        buscador=findViewById(R.id.buscadorVista);
        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Usuarios");
        userId=user.getUid();
        referenceRutas=FirebaseDatabase.getInstance().getReference("Rutas");
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listaRutas=new ArrayList<>();
        adpt= new MainAdapter(this,listaRutas,this);

        rv.setAdapter(adpt);


        referenceRutas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Ruta ruta= dataSnapshot.getValue(Ruta.class);
                    listaRutas.add(ruta);
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
                            botonAnadir.setVisibility(View.VISIBLE);
                        }else{
                            botonAnadir.setVisibility(View.INVISIBLE);
                        }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        intentAñadirRuta();
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
    private void intentAñadirRuta(){

        botonAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent= new Intent(pruebaVisibilidad.this, AnadirRuta.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void clickEnRuta(int position) {
        listaRutas.get(position);
        intent= new Intent(this, VerRuta.class);
        intent.putExtra("titulo", listaRutas.get(position).getTitulo());
        intent.putExtra("descripcion", listaRutas.get(position).getDescripcion());
        intent.putExtra("duracion", listaRutas.get(position).getDuración());
        startActivity(intent);
    }
    private void buscar(String s){
        ArrayList<Ruta>rutaBuscada=new ArrayList<>();

        for(Ruta ruta: listaRutas){
            if(ruta.getTitulo().toLowerCase().contains(s.toLowerCase())){
                rutaBuscada.add(ruta);
            }
        }
        adpt=new MainAdapter(this,rutaBuscada,this);
        rv.setAdapter(adpt);
    }
}

