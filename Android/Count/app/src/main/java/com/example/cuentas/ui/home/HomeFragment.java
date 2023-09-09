package com.example.cuentas.ui.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;

import com.example.cuentas.R;
import com.example.cuentas.databinding.FragmentHomeBinding;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    RecyclerView recicler;
    AdapterCard adapter;
    FloatingActionButton add;
    ArrayList listaPersonas;
    static TextView abajo;
    static TextView abajoDerecha;
    static double totalString=0;
    static double totalStringNomodf=0;
    static double totalapagar=0;
    EditText total;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        totalapagar=0;totalStringNomodf=0;totalString=0;
        listaPersonas = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recicler = (RecyclerView) binding.getRoot().findViewById(R.id.recicler);
        recicler.setLayoutManager(layoutManager);
        //recicler.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new AdapterCard(listaPersonas);

        recicler.setAdapter(adapter);

        abajo = binding.textView3;
        abajoDerecha = binding.textView4;
        total = binding.getRoot().findViewById(R.id.editTextTextPersonName);
        total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!total.getText().toString().equals("")) {
                    totalStringNomodf = totalStringNomodf - totalString;
                    totalString = Double.parseDouble(total.getText().toString()) - totalStringNomodf;
                    totalStringNomodf = Double.parseDouble(total.getText().toString());
                    enviarApagar(0);
                    enviarTotal(0);
                }
            }
        });
        //popup
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(R.layout.pop_up);

        // Obtener las referencias a los elementos del diseño del cuadro de diálogo
        Button botonSi = dialog.findViewById(R.id.si);
        EditText pagarpopup = dialog.findViewById(R.id.pagar2);
        EditText pagadopopup = dialog.findViewById(R.id.pagado2);

        // Configurar el clic del botón del cuadro de diálogo
        botonSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ar = pagarpopup.getText().toString();
                String ado = pagadopopup.getText().toString();
                listaPersonas.add(new CardPersona(ar, ado));
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        add = binding.floatingActionButton;
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
    public static void enviarTotal(double pagado){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        totalString = totalString - pagado;
        abajo.setText("      Vueltas cuenta: " + String.valueOf(decimalFormat.format(totalString*-1)));
        if(totalString*-1<0){
            abajo.setTextColor(Color.parseColor("#E42B2B"));
        }
        else if (totalString*-1>0){
            abajo.setTextColor(Color.parseColor("#1CCD46"));
        }
        else{
            abajo.setTextColor(Color.GRAY);
        }
        System.out.println(totalString);
    }

    public static void enviarApagar(double pagar){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        totalapagar = totalapagar + pagar;
        abajoDerecha.setText("Falta/sobra aportar: " + String.valueOf(decimalFormat.format(totalapagar-totalStringNomodf)));
    }
}