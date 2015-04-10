package cn.zxg;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloServlet
 */
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public HelloServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver", true, this.getClass().getClassLoader());
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		// 建立会话 
		String url = "jdbc:oracle:thin:@172.16.50.67:1521:orcl";
		String username = "e_channel";
		String password = "e_channel_test";
		try{
			Connection conn = DriverManager.getConnection(url, username,password);
			// PL/SQL Developer | SQL Window   
			Statement stmt = conn.createStatement();
			// 语句 
			String sql = "select * from zxg";
			ResultSet rs = stmt.executeQuery(sql);
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			while(rs.next()){
				out.println(rs.getString("ID")+":"+rs.getString("NAME")+":"+rs.getTimestamp("LOGON_TIME")+"<br>");
			}
			out.flush();
			out.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
