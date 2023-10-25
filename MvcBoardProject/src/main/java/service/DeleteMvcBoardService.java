package service;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ActionForward;
import repository.MvcBoardDAO;

public class DeleteMvcBoardService implements MvcBoardService {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int no = Integer.parseInt(request.getParameter("no"));
		int result = 0;
		PrintWriter out = response.getWriter();
		result = MvcBoardDAO.getInstance().deleteMvcBoard(no);
		
		if (result > 0) {
			out.println("<script>");
			out.println("alert('게시글이 삭제되었습니다.')");
			out.println("location.href='/MvcBoardProject/list.do'");
			out.println("</script>");
		} else {
			out.println("<script>");
			out.println("alert('게시글이 삭제되지 않았습니다.')");
			out.println("history.back()");
			out.println("</script>");
		}
		return null;
	}

}
