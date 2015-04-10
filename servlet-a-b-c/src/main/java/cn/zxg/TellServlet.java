package cn.zxg;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class TellServlet
 */
public class TellServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public TellServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * http://localhost:8080/servlet-a-b-c/TellServlet?say=something&session=1
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String paraSay = request.getParameter("say");
		HttpSession session = request.getSession();
		session.setAttribute("exist", "exist");
		String reUrl;
		if(paraSay == null){
			reUrl = "PrintServlet";
		}else{
			reUrl = "PrintServlet?say="+paraSay;
		}
		response.sendRedirect(reUrl);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
