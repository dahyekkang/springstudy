package service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ActionForward;
import repository.MvcBoardDAO;

public class UpdateMvcBoardHitService implements MvcBoardService {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int no = Integer.parseInt(request.getParameter("no"));
		MvcBoardDAO.getInstance().updateMvcBoardHit(no);
		return new ActionForward("/MvcBoardProject/detail.do?no=" + no, true);
	}

}
