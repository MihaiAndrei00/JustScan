package com.example.controladores.AdapterRutas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.controladores.pruebaVisibilidad;
import com.example.just_scan.R;
import com.example.modelo.Ruta;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.myViewHolder>{
    Context context;
    ArrayList<Ruta>rutas;

    public MainAdapter(Context context, ArrayList<Ruta> listaRutas) {
        this.context=context;
        this.rutas=listaRutas;
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView titulo;
        TextView duracion;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.imgRuta);
            titulo=(TextView) itemView.findViewById(R.id.titulo);
            duracion=(TextView) itemView.findViewById(R.id.duracionDeRuta);


        }

    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Ruta ruta = rutas.get(position);
        holder.titulo.setText(ruta.getTitulo());
        holder.duracion.setText(ruta.getDuración());
    }

    @Override
    public int getItemCount() {
        return rutas.size();
    }


}
