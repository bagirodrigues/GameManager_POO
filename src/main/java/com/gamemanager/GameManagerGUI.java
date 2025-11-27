package com.gamemanager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameManagerGUI extends JFrame {
    private GameService gameService;

    private final Color BG_COLOR = Color.BLACK;
    private final Color BTN_COLOR = new Color(50, 50, 50);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color BORDER_COLOR = new Color(80, 80, 80);

    public GameManagerGUI() {
        gameService = new GameService();
        setupGUI();
    }

    private void setupGUI() {
        setTitle("GERENCIADOR DE JOGOS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 480);
        setLocationRelativeTo(null);
        setResizable(false);

        //Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        JLabel titleLabel = new JLabel("Sua Coleção de Jogos", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        //Botões
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 0, 15));
        buttonPanel.setBackground(BG_COLOR);

        JButton btnAddAPI = new JButton("Buscar Jogo na API");
        JButton btnAddManual = new JButton("Adicionar Manualmente");
        JButton btnViewAll = new JButton("Ver Coleção");
        JButton btnDelete = new JButton("Excluir Jogo");
        JButton btnExit = new JButton("Sair");

        setupButton(btnAddAPI);
        btnAddAPI.addActionListener(e -> addGameFromApi());

        setupButton(btnAddManual);
        btnAddManual.addActionListener(e -> addManualGame());

        setupButton(btnViewAll);
        btnViewAll.addActionListener(e -> viewAllGames());

        setupButton(btnDelete);
        btnDelete.addActionListener(e -> deleteGame());

        setupButton(btnExit);
        btnExit.addActionListener(e -> System.exit(0));

        buttonPanel.add(btnAddAPI);
        buttonPanel.add(btnAddManual);
        buttonPanel.add(btnViewAll);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnExit);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
    }

    private void setupButton(JButton button) {
        button.setBackground(BTN_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
    }

    private void addGameFromApi() {
        String gameName = JOptionPane.showInputDialog(this,
                "Nome do jogo para buscar:",
                "Buscar na API", JOptionPane.PLAIN_MESSAGE);

        if (gameName != null && !gameName.trim().isEmpty()) {
            Game game = gameService.searchGameFromApi(gameName);
            if (game != null) {
                gameService.addGameToDatabase(game);
                JOptionPane.showMessageDialog(this,
                        "Jogo '" + game.getTitle() + "' adicionado com sucesso!",
                        "Sucesso", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Jogo não encontrado.",
                        "Erro", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    private void addManualGame() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBackground(new Color(30, 30, 30));

        panel.add(createStyledLabel("Título:"));
        JTextField titleField = createStyledTextField();
        panel.add(titleField);

        panel.add(createStyledLabel("Data (YYYY-MM-DD):"));
        JTextField dateField = createStyledTextField();
        panel.add(dateField);

        panel.add(createStyledLabel("Gênero:"));
        JTextField genreField = createStyledTextField();
        panel.add(genreField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Adicionar Jogo Manualmente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Game game = new Game(titleField.getText(), dateField.getText(), genreField.getText());
            gameService.addGameToDatabase(game);
            JOptionPane.showMessageDialog(this,
                    "Jogo adicionado!",
                    "Sucesso",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void deleteGame() {
        String idStr = JOptionPane.showInputDialog(this,
                "Digite o ID do jogo para excluir:",
                "Excluir Jogo",
                JOptionPane.PLAIN_MESSAGE);

        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Tem certeza que deseja excluir o jogo de ID " + id + "?",
                        "Confirmar Exclusão",
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (gameService.deleteGameFromDatabase(id)) {
                        JOptionPane.showMessageDialog(this,
                                "Jogo excluído com sucesso!",
                                "Sucesso", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Não foi possível excluir. Verifique o ID.",
                                "Erro",
                                JOptionPane.PLAIN_MESSAGE);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "ID inválido. Digite apenas números.",
                        "Erro de Formato",
                        JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    private void viewAllGames() {
        var games = gameService.getAllGamesFromDatabase();
        if (games.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Sua coleção está vazia.",
                    "Coleção",
                    JOptionPane.PLAIN_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Game game : games) {
            sb.append("ID: ").append(game.getId()).append(" - ")
                    .append(game.getTitle())
                    .append(" (").append(game.getReleaseDate()).append(") - ")
                    .append(game.getGenre())
                    .append("\n");
        }
        sb.append("\nTotal: ").append(games.size()).append(" jogos");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        //área de texto da lista de jogos
        textArea.setBackground(new Color(30, 30, 30));
        textArea.setForeground(TEXT_COLOR);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Sua Coleção (" + games.size() + ")",
                JOptionPane.PLAIN_MESSAGE);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setBackground(new Color(30, 30, 30));
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return field;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new GameManagerGUI().setVisible(true);
        });
    }
}