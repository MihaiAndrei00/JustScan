package com.example.controladores;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.just_scan.R;
import com.example.modelo.Ruta;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AnadirRuta extends AppCompatActivity {
    //vistas
    private EditText tituloRuta;
    private EditText descripcionRuta;
    private EditText duracionRuta;
    private Button anadirRuta;
    //bd
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private Ruta ruta;

    //declaramos la valoracion a null ya que mas adelan
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anadir_ruta);
        tituloRuta = findViewById(R.id.tituloRutaVista);
        descripcionRuta = findViewById(R.id.descripcionRutaVista);
        duracionRuta = findViewById(R.id.duracionRutaVista);
        anadirRuta = findViewById(R.id.agregarRuta);
        //Obtenemos la referencia de nuestra bd
        myRef = database.getReference();
        //llamo al metodo para registrar
        registrarRuta();
    }

    private void registrarRuta() {
        anadirRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titulo = tituloRuta.getText().toString();
                String descripcion = descripcionRuta.getText().toString();
                String duracion = duracionRuta.getText().toString();
                String foto = "https://firebasestorage.googleapis.com/v0/b/justscan-c5c3e.appspot.com/o/fotoRutas%2Flogo.png?alt=media&token=8fd56909-e3f5-463b-be59-cc106a09bb8e";
                String uId = UUID.randomUUID().toString();
                Ruta ruta = new Ruta(uId, titulo, descripcion, duracion, foto);
                myRef.child("Rutas").child(ruta.getuId()).setValue(ruta);
            }
        });
    }
}


