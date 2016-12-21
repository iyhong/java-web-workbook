package spms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import spms.util.DBConnectionPool;
import spms.vo.Member;

public class MemberDao {
	DBConnectionPool connPool;;

	public MemberDao() {
	}

	//의존성주입 (dependency injection) 작업(필요할때 받겠다)
	public void setConnPool(DBConnectionPool connPool) {
		this.connPool = connPool;
	}
	
	//전체 회원 리스트 메서드
	public List<Member> selectList() throws Exception{
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		System.out.println("MemberDao selectList() 호출");
		ArrayList<Member> members = new ArrayList<Member>();
		try {
			//ServletContext 을 가져올수없음 서블릿만 가능하다..그래서 다른방법으로 함
			connection = connPool.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"SELECT MNO,MNAME,EMAIL,CRE_DATE" + 
					" FROM MEMBERS" +
					" ORDER BY MNO ASC");
			
			
			// 데이터베이스에서 회원 정보를 가져와 Member에 담는다.
			// 그리고 Member객체를 ArrayList에 추가한다.
			while(rs.next()) {
				members.add(new Member()
							.setNo(rs.getInt("MNO"))
							.setName(rs.getString("MNAME"))
							.setEmail(rs.getString("EMAIL"))
							.setCreatedDate(rs.getDate("CRE_DATE"))	);
			}
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			//try {if (conn != null) conn.close();} catch(Exception e) {}
			if(connection!=null){connPool.returnConnetion(connection);}
		}
		return members;
		
	}

	//회원 등록 메서드
	public int insert(Member member) throws Exception{
		System.out.println("MemberDao insert() 호출");
		Connection connection = null;
		PreparedStatement stmt = null;
		int rowCount = 0;
		try{
			connection = connPool.getConnection();
			stmt = connection.prepareStatement(
					"INSERT INTO MEMBERS(EMAIL,PWD,MNAME,CRE_DATE,MOD_DATE)"
					+ " VALUES (?,?,?,NOW(),NOW())");
			stmt.setString(1, member.getEmail());
			stmt.setString(2, member.getPassword());
			stmt.setString(3, member.getName());
			rowCount = stmt.executeUpdate();
			System.out.println("rowCount : "+rowCount);
		}catch(Exception e){
			throw e;
		}finally{
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			if(connection!=null){connPool.returnConnetion(connection);}
		}
		return rowCount;
	}
	
	//한명의 회원정보 조회 메서드
	public Member selectOne(int no) throws Exception{
		System.out.println("MemberDao selectOne() 호출");
		Connection connection = null;
		Member member;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			connection = connPool.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
				"SELECT MNO,EMAIL,MNAME,CRE_DATE FROM MEMBERS" + 
				" WHERE MNO=" + no);	
			if (rs.next()) {
				member = new Member()
						.setNo(rs.getInt("MNO"))
						.setEmail(rs.getString("EMAIL"))
						.setName(rs.getString("MNAME"))
						.setCreatedDate(rs.getDate("CRE_DATE"));
				
			} else {
				throw new Exception("해당 번호의 회원을 찾을 수 없습니다.");
			}
		}catch(Exception e){
			throw e;
		}finally{
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			if(connection!=null){connPool.returnConnetion(connection);}
		}
		return member;
	}
	
	//한명의 회원정보 수정 메서드
	public int update(Member member) throws Exception{
		System.out.println("MemberDao update() 호출");
		Connection connection = null;
		PreparedStatement stmt = null;
		int rowCount = 0;
		
		try{
			connection = connPool.getConnection();
			stmt = connection.prepareStatement(
					"UPDATE MEMBERS SET EMAIL=?,MNAME=?,MOD_DATE=now()"
					+ " WHERE MNO=?");
			stmt.setString(1, member.getEmail());
			stmt.setString(2, member.getName());
			stmt.setInt(3, member.getNo());
			rowCount = stmt.executeUpdate();
			
		}catch(Exception e){
			throw e;
		}finally{
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			if(connection!=null){connPool.returnConnetion(connection);}
		}
		return rowCount;
	}
	
	//한명의 회원정보 삭제메서드
	public int delete (int no) throws Exception {
		System.out.println("MemberDao delete() 호출");
		Connection connection = null;
		Statement stmt = null;
		int rowCount = 0;
		try{
			connection = connPool.getConnection();
			stmt = connection.createStatement();
			rowCount = stmt.executeUpdate(
					"DELETE FROM MEMBERS WHERE MNO=" + 
					no);
			
		}catch(Exception e){
			throw e;
		}finally{
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			if(connection!=null){connPool.returnConnetion(connection);}
		}
		return rowCount;
	}
	
	//로그인 체크 메서드
	public Member exist(String email, String password) throws Exception{
		System.out.println("MemberDao exist() 호출");
		Connection connection = null;
		Member member = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try{
			connection = connPool.getConnection();
			stmt = connection.prepareStatement(
					"SELECT MNAME,EMAIL FROM MEMBERS"
					+ " WHERE EMAIL=? AND PWD=?");
			stmt.setString(1, email);
			stmt.setString(2, password);
			System.out.println("MemberDao exist() stmt : "+stmt);
			rs = stmt.executeQuery();
			if(rs.next()){
				System.out.println("MemberDao exist() rs : "+rs);
				member = new Member();
				member.setName(rs.getString("mname"));
				member.setEmail(rs.getString("email"));
			}
		}catch(Exception e){
			throw e;
		}finally{
			try {if (rs != null) rs.close();} catch (Exception e) {}
			try {if (stmt != null) stmt.close();} catch (Exception e) {}
			if(connection!=null){connPool.returnConnetion(connection);}
		}
		return member;
	}
}
