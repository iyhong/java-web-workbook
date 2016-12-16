package spms.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spms.dao.MemberDao;

// 오류 처리 JSP 적용  
@WebServlet("/member/delete")
public class MemberDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		Connection connection = null;
		int no = Integer.parseInt(request.getParameter("no"));
		int rowCount = 0;
		try {
			ServletContext sc = this.getServletContext();
			connection = (Connection) sc.getAttribute("conn");   
			MemberDao memberDao = new MemberDao();
			memberDao.setConnection(connection);
			rowCount = memberDao.delete(no);
			if(rowCount != 0){
				System.out.println("삭제성공");
				response.sendRedirect("list");
			}else{
				System.out.println("삭제실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e);
			RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
			rd.forward(request, response);
		}
	}
}