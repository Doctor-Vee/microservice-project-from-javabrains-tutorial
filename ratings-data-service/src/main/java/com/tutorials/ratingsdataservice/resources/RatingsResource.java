package com.tutorials.ratingsdataservice.resources;

import com.tutorials.ratingsdataservice.model.Rating;
import com.tutorials.ratingsdataservice.model.UserRating;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsResource {
    @RequestMapping("/{movieId}")
    public Rating getRating(@PathVariable("movieId") String movieId) throws InterruptedException {
        Thread.sleep(1000);
        return new Rating(movieId, movieId.length()%5+1);
    }

    @RequestMapping("users/{userId}")
    public UserRating getUserRating(@PathVariable("userId") String userId) throws InterruptedException {
        Thread.sleep(2000);
        List<Rating> ratings =  Arrays.asList(
                getRating(userId),
                new Rating(userId+"a", 4),
                new Rating("2345", 3)
        );
        return new UserRating(ratings);
    }
}
