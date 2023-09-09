package com.example.cuentas.ui.dashboard;

public class TriCountCard {
    private String dinero;
    private String result;

    public TriCountCard(String dinero) {
        this.dinero = dinero;
        this.result = "0";
    }

    public String getDinero() {
        return dinero;
    }

    public void setDinero(String dinero) {
        this.dinero = dinero;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
