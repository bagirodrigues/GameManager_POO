package com.gamemanager;

public class Game {
    private int id;
    private String title;
    private String releaseDate;
    private String genre;

    //Construtor API
    public Game(String title, String releaseDate, String genre) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.genre = genre;
    }

    //Getters e Setters
    //ID
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    //title
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    //releaseDate
    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    //genre
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }


    @Override
    public String toString() {
        return String.format("%s | %s | %s", title, releaseDate, genre);

    }
}
