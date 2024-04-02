package ru.alexeyva.todoback.utils;

import java.awt.*;
import java.util.Random;

public class Utils {

    public static String generateSoftColorHex() {
        Random random = new Random();
        float hue = random.nextFloat();
        float saturation = 0.2f; // Low saturation
        float luminance = 1.0f;  // High luminance
        Color color = Color.getHSBColor(hue, saturation, luminance);
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

}
