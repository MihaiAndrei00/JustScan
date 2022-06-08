package com.example.controladores.AdapterEdificios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.just_scan.R;
import com.example.modelo.Edificio;

import java.util.ArrayList;

public class EdificiosAdapter extends RecyclerView.Adapter<EdificiosAdapter.myViewHolder> {
    private Context context;
    private ArrayList<Edificio> listaEdificios;
    private myViewHolder.onEdificioListener onEdificioListener;
    private View view;
    private Edificio edificio;

    public EdificiosAdapter(Context context, ArrayList<Edificio> listaEdificios, myViewHolder.onEdificioListener onEdificioListener) {
        this.context=context;
        this.listaEdificios=listaEdificios;
        this.onEdificioListener=onEdificioListener;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edificio,parent,false);
        return new EdificiosAdapter.myViewHolder(view,onEdificioListener);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        edificio = listaEdificios.get(position);
        holder.nombre.setText(edificio.getNombre());
        holder.calleEdificio.setText("Localización: " + edificio.getCalle());
        Glide.with(context).load(listaEdificios.get(position).getFoto()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return listaEdificios.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img;
        TextView nombre;
        TextView calleEdificio;
        myViewHolder.onEdificioListener onEdificioListener;
        public myViewHolder(@NonNull View itemView, myViewHolder.onEdificioListener onEdificioListener) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.imgEdificioItem);
            nombre=(TextView) itemView.findViewById(R.id.nombreEdificio);
            calleEdificio=(TextView) itemView.findViewById(R.id.calleEdificioItem);
            this.onEdificioListener=onEdificioListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onEdificioListener.clickEnEdificio(getAdapterPosition());
        }
        //interfaz que se impplementará em la clase listar para realizar la función de , al clickar en un elemento del adapter
        // llevar a ver
        public interface onEdificioListener{
            void clickEnEdificio(int position);
        }

    }

}
