package spms.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spms.vo.Member;

// ServletContext에 보관된 Connection 객체 사용  
@WebServlet("/member/list")
public class MemberListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		final int pagePerRow = 3;	// 페이지당 몇개 row 보여줄지 정하는 변수
		final int groupPagePer = 5;	//그룹페이지를 몇개로 할지.. 페이지 보여줄 숫자 몇개로 할지..5개씩 또는 10개씩
		int currentPage = 1;	//현재페이지는 초기값은 1페이지
		if(request.getParameter("currentPage") != null){	//currentpage 를 get 방식으로 받으면
			currentPage = Integer.parseInt(request.getParameter("currentPage"));	//currentPage에 전송받은 현재페이지값을 대입한다.
		}	//currentPage값을 전송안받으면 즉 제일 첫화면이기때문에 초기값 그대로 1가지고감
		int totalCount = 0;
		
		try {
			ServletContext sc = this.getServletContext();
			conn = (Connection) sc.getAttribute("conn"); 
			stmt = conn.prepareStatement("select count(mno) from members");
			rs = stmt.executeQuery();
			if(rs.next()){
				totalCount = rs.getInt(1);
			}
			
			//rs = stmt.executeQuery("SELECT MNO,MNAME,EMAIL,CRE_DATE FROM MEMBERS ORDER BY MNO ASC limit ?,?");
			stmt = conn.prepareStatement("SELECT MNO,MNAME,EMAIL,CRE_DATE FROM MEMBERS ORDER BY MNO ASC limit ?,?");
			stmt.setInt(1, (currentPage-1)*pagePerRow);
			stmt.setInt(2, pagePerRow);
			rs = stmt.executeQuery();
			response.setContentType("text/html; charset=UTF-8");
			ArrayList<Member> members = new ArrayList<Member>();
			
			// 데이터베이스에서 회원 정보를 가져와 Member에 담는다.
			// 그리고 Member객체를 ArrayList에 추가한다.
			while(rs.next()) {
				members.add(new Member()
							.setNo(rs.getInt("MNO"))
							.setName(rs.getString("MNAME"))
							.setEmail(rs.getString("EMAIL"))
							.setCreatedDate(rs.getDate("CRE_DATE"))	);
			}
			
			// request에 회원 목록 데이터 보관한다.
			request.setAttribute("members", members);
			request.setAttribute("totalCount", totalCount);
			request.setAttribute("pagePerRow", pagePerRow);
			request.setAttribute("groupPagePer", groupPagePer);
			request.setAttribute("currentPage", currentPage);
			
			// JSP로 출력을 위임한다.
			RequestDispatcher rd = request.getRequestDispatcher("/member/MemberList.jsp");
			rd.include(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e);
			RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
			rd.forward(request, response);
			
		} finally {
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			//try {if (conn != null) conn.close();} catch(Exception e) {}
		}

	}
}