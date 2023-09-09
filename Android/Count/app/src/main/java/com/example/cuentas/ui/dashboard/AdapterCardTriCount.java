package com.example.cuentas.ui.dashboard;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cuentas.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterCardTriCount extends RecyclerView.Adapter<AdapterCardTriCount.ViewHolderTriCount> {

    static ArrayList<TriCountCard> listaTC;
    static HashMap<Integer,Double> listaTotales;
    static HashMap<Integer,TextView> listaText;
    DecimalFormat df = new DecimalFormat("#.##");
    public AdapterCardTriCount(ArrayList<TriCountCard> lista) {
        this.listaTC = lista;
        this.listaText = new HashMap<Integer,TextView>();
        this.listaTotales = new HashMap<Integer,Double>();
    }
    @NonNull
    @Override
    public AdapterCardTriCount.ViewHolderTriCount onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_tc, null, false);
        return new AdapterCardTriCount.ViewHolderTriCount(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCardTriCount.ViewHolderTriCount viewHolderTriCount, int i) {
        int color = Color.parseColor("#3949AB");
        double totalSum = DashboardFragment.enviarSuma();
        double dinerito = Double.parseDouble(df.format((Double.parseDouble(listaTC.get(i).getDinero().replace(",","."))-(totalSum/listaTC.size()))).replace(",","."));
        listaTotales.put(i,dinerito);
        listaText.put(i,viewHolderTriCount.result);
        viewHolderTriCount.result.setText("Total: " + dinerito + "\n");
        viewHolderTriCount.dinero.setText("Aportado: " + listaTC.get(i).getDinero());
        viewHolderTriCount.name_.setText("Persona "+(i+1));
        viewHolderTriCount.cabebcera_.setBackgroundColor(color);
        viewHolderTriCount.linear_.setBackgroundColor(ColorUtils.blendARGB(color, Color.WHITE, 0.35f));
        if(dinerito<0){
            viewHolderTriCount.result.setTextColor(Color.parseColor("#6E0202"));
        }
        else if (dinerito>0){
            viewHolderTriCount.result.setTextColor(Color.parseColor("#015F17"));
        }
        else{
            viewHolderTriCount.result.setTextColor(Color.DKGRAY);
        }
        if(i==listaTC.size()-1)
            repartirCantidades();
    }

    @Override
    public int getItemCount() {
        return listaTC.size();
    }

    public class ViewHolderTriCount extends RecyclerView.ViewHolder {
        TextView result, dinero, name_;
        LinearLayout cabebcera_, linear_;
        public ViewHolderTriCount(@NonNull View itemView) {
            super(itemView);
            result = (TextView) itemView.findViewById(R.id.resultTC);
            dinero = (TextView) itemView.findViewById(R.id.dineroTC);
            name_ = (TextView) itemView.findViewById(R.id.nameTC);
            cabebcera_ = (LinearLayout) itemView.findViewById(R.id.cabeceraTC);
            linear_ = (LinearLayout) itemView.findViewById(R.id.linearLayoutTC);
        }
    }

    public static void repartirCantidades() {
        double sumaPos = 0;
        for (double cantidad : listaTotales.values()) {
            if (cantidad > 0) {
                sumaPos += cantidad;
            }
        }

        // Realizar la distribución de las cantidades positivas
        Map<Integer, Map<Integer, Double>> distribucion = new HashMap<>();
        for (Map.Entry<Integer, Double> entry : listaTotales.entrySet()) {
            int posicion = entry.getKey();
            double cantidad = entry.getValue();
            if (cantidad < 0) {
                distribuirCantidadPositiva(posicion, cantidad, distribucion, listaTotales, sumaPos);
            }
        }
        DecimalFormat df = new DecimalFormat("#.##");
        // Mostrar la distribución realizada
        for (Map.Entry<Integer, Map<Integer, Double>> entry : distribucion.entrySet()) {
            int origen = entry.getKey();
            Map<Integer, Double> asignaciones = entry.getValue();
            //listaText.get(origen+1).setText("");
            for (Map.Entry<Integer, Double> asignacion : asignaciones.entrySet()) {
                int destino = asignacion.getKey();
                double cantidadAsignada = asignacion.getValue();
                if(destino!=-1)
                    listaText.get(origen).setText(listaText.get(origen).getText()+"\nDebe " + df.format(cantidadAsignada) + " a persona "+(destino+1));
            }
        }
    }

    private static void distribuirCantidadPositiva(int posicion, double cantidad, Map<Integer, Map<Integer, Double>> distribucion,
                                                   Map<Integer, Double> numeros, double sumaPos) {
        double cantidadRestante = cantidad;
        Map<Integer, Double> numeros2 = numeros;
        Map<Integer, Double> asignaciones = new HashMap<>();
        for (Map.Entry<Integer, Double> entry : numeros2.entrySet()) {
            int destino = entry.getKey();
            double cantidadPos = entry.getValue();
            if (cantidadRestante < 0 && cantidadPos > 0) {
                double cantidadAsignada = Math.min(Math.abs(cantidadRestante), cantidadPos);
                asignaciones.put(destino, cantidadAsignada);
                entry.setValue(entry.getValue()-cantidadAsignada);
                cantidadRestante += cantidadAsignada;
            }
        }
        distribucion.put(posicion,asignaciones);
    }


}
