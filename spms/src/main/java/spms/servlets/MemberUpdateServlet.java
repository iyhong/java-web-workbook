package spms.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
// - 변경폼 및 예외 처리
@SuppressWarnings("serial")
@WebServlet("/member/update")
public class MemberUpdateServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		Connection connection = null;
		int no = Integer.parseInt(request.getParameter("no"));
		Member member;
		try {
			ServletContext sc = this.getServletContext();
			connection = (Connection) sc.getAttribute("conn");   
			MemberDao memberDao = new MemberDao();
			memberDao.setConnection(connection);
			member = memberDao.selectOne(no);
			request.setAttribute("member", member);
			RequestDispatcher rd = request.getRequestDispatcher(
					"/member/MemberUpdateForm.jsp");
			rd.forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e);
			RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
			rd.forward(request, response);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection connection = null;
		
		Member member = new Member();
		member.setNo(Integer.parseInt(request.getParameter("no")));
		member.setEmail(request.getParameter("email"));
		member.setName(request.getParameter("name"));
		
		try {
			ServletContext sc = this.getServletContext();
			connection = (Connection) sc.getAttribute("conn");    
			MemberDao memberDao = new MemberDao();
			memberDao.setConnection(connection);
			int rowCount = memberDao.update(member);
			if(rowCount != 0){
				System.out.println("수정성공");
				response.sendRedirect("list");
			}else{
				System.out.println("수정실패");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e);
			RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
			rd.forward(request, response);
		}
	}
}