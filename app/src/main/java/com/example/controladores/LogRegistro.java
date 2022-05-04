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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.just_scan.R;
import com.example.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LogRegistro extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtNombreUsuario;
    private EditText txtTelefono;
    private EditText txtContra;
    private Button btnRegistrar;
    private Intent intent;
    private int esAdmin;
    private ProgressBar progressBar;
    //llamamos a la base de datos
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_registro);

        //variables para la animacion del main
        /*
        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        */
        //EditText
        txtEmail=findViewById(R.id.txtEmail);
        txtNombreUsuario=findViewById(R.id.txtNombreUsuario);
        txtTelefono=findViewById(R.id.txtTelefono);
        txtContra=findViewById(R.id.txtContra);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        //botón
        btnRegistrar=findViewById(R.id.btnRegistrar);
        //creamos la instancia de la base de datos con la referencia de nuestro modelo, en este caso Usuario
        mAuth = FirebaseAuth.getInstance();
        //llamo al metodo para registrar
        registrar();
    }

    private void registrar(){
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= txtEmail.getText().toString();
                String nombreUsuario= txtNombreUsuario.getText().toString();
                String telefono= txtTelefono.getText().toString();
                String password= txtContra.getText().toString();
                esAdmin=0;
                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Usuario usuario= new Usuario(email,nombreUsuario,telefono,password, esAdmin);
                            FirebaseDatabase.getInstance().getReference("Usuarios").child(FirebaseAuth.
                                    getInstance().getCurrentUser().getUid()).
                                    setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(LogRegistro.this,"Usuario Registrado",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }else{
                                        Toast.makeText(LogRegistro.this,"Registro Fallido",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(LogRegistro.this,"Fallido",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    }
