package com.gdu.movie.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gdu.movie.service.MovieService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController   // ajax 전용 Controller. 아래 메소드들에 @ResponseBody만 안 붙여도 되는 것
public class MovieController {

  private final MovieService movieService;
  
  @GetMapping(value="/searchAllMovies", produces="application/json")
  public Map<String, Object> searchAllMovies(){
    return movieService.getMovieList();
  }
  
  @GetMapping(value="/searchMovie", produces="application/json")
  public Map<String, Object> searchMovie(HttpServletRequest request, Model model) {
    return movieService.getSearchMovieList(request);
  }
  
}
