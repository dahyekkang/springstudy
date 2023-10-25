package service;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ActionForward;
import domain.MvcBoard;
import repository.MvcBoardDAO;

public class InsertMvcBoardService implements MvcBoardService {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String author = request.getParameter("author"); 
		String title = request.getParameter("title"); 
		String content = request.getParameter("content");
		String ip = request.getRemoteAddr();
		MvcBoard dto = new MvcBoard();
		dto.setAuthor(author);
		dto.setTitle(title);
		dto.setContent(content);
		dto.setIp(ip);
		int result = MvcBoardDAO.getInstance().insertMvcBoard(dto);
		PrintWriter out = response.getWriter();
		if (result > 0) {
			out.println("<script>");
			out.println("alert('게시글이 등록되었습니다.')");
			out.println("location.href='/MvcBoardProject/list.do'");
			out.println("</script>");
		} else {
			out.println("<script>");
			out.println("alert('게시글이 등록되지 않았습니다.')");
			out.println("history.back()");
			out.println("</script>");
		}
		return null;
	}

}
