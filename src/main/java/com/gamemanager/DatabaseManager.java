package com.gamemanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection connection;

    //conectar ao banco de dados
    public void connect() {
        try {
            String url = "jdbc:sqlite:games.db";
            connection = DriverManager.getConnection(url);
            createTable();
            System.out.println("BANCO DE DADOS CONECTADO! ");
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    //CRUD

    //criação da tabela, caso não exista
    private void createTable() {
        String sql = """ 
            CREATE TABLE IF NOT EXISTS games(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL UNIQUE,
                release_date TEXT,
                genre TEXT
                )""";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("TABELA PRONTA!");

        } catch (SQLException e) {
            System.out.println("ERRO NA TABELA: " + e.getMessage());
        }
    }

    //CREATE
    public boolean addGame(Game game) {
        String sql = "INSERT INTO games " +
                "(title, release_date, genre) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getReleaseDate());
            pstmt.setString(3, game.getGenre());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao adicionar o jogo: " + e.getMessage());
            return false;
        }
    }

    //READ - buscar todos os jogos
    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM games ORDER BY title";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Game game = new Game(
                        rs.getString("title"),
                        rs.getString("release_date"),
                        rs.getString("genre")
                );
                game.setId(rs.getInt("id"));
                games.add(game);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar jogo: " + e.getMessage());
        } return games;
    }

    //READ - buscar jogo por ID
    public Game getGameById(int id) {
        String sql = "SELECT * FROM games WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Game game = new Game(
                        rs.getString("title"),
                        rs.getString("release_date"),
                        rs.getString("genre")
                );
                game.setId(rs.getInt("id"));
                return game;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar jogo: " + e.getMessage());
        } return null;
    }

    //UPDATE
    public boolean updateGame(Game game) {
        String sql = "UPDATE games SET " +
                "title = ?, " +
                "release_date = ?, " +
                "genre = ?, " +
                "WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getReleaseDate());
            pstmt.setString(3, game.getGenre());
            pstmt.setInt(4, game.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar jogo: " + e.getMessage());
            return false;
        }
    }

    //DELETE
    public boolean deleteGame(int id) {
        String sql = "DELETE FROM games WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir: " + e.getMessage());
            return false;
        }
    }


}
