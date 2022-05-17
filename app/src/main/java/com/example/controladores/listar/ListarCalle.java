package com.example.controladores.listar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controladores.add.AnadirCalle;
import com.example.just_scan.R;

public class ListarCalle extends AppCompatActivity {
    private ImageButton botonListarCalle;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_calle);

        botonListarCalle=findViewById(R.id.botonAnadirCalleAdmin);
        botonListarCalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(ListarCalle.this, AnadirCalle.class);
                startActivity(intent);
            }
        });
    }
}

