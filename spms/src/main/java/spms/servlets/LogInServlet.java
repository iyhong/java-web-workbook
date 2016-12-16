package spms.servlets;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import spms.dao.MemberDao;
import spms.vo.Member;

@WebServlet("/auth/login")
public class LogInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(
				"/auth/LogInForm.jsp");
		rd.forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		Connection connection = null;
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		Member member;
		try {
			ServletContext sc = this.getServletContext();
			connection = (Connection) sc.getAttribute("conn");  
			MemberDao memberDao = new MemberDao();
			memberDao.setConnection(connection);
			member = memberDao.exist(email, password);
			
			if (member != null) {
				System.out.println("member 가 null 이 아니면");
				HttpSession session = request.getSession();
				session.setAttribute("member", member);
				
				response.sendRedirect("../member/list");
			} else {
				RequestDispatcher rd = request.getRequestDispatcher(
						"/auth/LogInFail.jsp");
				rd.forward(request, response);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}