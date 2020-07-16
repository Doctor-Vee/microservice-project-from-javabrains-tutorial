package com.tutorials.movieinfoservice.resources;

import com.tutorials.movieinfoservice.models.Movie;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;


@RestController
@RequestMapping("/movies")
@CrossOrigin(origins = "*")
public class MovieResource {

    @RequestMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId")String movieId, HttpServletRequest request) throws InterruptedException {
        Thread.sleep(2000);
        return new Movie(movieId, "Name"+movieId, "Movie " + movieId + " is a classic movie", request.getRemoteAddr());
    }
}
