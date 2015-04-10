package cn.zxg;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class PrintServlet
 */
public class PrintServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public PrintServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取参数
		String paraSay = request.getParameter("say");
		String kehuduan = request.getRequestedSessionId();
		HttpSession session = request.getSession();
		String fuwuqi = session.getId();
		String say;
		if(paraSay == null){
			say = "Hello World!";
		}else{
			say = paraSay;
		}
		// 注意区分attribute 和 parameter
		request.setAttribute("say", say);
		request.setAttribute("kehuduan", kehuduan);
		request.setAttribute("fuwuqi", fuwuqi);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
