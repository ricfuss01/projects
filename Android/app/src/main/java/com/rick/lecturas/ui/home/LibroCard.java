package com.rick.lecturas.ui.home;

import android.widget.ImageView;

public class LibroCard {
    private String titulo;
    private String autor;
    private String sinopsis;
    private String genero;
    private String paginas;
    private int foto;

    public LibroCard(String titulo, String autor, String sinopsis, String genero, String paginas, int foto) {
        this.titulo = titulo;
        this.autor = autor;
        this.sinopsis = sinopsis;
        this.genero = genero;
        this.foto = foto;
        this.paginas = paginas;
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

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getPaginas() {
        return paginas;
    }

    public void setPaginas(String paginas) {
        this.paginas = paginas;
    }
}
