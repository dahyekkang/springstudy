package com.gdu.board.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gdu.board.dto.BoardDto;


@Repository
public class BoardDao {
  
  private Connection con;
  private PreparedStatement ps;
  private ResultSet rs;
  private String sql;

  private Connection getConnection() throws Exception {
    Class.forName("oracle.jdbc.OracleDriver");
    return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "GD", "1111");
  }
  
  private void close() {
    try {
      if(rs != null) rs.close();
      if(ps != null) ps.close();
      if(con != null) con.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  
  public List<BoardDto> getBoardList() {
    List<BoardDto> list = new ArrayList<BoardDto>();
    try {
      con = getConnection();
      sql = "SELECT NO, AUTHOR, TITLE, CONTENT, HIT, IP, POSTDATE FROM MVC_BOARD_T ORDER BY NO DESC";
      ps = con.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()) {
        BoardDto board = new BoardDto(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getDate(7));
        list.add(board);
      }
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
    return list;
  }
  
  
  public int addBoard(BoardDto board) {
    int addResult = 0;
    try { 
      con = getConnection();
      sql = "INSERT INTO MVC_BOARD_T VALUES(MVC_BOARD_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";
      ps = con.prepareStatement(sql);
      ps.setString(1, board.getAuthor());
      ps.setString(2, board.getTitle());
      ps.setString(3, board.getContent());
      ps.setInt(4, board.getHit());
      ps.setString(5, board.getIp());
      ps.setDate(6, board.getPostdate());
      addResult = ps.executeUpdate();
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
    return addResult;
  }
  
  
  public BoardDto getBoard(int no) {
    BoardDto board = null;
    try {
      con = getConnection();
      sql = "SELECT NO, AUTHOR, TITLE, CONTENT, HIT, IP, POSTDATE FROM MVC_BOARD_T WHERE NO = ?";
      ps = con.prepareStatement(sql);
      ps.setInt(1, no);
      rs = ps.executeQuery();
      if(rs.next()) {
        board = new BoardDto(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getDate(7));
      }
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
    return board;
  }
  
  
  public int removeBoard(int no) {
    int result = 0;
    try {
      con = getConnection();
      sql = "DELETE FROM MVC_BOARD_T WHERE NO = ?";
      ps = con.prepareStatement(sql);
      ps.setInt(1, no);
      result = ps.executeUpdate();
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
    return result;
  }
   
}