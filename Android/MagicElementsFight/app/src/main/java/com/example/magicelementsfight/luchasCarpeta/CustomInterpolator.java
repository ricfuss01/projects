package com.example.magicelementsfight.luchasCarpeta;

import android.view.animation.Interpolator;

public class CustomInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        // Mapea el rango de tiempo de la animación al rango de valores deseados
        float minValue = 0f;  // Valor mínimo deseado
        float maxValue = 100f;  // Valor máximo deseado

        float interpolatedValue = minValue + (maxValue - minValue) * input;

        return interpolatedValue;
    }
}
