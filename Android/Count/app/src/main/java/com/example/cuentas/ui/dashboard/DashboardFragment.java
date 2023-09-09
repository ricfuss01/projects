package com.example.cuentas.ui.dashboard;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;

import com.example.cuentas.R;
import com.example.cuentas.databinding.FragmentDashboardBinding;
import com.example.cuentas.ui.home.AdapterCard;
import com.example.cuentas.ui.home.CardPersona;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    RecyclerView recicler;
    AdapterCardTriCount adapter;
    FloatingActionButton add;
    TextView totalTriCount;

    static Double totalSuma;
    ArrayList listaPersonas;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        totalSuma=0.0;
        listaPersonas = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recicler = (RecyclerView) binding.getRoot().findViewById(R.id.recicler2);
        recicler.setLayoutManager(layoutManager);
        //recicler.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new AdapterCardTriCount(listaPersonas);
        recicler.setAdapter(adapter);

        totalTriCount = binding.textViewTricont;
        totalTriCount.setText("Total: 0");
        //popup
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(R.layout.pop_up);

        // Obtener las referencias a los elementos del diseño del cuadro de diálogo
        Button botonSi = dialog.findViewById(R.id.si);
        EditText pagarpopup = dialog.findViewById(R.id.pagar2);
        TextView textPagar = dialog.findViewById(R.id.textView5);
        textPagar.setVisibility(View.GONE);
        pagarpopup.setVisibility(View.GONE);
        EditText pagadopopup = dialog.findViewById(R.id.pagado2);

        // Configurar el clic del botón del cuadro de diálogo
        botonSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ado = pagadopopup.getText().toString();
                listaPersonas.add(new TriCountCard(ado));
                totalSuma = totalSuma+Double.parseDouble(ado);
                totalTriCount.setText("Total: "+(Double.parseDouble(totalTriCount.getText().toString().replace("Total:",""))+Double.parseDouble(ado)));
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        add = binding.floatingActionButton2;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static double enviarSuma(){
        return totalSuma;
    }
}