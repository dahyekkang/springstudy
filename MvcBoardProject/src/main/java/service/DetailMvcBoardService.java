package service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ActionForward;
import repository.MvcBoardDAO;

public class DetailMvcBoardService implements MvcBoardService {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int no = Integer.parseInt(request.getParameter("no"));
		request.setAttribute("dto", MvcBoardDAO.getInstance().selectMvcBoardByNo(no));
		return new ActionForward("mvc/detail.jsp", false);
	}

}
