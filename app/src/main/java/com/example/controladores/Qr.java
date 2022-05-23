package com.example.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.example.just_scan.R;

public class Qr extends AppCompatActivity {
    private Button btn_escanear;
    private TextView txt_res;
    //ayuda la integracion de los escaneos mediante intents y recibe un resultado
    private IntentIntegrator integrador;
    //el IntentResult es el resultado que recibimos del codigo qr o de barras
    private IntentResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        btn_escanear=findViewById(R.id.btn_escanear);
        txt_res=findViewById(R.id.txt_res);

        btn_escanear.setOnClickListener(view -> {
            integrador=new IntentIntegrator(Qr.this);
            //personalizamos el lector

            //le establezco los tipos de codigos que puede leer , que le he estabecido a todos
            integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            //titulo que aparece en el lector
            integrador.setPrompt("Escanealo!");
            // se refiere a que camara va a usar (la 0 es la trasera)
            integrador.setCameraId(0);
            //hace un sonido cuando lea el codigo
            integrador.setBeepEnabled(true);
            //para que pueda leer codigos de barras
            integrador.setBarcodeImageEnabled(true);
            //para que se inicie el escaneo
            integrador.initiateScan();

        });
    }

    //
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        //creamos un obejto de tipo IntentResult que le pasamos lo que recibimos en el metodo
        result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        //le indicamos que si el result no es nulo recibes informacion del qr o codigo de barras
        if (result!=null){
            if (result.getContents()==null){
                Toast.makeText(this, "Lectura cancelada", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                txt_res.setText(result.getContents());
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);

        }
    }
}