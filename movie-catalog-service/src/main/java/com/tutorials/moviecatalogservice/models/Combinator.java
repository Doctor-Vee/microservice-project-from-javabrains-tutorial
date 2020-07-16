package com.tutorials.moviecatalogservice.models;

import lombok.Data;

import java.time.Duration;

@Data
public class Combinator {
    UserRating userRating;
    Movie movie;
    Movie movie2;
    Movie movie3;
    Duration executionTime;
}
