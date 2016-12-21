package spms.listener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import spms.dao.MemberDao;

public class ContextLoaderListener implements ServletContextListener {
	private Connection conn;
	
	public ContextLoaderListener() {
	}

	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("ContextLoaderListener ½ÇÇà");
		ServletContext sc = sce.getServletContext();
		
		try {
			Class.forName(sc.getInitParameter("driver"));
			conn = DriverManager.getConnection(
					sc.getInitParameter("url"),
					sc.getInitParameter("username"),
					sc.getInitParameter("password"));
			sc.setAttribute("conn", conn);
			
			MemberDao memberDao = new MemberDao();
			memberDao.setConnection(conn);
			sc.setAttribute("memberDao", memberDao);

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
		//Connection close()
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
