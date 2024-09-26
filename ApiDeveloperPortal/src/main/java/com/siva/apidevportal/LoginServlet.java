package com.siva.apidevportal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet implementation class LoginServlet
 */

public class LoginServlet extends HttpServlet {
	
	    private static final long serialVersionUID = 1L;
	    //creating final variables for the database connectoion properties
		private static final String  userName="root";
		private static final String  password="Siva@7567";
		private static final String  url="jdbc:mysql://localhost:3306/api_portal";
		
		// variable creation for accessing parameters
		String inputUserName,inputPassword;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("LoginForm.html").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
	    response.setCharacterEncoding("UTF-8");
		inputUserName=request.getParameter("UserName");
		inputPassword=request.getParameter("Password");
		
		StringBuilder loginResponse = new StringBuilder();
		loginResponse.append("<!DOCTYPE html>")
                    .append("<html lang=\"en\">")
                    .append("<head><meta charset=\"UTF-8\"><title>Login Status</title></head>")
                    .append("<body>")
                    .append("<h2>Login Status</h2>");
		try {
            // Load the MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connObject = DriverManager.getConnection(url, userName,password );

            // Query to authenticate the user
            String selectSql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement prpdStatement = connObject.prepareStatement(selectSql);
            prpdStatement.setString(1, inputUserName);
            prpdStatement.setString(2, inputPassword); 
            
           // handling resultset from prepared statement query
            ResultSet users = prpdStatement.executeQuery();
            if (users.next()) {
            	//storing the user name in the session to manage state
                HttpSession session = request.getSession();
                session.setAttribute("username", inputUserName); 
                loginResponse.append("<p>Login successful! You can now navigate to Dashboard</p>");
            } else {
            	loginResponse.append("<p>Login failed. Please try again.</p>");

            }

            prpdStatement.close();
            connObject.close();
        } catch (Exception e) {
            e.printStackTrace();
            loginResponse.append("<p>Cannot Login because of: ").append(e.getMessage()).append("</p>");
        }
		
		 loginResponse.append("<a href=\"Dashboard.html\">Click here to navigate to dashboard...</a>")
        .append("</body>")
        .append("</html>");

        // Sending the HTML response
        response.getWriter().write(loginResponse.toString());
    }
	

}
