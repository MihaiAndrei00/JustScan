package com.example.controladores.AdapterRutas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.controladores.AdapterRutas.MainAdapter.myViewHolder.onRutaListener;
import com.example.just_scan.R;
import com.example.modelo.Ruta;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.myViewHolder>{
    Context context;
    ArrayList<Ruta>rutas;

    private myViewHolder.onRutaListener onRutaListener;
    public MainAdapter(Context context, ArrayList<Ruta> listaRutas,onRutaListener onRutaListener) {
        this.context=context;
        this.rutas=listaRutas;
        this.onRutaListener=onRutaListener;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new myViewHolder(view,onRutaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Ruta ruta = rutas.get(position);
        holder.titulo.setText(ruta.getTitulo());
        holder.duracion.setText("Duracion: " + ruta.getDuración());

        Glide.with(context).load(rutas.get(position).getFoto()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return rutas.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img;
        TextView titulo;
        TextView duracion;
        onRutaListener onRutaListener;
        public myViewHolder(@NonNull View itemView,  onRutaListener onRutaListener) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.imgRuta);
            titulo=(TextView) itemView.findViewById(R.id.titulo);
            duracion=(TextView) itemView.findViewById(R.id.duracionDeRuta);
            this.onRutaListener=onRutaListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRutaListener.clickEnRuta(getAdapterPosition());
        }

        public interface onRutaListener{
            void clickEnRuta(int position);
        }

    }


}
