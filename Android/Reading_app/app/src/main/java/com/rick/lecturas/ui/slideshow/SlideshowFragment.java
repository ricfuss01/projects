package com.rick.lecturas.ui.slideshow;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rick.lecturas.R;
import com.rick.lecturas.databinding.FragmentHomeBinding;
import com.rick.lecturas.databinding.FragmentSlideshowBinding;
import com.rick.lecturas.ui.home.AdapterLibro;
import com.rick.lecturas.ui.home.LibroCard;

import java.util.ArrayList;
import java.util.Locale;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    //base de datos
    private FirebaseFirestore bd;
    //recyclerview
    ArrayList<UserCard> listaUsers;
    ArrayList<ComentCard> listaComents;
    ArrayList<ComentCard> listaComunity;
    RecyclerView recyclerUsers;
    RecyclerView recyclerViewComunity;
    //adapter
    public static AdapterUser adapter;
    public static AdapterComent adapterComents;
    public static AdapterComent adapterComunity;
    static String userCorreo;

    //Buscar
    ImageButton buscar;
    EditText buscarText;

    //expandir
    ImageButton expandAmigo;
    ImageButton expandComunity;

    LinearLayout layoutamigos, layoutcom;
    boolean clickamigo = true;
    boolean clickcomunity = true;
    boolean clickBuscar = true;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //base de datos
        bd = FirebaseFirestore.getInstance();
        //busqueda
        buscar = (ImageButton) binding.getRoot().findViewById(R.id.buscarButton);
        buscarText = (EditText) binding.getRoot().findViewById(R.id.editTextTextBuscar);
        buscarText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty() && buscarText.getVisibility() == View.VISIBLE) {
                    ArrayList<ComentCard> listaComentsFilter = new ArrayList<>();
                    for (ComentCard comCard : listaComents) {
                        if (comCard.getTitulo().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                                comCard.getAutor().toLowerCase().contains(charSequence.toString().toLowerCase())
                        || comCard.getUsuario().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            listaComentsFilter.add(comCard);
                            AdapterComent adapterComentsFilter = new AdapterComent(listaComentsFilter);
                            recyclerUsers.setAdapter(adapterComentsFilter);
                        }
                        else{
                            AdapterComent adapterComentsFilter = new AdapterComent(listaComentsFilter);
                            recyclerUsers.setAdapter(adapterComentsFilter);
                        }
                    }
                    ArrayList<ComentCard> listaComentsComunityFilter = new ArrayList<>();
                    for (ComentCard comCard1 : listaComunity) {
                        if (comCard1.getTitulo().toLowerCase().contains(buscarText.getText().toString().toLowerCase()) ||
                                comCard1.getAutor().toLowerCase().contains(buscarText.getText().toString().toLowerCase())
                                || comCard1.getUsuario().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            listaComentsComunityFilter.add(comCard1);
                            AdapterComent adapterComunityFilter = new AdapterComent(listaComentsComunityFilter);
                            recyclerViewComunity.setAdapter(adapterComunityFilter);
                        }
                        else{
                            AdapterComent adapterComunityFilter = new AdapterComent(listaComentsComunityFilter);
                            recyclerViewComunity.setAdapter(adapterComunityFilter);
                        }
                    }
                    //llenarUsers();
                    ArrayList<UserCard> listaUsersFilter = new ArrayList<>();
                    for (UserCard userCa : listaUsers) {
                        if(userCa.getCorreo().toLowerCase().contains(charSequence.toString().toLowerCase()) &&
                                charSequence.toString().toLowerCase().contains("@")){
                            listaUsersFilter.add(userCa);
                            AdapterUser adapterUsersFilter = new AdapterUser(listaUsersFilter, getContext());
                            adapterUsersFilter.enviarAdapterUSer(userCorreo);
                            recyclerUsers.setAdapter(adapterUsersFilter);
                        }
                    }
                }
                else{
                    recyclerUsers.setAdapter(adapterComents);
                    recyclerViewComunity.setAdapter(adapterComunity);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickBuscar){
                    buscarText.setVisibility(View.VISIBLE);
                    clickBuscar = false;
                }
                else{
                    buscarText.setVisibility(View.GONE);
                    clickBuscar = true;
                }
            }
        });

        expandAmigo = (ImageButton) binding.getRoot().findViewById(R.id.expandAmigos);
        layoutamigos = binding.getRoot().findViewById(R.id.lami);
        layoutamigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickamigo){
                    recyclerUsers.setVisibility(View.GONE);
                    expandAmigo.setImageResource(R.drawable.baseline_keyboard_arrow_right_24);
                    clickamigo = false;
                }
                else{
                    recyclerUsers.setVisibility(View.VISIBLE);
                    expandAmigo.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                    clickamigo = true;
                }
            }
        });

        expandComunity = (ImageButton) binding.getRoot().findViewById(R.id.expandComunity);
        layoutcom = binding.getRoot().findViewById(R.id.lcom);
        layoutcom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickcomunity){
                    recyclerViewComunity.setVisibility(View.GONE);
                    expandComunity.setImageResource(R.drawable.baseline_keyboard_arrow_right_24);
                    clickcomunity = false;
                }
                else{
                    recyclerViewComunity.setVisibility(View.VISIBLE);
                    expandComunity.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                    clickcomunity = true;
                }
            }
        });

        //recyclerview
        listaUsers = new ArrayList<>();
        listaComents = new ArrayList<>();
        recyclerUsers = (RecyclerView) binding.getRoot().findViewById(R.id.recyclerUser);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(root.getContext()));

        adapter = new AdapterUser(listaUsers, getContext());
        adapterComents = new AdapterComent(listaComents);
        llenarComents();

        adapter.enviarAdapterUSer(userCorreo);
        adapterComents.enviarAdapterComentUSer(userCorreo);
        //adapterComunity.enviarAdapterComentUSer(userCorreo);

        recyclerUsers.setAdapter(adapterComents);

        recyclerViewComunity = (RecyclerView) binding.getRoot().findViewById(R.id.comunidadrecycler);
        recyclerViewComunity.setLayoutManager(new LinearLayoutManager(root.getContext()));
        listaComunity  = new ArrayList<>();
        adapterComunity = new AdapterComent(listaComunity);
        llenarComunity();
        recyclerViewComunity.setAdapter(adapterComunity);

        llenarUsers();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void llenarUsers(){
        listaUsers.clear();
        bd.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        listaUsers.add(new UserCard(document.get("correo").toString(),R.drawable.j));
                        adapter.notifyDataSetChanged();
                    }
                } else {
                }
            }
        });
    }

    public void llenarComents(){
        bd.collection("amigos").whereEqualTo("user1", userCorreo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            bd.collection("coments").whereEqualTo("user", document.get("amigo2").toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (!task.getResult().getDocuments().isEmpty()) {
                                            for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                listaComents.add(new ComentCard(document2.get("titulo").toString(), document2.get("autor").toString(), document2.get("comentario").toString(), document2.get("valorar").toString(), document2.get("user").toString(), R.drawable.j));
                                                adapterComents.notifyDataSetChanged();
                                            }
                                        }
                                        else{
                                            adapterComents.notifyDataSetChanged();
                                        }
                                    } else {
                                        adapterComents.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                    else{
                        adapterComents.notifyDataSetChanged();
                    }
                }
                else{
                    adapterComents.notifyDataSetChanged();
                }
            }
        });
    }

    public void llenarComunity(){
        bd.collection("coments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        for (QueryDocumentSnapshot document2 : task.getResult()) {
                            listaComunity.add(new ComentCard(document2.get("titulo").toString(), document2.get("autor").toString(), document2.get("comentario").toString(), document2.get("valorar").toString(), document2.get("user").toString(), R.drawable.j));
                            adapterComunity.notifyDataSetChanged();
                        }
                    }
                    else{
                        adapterComunity.notifyDataSetChanged();
                    }
                } else {
                    adapterComunity.notifyDataSetChanged();
                }
            }
        });
    }


    public static void enviarFragmentoUsers(String correo) {
        userCorreo = correo;
    }
}