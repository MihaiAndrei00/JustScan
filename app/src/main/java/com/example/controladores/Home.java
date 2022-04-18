package com.example.controladores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.just_scan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
    private TextView tvCorreo,tvPrueba;
    private Intent recuperarCorreo;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tvCorreo=findViewById(R.id.tvCorreo);
        tvPrueba=findViewById(R.id.tvPrueba);
        recuperarCorreo=getIntent();
        Bundle b=recuperarCorreo.getExtras();

        if(b!=null){
            String j = b.getString("correo");
            tvCorreo.setText(j);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d("email", user.getProviderId() );
        } else {
            // No user is signed in
        }
    }

}