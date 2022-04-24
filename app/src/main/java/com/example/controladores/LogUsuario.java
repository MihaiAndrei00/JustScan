package com.example.controladores;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.just_scan.Home;
import com.example.just_scan.R;
import com.example.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogUsuario extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtContra;
    private Button btnInicioSesion;
    private TextView tvContra;
    private TextView tvRegistro;
    private ProgressBar progressBarLogIn;
    private ConstraintLayout constraintLayout;
    private AnimationDrawable animationDrawable;
    private Intent intent;

    //instancia del FirebaseAuth que nos proporciona diferentes metodos de autorización de firebase
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_usuario);

        //variables para la animacion del main
        constraintLayout = findViewById(R.id.mainLayout);

        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        //edittext
        txtEmail=findViewById(R.id.txtEmail);
        txtContra=findViewById(R.id.txtContra);
        //progressbar
        progressBarLogIn=findViewById(R.id.progressBarLogin);
        progressBarLogIn.setVisibility(View.GONE);
         //boton
        btnInicioSesion=findViewById(R.id.btnIniciarSesion);
        //textViews
        tvContra=findViewById(R.id.olvidarContra);
        tvRegistro=findViewById(R.id.tvRegistro);
        mAuth=FirebaseAuth.getInstance();

        //llamada al método de inicio de sesión
        iniciarSesion();

        //llamada al metodo de recuperar la contraseña
        linkArecuperarContrasena();

        //llamada al metodo de registro
        linkARegistro();


    }
    private void iniciarSesion(){

        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= txtEmail.getText().toString();
                String contrasena=txtContra.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    txtEmail.setError("Introduzca una dirección de email válida");
                    txtEmail.requestFocus();
                    return;
                }
                progressBarLogIn.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBarLogIn.setVisibility(View.GONE);
                            intent= new Intent(LogUsuario.this, Home.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LogUsuario.this,"Email o contraseña incorrectos",Toast.LENGTH_LONG).show();
                            progressBarLogIn.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
    }
    private void linkArecuperarContrasena(){
        tvContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(LogUsuario.this, LogCambiarContra.class);
                startActivity(intent);
            }
        });
    }

    private void linkARegistro(){
        tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(LogUsuario.this, LogRegistro.class);
                startActivity(intent);
            }
        });
    }
}

