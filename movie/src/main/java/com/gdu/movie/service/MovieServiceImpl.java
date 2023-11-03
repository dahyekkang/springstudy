package com.gdu.movie.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.gdu.movie.dao.MovieMapper;
import com.gdu.movie.dto.MovieDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {
  
  private final MovieMapper movieMapper;
  
  @Override
  public Map<String, Object> getMovieList() {
    
    int movieCount = movieMapper.getMovieCount();
    List<MovieDto> list = movieMapper.getMovieList();
    
    return Map.of("message", "전체 " + movieCount + "개의 목록을 가져왔습니다."
                , "list", list
                , "status", 200);
  }
  
  @Override
  public Map<String, Object> getSearchMovieList(HttpServletRequest request) {

    String column = request.getParameter("column");
    String searchText = request.getParameter("searchText");
    
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("column", column);
    map.put("searchText", searchText);
    
    int searchMovieCount = movieMapper.getSearchMovieCount(map);
    
    List<MovieDto> searchMovieList = movieMapper.getSearchMovieList(map);

    return Map.of("searchMovieList", searchMovieList
                , "searchMovieCount", searchMovieCount
                , "status", (searchMovieCount == 0 ? 500 : 200));
  }
  
}
