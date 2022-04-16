package com.example.controladores;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.just_scan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogUsuario extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtContra;
    private Button btnRegistro;
    private Intent intent;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_usuario);

        //variables para la animacion del main
        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();


        txtEmail=findViewById(R.id.txtEmail);
        txtContra=findViewById(R.id.txtContra);

        btnRegistro=findViewById(R.id.btnRegistro);

        mAuth=FirebaseAuth.getInstance();

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= txtEmail.getText().toString();
                String contrasena=txtContra.getText().toString();


                mAuth.signInWithEmailAndPassword(email,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            intent= new Intent(LogUsuario.this,home.class);
                            startActivity(intent);

                        }
                    }
                });

            }
        });

    }


}
