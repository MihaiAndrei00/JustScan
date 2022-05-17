package com.example.controladores.listar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controladores.add.AnadirRestaurante;
import com.example.just_scan.R;

public class ListarRestaurante extends AppCompatActivity {
    private ImageButton botonAnadirRestaurante;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_restaurante);
        botonAnadirRestaurante=findViewById(R.id.botonAnadirRestauranteAdmin);
        botonAnadirRestaurante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(ListarRestaurante.this, AnadirRestaurante.class);
                startActivity(intent);
            }
        });
    }
}
