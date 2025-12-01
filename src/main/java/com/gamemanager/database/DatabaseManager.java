package com.gamemanager.database;

import com.gamemanager.models.Game;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection connection;

    public void connect() {
        try {
            String url = "jdbc:sqlite:games.db";
            connection = DriverManager.getConnection(url);
            createTable();
            updateTableSchema();
            System.out.println("Conectado com sucesso!");
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS games(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL UNIQUE,
                release_date TEXT,
                genre TEXT,
                image_url TEXT,
                background_image_url TEXT,
                platform TEXT,
                rating REAL DEFAULT 0.0
                )""";

    try(
    Statement stmt = connection.createStatement()) {
        stmt.execute(sql);
        System.out.println("Games table ready! ");
    } catch(SQLException e) {
        System.out.println("ERRO: " + e.getMessage());
    }
}

    private void updateTableSchema() {
        try {

            String checkSql = "PRAGMA table_info(games)";
            boolean hasBackgroundImageUrl = false;

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(checkSql)) {

                while (rs.next()) {
                    if ("background_image_url".equals(rs.getString("name"))) {
                        hasBackgroundImageUrl = true;
                        break;
                    }
                }
            }


            if (!hasBackgroundImageUrl) {
                String alterSql = "ALTER TABLE games ADD COLUMN background_image_url TEXT";
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(alterSql);
                    System.out.println("Tabela atualizada com nova coluna!");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar tabela: " + e.getMessage());
        }
    }

    public boolean addGame(Game game) {
        String sql = "INSERT INTO games (title," +
                " release_date," +
                " genre," +
                " image_url," +
                " background_image_url," +
                " platform," +
                " rating)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getReleaseDate());
            pstmt.setString(3, game.getGenre());
            pstmt.setString(4, game.getImageUrl() != null ? game.getImageUrl() : "");
            pstmt.setString(5, game.getBackgroundImageUrl() != null ? game.getBackgroundImageUrl() : "");
            pstmt.setString(6, game.getPlatform() != null ? game.getPlatform() : "");
            pstmt.setDouble(7, game.getRating());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao adicionar o jogo: " + e.getMessage());
            return false;
        }
    }

    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM games ORDER BY title";

        try (Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Game game = createGameFromResultSet(rs);
                games.add(game);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar jogos: " + e.getMessage());
        }
        return games;
    }

    public Game getGameById(int id) {
        String sql = "SELECT * FROM games WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return createGameFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar jogo: " + e.getMessage());
        }
        return null;
    }

    public boolean updateGame(Game game) {
        String sql = "UPDATE games SET title = ?," +
                " release_date = ?," +
                " genre = ?," +
                " image_url = ?," +
                " background_image_url = ?," +
                " platform = ?," +
                " rating = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getReleaseDate());
            pstmt.setString(3, game.getGenre());
            pstmt.setString(4, game.getImageUrl() != null  ? game.getImageUrl() : "");
            pstmt.setString(5, game.getBackgroundImageUrl() != null ? game.getBackgroundImageUrl() : "");
            pstmt.setString(6, game.getPlatform() != null  ? game.getPlatform() : "");
            pstmt.setDouble(7, game.getRating());
            pstmt.setInt(8, game.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar jogo: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteGame(int id) {
        String sql = "DELETE FROM games WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql) ){
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir: " + e.getMessage());
            return false;
        }
    }

    public Game createGameFromResultSet(ResultSet rs) throws SQLException {
        Game game = new Game(
                rs.getString("title"),
                rs.getString("release_date"),
                rs.getString("genre")
        );
        game.setId(rs.getInt("id"));
        game.setImageUrl(rs.getString("image_url"));
        game.setBackgroundImageUrl(rs.getString("background_image_url"));
        game.setPlatform(rs.getString("platform"));
        game.setRating(rs.getDouble("rating"));
        return game;
    }
}