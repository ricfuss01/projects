package com.rick.lecturas.ui.gallery;

public class RetoCard {
    private String texto;
    private int foto;

    public RetoCard(String texto, int foto) {
        this.texto = texto;
        this.foto = foto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
