package com.example.controladores.listar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controladores.add.AnadirMonumento;
import com.example.just_scan.R;

public class ListarMonumento extends AppCompatActivity {
    private ImageButton botonListarMonumento;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_monumento);
        botonListarMonumento=findViewById(R.id.botonAnadirMonumentoAdmin);
        botonListarMonumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(ListarMonumento.this, AnadirMonumento.class);
                startActivity(intent);
            }
        });
    }
}

