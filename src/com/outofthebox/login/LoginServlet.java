
package com.outofthebox.login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/LoginServlet" )
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String userID = "admin";
	private final String password = "root";
       
    
	public void init() throws ServletException {
		//we can create DB connection resource here and set it to Servlet context
		if(getServletContext().getInitParameter("dbURL").equals("jdbc:mysql://localhost/outofthebox") &&
				getServletContext().getInitParameter("dbUser").equals("root") &&
				getServletContext().getInitParameter("dbUserPwd").equals("root"))
		getServletContext().setAttribute("DB_Success", "True");
		else throw new ServletException("DB Connection error");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//get request parameters for userID and password
		String user = request.getParameter("user");
		String pwd = request.getParameter("pwd");
		
		//logging example
		log("User="+user+"::password="+pwd);
		
		if(userID.equals(user) && password.equals(pwd)){
			
			HttpSession session = request.getSession();
			session.setAttribute("user", "Admin");
			//setting session to expiry in 30 mins
			session.setMaxInactiveInterval(30*60);
			Cookie loginCookie = new Cookie("user",user);
			loginCookie.setMaxAge(30*60);
			response.addCookie(loginCookie);
			String encodedURL = response.encodeRedirectURL("Home.jsp");
			response.sendRedirect(encodedURL);
		}else{
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/Index.html");
			PrintWriter out= response.getWriter();
			out.println("<font color=red>Either user name or password is wrong.</font>");
			rd.include(request, response);
		}
		
	}
}

