package com.gamemanager.gui.components;

import com.gamemanager.models.Game;
import com.gamemanager.utils.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class GameCard extends JPanel {
    private Game game;

    public GameCard(Game game, Color backgroundColor, Color accentColor) {
        this.game = game;
        setupCard(backgroundColor, accentColor);
    }

    private void setupCard(Color bgColor, Color accentColor) {
        setLayout(new BorderLayout(0, 5));
        setBackground(bgColor);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        setPreferredSize(new Dimension(200, 220));

        add(createImagePanel(), BorderLayout.NORTH);
        add(createInfoPanel(), BorderLayout.CENTER);
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(190, 110));
        imagePanel.setBackground(new Color(32, 7, 87));

        JLabel contentLabel = new JLabel("", JLabel.CENTER);
        contentLabel.setFont(new Font("Consolas",
                Font.BOLD,
                14));
        contentLabel.setForeground(Color.WHITE);
        contentLabel.setOpaque(true);

        if (game.getImageUrl() != null && !game.getImageUrl().isEmpty()) {
            ImageLoader.loadImageAsync(game.getImageUrl(), contentLabel, 190, 110);
            contentLabel.setBackground(new Color(51, 28, 127));
        } else {
            contentLabel.setText("<html><center>" + game.getTitle() + "</center></html>");
            contentLabel.setBackground(new Color(68, 37, 167));
            contentLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        }

        imagePanel.add(contentLabel);
        return imagePanel;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(getBackground());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        JLabel idLabel = createInfoLabel("ID: " + game.getId(),
                Font.BOLD,
                11,
                new Color(109, 196, 221));
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (game.getImageUrl() != null && !game.getImageUrl().isEmpty()) {
            JLabel titleLabel = createInfoLabel(formatTitle(game.getTitle()),
                    Font.BOLD,
                    9,
                    Color.white);
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(titleLabel);
            infoPanel.add(Box.createVerticalStrut(2));
        }

        JLabel dateLabel = createInfoLabel("- " + formatDate(game.getReleaseDate()),
                Font.PLAIN,
                9,
                Color.LIGHT_GRAY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel genreLabel = createInfoLabel("- " + game.getGenre(),
                Font.PLAIN,
                9,
                new Color(127, 83, 216));
        genreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(idLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createVerticalStrut(1));
        infoPanel.add(genreLabel);

        if (game.getRating() > 0) {
            infoPanel.add(Box.createVerticalStrut(1));
            JLabel ratingLabel = createInfoLabel("⭐ " + String.format("%.1f", game.getRating()),
                    Font.PLAIN,
                    9,
                    new Color(255, 178, 0));
            ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(ratingLabel);
        }

        if (game.getPlatform() != null && !game.getPlatform().isEmpty()) {
            infoPanel.add(Box.createVerticalStrut(1));
            JLabel platformLabel = createInfoLabel("- " + game.getPlatform(),
                    Font.PLAIN,
                    9,
                    Color.ORANGE);
            platformLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(platformLabel);
        }

        infoPanel.add(Box.createVerticalGlue());

        return infoPanel;
    }

    private String formatTitle(String title) {
        if (title.length() > 18) {
            return "<html>" + title.substring(0, 15) + "...</html>";
        }
        return title;
    }

    private String formatDate(String date) {
        if (date == null || date.equals("Unknown") || date.isEmpty()) {
            return "Sem data";
        }
        return date;
    }

    private JLabel createInfoLabel(String text, int style, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Consolas", style, 16));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public Game getGame() {
        return game;
    }
}