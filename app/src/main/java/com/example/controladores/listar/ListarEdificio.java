package com.example.controladores.listar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controladores.add.AnadirEdificio;
import com.example.just_scan.R;

public class ListarEdificio extends AppCompatActivity {
    private ImageButton botonListarEdificio;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_edificio);
        botonListarEdificio=findViewById(R.id.botonAnadirCalleAdmin);
        botonListarEdificio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(ListarEdificio.this, AnadirEdificio.class);
                startActivity(intent);
            }
        });
    }
}