package com.tutorials.movieinfoservice.models;

public class Movie {
    private String movieId;
    private String name;
    private String description;
    private String unnecessaryField;

    public Movie(){

    }

    public Movie(String movieId, String name, String description, String unnecessaryField) {
        this.movieId = movieId;
        this.name = name;
        this.description = description;
        this.unnecessaryField = unnecessaryField;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnnecessaryField() {
        return unnecessaryField;
    }

    public void setUnnecessaryField(String unnecessaryField) {
        this.unnecessaryField = unnecessaryField;
    }
}
