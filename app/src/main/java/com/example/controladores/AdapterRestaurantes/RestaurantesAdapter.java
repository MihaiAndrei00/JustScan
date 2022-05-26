package com.example.controladores.AdapterRestaurantes;

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
import com.example.modelo.Restaurante;

import java.util.ArrayList;

public class RestaurantesAdapter extends RecyclerView.Adapter<RestaurantesAdapter.myViewHolder>{
    Context context;
    ArrayList<Restaurante> listaRestaurantes;
    private RestaurantesAdapter.myViewHolder.onRestauranteListener onRestauranteListener;

    public RestaurantesAdapter(Context context,   ArrayList<Restaurante> listaRestaurantes, RestaurantesAdapter.myViewHolder.onRestauranteListener onRestauranteListener) {
        this.context=context;
        this.listaRestaurantes=listaRestaurantes;
        this.onRestauranteListener=onRestauranteListener;
    }

    @NonNull
    @Override
    public RestaurantesAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurante,parent,false);
        return new RestaurantesAdapter.myViewHolder(view,onRestauranteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantesAdapter.myViewHolder holder, int position) {
        Restaurante restaurante = listaRestaurantes.get(position);
        holder.nombre.setText(restaurante.getNombre());
        holder.calleRestaurante.setText("Localización: " + restaurante.getCalle());
        Glide.with(context).load(listaRestaurantes.get(position).getFoto()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return listaRestaurantes.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img;
        TextView nombre;
        TextView calleRestaurante;
        RestaurantesAdapter.myViewHolder.onRestauranteListener onRestauranteListener;
        public myViewHolder(@NonNull View itemView, RestaurantesAdapter.myViewHolder.onRestauranteListener onRestauranteListener) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.imgRestauranteItem);
            nombre=(TextView) itemView.findViewById(R.id.nombreRestauranteItem);
            calleRestaurante=(TextView) itemView.findViewById(R.id.calleRestauranteItem);


            this.onRestauranteListener=onRestauranteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRestauranteListener.clickEnRestaurante(getAdapterPosition());
        }

        public interface onRestauranteListener{
            void clickEnRestaurante(int position);
        }

    }

}
