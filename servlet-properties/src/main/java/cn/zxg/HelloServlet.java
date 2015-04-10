package cn.zxg;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloServlet
 */
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String defaultSay;

    /**
     * Default constructor. 
     */
	public HelloServlet() throws FileNotFoundException, IOException {
		// ContextClassLoader 加载classes文件夹下的文件
		// war包中    \WEB-INF\classes
		// / 代表classes文件夹，可以写可以不写。
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/test.properties");
		URL url = Thread.currentThread().getContextClassLoader().getResource("test/test.properties");
		Properties p = new Properties();
		p.load(in);
		this.defaultSay = p.getProperty("HelloServlet.defaultSay");
		in.close();
		System.out.println(this.defaultSay);
		System.out.println(url.toString());
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
