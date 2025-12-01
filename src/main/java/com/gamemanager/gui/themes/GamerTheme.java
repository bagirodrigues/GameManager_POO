package com.gamemanager.gui.themes;

import javax.swing.*;
import java.awt.*;

public class GamerTheme {
    public static void applyTheme(ColorScheme colors) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            UIManager.put("Panel.background", colors.getBackgroundColor());
            UIManager.put("OptionPane.background", colors.getBackgroundColor());
            UIManager.put("OptionPane.messageForeground", colors.getTextColor());

            UIManager.put("Button.background", colors.getAccentColor());
            UIManager.put("Button.foreground", Color.lightGray);
            UIManager.put("Button.focus", colors.getAccentColor().darker());
            UIManager.put("Button.select", colors.getAccentColor().brighter());

            UIManager.put("TextField.background", colors.getCardColor());
            UIManager.put("TextField.foreground", colors.getTextColor());
            UIManager.put("TextField.caretForeground", colors.getTextColor());
            UIManager.put("TextField.border", BorderFactory.createLineBorder(colors.getAccentColor(), 1));

            UIManager.put("ComboBox.background", colors.getCardColor());
            UIManager.put("ComboBox.foreground", colors.getTextColor());
            UIManager.put("ComboBox.selectionBackground", colors.getAccentColor());
            UIManager.put("ComboBox.selectionForeground", Color.lightGray);

            UIManager.put("Label.foreground", colors.getTextColor());

            UIManager.put("ScrollPane.background", colors.getBackgroundColor());
            UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());

            UIManager.put("Viewport.background", colors.getBackgroundColor());
        } catch (Exception e) {
            System.err.println("Error loading theme" + e.getMessage());
        }
    }
}
