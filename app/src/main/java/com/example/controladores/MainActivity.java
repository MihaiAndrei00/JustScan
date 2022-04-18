package com.example.controladores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.just_scan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button btnRegistro;
    private Button btnInicioSesion;
    private ConstraintLayout constraintLayout;
    private AnimationDrawable animationDrawable;
    private Intent intent;
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //variables para la animacion del main
        constraintLayout = findViewById(R.id.mainLayout);
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        //asocio los botones con sus vistas
        btnRegistro=findViewById(R.id.btnIniciarSesion);
        btnInicioSesion=findViewById(R.id.btnInicioSesion);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
         myRef = database.getReference("message");

        myRef.setValue("Base de datos");


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("JUST_SCAN_APP", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("JUST_SCAN_APP", "Failed to read value.", error.toException());
            }
        });



        //listener de los botones
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(MainActivity.this,LogRegistro.class);
                startActivity(intent);
            }
        });
        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(MainActivity.this,LogUsuario.class);
                startActivity(intent);
            }
        });
    }
}