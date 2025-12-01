package com.gamemanager.models;

public class Game {
    private int id;
    private String title;
    private String releaseDate;
    private String genre;
    private String imageUrl;
    private String backgroundImageUrl;
    private String platform;
    private double rating;


    public Game(String title, String releaseDate, String genre) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.imageUrl = "";
        this.backgroundImageUrl = "";
        this.platform = "";
        this.rating = 0.0;
    }

    public Game(String title, String releaseDate, String genre, String imageUrl, String backgroundImageUrl) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.imageUrl = imageUrl != null ? imageUrl : "";
        this.backgroundImageUrl = backgroundImageUrl != null ? backgroundImageUrl : "";
        this.platform = "";
        this.rating = 0.0;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public String getBackgroundImageUrl() {return backgroundImageUrl;}
    public void setBackgroundImageUrl(String backgroundImageUrl) {this.backgroundImageUrl = backgroundImageUrl;}

    public String getPlatform() {return platform;}
    public void setPlatform(String plataform) {this.platform = plataform;}

    public double getRating() {return rating;}
    public void setRating(double rating) {this.rating = rating;}

    @Override
    public String toString() {
        return String.format("%s | %s | %s", title, releaseDate, genre);

    }
}
