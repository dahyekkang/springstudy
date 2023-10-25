package service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ActionForward;
import repository.MvcBoardDAO;

public class ListMvcBoardService implements MvcBoardService {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("list", MvcBoardDAO.getInstance().selectMvcBoardList());
		return new ActionForward("mvc/list.jsp", false);
	}

}
