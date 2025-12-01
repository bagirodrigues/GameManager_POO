package com.gamemanager.services;

import com.gamemanager.database.DatabaseManager;
import com.gamemanager.models.Game;

import java.util.List;
import java.util.Scanner;

public class GameService {
    private DatabaseManager dbManager;
    private ApiService apiService;
    private Scanner scanner;

    public GameService() {
        this.dbManager = new DatabaseManager();
        this.apiService = new ApiService("d934e04617454fdbafa6494bcfb3548c"); //apiKey chave pessoal, para facilitar o trabalho deixei ela no código, mas a recomendação é criar a prórpria, pro motivos de segurança.
        this.scanner = new Scanner(System.in);
        initializeDatabase();
    }

    private void initializeDatabase() {
        dbManager.connect();
        System.out.println("GameService inicializado com sucesso!");
    }

    public List<Game> getAllGamesFromDatabase() {
        return dbManager.getAllGames();
    }

    public boolean addGameToDatabase(Game game) {
        if (game == null || game.getTitle() == null || game.getTitle().trim().isEmpty()) {
            System.out.println("Não é possível adicionar jogo vazio ou nulo");
            return false;
        }

        boolean success = dbManager.addGame(game);
        if (success) {
            System.out.println("Jogo adicionado ao banco: " + game.getTitle());
        } else {
            System.out.println("Falha ao adicionar jogo: " + game.getTitle());
        }
        return success;
    }

    public boolean updateGameInDatabase(Game game) {
        if (game == null || game.getId() <= 0) {
            System.out.println("Jogo inválido para atualização");
            return false;
        }

        boolean success = dbManager.updateGame(game);
        if (success) {
            System.out.println("Jogo atualizado: " + game.getTitle());
        } else {
            System.out.println("Falha ao atualizar jogo: " + game.getTitle());
        }
        return success;
    }

    public boolean deleteGameFromDatabase(int id) {
        if (id <= 0) {
            System.out.println("ID de jogo inválido");
            return false;
        }

        boolean success = dbManager.deleteGame(id);
        if (success) {
            System.out.println("Jogo excluído com ID: " + id);
        } else {
            System.out.println("Falha ao excluir jogo com ID: " + id);
        }
        return success;
    }

    public Game getGameFromDatabase(int id) {
        if (id <= 0) {
            System.out.println("ID de jogo inválido");
            return null;
        }

        Game game = dbManager.getGameById(id);
        if (game == null) {
            System.out.println("Jogo não encontrado com ID: " + id);
        }
        return game;
    }

    public Game searchGameFromApi(String gameName) {
        if (gameName == null || gameName.trim().isEmpty()) {
            System.out.println("Nome do jogo não pode estar vazio");
            return null;
        }

        System.out.println("Buscando na API: " + gameName);
        Game game = apiService.searchGame(gameName);

        if (game != null) {
            System.out.println("Jogo encontrado: " + game.getTitle());
        } else {
            System.out.println("Jogo não encontrado na API: " + gameName);
        }

        return game;
    }

    public List<Game> searchGamesByTitle(String searchTerm) {
        List<Game> allGames = getAllGamesFromDatabase();
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return allGames;
        }

        return allGames.stream()
                .filter(game -> game.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
                .toList();
    }

    public List<Game> getGamesByGenre(String genre) {
        List<Game> allGames = getAllGamesFromDatabase();
        if (genre == null || genre.equals("Todos")) {
            return allGames;
        }

        return allGames.stream()
                .filter(game -> game.getGenre().equalsIgnoreCase(genre))
                .toList();
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
        }
        System.out.println("GameService resources cleaned up.");
    }
}