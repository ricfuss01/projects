package com.example.cuentas.ui.home;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cuentas.R;

import java.util.ArrayList;
import java.util.Random;


public class AdapterCard extends RecyclerView.Adapter<AdapterCard.ViewHolderCard>{
    ArrayList<CardPersona> lista;

    public AdapterCard(ArrayList<CardPersona> lista) {
        this.lista = lista;
    }
    @NonNull
    @Override
    public AdapterCard.ViewHolderCard onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_persona, null, false);
        return new ViewHolderCard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCard.ViewHolderCard viewHolderCard, int i) {
        int color = Color.parseColor("#3949AB");
        viewHolderCard.result.setText("Total: " + lista.get(i).getResult());
        viewHolderCard.pagar.setText("A pagar: " + lista.get(i).getPagar());
        viewHolderCard.pagado.setText("Aportado: " + lista.get(i).getPagado());
        viewHolderCard.name.setText("Persona "+(i+1));
        viewHolderCard.cabebcera.setBackgroundColor(color);
        viewHolderCard.linear.setBackgroundColor(ColorUtils.blendARGB(color, Color.WHITE, 0.35f));
        if(Double.parseDouble(lista.get(i).getResult())<0){
            viewHolderCard.result.setTextColor(Color.parseColor("#6E0202"));
        }
        else if (Double.parseDouble(lista.get(i).getResult())>0){
            viewHolderCard.result.setTextColor(Color.parseColor("#015F17"));
        }
        else{
            viewHolderCard.result.setTextColor(Color.DKGRAY);
        }



    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolderCard extends RecyclerView.ViewHolder {
        TextView result, pagar,pagado,name;
        LinearLayout cabebcera, linear;
        public ViewHolderCard(@NonNull View itemView) {
            super(itemView);
            result = (TextView) itemView.findViewById(R.id.result);
            pagar = (TextView) itemView.findViewById(R.id.pagar);
            pagado = (TextView) itemView.findViewById(R.id.pagado);
            name = (TextView) itemView.findViewById(R.id.name);
            cabebcera = (LinearLayout) itemView.findViewById(R.id.cabecera);
            linear = (LinearLayout) itemView.findViewById(R.id.linearLayout1);
        }
    }

    public int colorAleatorio(){
        Random random = new Random();
        int red = random.nextInt(155) + 50; // Rango de 50 a 204
        int green = random.nextInt(155) + 50; // Rango de 50 a 204
        int blue = random.nextInt(155) + 50; // Rango de 50 a 204
        int color = Color.rgb(red, green, blue);
        return color;
    }
}
