package com.example.magicelementsfight.luchasCarpeta;

import android.graphics.drawable.Drawable;

public class ItemHistorial {
    private String text;
    private int color;
    private Drawable photo;

    public ItemHistorial(String text, int color, Drawable photo) {
        this.text = text;
        this.color = color;
        this.photo = photo;
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }

    public Drawable getPhoto() {
        return photo;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setPhoto(Drawable photo) {
        this.photo = photo;
    }
}

