package com.gamemanager.utils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImageLoader {

    public static void loadImageAsync(String imageUrl, JLabel imageLabel, int width, int height) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            setPlaceholderImage(imageLabel);
            return;
        }

        new Thread(() -> {
            try {
                URL url = new URL(imageUrl);
                BufferedImage originalImage = ImageIO.read(url);

                if (originalImage != null) {
                    Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(scaledImage);

                    SwingUtilities.invokeLater(() -> {
                        imageLabel.setIcon(icon);
                        imageLabel.setText("");
                    });
                } else {
                    setPlaceholderImage(imageLabel);
                }
            } catch (Exception e) {
                System.out.println("Erro ao carregar imagem: " + e.getMessage());
                setPlaceholderImage(imageLabel);
            }
        }).start();
    }

    private static void setPlaceholderImage(JLabel imageLabel) {
        SwingUtilities.invokeLater(() -> {
            imageLabel.setText("🎮");
            imageLabel.setFont(new Font("Consolas", Font.BOLD, 48));
            imageLabel.setForeground(Color.WHITE);
            imageLabel.setOpaque(true);
            imageLabel.setBackground(new Color(20, 20, 40));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        });
    }

    public static ImageIcon createRoundedImageIcon(ImageIcon originalIcon, int cornerRadius) {
        if (originalIcon == null) {
            return null;
        }

        BufferedImage image = new BufferedImage(
                originalIcon.getIconWidth(),
                originalIcon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 0));
        g2.fillRoundRect(0, 0, image.getWidth(), image.getHeight(), cornerRadius, cornerRadius);

        g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, image.getWidth(), image.getHeight(), cornerRadius, cornerRadius));
        g2.drawImage(originalIcon.getImage(), 0, 0, null);

        g2.dispose();
        return new ImageIcon(image);
    }

    public static boolean isValidImageUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        return url.matches("^https?://.+\\.(jpg|jpeg|png|gif|bmp|webp)(\\?.*)?$");
    }
}