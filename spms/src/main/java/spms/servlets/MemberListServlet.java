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

// ServletContext�� ������ Connection ��ü ���  
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
		final int pagePerRow = 3;	// �������� � row �������� ���ϴ� ����
		final int groupPagePer = 5;	//�׷��������� ��� ����.. ������ ������ ���� ��� ����..5���� �Ǵ� 10����
		int currentPage = 1;	//������������ �ʱⰪ�� 1������
		if(request.getParameter("currentPage") != null){	//currentpage �� get ������� ������
			currentPage = Integer.parseInt(request.getParameter("currentPage"));	//currentPage�� ���۹��� �������������� �����Ѵ�.
		}	//currentPage���� ���۾ȹ����� �� ���� ùȭ���̱⶧���� �ʱⰪ �״�� 1������
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
			
			// �����ͺ��̽����� ȸ�� ������ ������ Member�� ��´�.
			// �׸��� Member��ü�� ArrayList�� �߰��Ѵ�.
			while(rs.next()) {
				members.add(new Member()
							.setNo(rs.getInt("MNO"))
							.setName(rs.getString("MNAME"))
							.setEmail(rs.getString("EMAIL"))
							.setCreatedDate(rs.getDate("CRE_DATE"))	);
			}
			
			// request�� ȸ�� ��� ������ �����Ѵ�.
			request.setAttribute("members", members);
			request.setAttribute("totalCount", totalCount);
			request.setAttribute("pagePerRow", pagePerRow);
			request.setAttribute("groupPagePer", groupPagePer);
			request.setAttribute("currentPage", currentPage);
			
			// JSP�� ����� �����Ѵ�.
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