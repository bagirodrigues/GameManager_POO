package com.gamemanager.gui.themes;

import java.awt.*;

public class ColorScheme {
    private Color backgroundColor;
    private Color cardColor;
    private Color accentColor;
    private Color textColor;
    private Color successColor;
    private Color warningColor;
    private Color errorColor;

    public ColorScheme(Color backgroundColor,
                       Color cardColor,
                       Color accentColor,
                       Color textColor,
                       Color successColor,
                       Color warningColor,
                       Color errorColor) {
        this.backgroundColor = backgroundColor;
        this.cardColor = cardColor;
        this.accentColor = accentColor;
        this.textColor = textColor;
        this.successColor = successColor;
        this.warningColor = warningColor;
        this.errorColor = errorColor;
    }

    public Color getBackgroundColor() {return backgroundColor;}
    public Color getCardColor() {return cardColor;}
    public Color getAccentColor() {return accentColor;}
    public Color getTextColor() {return textColor;}
    public Color getSuccessColor() {return successColor;}
    public Color getWarningColor() {return warningColor;}
    public Color getErrorColor() {return errorColor;}

    public static ColorScheme GAMER_THEME() {
        return new ColorScheme(
                new Color(19, 9, 66),
                new Color(43, 28, 127),
                new Color(70, 46, 208),
                Color.WHITE,
                new Color(70, 46, 208),
                new Color(7, 184, 223, 199),
                new Color(43, 28, 127)
        );
    }
}
