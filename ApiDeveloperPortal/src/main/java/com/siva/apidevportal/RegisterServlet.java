package com.siva.apidevportal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet implementation class RegisterServlet
 */

public class RegisterServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	//creating final variables for the database connectoion properties
	private static final String  userName="root";
	private static final String  password="Siva@7567";
	private static final String  url="jdbc:mysql://localhost:3306/api_portal";
			
	
	// variable creation for accessing parameters
	String inputUserName,inputPassword,inputEmail;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("RegisterForm.html").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
	    response.setCharacterEncoding("UTF-8");
	    
		inputUserName=request.getParameter("UserName");
		inputPassword=request.getParameter("Password");
		inputEmail=request.getParameter("email");
		
		StringBuilder postResponse = new StringBuilder();
		postResponse.append("<!DOCTYPE html>")
                    .append("<html lang=\"en\">")
                    .append("<head><meta charset=\"UTF-8\"><title>Registration Status</title></head>")
                    .append("<body>")
                    .append("<h2>Registration Status</h2>");

		
		try {
            // Load the MySQL drive to connect with MYSQL Database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connObject = DriverManager.getConnection(url, userName, password);

            // Building prepared statement to insert user record into respective table
            String insertStatement = "INSERT INTO users (username, password, email, created_at) VALUES (?, ?, ?, NOW())";
            PreparedStatement prpdStatement = connObject.prepareStatement(insertStatement);
            prpdStatement.setString(1, inputUserName);
            prpdStatement.setString(2, inputPassword); 
            prpdStatement.setString(3, inputEmail);

            Integer counter=prpdStatement.executeUpdate();
            
            
            // redirecting after successful insertion
            if ( counter > 0) postResponse.append("<p>Registration successful! You can now log in.</p>");
            else postResponse.append("<p>Registration failed. Please try again.</p>");

            prpdStatement.close();
            connObject.close();
        } catch (Exception e) {
            e.printStackTrace();
            postResponse.append("<p>Cannot signup because of: ").append(e.getMessage()).append("</p>");

        }
	    postResponse.append("<a href=\"LoginForm.html\">Click here to login...</a>")
         .append("</body>")
         .append("</html>");

         // Sending the HTML response
         response.getWriter().write(postResponse.toString());
    }
	

}
