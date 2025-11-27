package com.gamemanager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient;

import java.util.Scanner;
import java.util.List;

public class GameService {
    private DatabaseManager dbManager;
    private Scanner scanner;
    private OkHttpClient httpClient;
    // Substitua "SUACHAVE" por uma chave pessoal -> acesse rawg.io para ter acesso a sua.
    // Se tentar rodar sem uma chave pessoal o console abre, mas dá erro ao buscar o jogo na API.
    private final String API_KEY = "SUACHAVEAQUI"; //chave pessoal

    public GameService() {
        this.dbManager = new DatabaseManager();
        this.scanner = new Scanner(System.in);
        this.httpClient = new OkHttpClient();
        dbManager.connect();
        //testAPI();
    }

    public List<Game> getAllGamesFromDatabase() {
        return dbManager.getAllGames();
    }

    public boolean addGameToDatabase(Game game) {
        return dbManager.addGame(game);
    }

    public boolean updateGameInDatabase(Game game) {
        return dbManager.updateGame(game);
    }

    public boolean deleteGameFromDatabase(int id) {
        return dbManager.deleteGame(id);
    }

    public Game getGameFromDatabase(int id) {
        return dbManager.getGameById(id);
    }

    // Buscar jogo na API RAWG
    public Game searchGameFromApi(String gameName) {
        try {
            String url = "https://api.rawg.io/api/games?key=" + API_KEY + "&search=" + gameName.replace(" ", "%20");
            System.out.println(" Buscando: " + url.replace(API_KEY, "***"));

            Request request = new Request.Builder().url(url).build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.out.println("Erro HTTP: " + response.code());
                    return null;
                }

                String jsonResponse = response.body().string();
                System.out.println("Resposta: " + jsonResponse.substring(0, Math.min(200, jsonResponse.length())));

                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

                //verificar se a resposta tem a estrutura esperada
                if (!jsonObject.has("results")) {
                    System.out.println("Resposta inesperada da API");
                    return null;
                }

                JsonArray results = jsonObject.getAsJsonArray("results");

                if (results.size() > 0) {
                    JsonObject gameData = results.get(0).getAsJsonObject();

                    String title = gameData.get("name").getAsString();
                    String releaseDate = gameData.has("released") ? gameData.get("released").getAsString() : "Unknown";

                    //Extrair gênero
                    String genre = "";
                    if (gameData.has("genres") && gameData.get("genres").isJsonArray()) {
                        JsonArray genres = gameData.getAsJsonArray("genres");
                        if (genres.size() > 0) {
                            JsonObject firstGenre = genres.get(0).getAsJsonObject();
                            genre = firstGenre.get("name").getAsString();
                        }
                    }

                    System.out.println("Jogo encontrado: " + title);
                    return new Game(title, releaseDate, genre);
                } else {
                    System.out.println("Nenhum jogo encontrado. ");
                    return null;
                }
            }
        } catch (Exception e) {
            System.out.println("Erro na API: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //Menu principal
    public void showMainMenu() {
        while (true) {
            System.out.println("\n" +
                    "                           \n" +
                    "        GAME MANAGER       \n" +
                    "                           \n" +
                    "  1. Buscar jogo na API    \n" +
                    "  2. Adicionar jogo manual \n" +
                    "  3. Ver todos os jogos    \n" +
                    "  4. Atualizar os jogos    \n" +
                    "  5. Excluir jogo          \n" +
                    "  0. Sair                  \n" +
                    "                           \n" +
                    "Escolha uma das opções acima: ");

            int option = getIntInput();
            scanner.nextLine();

            switch (option) {
                case 1 -> addGameFromAPIConsole();
                case 2 -> addManualGameConsole();
                case 3 -> viewAllGamesConsole();
                case 0 -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void addGameFromAPIConsole() {
        System.out.print("Digite o nome do jogo: ");
        String gameName = scanner.nextLine();

        Game game = searchGameFromApi(gameName);
        if (game != null) {
            if (addGameToDatabase(game)) {
                System.out.println("Jogo adicionado: " + game.getTitle());
            } else {
                System.out.println("Erro ao adicionar jogo.");
            }
        } else {
            System.out.println("Jogo não encontrado na API.");
        }
    }

    private void addManualGameConsole() {
        System.out.print("Título: ");
        String title = scanner.nextLine();

        System.out.print("Data (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        System.out.print("Gênero: ");
        String genre = scanner.nextLine();

        Game game = new Game(title, date, genre);
        if (addGameToDatabase(game)) {
            System.out.println("Jogo adicionado com sucesso!");
        } else {
            System.out.println("Erro ao adicionar jogo.");
        }
    }

    private void viewAllGamesConsole() {
        List<Game> games = getAllGamesFromDatabase();
        if (games.isEmpty()) {
            System.out.println("Nenhum jogo cadastrado.");
        } else {
            System.out.println("\n SEUS JOGOS: ");
            for (Game game : games) {
                System.out.println("• " + game.getTitle() + " (" + game.getReleaseDate() + ") - " + game.getGenre());
            }
            System.out.println("Total: " + games.size() + " jogos");
        }
    }

    private int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Digite um número: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    //Testes de metodo
    /*
    public void testAPI() {
        System.out.println("Testando API...");
        Game game = searchGameFromApi("minecraft");
        if (game != null) {
            System.out.println("API funcionando: " + game.getTitle());
        } else {
            System.out.println("API com problemas");
        }
    }

    public static void main(String[] args) {
        GameService service = new GameService();
        service.testAPI();

        System.out.println("\nTeste de funcionalidades:");

        // Teste de adição
        Game testGame = new Game("Jogo Teste", "2024-01-01", "Ação");
        boolean added = service.addGameToDatabase(testGame);
        System.out.println("Jogo adicionado: " + added);

        // Teste de listagem
        List<Game> games = service.getAllGamesFromDatabase();
        System.out.println("Total de jogos: " + games.size());

        // Teste de exclusão (se houver jogos)
        if (!games.isEmpty()) {
            int lastId = games.get(games.size() - 1).getId();
            boolean deleted = service.deleteGameFromDatabase(lastId);
            System.out.println("Jogo excluído: " + deleted);
        }
    }*/
    //Fim do teste

    //Teste via console
    /*
    public static void main(String[] args) {
        GameService service = new GameService();
        service.showMainMenu();
    }*/

}