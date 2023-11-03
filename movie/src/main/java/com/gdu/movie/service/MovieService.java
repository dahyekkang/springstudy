package com.gdu.movie.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface MovieService {
  public Map<String, Object> getMovieList();
  public Map<String, Object> getSearchMovieList(HttpServletRequest request);
}
