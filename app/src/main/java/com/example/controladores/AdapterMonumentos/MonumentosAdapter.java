package com.example.controladores.AdapterMonumentos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.controladores.AdapterCalles.CallesAdapter;
import com.example.just_scan.R;
import com.example.modelo.Calle;
import com.example.modelo.Monumento;

import java.util.ArrayList;

public class MonumentosAdapter extends RecyclerView.Adapter<MonumentosAdapter.myViewHolder>{
    Context context;
    ArrayList<Monumento> listaMonumentos;
    private MonumentosAdapter.myViewHolder.onMonumentoListener onMonumentoListener;


    public MonumentosAdapter(Context context, ArrayList<Monumento> listaMonumentos, MonumentosAdapter.myViewHolder.onMonumentoListener onMonumentoListener) {
        this.context=context;
        this.listaMonumentos=listaMonumentos;
        this.onMonumentoListener=onMonumentoListener;
    }

    @NonNull
    @Override
    public MonumentosAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calle,parent,false);
        return new MonumentosAdapter.myViewHolder(view,onMonumentoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Monumento monumento = listaMonumentos.get(position);
        holder.nombre.setText(monumento.getNombre());
        Glide.with(context).load(listaMonumentos.get(position).getFoto()).into(holder.img);
    }



    @Override
    public int getItemCount() {
        return listaMonumentos.size();
    }
    public static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img;
        TextView nombre;
        TextView calleMonumento;
        MonumentosAdapter.myViewHolder.onMonumentoListener onMonumentoListener;
        public myViewHolder(@NonNull View itemView, MonumentosAdapter.myViewHolder.onMonumentoListener onMonumentoListener) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.imgCalleItem);
            nombre=(TextView) itemView.findViewById(R.id.nombreCalleItem);
            calleMonumento= (TextView) itemView.findViewById(R.id.calleMonumentoItem);
            this.onMonumentoListener=onMonumentoListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMonumentoListener.clickEnMonumento(getAdapterPosition());
        }

        public interface onMonumentoListener{
            void clickEnMonumento(int position);
        }

    }

}