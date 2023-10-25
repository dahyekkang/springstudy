package repository;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import domain.MvcBoard;

public class MvcBoardDAO {
	
	private SqlSessionFactory factory;
	private static MvcBoardDAO instance = new MvcBoardDAO();
	
	private MvcBoardDAO() {
		try {
			String resource = "mybatis/config/mybatis-config.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);
			factory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static MvcBoardDAO getInstance() {
		return instance;
	}
	
	public List<MvcBoard> selectMvcBoardList() {
		SqlSession ss = factory.openSession();
		List<MvcBoard> list = ss.selectList("mybatis.mapper.mvc_board.selectMvcBoardList");
		ss.close();
		return list;
	}
	
	public MvcBoard selectMvcBoardByNo(int no) {
		SqlSession ss = factory.openSession();
		MvcBoard dto = ss.selectOne("mybatis.mapper.mvc_board.selectMvcBoardByNo", no);
		ss.close();
		return dto;
	}
	
	public int insertMvcBoard(MvcBoard dto) {
		SqlSession ss = factory.openSession(false);
		int result = ss.insert("mybatis.mapper.mvc_board.insertMvcBoard", dto);
		if (result > 0) ss.commit();
		ss.close();
		return result;
	}
	
	public int deleteMvcBoard(int no) {
		SqlSession ss = factory.openSession(false);
		int result = ss.delete("mybatis.mapper.mvc_board.deleteMvcBoard", no);
		if (result > 0) ss.commit();
		ss.close();
		return result;
	}

	public int updateMvcBoardHit(int no) {
		SqlSession ss = factory.openSession(false);
		int result = ss.update("mybatis.mapper.mvc_board.updateMvcBoardHit", no);
		if (result > 0) ss.commit();
		ss.close();
		return result;
	}

	public MvcBoard selectMvcBoardBestHit() {
		SqlSession ss = factory.openSession();
		MvcBoard dto = ss.selectOne("mybatis.mapper.mvc_board.selectMvcBoardBestHit");
		ss.close();
		return dto;
	}
	
}
