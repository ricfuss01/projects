package com.rick.lecturas.ui.slideshow;

public class UserCard {
    private String correo;
    private int foto;

    public UserCard(String correo, int foto) {
        this.correo = correo;
        this.foto = foto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
