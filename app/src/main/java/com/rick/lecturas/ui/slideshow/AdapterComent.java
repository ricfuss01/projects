package com.rick.lecturas.ui.slideshow;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rick.lecturas.R;
import com.rick.lecturas.ui.home.AdapterLibro;
import com.rick.lecturas.ui.home.LibroCard;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterComent extends RecyclerView.Adapter<AdapterComent.ViewHolderComent>{
    ArrayList<ComentCard> listaComents;
    String userCom;
    private FirebaseFirestore bd = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    public AdapterComent(ArrayList<ComentCard> listaComents) {
        this.listaComents = listaComents;
    }

    @NonNull
    @Override
    public ViewHolderComent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_coments, null, false);
        return new AdapterComent.ViewHolderComent(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterComent.ViewHolderComent holder, int position) {
        holder.tituloCom.setText(listaComents.get(position).getTitulo());
        holder.autorCom.setText(listaComents.get(position).getAutor());
        holder.usuarioCom.setText(listaComents.get(position).getUsuario().split("@")[0]);
        holder.comentarioCom.setText(listaComents.get(position).getComentario());
        holder.valorCom.setRating(Float.parseFloat(listaComents.get(position).getValorar().toString()));
        holder.fotoCom.setImageResource(listaComents.get(position).getFoto());
        //mostrar foto
        StorageReference str = storage.getReferenceFromUrl("gs://lecturas-fe182.appspot.com/images/" + holder.tituloCom.getText().toString());
        str.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.fotoCom);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //error
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaComents.size();
    }

    public class ViewHolderComent extends RecyclerView.ViewHolder {
        TextView tituloCom, autorCom, comentarioCom, usuarioCom;
        ImageButton deleteCom, infoCom;
        RatingBar valorCom;
        ImageView fotoCom;
        public ViewHolderComent(@NonNull View itemView) {
            super(itemView);
            tituloCom = (TextView) itemView.findViewById(R.id.titulocardcoment);
            autorCom = (TextView) itemView.findViewById(R.id.autorcardcoment);
            comentarioCom = (TextView) itemView.findViewById(R.id.comentariocard);
            fotoCom = (ImageView) itemView.findViewById(R.id.fotocardcoment);
            usuarioCom = (TextView) itemView.findViewById(R.id.usuariocardcoment);
            valorCom = (RatingBar) itemView.findViewById(R.id.ratingBarCom);
            //deleteCom = (ImageButton) itemView.findViewById(R.id.deleteButton);
            infoCom = (ImageButton) itemView.findViewById(R.id.infoButtoncom);
        }
    }

    public void enviarAdapterComentUSer(String correo){
        this.userCom = correo;
    }
}
