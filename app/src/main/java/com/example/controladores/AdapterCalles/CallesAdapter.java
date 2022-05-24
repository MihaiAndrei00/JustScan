package com.example.controladores.AdapterCalles;

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
import com.example.modelo.Calle;

import java.util.ArrayList;

public class CallesAdapter extends RecyclerView.Adapter<CallesAdapter.myViewHolder>{
    Context context;
    ArrayList<Calle> listaCalles;
    private CallesAdapter.myViewHolder.onCalleListener onCalleListener;


    public CallesAdapter(Context context, ArrayList<Calle> listaCalles, CallesAdapter.myViewHolder.onCalleListener onCalleListener) {
        this.context=context;
        this.listaCalles=listaCalles;
        this.onCalleListener=onCalleListener;
    }

    @NonNull
    @Override
    public CallesAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calle,parent,false);
        return new CallesAdapter.myViewHolder(view,onCalleListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CallesAdapter.myViewHolder holder, int position) {
        Calle calle = listaCalles.get(position);
        holder.nombre.setText(calle.getNombre());
        Glide.with(context).load(listaCalles.get(position).getFoto()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return listaCalles.size();
    }
    public static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img;
        TextView nombre;
        TextView calleEdificio;
        CallesAdapter.myViewHolder.onCalleListener onCalleListener;
        public myViewHolder(@NonNull View itemView, CallesAdapter.myViewHolder.onCalleListener onCalleListener) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.imgCalleItem);
            nombre=(TextView) itemView.findViewById(R.id.nombreCalleItem);
            this.onCalleListener=onCalleListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCalleListener.clickEnCalle(getAdapterPosition());
        }

        public interface onCalleListener{
            void clickEnCalle(int position);
        }

    }

}