package com.gamemanager.models;

import java.util.ArrayList;
import java.util.List;

public class GameCollection {
    private List<Game> games;
    private String name;

    public GameCollection(String name) {
        this.name = name;
        this.games = new ArrayList<>();
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public boolean removeGame(int gameId) {
        return games.removeIf(game -> game.getId() == gameId);
    }

    public List<Game> getGamesByGenre(String genre) {
        return games.stream()
                .filter(game -> game.getGenre().equalsIgnoreCase(genre))
                .toList();
    }

    public List<Game> getAllGames() {
        return new ArrayList<>(games);
    }

    public int getTotalGames() {
        return games.size();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
