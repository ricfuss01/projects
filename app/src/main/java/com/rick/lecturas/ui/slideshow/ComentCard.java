package com.rick.lecturas.ui.slideshow;

public class ComentCard {
    private String titulo;
    private String autor;
    private String comentario;
    private String valorar;
    private String usuario;
    private int foto;

    public ComentCard(String titulo, String autor, String comentario, String valorar,String usuario, int foto) {
        this.titulo = titulo;
        this.autor = autor;
        this.comentario = comentario;
        this.valorar = valorar;
        this.foto = foto;
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getValorar() {
        return valorar;
    }

    public void setValorar(String valorar) {
        this.valorar = valorar;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
