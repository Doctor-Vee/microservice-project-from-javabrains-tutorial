package com.tutorials.moviecatalogservice.resources;

import com.tutorials.moviecatalogservice.models.CatalogItem;
import com.tutorials.moviecatalogservice.models.Combinator;
import com.tutorials.moviecatalogservice.models.Movie;
import com.tutorials.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/catalog")
public class CatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder builder;


    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);

        return userRating.getUserRating().stream().map(ratingDetail -> {
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + ratingDetail.getMovieId(), Movie.class);
            return new CatalogItem(movie.getName(), movie.getDescription(), ratingDetail.getRating());
        }).collect(Collectors.toList());
    }

    @RequestMapping("/new/{userId}")
    public Combinator getCombinator(@PathVariable("userId") String userId) throws ExecutionException, InterruptedException {
        Instant start = Instant.now();
        Combinator combinator = new Combinator();

//        combinator.setUserRating(restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class));
//        combinator.setMovie(restTemplate.getForObject("http://movie-info-service/movies/" + userId, Movie.class));

        /*
        I made the movie info service and ratings data service to take 3 minutes each to execute ...
        so, if you uncomment the above commented 2 lines of code, you'd see that the execution time displayed in the result will be about 6 seconds
        However, if you run it as it is this way it takes 3 seconds because it makes the API calls concurrently.
         */

        ExecutorService taskExecutor = Executors.newFixedThreadPool(3); // set this to be >= the number of api calls you need to make... if you set it to 1, it takes the same time used in executing the api calls one after the other and if you set it to 0, it doesn't run at all
        CompletionService<Movie> movieCompletionService = new ExecutorCompletionService(taskExecutor);
        CompletionService<UserRating> userRatingCompletionService = new ExecutorCompletionService(taskExecutor);
        movieCompletionService.submit(() -> restTemplate.getForObject("http://movie-info-service/movies/" + userId, Movie.class));
        userRatingCompletionService.submit(() -> restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class));
        Future<Movie> movieFuture = movieCompletionService.take();
        Future<UserRating> userRatingFuture = userRatingCompletionService.take();
        combinator.setMovie(movieFuture.get());
        combinator.setUserRating(userRatingFuture.get());

        combinator.setExecutionTime(Duration.between(start, Instant.now()));
        return combinator;
    }
}


//    @Autowired
//    private DiscoveryClient discoveryClient;

//        System.out.println("now printing");
//        System.out.println(discoveryClient.getInstances("ratings-data-service"));
//        System.out.println("done printing");


//// Former technique
//    String[] ids = new String[]{userId+"a", userId+"ab", userId+"abc"};
//    List<String> movieIds = Arrays.asList(ids);
//Rating rating = restTemplate.getForObject("http://localhost:8083/ratingsdata/" + movieId, Rating.class);


//// Web Client style
//            Movie movie = builder.build().get().uri("http://localhost:8082/movies/" + movieId).retrieve().bodyToMono(Movie.class).block();
//            Rating rating = builder.build().get().uri("http://localhost:8083/ratingsdata/" + movieId).retrieve().bodyToMono(Rating.class).block();