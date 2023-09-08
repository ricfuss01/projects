package com.rick.lecturas.ui.home;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rick.lecturas.R;
import com.rick.lecturas.databinding.FragmentHomeBinding;
import com.rick.lecturas.ui.slideshow.AdapterComent;
import com.rick.lecturas.ui.slideshow.ComentCard;
import com.rick.lecturas.ui.slideshow.UserCard;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    //base de datos
    private FirebaseFirestore bd;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    //botones tod@ / mis libros
    Button botonTodo, botonMisLibros;
    public static ImageButton buscarLibro, filtrarLibro;
    EditText buscarLibroText;
    Uri uri;
    ImageView verFoto;
    boolean click = false;
    //recyclerview
    public static ArrayList<LibroCard> listaLibros;
    RecyclerView recyclerLibros;
    //adapter
    public static AdapterLibro adapter;
    static String userCorreo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //base de datos
        bd = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        //botones
        buscarLibro = binding.buscarLibroButton;
        buscarLibroText = binding.editTextTextBuscarLibro;
        filtrarLibro = binding.filtroButton;
        //popup boton
        final FloatingActionButton add = binding.add;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //animacion
                click=!click;
                Interpolator interpolador = AnimationUtils.loadInterpolator(root.getContext(), android.R.interpolator.fast_out_slow_in);
                view.animate().rotation(click ? 720f : 0).setInterpolator(interpolador).start();

                BottomSheetDialog dialog = new BottomSheetDialog(getContext());
                dialog.setContentView(R.layout.popup_libro);
                dialog.show();
                //edit texts del popup
                EditText titulo = dialog.findViewById(R.id.titulo);
                EditText autor = dialog.findViewById(R.id.autor);
                EditText numpag = dialog.findViewById(R.id.paginas);
                EditText sinopsis = dialog.findViewById(R.id.sinopsis);
                Spinner generoo = dialog.findViewById(R.id.genero);

                //spinner
                @SuppressLint("ResourceType") ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.generos, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                generoo.setAdapter(adapter);

                HashMap maplibros = new HashMap();
                generoo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        maplibros.put("genero",adapterView.getItemAtPosition(i).toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                //boton foto del popup
                ImageButton botonFoto = dialog.findViewById(R.id.botonFoto);
                verFoto = dialog.findViewById(R.id.verFoto);

                //boton del popup
                Button addLibro = dialog.findViewById(R.id.botonAdd);
                addLibro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!titulo.getText().toString().isEmpty() && !autor.getText().toString().isEmpty()
                                && !numpag.getText().toString().isEmpty() && !sinopsis.getText().toString().isEmpty()) {

                            maplibros.put("titulo", titulo.getText().toString());
                            maplibros.put("autor", autor.getText().toString());
                            maplibros.put("paginas", numpag.getText().toString());
                            maplibros.put("sinopsis", sinopsis.getText().toString());

                            bd.collection("libros").document(titulo.getText().toString()).set(maplibros);

                            //subir foto
                            StorageReference fotosRef = storageRef.child("images/"+titulo.getText().toString());
                            verFoto.setDrawingCacheEnabled(true);
                            verFoto.buildDrawingCache();
                            Bitmap bitmap = ((BitmapDrawable) verFoto.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            UploadTask uploadTask = fotosRef.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(getContext(), "No se ha podidido guardar la imagen", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                }
                            });
                            //fin subir foto

                            Toast.makeText(getContext(), "Libro añadido correctamente", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            listaLibros.add(new LibroCard(titulo.getText().toString(),autor.getText().toString(),sinopsis.getText().toString(),maplibros.get("genero").toString(),numpag.getText().toString(),R.drawable.j));
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(getContext(), "Error: Rellena todos los campos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                botonFoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        permisos();
                    }
                });
            }
        });

        // boton todos los libros
        botonTodo = (Button) binding.buttonTodos;
        botonTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listaLibros.clear();
                llenarLibros();
                adapter.notifyDataSetChanged();
                recyclerLibros.setAdapter(adapter);
                filtrarLibro.setVisibility(View.GONE);
                botonTodo.setBackgroundResource(R.drawable.round_button_border);
                botonMisLibros.setBackgroundResource(R.drawable.boton_invisible);
            }
        });

        //boton mis libros
        botonMisLibros = (Button) binding.buttonMisLibros;
        botonMisLibros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listaLibros.clear();
                llenarMisLibros();
                adapter.notifyDataSetChanged();
                recyclerLibros.setAdapter(adapter);
                filtrarLibro.setVisibility(View.VISIBLE);
                botonTodo.setBackgroundResource(R.drawable.boton_invisible);
                botonMisLibros.setBackgroundResource(R.drawable.round_button_border);
            }
        });

        //busqueda
        //buscarLibro = (ImageButton) binding.getRoot().findViewById(R.id.buscarButton);
        //buscarLibroText = (EditText) binding.getRoot().findViewById(R.id.editTextTextBuscar);

        buscarLibroText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(buscarLibroText.isFocused()) {
                    ArrayList<LibroCard> listaLibrosFilter = new ArrayList<>();
                    for (LibroCard libCard : listaLibros) {
                        if (libCard.getTitulo().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                                libCard.getAutor().toLowerCase().contains(charSequence.toString().toLowerCase())
                        || libCard.getGenero().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            listaLibrosFilter.add(libCard);
                        }
                    }
                    AdapterLibro adapterLibrosFilter = new AdapterLibro(listaLibrosFilter);
                    adapterLibrosFilter.enviar(userCorreo);
                    recyclerLibros.setAdapter(adapterLibrosFilter);
                    if(filtrarLibro.getVisibility() != View.VISIBLE && listaLibrosFilter.isEmpty() && charSequence.toString().isEmpty()){
                        llenarLibros();
                        adapter.notifyDataSetChanged();
                        recyclerLibros.setAdapter(adapter);
                    }
                    else if (listaLibrosFilter.isEmpty() && charSequence.toString().isEmpty()){
                        recyclerLibros.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        llenarMisLibros();
                    }
                }
                /*else if(filtrarLibro.getVisibility() != View.VISIBLE && botonTodo.isActivated()){
                    llenarLibros();
                    adapter.notifyDataSetChanged();
                    recyclerLibros.setAdapter(adapter);
                }
                else if (botonMisLibros.isActivated()){
                    recyclerLibros.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    llenarMisLibros();
                }*/
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        filtrarLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menu de opciones
                PopupMenu popupFiltro = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popupFiltro.getMenuInflater();
                inflater.inflate(R.menu.menu_libro_leido_filtro, popupFiltro.getMenu());
                popupFiltro.show();

                popupFiltro.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.leido:
                                llenarMisLibrosPopUp( "leido");
                                return true;
                            case R.id.leyendo:
                                llenarMisLibrosPopUp("leyendo");
                                return true;
                            case R.id.quieroleer:
                                llenarMisLibrosPopUp("quieroleer");
                                return true;
                            case R.id.sinterminar:
                                llenarMisLibrosPopUp("sinterminar");
                                return true;
                            case R.id.todo:
                                llenarMisLibros();
                            default:
                                return false;
                        }
                    }
                });
            }
        });

        //recyclerview
        listaLibros = new ArrayList<>();
        recyclerLibros = (RecyclerView) binding.getRoot().findViewById(R.id.recyclerlibro);
        recyclerLibros.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new AdapterLibro(listaLibros);
        llenarLibros();
        //buscarLibroText.setText("");
        adapter.enviar(userCorreo);
        recyclerLibros.setAdapter(adapter);
        return root;
    }

    public void permisos() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        galleryARL.launch(intent);
    }

    private ActivityResultLauncher galleryARL = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        uri = data.getData();
                        verFoto.setImageURI(uri);
                    }
                    else{
                        Toast.makeText(getContext(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void llenarLibros(){
        listaLibros.clear();
        bd.collection("libros").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        listaLibros.add(new LibroCard(document.getData().get("titulo").toString(),document.getData().get("autor").toString(),document.getData().get("sinopsis").toString(),
                                document.getData().get("genero").toString(),document.getData().get("paginas").toString(),R.drawable.j));
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void llenarMisLibros(){
        listaLibros.clear();
        bd.collection("favs").whereEqualTo("user",userCorreo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            bd.collection("libros").whereEqualTo("titulo", document.getData().get("titulo").toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document1 : task.getResult()) {
                                            listaLibros.add(new LibroCard(document1.getData().get("titulo").toString(), document1.getData().get("autor").toString(), document1.getData().get("sinopsis").toString(),
                                                    document1.getData().get("genero").toString(), document1.getData().get("paginas").toString(), R.drawable.j));
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });
                        }
                    }
                    else{
                        adapter.notifyDataSetChanged();
                    }
                } else {
                }
            }
        });
    }

    public static void enviarFragmentoLibros(String correo){
        userCorreo = correo;
    }

    public void llenarMisLibrosPopUp(String estado){
        listaLibros.clear();
        bd.collection("favs").whereEqualTo("user", userCorreo).whereEqualTo("estado",estado).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            bd.collection("libros").whereEqualTo("titulo", document.getData().get("titulo").toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && !task.getResult().getDocuments().isEmpty()) {
                                        for (QueryDocumentSnapshot document2 : task.getResult()) {
                                            listaLibros.add(new LibroCard(document2.getData().get("titulo").toString(), document2.getData().get("autor").toString(), document2.getData().get("sinopsis").toString(),
                                                    document2.getData().get("genero").toString(), document2.getData().get("paginas").toString(), R.drawable.j));
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                    else{
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                    else{
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void buscarLibroFunction(String texto){
        listaLibros.clear();
        ArrayList <String> cosas = new ArrayList<>();
        cosas.add("titulo"); cosas.add("autor"); cosas.add("genero");
        for(int i = 0; i<cosas.size(); i++) {
            bd.collection("libros").whereEqualTo(cosas.get(i), texto).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && !task.getResult().getDocuments().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(filtrarLibro.getVisibility() != View.VISIBLE) {
                                listaLibros.add(new LibroCard(document.getData().get("titulo").toString(), document.getData().get("autor").toString(), document.getData().get("sinopsis").toString(),
                                        document.getData().get("genero").toString(), document.getData().get("paginas").toString(), R.drawable.j));
                            }
                            else{
                                bd.collection("favs").whereEqualTo("titulo", document.getData().get("titulo")).whereEqualTo("user", userCorreo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful() && !task.getResult().getDocuments().isEmpty()) {
                                            listaLibros.add(new LibroCard(document.getData().get("titulo").toString(), document.getData().get("autor").toString(), document.getData().get("sinopsis").toString(),
                                                    document.getData().get("genero").toString(), document.getData().get("paginas").toString(), R.drawable.j));
                                            adapter.notifyDataSetChanged();

                                        }
                                    }
                                });
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                    else{
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}