package com.example.controladores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.just_scan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LogCambiarContra extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    AnimationDrawable animationDrawable;

    private EditText txtEmail;
    private FirebaseAuth mAuth;
    private Button btnCambiarContra;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_cambiar_contra);

        //variables para la animacion del main
        constraintLayout = findViewById(R.id.mainLayout);
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        //declaro vistas
        txtEmail=findViewById(R.id.txtEmail);
        btnCambiarContra=findViewById(R.id.btnIniciarSesion);
        mAuth=FirebaseAuth.getInstance();

        btnCambiarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }
    private void resetPassword(){
        String email= txtEmail.getText().toString();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LogCambiarContra.this,"Revisa tu email para el cambio de contraseña",Toast.LENGTH_LONG).show();
                    intent= new Intent(LogCambiarContra.this,LogUsuario.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LogCambiarContra.this,"Error al cambiar la contraseña",Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}