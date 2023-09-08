package com.rick.lecturas.ui.gallery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rick.lecturas.R;
import com.rick.lecturas.ui.slideshow.AdapterUser;
import com.rick.lecturas.ui.slideshow.UserCard;

import java.util.ArrayList;

public class AdapterReto extends RecyclerView.Adapter<AdapterReto.ViewHolderReto> {
    ArrayList<RetoCard> listaRetos;

    public AdapterReto(ArrayList<RetoCard> retos) {
        this.listaRetos = retos;
    }

    @NonNull
    @Override
    public AdapterReto.ViewHolderReto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reto, null, false);
        return new AdapterReto.ViewHolderReto(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReto.ViewHolderReto holder, int position) {
        holder.textoEt.setText(listaRetos.get(position).getTexto());
        holder.fotoretoEt.setImageResource(listaRetos.get(position).getFoto());
    }

    @Override
    public int getItemCount() {
        return listaRetos.size();
    }

    public class ViewHolderReto extends RecyclerView.ViewHolder {
        TextView textoEt;
        ImageView fotoretoEt;
        public ViewHolderReto(@NonNull View itemView) {
            super(itemView);
            textoEt = (TextView) itemView.findViewById(R.id.textoretocard);
            fotoretoEt = (ImageView) itemView.findViewById(R.id.fotoretocard);
        }
    }
}
