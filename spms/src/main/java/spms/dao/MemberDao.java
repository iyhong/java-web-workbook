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

	//���������� (dependency injection) �۾�(�ʿ��Ҷ� �ްڴ�)
	public void setConnPool(DBConnectionPool connPool) {
		this.connPool = connPool;
	}
	
	//��ü ȸ�� ����Ʈ �޼���
	public List<Member> selectList() throws Exception{
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		System.out.println("MemberDao selectList() ȣ��");
		ArrayList<Member> members = new ArrayList<Member>();
		try {
			//ServletContext �� �����ü����� ������ �����ϴ�..�׷��� �ٸ�������� ��
			connection = connPool.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"SELECT MNO,MNAME,EMAIL,CRE_DATE" + 
					" FROM MEMBERS" +
					" ORDER BY MNO ASC");
			
			
			// �����ͺ��̽����� ȸ�� ������ ������ Member�� ��´�.
			// �׸��� Member��ü�� ArrayList�� �߰��Ѵ�.
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

	//ȸ�� ��� �޼���
	public int insert(Member member) throws Exception{
		System.out.println("MemberDao insert() ȣ��");
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
	
	//�Ѹ��� ȸ������ ��ȸ �޼���
	public Member selectOne(int no) throws Exception{
		System.out.println("MemberDao selectOne() ȣ��");
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
				throw new Exception("�ش� ��ȣ�� ȸ���� ã�� �� �����ϴ�.");
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
	
	//�Ѹ��� ȸ������ ���� �޼���
	public int update(Member member) throws Exception{
		System.out.println("MemberDao update() ȣ��");
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
	
	//�Ѹ��� ȸ������ �����޼���
	public int delete (int no) throws Exception {
		System.out.println("MemberDao delete() ȣ��");
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
	
	//�α��� üũ �޼���
	public Member exist(String email, String password) throws Exception{
		System.out.println("MemberDao exist() ȣ��");
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
