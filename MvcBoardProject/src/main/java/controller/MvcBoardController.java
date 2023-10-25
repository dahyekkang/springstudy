package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ActionForward;
import service.DeleteMvcBoardService;
import service.DetailMvcBoardService;
import service.InsertMvcBoardService;
import service.ListMvcBoardService;
import service.MvcBoardService;
import service.UpdateMvcBoardHitService;

@WebServlet("*.do")
public class MvcBoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public MvcBoardController() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length() + 1);
		
		MvcBoardService service = null;
		ActionForward af = null;
		
		switch (command) {
		case "list.do":
			service = new ListMvcBoardService();
			break;
		case "detail.do":
			service = new DetailMvcBoardService();
			break;
		case "write.do":
			af = new ActionForward("mvc/write.jsp", false);
			break;
		case "insert.do":
			service = new InsertMvcBoardService();
			break;
		case "delete.do":
			service = new DeleteMvcBoardService();
			break;
		case "updateHit.do":
			service = new UpdateMvcBoardHitService();
			break;
		}

		try {
			if (service != null) {
				af = service.execute(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (af != null) {
			if (af.isRedirect()) {
				response.sendRedirect(af.getView());
			} else {
				request.getRequestDispatcher(af.getView()).forward(request, response);
			}
		}
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
