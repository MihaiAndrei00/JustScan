package com.example.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.just_scan.Home;
import com.example.just_scan.R;
import com.example.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class pruebaVisibilidad extends AppCompatActivity {
   private Button botonAnadir;
   private TextView permisostv;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private String  fullName,email;
    private int permisos;
    private String telefono;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba_visibilidad);
        botonAnadir=findViewById(R.id.anadirRuta);
        permisostv=findViewById(R.id.txtViewPermisos);
        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Usuarios");
        userId=user.getUid();

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
                        permisostv.setText(fullName);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        intentAñadirRuta();
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
}

