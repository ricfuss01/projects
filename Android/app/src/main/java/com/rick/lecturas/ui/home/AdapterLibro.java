package com.rick.lecturas.ui.home;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rick.lecturas.R;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterLibro extends RecyclerView.Adapter<AdapterLibro.ViewHolderLibro> {
    ArrayList<LibroCard> listaLibros;
    String user;
    String estadoLectura;
    private FirebaseFirestore bd = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    public AdapterLibro(ArrayList<LibroCard> listaLibros) {
        this.listaLibros = listaLibros;
    }

    @NonNull
    @Override
    public ViewHolderLibro onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_libro, null, false);
        return new ViewHolderLibro(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLibro.ViewHolderLibro holder, @SuppressLint("RecyclerView") int position) {
        holder.tituloEt.setText(listaLibros.get(position).getTitulo());
        holder.autorEt.setText(listaLibros.get(position).getAutor());
        holder.generoEt.setText(listaLibros.get(position).getGenero());
        holder.fotoEt.setImageResource(listaLibros.get(position).getFoto());
        //mostrar foto
        StorageReference str = storage.getReferenceFromUrl("gs://lecturas-fe182.appspot.com/images/" + holder.tituloEt.getText().toString());
        str.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.fotoEt);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //error
            }
        });

        //color del boton favorito/add
        holder.add.setBackgroundResource(R.drawable.round_buttons);
        bd.collection("favs").whereEqualTo("estado", "leido").whereEqualTo("user", user).whereEqualTo("titulo",holder.tituloEt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        holder.add.setBackgroundResource(R.drawable.boton_green);
                    }
                }
            }
        });
        bd.collection("favs").whereEqualTo("estado", "leyendo").whereEqualTo("user", user).whereEqualTo("titulo",holder.tituloEt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        holder.add.setBackgroundResource(R.drawable.round_boton_yellow);
                    }
                }
            }
        });
        bd.collection("favs").whereEqualTo("estado", "sinterminar").whereEqualTo("user", user).whereEqualTo("titulo",holder.tituloEt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        holder.add.setBackgroundResource(R.drawable.round_boton_blue);
                    }
                }
            }
        });
        bd.collection("favs").whereEqualTo("estado", "quieroleer").whereEqualTo("user", user).whereEqualTo("titulo",holder.tituloEt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        holder.add.setBackgroundResource(R.drawable.round_button_red);
                    }
                }
                else{
                    holder.add.setBackgroundResource(R.drawable.round_buttons);
                }
            }
        });


        //boton informacion
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(holder.itemView, position);
            }
        });

        //cambiar entre boton delete y boton comentario
        if(HomeFragment.filtrarLibro.getVisibility() == View.VISIBLE) {
            holder.delete.setImageResource(R.drawable.baseline_edit_24);
        }
        else{
            holder.delete.setImageResource(R.drawable.baseline_delete_24);
        }

        //solo admin borra
        if(HomeFragment.filtrarLibro.getVisibility() == View.VISIBLE || user.equals("admin@gmail.com")){
            holder.delete.setVisibility(View.VISIBLE);
        }
        else{
            holder.delete.setVisibility(View.INVISIBLE);
        }

        //funcionalidad if=delete else=comentario
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HomeFragment.filtrarLibro.getVisibility() != View.VISIBLE) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setTitle("Eliminar libro");
                    builder.setMessage("¿Quieres eliminar el libro \"" + holder.tituloEt.getText().toString() + "\"?");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            bd.collection("libros").document(holder.tituloEt.getText().toString()).delete();
                            bd.collection("favs").document(user + holder.tituloEt.getText().toString()).delete();
                            //holder.itemView.setVisibility(View.GONE);
                            //holder.itemView.getRootView().setLayoutParams(new RelativeLayout.LayoutParams(0,0));
                            notifyItemRemoved(position);
                            listaLibros.remove(position);

                            //eliminar foto
                            str.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Done
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    //Error
                                }
                            });

                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    holder.delete.setImageResource(R.drawable.baseline_edit_24);
                    //popup
                    BottomSheetDialog dialog = new BottomSheetDialog(holder.itemView.getContext());
                    dialog.setContentView(R.layout.popup_resena);
                    dialog.show();
                    //edit texts del popup
                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText comentario = dialog.findViewById(R.id.comentario);
                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RatingBar rating = dialog.findViewById(R.id.ratingBar);
                    //boton del popup
                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button addComent = dialog.findViewById(R.id.botonAddComentario);
                    addComent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HashMap mapComents = new HashMap();
                            mapComents.put("titulo", holder.tituloEt.getText().toString());
                            mapComents.put("autor", holder.autorEt.getText().toString());
                            mapComents.put("comentario", comentario.getText().toString());
                            mapComents.put("user", user);
                            mapComents.put("valorar", rating.getRating());
                            bd.collection("coments").document(user+holder.tituloEt.getText().toString()).set(mapComents);
                            Toast.makeText(dialog.getContext(), "Comentario publicado", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        //boton favorito/add
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bd.collection("favs").whereEqualTo("user",user).whereEqualTo("titulo",holder.tituloEt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getDocuments().isEmpty()) {

                                //menu de opciones
                                PopupMenu popup = new PopupMenu(view.getContext(), view);
                                MenuInflater inflater = popup.getMenuInflater();
                                inflater.inflate(R.menu.menu_libro_liedo, popup.getMenu());
                                popup.show();

                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        switch (menuItem.getItemId()){
                                            case R.id.leido:
                                                estadoLectura = "leido";
                                                accionMenu(holder,view);
                                                return true;
                                            case R.id.leyendo:
                                                estadoLectura = "leyendo";
                                                accionMenu(holder,view);
                                                return true;
                                            case R.id.quieroleer:
                                                estadoLectura = "quieroleer";
                                                accionMenu(holder,view);
                                                return true;
                                            case R.id.sinterminar:
                                                estadoLectura = "sinterminar";
                                                accionMenu(holder,view);
                                            default:
                                                return false;
                                        }
                                    }
                                });

                            } else {
                                bd.collection("favs").document(user + holder.tituloEt.getText().toString()).delete();
                                bd.collection("favs").whereEqualTo("user", user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        bd.collection("favs").whereEqualTo("titulo", holder.tituloEt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                holder.add.setBackgroundResource(R.drawable.round_buttons);
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaLibros.size();
    }

    public class ViewHolderLibro extends RecyclerView.ViewHolder {
        TextView tituloEt, autorEt, generoEt;
        ImageButton delete, add, info;
        ImageView fotoEt;
        public ViewHolderLibro(@NonNull View itemView) {
            super(itemView);
            tituloEt = (TextView) itemView.findViewById(R.id.titulocard);
            autorEt = (TextView) itemView.findViewById(R.id.autorcard);
            generoEt = (TextView) itemView.findViewById(R.id.generocard);
            fotoEt = (ImageView) itemView.findViewById(R.id.fotocard);
            delete = (ImageButton) itemView.findViewById(R.id.deleteButton);
            add = (ImageButton) itemView.findViewById(R.id.addPropioButton);
            info = (ImageButton) itemView.findViewById(R.id.infoButton);
        }
    }

    private void showAlert(View holder, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.getContext());
        builder.setTitle("Sinopsis: "+listaLibros.get(position).getTitulo());
        builder.setMessage("Número de páginas: "+listaLibros.get(position).getPaginas()+"\n\n"+listaLibros.get(position).getSinopsis());
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void enviar(String correo){
        this.user = correo;
    }

    public void accionMenu(AdapterLibro.ViewHolderLibro holder, View view){
        Toast.makeText(view.getContext(), "\"" + holder.tituloEt.getText().toString() + "\" añadido a favoritos", Toast.LENGTH_SHORT).show();
        //base de datos para almacenar que usuario ha guardado que y cambiar color del boton fav
        HashMap mapUserLibro = new HashMap();
        mapUserLibro.put("user", user);
        mapUserLibro.put("titulo", holder.tituloEt.getText().toString());
        mapUserLibro.put("estado", estadoLectura);
        mapUserLibro.put("autor", holder.autorEt.getText().toString());
        mapUserLibro.put("genero", holder.generoEt.getText().toString());
        bd.collection("favs").document(user + holder.tituloEt.getText().toString()).set(mapUserLibro);
        bd.collection("favs").whereEqualTo("user", user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                bd.collection("favs").whereEqualTo("estado", "leido").whereEqualTo("user", user).whereEqualTo("titulo",holder.tituloEt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().getDocuments().isEmpty()) {
                                holder.add.setBackgroundResource(R.drawable.boton_green);
                            }
                        }
                    }
                });
                bd.collection("favs").whereEqualTo("estado", "leyendo").whereEqualTo("user", user).whereEqualTo("titulo",holder.tituloEt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().getDocuments().isEmpty()) {
                                holder.add.setBackgroundResource(R.drawable.round_boton_yellow);
                            }
                        }
                    }
                });
                bd.collection("favs").whereEqualTo("estado", "sinterminar").whereEqualTo("user", user).whereEqualTo("titulo",holder.tituloEt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().getDocuments().isEmpty()) {
                                holder.add.setBackgroundResource(R.drawable.round_boton_blue);
                            }
                        }
                    }
                });
                bd.collection("favs").whereEqualTo("estado", "quieroleer").whereEqualTo("user", user).whereEqualTo("titulo",holder.tituloEt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().getDocuments().isEmpty()) {
                                holder.add.setBackgroundResource(R.drawable.round_button_red);
                            }
                        }
                        else{
                            holder.add.setBackgroundResource(R.drawable.round_buttons);
                        }
                    }
                });
            }
        });
    }
}
