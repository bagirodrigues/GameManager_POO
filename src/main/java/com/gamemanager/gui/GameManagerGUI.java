package com.gamemanager.gui;

import com.gamemanager.models.Game;
import com.gamemanager.services.GameService;
import com.gamemanager.gui.components.GameGridPanel;
import com.gamemanager.gui.themes.ColorScheme;
import com.gamemanager.gui.themes.GamerTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameManagerGUI extends JFrame {
    private GameService gameService;
    private GameGridPanel gameGridPanel;
    private ColorScheme colors;

    public GameManagerGUI() {
        this.colors = ColorScheme.GAMER_THEME();
        this.gameService = new GameService();
        initializeGUI();
    }

    private void initializeGUI() {
        setupFrame();
        setupComponents();
        loadInitialData();
    }

    private void setupFrame() {
        setTitle("Game Collection Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(880, 800);
        setMinimumSize(new Dimension(860, 800));
        setMaximumSize(new Dimension(860, 800));
        setResizable(false);

        setLocationRelativeTo(null);

        GamerTheme.applyTheme(colors);
        getContentPane().setBackground(colors.getBackgroundColor());
    }

    private void setupComponents() {
        setLayout(new BorderLayout(10, 10));

        add(createHeaderPanel(), BorderLayout.NORTH);

        gameGridPanel = new GameGridPanel(colors);

        JScrollPane scrollPane = new JScrollPane(gameGridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(colors.getBackgroundColor());

        add(scrollPane, BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(colors.getBackgroundColor());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("SUA COLEÇÃO DE JOGOS", JLabel.CENTER);
        titleLabel.setFont(new Font("Orbitron", Font.BOLD, 32));
        titleLabel.setForeground(colors.getAccentColor());

        JLabel subtitleLabel = new JLabel("\nGerencie sua biblioteca de jogos", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Exo 2", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(0x8228BD));

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        actionPanel.setBackground(colors.getBackgroundColor());
        actionPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JButton searchApiBtn = createRoundedButton("Buscar na API", e -> searchGameFromAPI());
        JButton addManualBtn = createRoundedButton("Add Manual", e -> addManualGame());
        JButton refreshBtn = createRoundedButton("Atualizar", e -> refreshCollection());
        JButton updateBtn = createRoundedButton("Editar Jogo", e -> updateGame());
        JButton deleteBtn = createRoundedButton("Excluir Jogo", e -> deleteGame());

        actionPanel.add(searchApiBtn);
        actionPanel.add(addManualBtn);
        actionPanel.add(refreshBtn);
        actionPanel.add(updateBtn);
        actionPanel.add(deleteBtn);

        return actionPanel;
    }

    private JButton createRoundedButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(colors.getAccentColor().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(colors.getAccentColor().brighter());
                } else {
                    g2.setColor(colors.getAccentColor());
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2.setColor(colors.getAccentColor().darker());
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 25, 25);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.YELLOW);
        button.setFont(new Font("Consolas", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 45));
        button.addActionListener(action);

        return button;
    }

    private void loadInitialData() {
        refreshCollection();
    }

    private void refreshCollection() {
        List<Game> games = gameService.getAllGamesFromDatabase();
        gameGridPanel.loadGames(games);

        if (!games.isEmpty()) {
            setTitle("Game Collection Manager - " + games.size() + " jogos");
        } else {
            setTitle("Game Collection Manager");
        }
    }

    private void searchGameFromAPI() {
        String searchText = JOptionPane.showInputDialog(
                this,
                "Digite o nome do jogo para buscar na API:",
                "Buscar na API",
                JOptionPane.QUESTION_MESSAGE
        );

        if (searchText != null && !searchText.trim().isEmpty()) {
            setTitle("Buscando jogo...");

            new Thread(() -> {
                try {
                    Game game = gameService.searchGameFromApi(searchText);

                    SwingUtilities.invokeLater(() -> {
                        if (game != null) {
                            showGameSearchResult(game);
                        } else {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Jogo não encontrado na API.",
                                    "Jogo Não Encontrado",
                                    JOptionPane.WARNING_MESSAGE
                            );
                        }
                        refreshCollection();
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(
                                this,
                                "Erro ao buscar jogo: " + e.getMessage(),
                                "Erro na Busca",
                                JOptionPane.ERROR_MESSAGE
                        );
                        refreshCollection();
                    });
                }
            }).start();
        }
    }

    private void addManualGame() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.setBackground(colors.getCardColor());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(createStyledLabel("Título do Jogo:"));
        JTextField titleField = createStyledTextField();
        panel.add(titleField);

        panel.add(createStyledLabel("Data de Lançamento:"));
        JTextField dateField = createStyledTextField();
        panel.add(dateField);

        panel.add(createStyledLabel("Gênero:"));
        JTextField genreField = createStyledTextField();
        panel.add(genreField);

        panel.add(createStyledLabel("Plataforma (opcional):"));
        JTextField platformField = createStyledTextField();
        panel.add(platformField);

        panel.add(createStyledLabel("URL da Imagem (opcional):"));
        JTextField imageField = createStyledTextField();
        panel.add(imageField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Adicionar Jogo Manualmente",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Título é obrigatório!", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Game game = new Game(
                    titleField.getText().trim(),
                    dateField.getText().trim(),
                    genreField.getText().trim()
            );

            if (!platformField.getText().trim().isEmpty()) {
                game.setPlatform(platformField.getText().trim());
            }

            if (!imageField.getText().trim().isEmpty()) {
                game.setImageUrl(imageField.getText().trim());
            }

            boolean success = gameService.addGameToDatabase(game);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Jogo adicionado com sucesso!", "Sucesso!",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshCollection();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao adicionar jogo. Tente um título diferente.",
                        "Erro!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateGame() {
        String gameId = JOptionPane.showInputDialog(
                this,
                "Digite o ID do jogo para editar:",
                "Editar Jogo",
                JOptionPane.QUESTION_MESSAGE
        );

        if (gameId != null && !gameId.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(gameId.trim());
                Game existingGame = gameService.getGameFromDatabase(id);

                if (existingGame == null) {
                    JOptionPane.showMessageDialog(this,
                            "Jogo não encontrado!",
                            "Erro!",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                showEditForm(existingGame);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "ID inválido!",
                        "Erro!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditForm(Game game) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.setBackground(colors.getCardColor());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(createStyledLabel("Editando: " + game.getTitle()));

        panel.add(createStyledLabel("Novo Título:"));
        JTextField titleField = createStyledTextField();
        titleField.setText(game.getTitle());
        panel.add(titleField);

        panel.add(createStyledLabel("Nova Data:"));
        JTextField dateField = createStyledTextField();
        dateField.setText(game.getReleaseDate());
        panel.add(dateField);

        panel.add(createStyledLabel("Novo Gênero:"));
        JTextField genreField = createStyledTextField();
        genreField.setText(game.getGenre());
        panel.add(genreField);

        panel.add(createStyledLabel("Nova Plataforma (opcional):"));
        JTextField platformField = createStyledTextField();
        platformField.setText(game.getPlatform() != null ? game.getPlatform() : "");
        panel.add(platformField);

        panel.add(createStyledLabel("Nova URL da Imagem (opcional):"));
        JTextField imageField = createStyledTextField();
        imageField.setText(game.getImageUrl() != null ? game.getImageUrl() : "");
        panel.add(imageField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Editar Jogo - ID: " + game.getId(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            if (!titleField.getText().trim().isEmpty()) {
                game.setTitle(titleField.getText().trim());
            }
            if (!dateField.getText().trim().isEmpty()) {
                game.setReleaseDate(dateField.getText().trim());
            }
            if (!genreField.getText().trim().isEmpty()) {
                game.setGenre(genreField.getText().trim());
            }
            game.setPlatform(platformField.getText().trim());
            game.setImageUrl(imageField.getText().trim());

            if (gameService.updateGameInDatabase(game)) {
                JOptionPane.showMessageDialog(this,
                        "Jogo atualizado!",
                        "Sucesso!",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshCollection();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao atualizar.",
                        "Erro!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteGame() {
        String gameId = JOptionPane.showInputDialog(
                this,
                "Digite o ID do jogo para excluir:",
                "Excluir Jogo",
                JOptionPane.QUESTION_MESSAGE
        );

        if (gameId != null && !gameId.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(gameId.trim());
                Game game = gameService.getGameFromDatabase(id);

                if (game == null) {
                    JOptionPane.showMessageDialog(this,
                            "Jogo não encontrado!",
                            "Erro!",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Tem certeza que deseja excluir?\n\n" +
                                game.getTitle() + "\n" +
                                game.getReleaseDate() + " - " + game.getGenre() + "\n" +
                                "ID: " + game.getId(),
                        "Confirmar Exclusão",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    if (gameService.deleteGameFromDatabase(id)) {
                        JOptionPane.showMessageDialog(this,
                                "Jogo excluído!",
                                "Sucesso!",
                                JOptionPane.INFORMATION_MESSAGE);
                        refreshCollection();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Erro ao excluir.",
                                "Erro!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "ID inválido!",
                        "Erro!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showGameSearchResult(Game game) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(colors.getCardColor());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("<html><h2>" + game.getTitle() + "</h2></html>");
        titleLabel.setForeground(colors.getAccentColor());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(colors.getCardColor());

        infoPanel.add(createInfoLabel("Data: " + game.getReleaseDate()));
        infoPanel.add(createInfoLabel("Gênero: " + game.getGenre()));

        if (game.getRating() > 0) {
            infoPanel.add(createInfoLabel("Avaliação: " + game.getRating()));
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(350, 120));

        int option = JOptionPane.showConfirmDialog(this,
                panel,
                "Jogo Encontrado - Adicionar?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            boolean success = gameService.addGameToDatabase(game);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Jogo adicionado com sucesso!",
                        "Sucesso!",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshCollection();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao adicionar jogo.",
                        "Erro!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(colors.getTextColor());
        label.setFont(new Font("Consolas", Font.BOLD, 12));
        return label;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(colors.getTextColor());
        label.setFont(new Font("Consolas", Font.PLAIN, 12));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setBackground(colors.getCardColor());
        field.setForeground(colors.getTextColor());
        field.setCaretColor(colors.getTextColor());
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colors.getAccentColor(), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        field.setFont(new Font("Consolas", Font.PLAIN, 12));
        return field;
    }

    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                new GameManagerGUI().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Erro ao iniciar: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}