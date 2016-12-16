package spms.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spms.dao.MemberDao;
import spms.vo.Member;

// JSP 적용
// - 입력폼 및 오류 처리 
@WebServlet("/member/add")
public class MemberAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(
				"/member/MemberForm.jsp");
		rd.forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			ServletContext sc = this.getServletContext();
			Connection connection = (Connection) sc.getAttribute("conn");
			int rowCount = 0;
			
			Member member = new Member();
			member.setEmail(request.getParameter("email"));
			member.setPassword(request.getParameter("password"));
			member.setName(request.getParameter("name"));
			
			MemberDao memberDao = new MemberDao();
			memberDao.setConnection(connection);
			rowCount = memberDao.insert(member);
			if(rowCount != 0){
				System.out.print("등록성공");
				response.sendRedirect("list");
			}else{
				System.out.print("등록실패");
			}
			
		}catch(Exception e){
			request.setAttribute("error", e);
			RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
			rd.forward(request, response);
		}
	}
}