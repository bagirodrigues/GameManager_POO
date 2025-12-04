package com.gamemanager.services;

import com.gamemanager.models.Game;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiService {
    private OkHttpClient httpClient;
    private String apiKey;

    public ApiService(String apiKey) {
        this.httpClient = new OkHttpClient();
        this.apiKey = "SUA_CHAVE_API"; //A apiKey é uma chave pessoal, substitua para a sua própria chave da API, acesse rawg.io
    }

    public Game searchGame(String gameName) {
        try {
            String url = buildSearchUrl(gameName);
            System.out.println("Buscando na API: " + url.replace(apiKey, "***"));

            Request request = new Request.Builder().url(url).build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.out.println("Erro HTTP: " + response.code());
                    return null;
                }

                String jsonResponse = response.body().string();
                System.out.println("Resposta API: " + jsonResponse.substring(0, Math.min(200, jsonResponse.length())));

                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

                if (!jsonObject.has("results")) {
                    System.out.println("Resposta inesperada da API");
                    return null;
                }

                JsonArray results = jsonObject.getAsJsonArray("results");

                if (results.size() > 0) {
                    JsonObject gameData = results.get(0).getAsJsonObject();
                    return createGameFromJson(gameData);
                } else {
                    System.out.println("Nenhum jogo encontrado na API");
                    return null;
                }
            }
        } catch (Exception e) {
            System.out.println("Erro na API: " + e.getMessage());
            return null;
        }
    }

    private String buildSearchUrl(String gameName) {
        return "https://api.rawg.io/api/games?key=" + apiKey +
                "&search=" + gameName.replace(" ", "%20") +
                "&page_size=1";
    }

    private Game createGameFromJson(JsonObject gameData) {
        String title = gameData.get("name").getAsString();
        String releaseDate = gameData.has("released") ? gameData.get("released").getAsString() : "Unknown";

        String genre = extractGenre(gameData);
        String imageUrl = extractImageUrl(gameData);
        String backgroundImageUrl = extractBackgroundImage(gameData);
        double rating = extractRating(gameData);

        System.out.println("Jogo encontrado: " + title);
        System.out.println("Data: " + releaseDate);
        System.out.println("Gênero: " + genre);
        System.out.println("Imagem: " + imageUrl);
        System.out.println("Rating: " + rating);

        Game game = new Game(title, releaseDate, genre, imageUrl, backgroundImageUrl);
        game.setRating(rating);
        return game;
    }

    private String extractGenre(JsonObject gameData) {
        if (gameData.has("genres") && gameData.get("genres").isJsonArray()) {
            JsonArray genres = gameData.getAsJsonArray("genres");
            if (genres.size() > 0) {
                return genres.get(0).getAsJsonObject().get("name").getAsString();
            }
        }
        return "Unknown";
    }

    private String extractImageUrl(JsonObject gameData) {
        if (gameData.has("short_screenshots") && gameData.get("short_screenshots").isJsonArray()) {
            JsonArray screenshots = gameData.getAsJsonArray("short_screenshots");
            if (screenshots.size() > 1) {
                return screenshots.get(1).getAsJsonObject().get("image").getAsString();
            } else if (screenshots.size() > 0) {
                return screenshots.get(0).getAsJsonObject().get("image").getAsString();
            }
        }

        if (gameData.has("background_image")) {
            return gameData.get("background_image").getAsString();
        }

        return "";
    }

    private String extractBackgroundImage(JsonObject gameData) {
        return gameData.has("background_image") ? gameData.get("background_image").getAsString() : "";
    }

    private double extractRating(JsonObject gameData) {
        return gameData.has("rating") ? gameData.get("rating").getAsDouble() : 0.0;
    }
}