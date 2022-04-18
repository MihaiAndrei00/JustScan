package com.example.controladores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.just_scan.R;

public class Home extends AppCompatActivity {
    private TextView tvCorreo;
    private Intent recuperarCorreo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tvCorreo=findViewById(R.id.tvCorreo);
        recuperarCorreo=getIntent();
        Bundle b=recuperarCorreo.getExtras();

        if(b!=null){
            String j = b.getString("correo");
            tvCorreo.setText(j);
        }
    }
}