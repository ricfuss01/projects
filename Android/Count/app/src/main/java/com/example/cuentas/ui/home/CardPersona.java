package com.example.cuentas.ui.home;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CardPersona {
    private String pagar;
    private String pagado;
    private String result;

    public CardPersona(String pagar, String pagado) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        this.pagar = pagar;
        this.pagado = pagado;
        this.result = String.valueOf(decimalFormat.format(Double.parseDouble(this.pagado)-Double.parseDouble(this.pagar)).replace(",","."));
        HomeFragment.enviarTotal(Double.parseDouble(this.pagado));
        HomeFragment.enviarApagar(Double.parseDouble(this.pagar));
    }

    public String getPagar() {
        return pagar;
    }

    public void setPagar(String pagar) {
        this.pagar = pagar;
    }

    public String getPagado() {
        return pagado;
    }

    public void setPagado(String pagado) {
        this.pagado = pagado;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
