package com.gamemanager.gui.components;

import com.gamemanager.models.Game;
import com.gamemanager.gui.themes.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class GameGridPanel extends JPanel {
    private List<GameCard> gameCards;
    private ColorScheme colors;
    private JPanel gridPanel;

    public GameGridPanel(ColorScheme colors) {
        this.colors = colors;
        this.gameCards = new ArrayList<>();
        setupPanel();
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(colors.getBackgroundColor());

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 4, 10, 15));
        gridPanel.setBackground(colors.getBackgroundColor());
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(gridPanel, BorderLayout.CENTER);
    }

    public void loadGames(List<Game> games) {
        gridPanel.removeAll();
        gameCards.clear();

        if (games.isEmpty()) {
            showEmptyState();
        } else {
            List<Game> sortedGames = games.stream()
                    .sorted(Comparator.comparingInt(Game::getId))
                    .toList();

            for (Game game : sortedGames) {
                GameCard card = new GameCard(game, colors.getCardColor(), colors.getAccentColor());
                gameCards.add(card);
                gridPanel.add(card);
            }

            int totalCards = sortedGames.size();
            int cardsPerRow = 4;
            int emptySlotsNeeded = (cardsPerRow - (totalCards % cardsPerRow)) % cardsPerRow;

            for (int i = 0; i < emptySlotsNeeded; i++) {
                gridPanel.add(createEmptySlot());
            }
        }

        revalidate();
        repaint();
    }

    private JPanel createEmptySlot() {
        JPanel emptySlot = new JPanel();
        emptySlot.setOpaque(false);
        emptySlot.setPreferredSize(new Dimension(200, 220));
        return emptySlot;
    }

    private void showEmptyState() {
        removeAll();

        setLayout(new BorderLayout());

        JLabel emptyLabel = new JLabel(
                "<html><center>" +
                        "<font size='5'>🎮</font><br><br>" +
                        "<b>Sua coleção está vazia:(</b><br>" +
                        "Adicione alguns jogos para começar." +
                        "</center></html>",
                JLabel.CENTER
        );
        emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emptyLabel.setForeground(new Color(0xA432E6));
        emptyLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));

        add(emptyLabel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    public void refresh() {
        revalidate();
        repaint();
    }
}