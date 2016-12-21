package spms.listeners;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.dbcp.BasicDataSource;

import spms.dao.MemberDao;

public class ContextLoaderListener implements ServletContextListener {
	private Connection conn;
	
	public ContextLoaderListener() {
	}

	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("ContextLoaderListener ½ÇÇà");
		ServletContext sc = sce.getServletContext();
		
		try {
			String driver = sc.getInitParameter("driver");
			String url = sc.getInitParameter("url");
			String username = sc.getInitParameter("username");
			String password = sc.getInitParameter("password");
			BasicDataSource ds = new BasicDataSource();
			ds.setDriverClassName(driver);
			ds.setUrl(url);
			ds.setUsername(username);
			ds.setPassword(password);
			
			MemberDao memberDao = new MemberDao();
			memberDao.setDs(ds);
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
